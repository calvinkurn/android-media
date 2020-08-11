package com.tokopedia.sellerhome.settings.view.bottomsheet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.centralizedpromo.constant.CentralizedPromoUrl
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.di.component.DaggerSellerHomeComponent
import com.tokopedia.sellerhome.settings.analytics.SettingFreeShippingTracker
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.user.session.UserSessionInterface
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
    lateinit var userSession: UserSessionInterface

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
            SettingFreeShippingTracker.trackFreeShippingDetailClick(userSession)
            RouteManager.route(context, ApplinkConstInternalGlobal.WEBVIEW,
                CentralizedPromoUrl.URL_FREE_SHIPPING_INTERIM_PAGE)
            dismiss()
        }
    }

    fun show(fm: FragmentManager?) {
        fm?.let { show(it, TAG) }
    }

    private fun initInjector() {
        DaggerSellerHomeComponent.builder()
            .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }
}