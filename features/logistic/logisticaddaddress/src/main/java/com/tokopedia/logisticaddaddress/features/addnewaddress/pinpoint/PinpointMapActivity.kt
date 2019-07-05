package com.tokopedia.logisticaddaddress.features.addnewaddress.pinpoint

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.google.android.gms.location.LocationServices
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.locationmanager.DeviceLocation
import com.tokopedia.locationmanager.LocationDetectorHelper
import com.tokopedia.logisticaddaddress.AddressConstants
import com.tokopedia.logisticaddaddress.AddressConstants.*
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.features.addnewaddress.analytics.AddNewAddressAnalytics
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.save_address.SaveAddressDataModel
import com.tokopedia.logisticdata.data.entity.address.Token

/**
 * Created by fwidjaja on 2019-05-07.
 */
class PinpointMapActivity : BaseSimpleActivity() {
    private val FINISH_FLAG = 1212
    var SCREEN_NAME = "PinpointMapActivity"

    companion object {
        @JvmStatic
        fun newInstance(context: Context, lat: Double?, long: Double?, isShowAutoComplete: Boolean, token: Token?, isPolygon: Boolean, districtId: Int?,
                        isMismatchSolved: Boolean, isMismatch: Boolean, saveAddressDataModel: SaveAddressDataModel?, isChangesRequested: Boolean): Intent =
                Intent(context, PinpointMapActivity::class.java).apply {
                    putExtra(KERO_TOKEN, token)
                    putExtra(EXTRA_LAT, lat)
                    putExtra(EXTRA_LONG, long)
                    putExtra(EXTRA_SHOW_AUTOCOMPLETE, isShowAutoComplete)
                    putExtra(EXTRA_IS_POLYGON, isPolygon)
                    putExtra(EXTRA_DISTRICT_ID, districtId)
                    putExtra(EXTRA_IS_MISMATCH, isMismatch)
                    putExtra(EXTRA_IS_MISMATCH_SOLVED, isMismatchSolved)
                    putExtra(EXTRA_SAVE_DATA_UI_MODEL, saveAddressDataModel)
                    putExtra(EXTRA_IS_CHANGES_REQUESTED, isChangesRequested)
                }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        /*this.window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)*/
        super.onCreate(savedInstanceState)
    }

    override fun getScreenName(): String {
        return SCREEN_NAME
    }

    override fun getLayoutRes(): Int = R.layout.activity_pinpoint_map

    override fun getNewFragment(): PinpointMapFragment? {
        var bundle = Bundle()
        if (intent.extras != null) {
            bundle = intent.extras
        } else {
            bundle.putDouble(EXTRA_LAT, MONAS_LAT)
            bundle.putDouble(EXTRA_LONG, MONAS_LONG)
            bundle.putBoolean(EXTRA_SHOW_AUTOCOMPLETE, true)
        }
        return PinpointMapFragment.newInstance(bundle)
    }

    override fun onStop() {
        setResult(FINISH_FLAG)
        super.onStop()
    }

    override fun onDestroy() {
        setResult(FINISH_FLAG)
        super.onDestroy()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        var isAllowed = false
        for (i in permissions.indices) {
            if (grantResults.isNotEmpty() && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                isAllowed = true
            }
        }

        if (isAllowed) {
            AddNewAddressAnalytics.eventClickButtonOkOnAllowLocation()
        } else {
            AddNewAddressAnalytics.eventClickButtonDoNotAllowOnAllowLocation()
        }

    }
}