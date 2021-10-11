package com.tokopedia.webview.download

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.webview.BaseSimpleWebViewActivity
import com.tokopedia.webview.KEY_URL

open class BaseDownloadAppLinkActivity : BaseSimpleWebViewActivity() {
    protected var extensions: String = "";
    protected var web_url: String = ""

    companion object {
        const val KEY_EXT = "ext"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val data = intent.data
        if (data != null) {
            web_url = data.getQueryParameter(KEY_URL) ?: ""
            extensions = data.getQueryParameter(KEY_EXT) ?: ""
        } else {
            finish()
        }
        super.onCreate(savedInstanceState)
    }


    override fun getNewFragment(): Fragment {
        return BaseDownloadWebViewFragment.newInstance(web_url, extensions)
    }

}
