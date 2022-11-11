package com.tokopedia.privacycenter.main.section.privacypolicy.webview

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.privacycenter.main.section.privacypolicy.PrivacyPolicyConst.DEFAULT_TITLE
import com.tokopedia.webview.BaseSimpleWebViewActivity

class PrivacyPolicyWebViewActivity : BaseSimpleWebViewActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateTitle(intent.extras?.getString(KEY_TITLE, DEFAULT_TITLE))
    }

    override fun getNewFragment(): Fragment = PrivacyPolicyWebViewFragment.instance(intent.extras)

    companion object {
        const val KEY_TITLE = "keyTitle"
    }
}
