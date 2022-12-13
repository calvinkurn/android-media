package com.tokopedia.privacycenter.ui.privacypolicywebview

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.privacycenter.ui.main.section.privacypolicy.PrivacyPolicyConst
import com.tokopedia.webview.BaseSimpleWebViewActivity

class PrivacyPolicyWebViewActivity : BaseSimpleWebViewActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateTitle(
            intent.extras?.getString(
                PrivacyPolicyConst.KEY_TITLE,
                PrivacyPolicyConst.DEFAULT_TITLE
            )
        )
    }

    override fun getNewFragment(): Fragment = PrivacyPolicyWebViewFragment.instance(intent.extras)
}
