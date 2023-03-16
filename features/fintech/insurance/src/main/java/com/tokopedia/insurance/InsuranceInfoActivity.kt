package com.tokopedia.insurance

import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.internal.ApplinkConstInternalFintech

class InsuranceInfoActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? = null

    override fun getLayoutRes(): Int {
        return R.layout.activity_insurance_info
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adjustOrientation()

        intent.extras?.let {
            val url = it.getString(ApplinkConstInternalFintech.PARAM_INSURANCE_INFO_URL) ?: ""
            if (url.isNotBlank()) {
                InsuranceInfoBottomSheet(url).show(supportFragmentManager, "")
            } else {
                finish()
            }
        }
    }

    private fun adjustOrientation() {
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }

}
