package com.tokopedia.kol.feature.postdetail.view.datamodel

import com.tokopedia.feedcomponent.data.feedrevamp.FeedXProduct


data class ContentDetailPageAnalyticsDataModel(
    var activityId: String = "",
    val shopId: String = "",
    val shopName: String = "",
    val rowNumber: Int = 0,
    val mediaType: String = "",
    val mediaUrl:String = "",
    val productId: String = "",
    val channelId: String = "", //used for VOD only
    val duration: Long = 0L, //used for Videos only,
    val feedXProduct: FeedXProduct = FeedXProduct(),
    val productPosition :Int = 0,
    val isMute: Boolean = false,
    val source: String = "",
    val isFollowed: Boolean = false,
    val type: String = "",
    val trackerId: String = "",
    val shareMedia: String= "",
    val hashtag :String = ""
) {
    companion object {
        const val POST_ITEM_NAME = "/feed - content detail page"
    }
}