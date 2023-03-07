package com.tokopedia.kol.feature.postdetail.view.adapter.viewholder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXCard
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXProduct
import com.tokopedia.kol.R
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.user.session.UserSession


class ContentDetailPostViewHolder(
    itemView: View,
    private val cdpListener: CDPListener
) : BaseViewHolder(itemView) {

    private val cdpView =
        itemView.findViewById<ContentDetailPostTypeViewHolder>(R.id.item_cdp_revamp_view_item)


    companion object {
        const val IMAGE_POST_LIKED_UNLIKED = "image_liked_unliked"
        const val IMAGE_POST_COMMENT_ADD_DELETE = "post_comment_add_delete"
        const val IMAGE_POST_FOLLOW_UNFOLLOW = "image_follow_unfollow"
        const val IMAGE_ITEM_IMPRESSED = "image_item_impressed"
        const val VOD_ITEM_IMPRESSED = "vod_item_impressed"
        private const val IMAGE_ASGC_CTA_IMPRESSED = "image_asgc_cta_impressed"
        const val PAYLOAD_REMINDER_BTN_STATUS_UPDATED = "reminder_btn_status_updated"

        fun create(
            parent: ViewGroup,
            cdpListener: CDPListener
        ) = ContentDetailPostViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.item_content_detail_view,
                    parent,
                    false,
                ),
            cdpListener
        )
    }
    fun bind(feedXCard: FeedXCard) {
        cdpView.bindData(cdpListener, adapterPosition, feedXCard, UserSession(itemView.context))
    }
    fun bindWithPayloads(feedXCard: FeedXCard, payloads: Bundle) {
        if (feedXCard == null) {
            itemView.hide()
            return
        }
        if (payloads.containsKey(IMAGE_ITEM_IMPRESSED) || payloads.containsKey(VOD_ITEM_IMPRESSED)) {
            if (feedXCard.media.size > feedXCard.lastCarouselIndex) {
                val media = feedXCard.media[feedXCard.lastCarouselIndex]
                if (feedXCard.isTypeVOD || feedXCard.isTypeLongVideo)
                    cdpView.playVOD(feedXCard)
                else if (media.isVideo || media.isImage)
                    cdpView.bindImageOnImpress()
            }
        } else if (payloads.containsKey(IMAGE_ASGC_CTA_IMPRESSED)) {
            cdpView.onCTAVisible(feedXCard)
        } else if (payloads.containsKey(PAYLOAD_REMINDER_BTN_STATUS_UPDATED)) {
            cdpView.onFSTReminderStatusUpdated()
        } else if (payloads.containsKey(IMAGE_POST_LIKED_UNLIKED)) {
            cdpView.bindLike(feedXCard)
        } else if (payloads.containsKey(IMAGE_POST_COMMENT_ADD_DELETE)) {
            cdpView.setCommentCount(feedXCard.comments)
        } else if (payloads.containsKey(IMAGE_POST_FOLLOW_UNFOLLOW)) {
            if (payloads.getBoolean(IMAGE_POST_FOLLOW_UNFOLLOW)) {
                cdpView.bindHeader(feedXCard)
            }
        }
    }
    interface CDPListener {
        fun onLikeClicked(feedXCard: FeedXCard, postPosition: Int, isDoubleTap: Boolean = false)
        fun onCommentClicked(feedXCard: FeedXCard, postPosition: Int, isSeeMoreComment: Boolean = false)
        fun onSharePostClicked(feedXCard: FeedXCard, postPosition: Int)
        fun onFollowUnfollowClicked(feedXCard: FeedXCard, postPosition: Int, isFollowedFromRSRestrictionBottomSheet: Boolean = false)
        fun onClickOnThreeDots(feedXCard: FeedXCard, postPosition: Int)
        fun onFullScreenButtonClicked(
            feedXCard: FeedXCard,
            postPosition: Int,
            currentTime: Long,
            shouldTrack: Boolean,
            isFullScreenBtn: Boolean
        )

        fun onShopHeaderItemClicked(feedXCard: FeedXCard, isShopNameBelow: Boolean = false)
        fun addViewsToVOD(
            feedXCard: FeedXCard,
            rowNumber: Int,
            time: Long,
            hitTrackerApi: Boolean
        )

        fun onLihatProdukClicked(
            feedXCard: FeedXCard,
            postPosition: Int,
            products: List<FeedXProduct>
        )

        fun onCekSekarangButtonClicked(
            feedXCard: FeedXCard,
            postPosition: Int
        )

        fun onPostTagBubbleClicked(
            positionInFeed: Int,
            redirectUrl: String,
            postTagItem: FeedXProduct,
        )
        fun onCarouselItemImpressed(feedXCard: FeedXCard, postPosition: Int)
        fun onPostImpressed(feedXCard: FeedXCard, postPosition: Int)
        fun onImageClicked(feedXCard: FeedXCard)
        fun onReadMoreClicked(feedXCard: FeedXCard)
        fun onHashtagClicked(hashTag: String, feedXCard: FeedXCard)
        fun onVolumeClicked(feedXCard: FeedXCard, mute: Boolean, mediaType: String)
        fun onVideoStopTrack(feedXCard: FeedXCard, duration: Long)
        fun onSgcVideoTapped(feedXCard: FeedXCard)
        fun sendWatchVODTracker(feedXCard: FeedXCard, duration: Long)
        fun onIngatkanSayaBtnImpressed(card: FeedXCard, positionInFeed: Int)
        fun onIngatkanSayaBtnClicked(card: FeedXCard, positionInFeed: Int)
        fun changeUpcomingWidgetToOngoing(card: FeedXCard, positionInFeed: Int)
        fun removeOngoingCampaignSaleWidget(card: FeedXCard, positionInFeed: Int)
    }

}
