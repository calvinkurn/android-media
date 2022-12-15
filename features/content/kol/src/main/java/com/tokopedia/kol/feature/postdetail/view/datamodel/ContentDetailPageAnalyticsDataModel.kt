package com.tokopedia.kol.feature.postdetail.view.datamodel

import com.tokopedia.feedcomponent.data.feedrevamp.FeedXProduct
import com.tokopedia.feedcomponent.domain.mapper.TYPE_FEED_X_CARD_PLAY
import com.tokopedia.feedcomponent.domain.mapper.TYPE_FEED_X_CARD_POST
import com.tokopedia.feedcomponent.domain.mapper.TYPE_FEED_X_CARD_PRODUCT_HIGHLIGHT
import com.tokopedia.feedcomponent.domain.mapper.TYPE_VIDEO


data class ContentDetailPageAnalyticsDataModel(
    var activityId: String = "",
    val shopId: String = "",
    val shopName: String = "",
    val itemName: String = "",
    val rowNumber: Int = 0,
    val mediaType: String = "",
    val mediaUrl: String = "",
    val productId: String = "",
    val channelId: String = "", //used for VOD only
    val duration: Long = 0L, //used for Videos only,
    val feedXProduct: FeedXProduct = FeedXProduct(),
    val productPosition: Int = 0,
    val isMute: Boolean = false,
    val source: String = "",
    val isFollowed: Boolean = false,
    val type: String = "",
    val trackerId: String = "",
    val shareMedia: String = "",
    val hashtag: String = "",
    val authorType: String
) {
    val isTypeVOD: Boolean
        get() = type == TYPE_FEED_X_CARD_PLAY
    val isTypeASGC: Boolean
        get() = type == TYPE_FEED_X_CARD_PRODUCT_HIGHLIGHT
    val isTypeSGCVideo: Boolean
        get() = type == TYPE_FEED_X_CARD_POST && mediaType == TYPE_VIDEO

    companion object {
        const val POST_ITEM_NAME = "/feed - content detail page"
    }
}
