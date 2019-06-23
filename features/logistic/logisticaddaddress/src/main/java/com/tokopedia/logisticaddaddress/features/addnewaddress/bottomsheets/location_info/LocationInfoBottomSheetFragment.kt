package com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.location_info

import android.content.Intent
import android.provider.Settings
import android.view.View
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.design.component.ButtonCompat
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.features.addnewaddress.analytics.AddNewAddressAnalytics

/**
 * Created by fwidjaja on 2019-06-18.
 */
class LocationInfoBottomSheetFragment : BottomSheets() {
    private var bottomSheetView: View? = null
    private lateinit var btnActivateLocation: ButtonCompat

    companion object {
        @JvmStatic
        fun newInstance(): LocationInfoBottomSheetFragment {
            return LocationInfoBottomSheetFragment()
        }
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.bottomsheet_location_info
    }

    override fun initView(view: View) {
        bottomSheetView = view
        btnActivateLocation = view.findViewById(R.id.btn_activate_location)
        btnActivateLocation.setOnClickListener {
            AddNewAddressAnalytics.eventClickButtonAktifkanLayananLokasiOnBlockGps()
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            dismiss()
        }
    }

    override fun title(): String {
        return getString(R.string.undetected_location)
    }

    override fun configView(parentView: View?) {
        super.configView(parentView)
        parentView?.findViewById<View>(R.id.layout_title)?.setOnClickListener(null)
        parentView?.findViewById<View>(R.id.btn_close)?.setOnClickListener {
            AddNewAddressAnalytics.eventClickButtonXOnBlockGps()
            onCloseButtonClick()
        }
    }
}