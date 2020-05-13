package com.tokopedia.hotlist.interfaces

import com.tokopedia.hotlist.data.cpmAds.CpmItem

interface CpmTopAdsListener {
    fun onCpmClicked(applink: String,clickTrackerUrl:String, item: CpmItem)
    fun onCpmImpression(impressionTrackUrl: String)
}