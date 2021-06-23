package com.tokopedia.play.view.measurement.bounds.manager.chatlistheight

import android.view.View
import android.view.ViewGroup
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.play.R
import com.tokopedia.play.util.measureWithTimeout
import com.tokopedia.play.view.custom.MaximumHeightRecyclerView
import com.tokopedia.play.view.type.VideoOrientation
import com.tokopedia.play.view.uimodel.recom.PlayVideoPlayerUiModel
import com.tokopedia.play_common.util.extension.*
import kotlinx.coroutines.*

/**
 * Created by jegul on 02/09/20
 */
class PortraitChatListHeightManager(
        container: ViewGroup,
        private val chatListHeightMap: MutableMap<ChatHeightMapKey, ChatHeightMapValue>
) : ChatListHeightManager {

    private val pinnedMessageView: View = container.findViewById(R.id.view_pinned)
    private val pinnedVoucherView: View = container.findViewById(R.id.view_pinned_voucher)
    private val productFeaturedView: View = container.findViewById(R.id.view_product_featured)
    private val rvChatList: MaximumHeightRecyclerView = container.findViewById(R.id.rv_chat_list)
    private val sendChatView: View = container.findViewById(R.id.view_send_chat)
    private val immersiveBoxView: View = container.findViewById(R.id.v_immersive_box)
    private val quickReplyView: View = container.findViewById(R.id.rv_quick_reply)

    private val videoChatMargin = container.resources.getDimensionPixelOffset(R.dimen.play_landscape_video_chat_margin)
    private val maxVerticalChatHeight = container.resources.getDimension(R.dimen.play_chat_vertical_max_height)
    private val differencesHorizontalChatMode = container.resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl6)

    override suspend fun invalidateHeightNonChatMode(
            videoOrientation: VideoOrientation,
            videoPlayer: PlayVideoPlayerUiModel,
            forceInvalidate: Boolean,
            hasProductFeatured: Boolean,
            hasPinnedVoucher: Boolean
    ) {
        val key = getKey(videoOrientation, null, null, hasProductFeatured, hasPinnedVoucher)
        val value = chatListHeightMap[key]
        if (value != null && value.height.orZero() > 0f && value.consistency >= CONSISTENCY_THRESHOLD && !forceInvalidate) {
            rvChatList.setMaxHeight(value.height)
            return
        }

        try {
            val measuredHeight = if (videoOrientation.isHorizontal) measureHorizontalVideoNonChatMode(forceInvalidate)
            else measurePinnedVerticalVideo()

            if (!forceInvalidate) {
                val currentHeight = chatListHeightMap[key]
                chatListHeightMap[key] = if (currentHeight != null && currentHeight.height == measuredHeight) {
                    currentHeight.copy(consistency = currentHeight.consistency + 1)
                } else {
                    ChatHeightMapValue(measuredHeight, consistency = 0)
                }
            }

            rvChatList.setMaxHeight(measuredHeight)
        } catch (e: Throwable) {}
    }

    override suspend fun invalidateHeightChatMode(videoOrientation: VideoOrientation, videoPlayer: PlayVideoPlayerUiModel, maxTopPosition: Int, hasQuickReply: Boolean) {
        val key = getKey(videoOrientation, maxTopPosition, hasQuickReply, null, null)
        val value = chatListHeightMap[key]
        if (value != null && value.height.orZero() > 0f && value.consistency >= CONSISTENCY_THRESHOLD) {
            rvChatList.setMaxHeight(value.height)
            return
        }

        try {
            val measuredHeight = if (videoOrientation.isHorizontal) measureHorizontalVideoChatMode(maxTopPosition, hasQuickReply)
            else measurePinnedVerticalVideo()

            val currentHeight = chatListHeightMap[key]
            chatListHeightMap[key] = if (currentHeight != null && currentHeight.height == measuredHeight) {
                currentHeight.copy(consistency = currentHeight.consistency + 1)
            } else {
                ChatHeightMapValue(measuredHeight, consistency = 0)
            }
            rvChatList.setMaxHeight(measuredHeight)
        } catch (e: Throwable) {}
    }

    private suspend fun measureHorizontalVideoNonChatMode(forceInvalidation: Boolean): Float = coroutineScope {
        val immersiveBoxLayout = asyncCatchError(block = { measureWithTimeout { immersiveBoxView.awaitMeasured() } }) {}
        val pinnedViewLayout = asyncCatchError(block = {
            measureWithTimeout {
                if (forceInvalidation) pinnedMessageView.awaitNextGlobalLayout()
                pinnedMessageView.awaitMeasured()
            }
        }) {}
        val sendChatViewLayout = asyncCatchError(block = { measureWithTimeout { sendChatView.awaitMeasured() } }) {}
        val productFeaturedViewLayout = asyncCatchError(block = { measureWithTimeout { productFeaturedView.awaitMeasured() } }) {}
        val pinnedVoucherViewLayout = asyncCatchError(block = { measureWithTimeout { pinnedVoucherView.awaitMeasured() } }) {}

        awaitAll(immersiveBoxLayout, pinnedViewLayout, sendChatViewLayout, productFeaturedViewLayout, pinnedVoucherViewLayout)

        val bottomBounds = sendChatView.globalVisibleRect.top
        val topBounds = immersiveBoxView.globalVisibleRect.bottom
        val nonOffsetOccupiedHeight = productFeaturedView.visibleHeight + pinnedVoucherView.visibleHeight + pinnedMessageView.visibleHeight
        val offsetOccupiedHeight = productFeaturedView.marginLp.bottomMargin + pinnedVoucherView.marginLp.bottomMargin + pinnedMessageView.marginLp.bottomMargin

        val maxHeight = (bottomBounds - topBounds) - nonOffsetOccupiedHeight - offsetOccupiedHeight - videoChatMargin

        maxHeight.toFloat()
    }

    private suspend fun measureHorizontalVideoChatMode(maxTopPosition: Int, hasQuickReply: Boolean) = coroutineScope {
        val sendChatViewLayout = asyncCatchError(block = { measureWithTimeout { sendChatView.awaitMeasured() } }) {}
        val quickReplyViewLayout = asyncCatchError(block = { measureWithTimeout { quickReplyView.awaitMeasured() } }) {}
        awaitAll(sendChatViewLayout, quickReplyViewLayout)

        val bottomBounds = sendChatView.globalVisibleRect.top
        val nonOffsetOccupiedHeight = if (hasQuickReply) quickReplyView.measuredHeight else 0
        val offsetOccupiedHeight = quickReplyView.marginLp.bottomMargin

        val maxHeight = (bottomBounds - maxTopPosition) - nonOffsetOccupiedHeight - offsetOccupiedHeight - differencesHorizontalChatMode

        maxHeight.toFloat()
    }

    private suspend fun measurePinnedVerticalVideo(): Float = coroutineScope {
        rvChatList.setMaxHeight(maxVerticalChatHeight)
        maxVerticalChatHeight
    }

    private fun getKey(videoOrientation: VideoOrientation, maxTop: Int?, hasQuickReply: Boolean?, hasProductFeatured: Boolean?, hasPinnedVoucher: Boolean?)
            = ChatHeightMapKey(videoOrientation, maxTop, hasQuickReply, hasProductFeatured, hasPinnedVoucher)

    companion object {
        private const val CONSISTENCY_THRESHOLD = 3
    }
}