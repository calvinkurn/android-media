package com.tokopedia.product.manage.list.view.fragment

import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.product.manage.item.main.base.view.service.UploadProductService
import com.tokopedia.product.manage.list.R
import com.tokopedia.product.manage.list.constant.DRAFT_PRODUCT
import com.tokopedia.product.manage.list.di.DaggerProductManageComponent
import com.tokopedia.product.manage.list.utils.ProductManageTracking
import com.tokopedia.seller.product.draft.view.activity.ProductDraftListActivity
import com.tokopedia.seller.product.draft.view.listener.ProductDraftListCountView
import com.tokopedia.seller.product.draft.view.presenter.ProductDraftListCountPresenter
import javax.inject.Inject

class ProductManageSellerFragment : ProductManageFragment(), ProductDraftListCountView {

    lateinit var tvDraftProductInfo: TextView

    lateinit var draftBroadCastReceiver: BroadcastReceiver

    @Inject
    lateinit var productDraftListCountPresenter: ProductDraftListCountPresenter

    override fun onDraftCountLoaded(rowCount: Long) {
        if (rowCount == 0L) {
            tvDraftProductInfo.visibility = View.GONE
        } else {
            tvDraftProductInfo.text = MethodChecker.fromHtml(getString(com.tokopedia.product.manage.list.R.string.product_manage_you_have_x_unfinished_product, rowCount))
            tvDraftProductInfo.setOnClickListener {
                ProductManageTracking.eventDraftClick(DRAFT_PRODUCT)
                startActivity(Intent(activity, ProductDraftListActivity::class.java))
            }
            tvDraftProductInfo.visibility = View.VISIBLE
        }
    }

    override fun getLayoutRes(): Int = R.layout.fragment_product_manage_seller

    override fun getRecyclerViewResourceId(): Int = R.id.recycler_view

    override fun getSearchInputViewResourceId(): Int = R.id.search_input_view_list

    override fun getSwipeRefreshLayoutResourceId(): Int = R.id.swipe_refresh_layout

    override fun onDraftCountLoadError() {
        // delete all draft when error loading draft
        productDraftListCountPresenter.clearAllDraft()
        tvDraftProductInfo.visibility = View.GONE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvDraftProductInfo = view.findViewById(com.tokopedia.product.manage.list.R.id.tv_draft_product)
        tvDraftProductInfo.visibility = View.GONE
    }

    override fun initInjector() {
        super.initInjector()
        activity?.let{
            val appComponent = (it.application as BaseMainApplication).baseAppComponent
            DaggerProductManageComponent.builder()
                    .baseAppComponent(appComponent)
                    .build()
                    .inject(this)
        }
        productDraftListCountPresenter.attachView(this)
        productManagePresenter.attachView(this)
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

    override fun onErrorGetPopUp(e: Throwable) {
        onSuccessGetPopUp(false, "")
    }


}