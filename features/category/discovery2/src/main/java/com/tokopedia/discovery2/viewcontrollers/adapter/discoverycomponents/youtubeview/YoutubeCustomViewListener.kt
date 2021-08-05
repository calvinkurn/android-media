package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.youtubeview

import android.view.View


interface YoutubeCustomViewListener {
    fun onShowCustomView(view: View)
    fun onHideCustomView()
    fun renderProcessKilled()
}