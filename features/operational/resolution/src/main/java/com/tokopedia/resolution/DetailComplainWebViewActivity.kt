package com.tokopedia.resolution

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.webview.BaseSimpleWebViewActivity

class DetailComplainWebViewActivity : BaseSimpleWebViewActivity() {

    override fun createFragmentInstance(): Fragment {
        return DetailComplainWebviewFragment.instance(intent.extras)
    }

}