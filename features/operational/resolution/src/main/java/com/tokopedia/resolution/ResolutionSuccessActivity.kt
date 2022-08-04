package com.tokopedia.resolution

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.webview.KEY_URL

class ResolutionSuccessActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment {
        val url = intent.data?.getQueryParameter(KEY_URL) ?: ""
        return ResolutionSuccessFragment.createNewInstance(url)
    }
}
