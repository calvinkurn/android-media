package com.tokopedia.sellerhome.settings.view.bottomsheet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.centralizedpromo.constant.CentralizedPromoUrl
import com.tokopedia.sellerhome.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.bottom_sheet_settings_free_shipping.*

class SettingsFreeShippingBottomSheet: BottomSheetUnify() {

    companion object {
        val TAG: String = SettingsFreeShippingBottomSheet::class.java.simpleName

        fun createInstance(): SettingsFreeShippingBottomSheet {
            return SettingsFreeShippingBottomSheet()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val itemView = View.inflate(context,
            R.layout.bottom_sheet_settings_free_shipping, null)

        setChild(itemView)
        setTitle(getString(R.string.settings_free_shipping_title))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnFreeShippingDetail.setOnClickListener {
            RouteManager.route(context, ApplinkConstInternalGlobal.WEBVIEW,
                CentralizedPromoUrl.URL_FREE_SHIPPING_INTERIM_PAGE)
            dismiss()
        }
    }

    fun show(fm: FragmentManager?) {
        fm?.let { show(it, TAG) }
    }
}