package com.tokopedia.feedplus.presentation.model.type

import com.tokopedia.feedplus.data.FeedXCard

/**
 * Created by meyta.taliti on 10/10/23.
 */
enum class FeedContentType {
    FollowRecommendation,
    TopAds,
    PlayLivePreview,
    PlayChannel,
    PlayShortVideo,
    ProductHighlight, // or ASGC (Automated Seller Generated Content)
    Image, // old feed post
    Video, // old feed post
    None;

    companion object {

        fun getType(typeName: String, type: String, mediaType: String): FeedContentType {
            if (typeName == FeedXCard.TYPE_FEED_X_CARD_PLACEHOLDER) {
                if (type == FeedXCard.TYPE_FEED_TOP_ADS) {
                    return TopAds
                } else if (type == FeedXCard.TYPE_FEED_FOLLOW_RECOM) {
                    return FollowRecommendation
                }
            } else if (typeName == FeedXCard.TYPE_FEED_X_CARD_PLAY) {
                when (type) {
                    FeedXCard.TYPE_FEED_PLAY_LIVE -> {
                        return PlayLivePreview
                    }
                    FeedXCard.TYPE_FEED_PLAY_CHANNEL -> {
                        return PlayChannel
                    }
                    FeedXCard.TYPE_FEED_PLAY_SHORT_VIDEO -> {
                        return PlayShortVideo
                    }
                }
            } else if (typeName == FeedXCard.TYPE_FEED_X_CARD_PRODUCTS_HIGHLIGHT) {
                return ProductHighlight
            } else if (typeName == FeedXCard.TYPE_FEED_X_CARD_POST) {
                return if (mediaType == FeedXCard.TYPE_MEDIA_VIDEO) {
                    Video
                } else {
                    Image
                }
            }
            return None
        }
    }
}

fun FeedContentType.isPlayContent(): Boolean {
    return this == FeedContentType.PlayLivePreview
        || this == FeedContentType.PlayChannel
        || this == FeedContentType.PlayShortVideo
}
