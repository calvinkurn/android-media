package com.tokopedia.discovery.hotlistRevamp.view.interfaces

import com.tokopedia.discovery.hotlistRevamp.data.cpmAds.CpmItem

interface CpmTopAdsListener {
    fun onCpmClicked(trackerUrl: String, item: CpmItem)
    fun onCpmImpression(item: CpmItem)
}