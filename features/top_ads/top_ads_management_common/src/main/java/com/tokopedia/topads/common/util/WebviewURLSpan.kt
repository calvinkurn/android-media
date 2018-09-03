package com.tokopedia.topads.common.util

import android.text.style.URLSpan
import android.view.View
import com.tokopedia.topads.common.TopAdsWebViewActivity

open class WebviewURLSpan(url: String?) : URLSpan(url){
    override fun onClick(widget: View?) {
        widget?.context?.run {
            startActivity(TopAdsWebViewActivity.createIntent(this, url))
        }
    }
}