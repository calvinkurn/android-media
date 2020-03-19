package com.tokopedia.product.manage.feature.list.view.fragment

import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.list.di.ProductManageListComponent
import com.tokopedia.product.manage.item.main.base.view.service.UploadProductService
import com.tokopedia.product.manage.oldlist.constant.DRAFT_PRODUCT
import com.tokopedia.product.manage.oldlist.utils.ProductManageTracking
import com.tokopedia.product.manage.oldlist.view.listener.ProductDraftListCountView
import com.tokopedia.product.manage.oldlist.view.presenter.ProductDraftListCountPresenter
import kotlinx.android.synthetic.main.fragment_product_manage_seller.*
import javax.inject.Inject

class ProductManageSellerFragment : ProductManageFragment(), ProductDraftListCountView {

    private lateinit var draftBroadCastReceiver: BroadcastReceiver

    @Inject
    lateinit var productDraftListCountPresenter: ProductDraftListCountPresenter

    override fun onDraftCountLoaded(rowCount: Long) {
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

    override fun getLayoutRes(): Int = R.layout.fragment_product_manage_seller

    override fun getRecyclerViewResourceId(): Int = R.id.recycler_view

    override fun getSwipeRefreshLayoutResourceId(): Int = R.id.swipe_refresh_layout

    override fun onDraftCountLoadError() {
        // delete all draft when error loading draft
        productDraftListCountPresenter.clearAllDraft()
        tvDraftProduct.visibility = View.GONE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        checkLogin()
        super.onViewCreated(view, savedInstanceState)
        tvDraftProduct.visibility = View.GONE
    }

    override fun initInjector() {
        getComponent(ProductManageListComponent::class.java).inject(this)
        productDraftListCountPresenter.attachView(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        productDraftListCountPresenter.detachView()
    }

    override fun onResume() {
        super.onResume()
        registerDraftReceiver()
        if (isMyServiceRunning(UploadProductService::class.java)) {
            productDraftListCountPresenter.fetchAllDraftCount()
        } else {
            productDraftListCountPresenter.fetchAllDraftCountWithUpdateUploading()
        }
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
                if (intent.action == UploadProductService.ACTION_DRAFT_CHANGED) {
                    productDraftListCountPresenter.fetchAllDraftCount()
                }
            }
        }

        activity?.let {
            LocalBroadcastManager.getInstance(it).registerReceiver(
                    draftBroadCastReceiver, IntentFilter(UploadProductService.ACTION_DRAFT_CHANGED))
        }
    }

    private fun unregisterDraftReceiver() {
        activity?.let {
            LocalBroadcastManager.getInstance(it).unregisterReceiver(draftBroadCastReceiver)
        }
    }

    override fun onPause() {
        super.onPause()
        unregisterDraftReceiver()
    }

    override fun callInitialLoadAutomatically() = false

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