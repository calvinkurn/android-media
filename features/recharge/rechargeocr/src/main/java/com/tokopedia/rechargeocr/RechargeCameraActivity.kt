package com.tokopedia.rechargeocr

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity

/**
 * applink
 * tokopedia-android-internal://recharge/ocr
 * or
 * RouteManager.route(this, ApplinkConsInternalDigital.CAMERA_OCR)
 */

class RechargeCameraActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? {
        return RechargeCameraFragment.newInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar.visibility = View.GONE
    }


}