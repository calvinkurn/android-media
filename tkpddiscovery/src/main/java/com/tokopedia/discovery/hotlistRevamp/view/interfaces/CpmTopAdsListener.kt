package com.tokopedia.discovery.hotlistRevamp.view.interfaces

import com.tokopedia.discovery.hotlistRevamp.data.cpmAds.CpmItem

interface CpmTopAdsListener {
    fun onCpmClicked(applink: String,clickTrackerUrl:String, item: CpmItem)
    fun onCpmImpression(impressionTrackUrl: String)
}