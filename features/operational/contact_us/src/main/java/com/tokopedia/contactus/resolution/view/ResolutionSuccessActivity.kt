package com.tokopedia.contactus.resolution.view

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity

class ResolutionSuccessActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment {
        val url = intent.data?.getQueryParameter(KEY_URL) ?: ""
        return ResolutionSuccessFragment.createNewInstance(url)
    }

    companion object {
        private const val KEY_URL = "url"
    }
}
