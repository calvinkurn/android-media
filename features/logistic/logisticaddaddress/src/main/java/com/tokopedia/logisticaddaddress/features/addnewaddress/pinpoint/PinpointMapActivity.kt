package com.tokopedia.logisticaddaddress.features.addnewaddress.pinpoint

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.google.android.gms.location.LocationServices
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.locationmanager.DeviceLocation
import com.tokopedia.locationmanager.LocationDetectorHelper
import com.tokopedia.logisticaddaddress.AddressConstants.*
import com.tokopedia.logisticaddaddress.R

/**
 * Created by fwidjaja on 2019-05-07.
 */
class PinpointMapActivity: BaseSimpleActivity() {
    private val FINISH_FLAG = 1212
    var SCREEN_NAME = "PinpointMapActivity"

    companion object {
        val defaultLat: Double by lazy { -6.175794 }
        val defaultLong: Double by lazy { 106.826457 }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
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
}