package com.tokopedia.product.manage.list.view.fragment

import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.core.analytics.AppEventTracking
import com.tokopedia.product.manage.item.common.di.component.ProductComponent
import com.tokopedia.product.manage.item.main.base.view.service.UploadProductService
import com.tokopedia.product.manage.list.R
import com.tokopedia.product.manage.list.di.DaggerProductDraftListCountComponent
import com.tokopedia.product.manage.list.di.ProductDraftListCountModule
import com.tokopedia.seller.product.draft.view.activity.ProductDraftListActivity
import com.tokopedia.seller.product.draft.view.listener.ProductDraftListCountView
import com.tokopedia.seller.product.draft.view.presenter.ProductDraftListCountPresenter
import com.tokopedia.track.TrackApp
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
            tvDraftProductInfo.text = MethodChecker.fromHtml(getString(R.string.product_manage_you_have_x_unfinished_product, rowCount))
            tvDraftProductInfo.setOnClickListener {
                eventManageProductClicked(AppEventTracking.EventLabel.DRAFT_PRODUCT)
                startActivity(Intent(activity, ProductDraftListActivity::class.java))
            }
            tvDraftProductInfo.visibility = View.VISIBLE
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_manage_product_seller, container, false)
    }

    override fun onDraftCountLoadError() {
        // delete all draft when error loading draft
        productDraftListCountPresenter.clearAllDraft()
        tvDraftProductInfo.visibility = View.GONE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvDraftProductInfo = view.findViewById(R.id.tv_draft_product)
        tvDraftProductInfo.visibility = View.GONE
    }

    override fun initInjector() {
        super.initInjector()
        DaggerProductDraftListCountComponent
                .builder()
                .productDraftListCountModule(ProductDraftListCountModule())
                .productComponent(getComponent(ProductComponent::class.java))
                .build()
                .inject(this)
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

    private fun eventManageProductClicked(label: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                AppEventTracking.Event.CLICK_MANAGE_PRODUCT,
                AppEventTracking.Category.MANAGE_PRODUCT,
                AppEventTracking.Action.CLICK,
                label)
    }

    override fun onErrorGetPopUp(e: Throwable) {
        onSuccessGetPopUp(false, "")
    }


}