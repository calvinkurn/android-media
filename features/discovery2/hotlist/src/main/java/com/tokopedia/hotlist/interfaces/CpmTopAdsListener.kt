package com.tokopedia.hotlist.interfaces

import com.tokopedia.hotlist.data.cpmAds.CpmItem

interface CpmTopAdsListener {
    fun onCpmClicked(applink: String, clickTrackerUrl: String, item: CpmItem, id: String, name: String, image: String)
    fun onCpmImpression(impressionTrackUrl: String, id: String, name: String, image: String)
}