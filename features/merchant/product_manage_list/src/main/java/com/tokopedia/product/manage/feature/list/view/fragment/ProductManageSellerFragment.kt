package com.tokopedia.product.manage.feature.list.view.fragment

import android.app.Activity.RESULT_OK
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMechant
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.common.feature.list.analytics.ProductManageTracking
import com.tokopedia.product.manage.common.feature.list.constant.DRAFT_PRODUCT
import com.tokopedia.product.manage.common.feature.list.constant.ProductManageDataLayer
import com.tokopedia.product.manage.common.util.ProductManageListErrorHandler
import com.tokopedia.product.manage.feature.list.constant.ProductManageListConstant
import com.tokopedia.product.manage.feature.list.constant.ProductManageListConstant.BROADCAST_ADD_PRODUCT
import com.tokopedia.product.manage.feature.list.constant.ProductManageListConstant.REQUEST_CODE_DRAFT_PRODUCT
import com.tokopedia.product.manage.feature.list.di.ProductManageListInstance
import com.tokopedia.product.manage.feature.list.view.viewmodel.ProductDraftListCountViewModel
import com.tokopedia.shop.common.data.source.cloud.query.param.option.FilterMapper
import com.tokopedia.shop.common.data.source.cloud.query.param.option.FilterOption
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_product_manage.*
import kotlinx.android.synthetic.main.fragment_product_manage_seller.*
import java.util.*
import javax.inject.Inject

class ProductManageSellerFragment : ProductManageFragment() {

    companion object {
        private const val FILTER_OPTIONS = "filter_options"
        private const val SEARCH_KEYWORD_OPTIONS = "search_keyword_options"

        @JvmStatic
        fun newInstance(
                filterOptions: ArrayList<String>,
                searchKeyWord: String): ProductManageSellerFragment {
            return ProductManageSellerFragment().apply {
                arguments = Bundle().apply {
                    putStringArrayList(FILTER_OPTIONS, filterOptions)
                    putString(SEARCH_KEYWORD_OPTIONS, searchKeyWord)
                }
            }
        }
    }

    private lateinit var draftBroadCastReceiver: BroadcastReceiver

    @Inject
    lateinit var productDraftListCountViewModel: ProductDraftListCountViewModel

    private var alreadySendScreenNameAfterAddEditProduct: Boolean = false

    override fun getScreenName(): String = "/product list page"

    override fun getLayoutRes(): Int = R.layout.fragment_product_manage_seller

    override fun getRecyclerViewResourceId(): Int = R.id.recycler_view

    override fun getSwipeRefreshLayout(view: View?): SwipeRefreshLayout? {
        return view?.findViewById(R.id.swipe_refresh_layout)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        checkLogin()
        super.onViewCreated(view, savedInstanceState)
        activity?.window?.decorView?.setBackgroundColor(ContextCompat.getColor(requireContext(), com.tokopedia.unifyprinciples.R.color.Unify_N0))
        tvDraftProduct.visibility = View.GONE
        getDefaultKeywordOptionFromArguments()
        getDefaultFilterOptionsFromArguments()
        observeGetAllDraftCount()
    }

    override fun initInjector() {
        ProductManageListInstance.getComponent(requireContext())
                .inject(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        productDraftListCountViewModel.detachView()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)

        if(!hidden) {
            sendNormalSendScreen()
        }
    }

    override fun onResume() {
        super.onResume()
        registerDraftReceiver()
        productDraftListCountViewModel.getAllDraftCount()
    }

    override fun onPause() {
        super.onPause()
        unregisterDraftReceiver()
    }

    override fun callInitialLoadAutomatically() = false

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                ProductManageListConstant.REQUEST_CODE_ADD_PRODUCT ->
                    sendScreenWithCustomDimension(ProductManageDataLayer.CUSTOM_DIMENSION_PAGE_SOURCE_ADD_PRODUCT)
                ProductManageListConstant.REQUEST_CODE_EDIT_PRODUCT ->
                    sendScreenWithCustomDimension(ProductManageDataLayer.CUSTOM_DIMENSION_PAGE_SOURCE_EDIT_PRODUCT)
                else -> super.onActivityResult(requestCode, resultCode, intent)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, intent)
        }
    }

    private fun sendScreenWithCustomDimension(pageSource: String) {
        ProductManageTracking.sendScreen(screenName, pageSource)
        alreadySendScreenNameAfterAddEditProduct = true
    }

    private fun sendNormalSendScreen() {
        if (!alreadySendScreenNameAfterAddEditProduct) {
            ProductManageTracking.sendScreen(screenName)
        } else {
            alreadySendScreenNameAfterAddEditProduct = false
        }
    }

    private fun onDraftCountLoaded(rowCount: Long) {
        if (rowCount == 0L) {
            tvDraftProduct.visibility = View.GONE
        } else {
            tvDraftProduct.text = MethodChecker.fromHtml(getString(R.string.product_manage_you_have_x_unfinished_product, rowCount))
            tvDraftProduct.setOnClickListener {
                ProductManageTracking.eventDraftClick(DRAFT_PRODUCT)
                val intent = RouteManager.getIntent(activity, ApplinkConstInternalMechant.MERCHANT_PRODUCT_DRAFT)
                startActivityForResult(intent, REQUEST_CODE_DRAFT_PRODUCT)
            }
            tvDraftProduct.visibility = View.VISIBLE
        }
    }

    private fun onDraftCountLoadError() {
        productDraftListCountViewModel.clearAllDraft()
        tvDraftProduct.visibility = View.GONE
    }

    private fun getDefaultKeywordOptionFromArguments() {
        val searchKeyword = arguments?.getString(SEARCH_KEYWORD_OPTIONS).orEmpty()
        super.setSearchKeywordOptions(searchKeyword)
    }

    private fun getDefaultFilterOptionsFromArguments() {
        val filterOptionKeys: List<String> = arguments?.getStringArrayList(FILTER_OPTIONS).orEmpty()
        val filterOptions: List<FilterOption> = FilterMapper.mapKeysToFilterOptionList(filterOptionKeys)
        super.setDefaultFilterOptions(filterOptions)
    }

    private fun registerDraftReceiver() {
        draftBroadCastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (intent.action == BROADCAST_ADD_PRODUCT) {
                    productDraftListCountViewModel.getAllDraftCount()
                }
            }
        }

        activity?.let {
            val intentFilters = IntentFilter().apply {
                addAction(BROADCAST_ADD_PRODUCT)
            }
            LocalBroadcastManager.getInstance(it).registerReceiver(draftBroadCastReceiver, intentFilters)
        }
    }

    private fun unregisterDraftReceiver() {
        activity?.let {
            LocalBroadcastManager.getInstance(it).unregisterReceiver(draftBroadCastReceiver)
        }
    }

    private fun observeGetAllDraftCount() {
        observe(productDraftListCountViewModel.getAllDraftCountResult) {
            when (it) {
                is Success -> onDraftCountLoaded(it.data)
                is Fail -> {
                    onDraftCountLoadError()
                    ProductManageListErrorHandler.logExceptionToCrashlytics(it.throwable)
                }
            }
        }
    }

    private fun checkLogin() {
        if (!userSession.isLoggedIn) {
            RouteManager.route(activity, ApplinkConst.LOGIN)
            activity?.finish()
        } else if (!userSession.hasShop()) {
            RouteManager.route(activity, ApplinkConst.HOME)
            activity?.finish()
        }
    }
}