package com.tokopedia.review.feature.reputationhistory.view.activity

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.webkit.WebView
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tokopedia.review.R

/**
 * @author normansyahputa on 3/21/17.
 */
class SellerReputationInfoActivity : BaseSimpleActivity() {

    private var webviewReputationInfo: WebView? = null

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setWhiteStatusBar()
        webviewReputationInfo = findViewById<View>(R.id.webview_reputation_review_info) as? WebView
        webviewReputationInfo?.settings?.javaScriptEnabled = true
        webviewReputationInfo?.loadUrl("file:///android_asset/poin-reputasi.html")
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_reputation_review_info
    }

    override fun getNewFragment(): Fragment? {
        return null
    }

    private fun setWhiteStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor =
                ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_NN0)
        }
    }
}