package com.tokopedia.product.detail.webview

import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.showImmediately

class ProductWebViewActivity : BaseSimpleActivity() {
    override fun getNewFragment(): Fragment? = null

    override fun getLayoutRes(): Int {
        return R.layout.product_educational_activity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adjustOrientation()
        val uri = intent.data ?: return
        val url = ""
        showImmediately(supportFragmentManager, ProductWebViewBottomSheet.TAG) {
            ProductWebViewBottomSheet.instance(
                url = "https://www.tokopedia.com/help/article/faq-seputar-jaminan-pasti-ori"
            )
        }
    }

    private fun adjustOrientation() {
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }
}
