package com.tokopedia.seller.menu.common.view.bottomsheet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.seller.menu.common.R
import com.tokopedia.seller.menu.common.di.component.DaggerSellerMenuCommonComponent
import com.tokopedia.seller.menu.common.analytics.SettingFreeShippingTracker
import com.tokopedia.seller.menu.common.constant.SellerMenuFreeShippingUrl
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.bottom_sheet_settings_free_shipping.*
import javax.inject.Inject

class SettingsFreeShippingBottomSheet: BottomSheetUnify() {

    companion object {
        val TAG: String = SettingsFreeShippingBottomSheet::class.java.simpleName

        fun createInstance(): SettingsFreeShippingBottomSheet {
            return SettingsFreeShippingBottomSheet()
        }
    }

    @Inject
    lateinit var freeShippingTracker: SettingFreeShippingTracker

    override fun onCreate(savedInstanceState: Bundle?) {
        initInjector()
        super.onCreate(savedInstanceState)
        val itemView = View.inflate(context,
            R.layout.bottom_sheet_settings_free_shipping, null)

        setChild(itemView)
        setTitle(getString(R.string.settings_free_shipping_title))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnFreeShippingDetail.setOnClickListener {
            freeShippingTracker.trackFreeShippingDetailClick()
            RouteManager.route(context, ApplinkConstInternalGlobal.WEBVIEW,
                SellerMenuFreeShippingUrl.URL_FREE_SHIPPING_INTERIM_PAGE)
            dismiss()
        }
    }

    fun show(fm: FragmentManager?) {
        fm?.let { show(it, TAG) }
    }

    private fun initInjector() {
        DaggerSellerMenuCommonComponent.builder()
            .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }
}