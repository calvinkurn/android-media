package com.tokopedia.product.detail.bseducational.view

import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.product.detail.R
import timber.log.Timber

/**
 * RouteManager.route(this, ApplinkConst.PRODUCT_EDUCATIONAL, type)
 */
class ProductEducationalActivity : BaseSimpleActivity() {
    override fun getNewFragment(): Fragment? = null

    override fun getLayoutRes(): Int {
        return R.layout.product_educational_activity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val uri = intent.data
        val type = if (uri != null) {
            uri.lastPathSegment ?: ""
        } else {
            ""
        }

        super.onCreate(savedInstanceState)
        try {
            window.setDimAmount(0f)
        } catch (th: Throwable) {
            Timber.e(th)
        }

        adjustOrientation()
        ProductEducationalBottomSheet().show(type, supportFragmentManager)
    }

    private fun adjustOrientation() {
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }
}