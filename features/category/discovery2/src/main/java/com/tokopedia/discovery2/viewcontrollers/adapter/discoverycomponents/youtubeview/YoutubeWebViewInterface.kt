package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.youtubeview

import android.webkit.JavascriptInterface
import androidx.fragment.app.Fragment
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment

class YoutubeWebViewInterface(val fragment: Fragment, val videoId: String, private val videoName: String) {

    @JavascriptInterface
    fun onStateChanged(time: Int) {
        (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()?.trackClickVideo(videoId, videoName, time.toString())
    }
}