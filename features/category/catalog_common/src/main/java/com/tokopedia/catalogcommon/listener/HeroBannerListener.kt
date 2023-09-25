package com.tokopedia.catalogcommon.listener

interface HeroBannerListener {
    fun onNavBackClicked()
    fun onNavShareClicked()
    fun onNavMoreMenuClicked()
    fun onHeroBannerImpression(element: Int, orEmpty: List<String>, brandImageUrl: List<String>)
}
