package com.tokopedia.product.manage.feature.list.view.fragment

import android.app.Activity.RESULT_OK
import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.abstraction.constant.TkpdState
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.list.analytics.ProductManageTracking
import com.tokopedia.product.manage.feature.list.constant.DRAFT_PRODUCT
import com.tokopedia.product.manage.feature.list.constant.ProductManageDataLayer
import com.tokopedia.product.manage.feature.list.constant.ProductManageListConstant
import com.tokopedia.product.manage.feature.list.di.ProductManageListInstance
import com.tokopedia.product.manage.feature.list.view.viewmodel.ProductDraftListCountViewModel
import com.tokopedia.product.manage.item.main.base.view.service.UploadProductService
import com.tokopedia.shop.common.data.source.cloud.query.param.option.FilterMapper
import com.tokopedia.shop.common.data.source.cloud.query.param.option.FilterOption
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_product_manage_seller.*
import java.util.*
import javax.inject.Inject

class ProductManageSellerFragment : ProductManageFragment() {

    companion object {
        private const val FILTER_OPTIONS = "filter_options"

        @JvmStatic
        fun newInstance(filterOptions: ArrayList<String>): ProductManageSellerFragment {
            return ProductManageSellerFragment().apply {
                arguments = Bundle().apply {
                    putStringArrayList(FILTER_OPTIONS, filterOptions)
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
        tvDraftProduct.visibility = View.GONE
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

    override fun onResume() {
        super.onResume()
        registerDraftReceiver()
        if (isMyServiceRunning(UploadProductService::class.java)) {
            productDraftListCountViewModel.getAllDraftCount()
        } else {
            productDraftListCountViewModel.fetchAllDraftCountWithUpdateUploading()
        }
        if (userVisibleHint) {
            sendNormalSendScreen()
        }
    }

    override fun onPause() {
        super.onPause()
        unregisterDraftReceiver()
    }

    override fun callInitialLoadAutomatically() = false

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            sendNormalSendScreen()
        }
    }

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
                RouteManager.route(activity, ApplinkConst.PRODUCT_DRAFT)
            }
            tvDraftProduct.visibility = View.VISIBLE
        }
    }

    private fun onDraftCountLoadError() {
        // delete all draft when error loading draft
        productDraftListCountViewModel.clearAllDraft()
        tvDraftProduct.visibility = View.GONE
    }

    private fun getDefaultFilterOptionsFromArguments() {
        val filterOptionKeys: List<String> = arguments?.getStringArrayList(FILTER_OPTIONS).orEmpty()
        val filterOptions: List<FilterOption> = FilterMapper.mapKeysToFilterOptionList(filterOptionKeys)
        super.setDefaultFilterOptions(filterOptions)
    }

    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        activity?.let {
            val manager = it.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            if (manager.getRunningServices(Integer.MAX_VALUE) != null) {
                for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
                    if (serviceClass.name == service.service.className) {
                        return true
                    }
                }
                return false
            } else {
                return false
            }
        }
        return false
    }

    private fun registerDraftReceiver() {
        draftBroadCastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (intent.action == UploadProductService.ACTION_DRAFT_CHANGED || intent.action == TkpdState.ProductService.BROADCAST_ADD_PRODUCT) {
                    productDraftListCountViewModel.getAllDraftCount()
                }
            }
        }

        activity?.let {
            val intentFilters = IntentFilter().apply {
                addAction(UploadProductService.ACTION_DRAFT_CHANGED)
                addAction(TkpdState.ProductService.BROADCAST_ADD_PRODUCT)
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
            when(it) {
                is Success -> onDraftCountLoaded(it.data)
                is Fail -> onDraftCountLoadError()
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