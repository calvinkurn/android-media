package com.tokopedia.officialstore.official.presentation.listener

interface BannerListener {

    fun putEEToTrackingQueue(categoryName: String, bannerId: String, bannerPosition: Int, bannerName: String, imageUrl: String)

}