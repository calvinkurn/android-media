package com.tokopedia.shop.common.view.model

import com.tokopedia.shop.common.view.customview.bannerhotspot.HotspotBubbleView
import com.tokopedia.shop.common.view.customview.bannerhotspot.HotspotTagView

data class ImageHotspotData(
    val imageBannerUrl: String = "",
    val listHotspot: List<HotspotData> = listOf(),
){
    data class HotspotData(
        val x: Float = 0f,
        val y: Float = 0f,
        val productImage: String = "",
        val productName: String = "",
        val productPrice: String = "",
        var hotspotTagView: HotspotTagView? = null,
        var bubbleView: HotspotBubbleView? = null
    )
}
