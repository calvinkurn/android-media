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
import com.tokopedia.unifycomponents.R as unifyR
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

/**
 * Created by jegul on 02/09/20
 */
class PortraitChatListHeightManager(
        container: ViewGroup,
        private val chatListHeightMap: MutableMap<ChatHeightMapKey, ChatHeightMapValue>
) : ChatListHeightManager {

    private val pinnedMessageView: View = container.findViewById(R.id.view_pinned)
    private val productFeaturedView: View = container.findViewById(R.id.view_product_featured)
    private val rvChatList: MaximumHeightRecyclerView = container.findViewById(R.id.rv_chat_list)
    private val sendChatView: View = container.findViewById(R.id.view_send_chat)
    private val immersiveBoxView: View = container.findViewById(R.id.v_immersive_box)
    private val quickReplyView: View = container.findViewById(R.id.rv_quick_reply)

    private val videoChatMargin = container.resources.getDimensionPixelOffset(R.dimen.play_landscape_video_chat_margin)
    private val chatPinnedMargin = container.resources.getDimensionPixelOffset(unifyR.dimen.spacing_lvl3)
    private val reservedMargin = container.resources.getDimensionPixelOffset(unifyR.dimen.spacing_lvl3)
    private val maxVerticalChatHeight = container.resources.getDimension(R.dimen.play_chat_vertical_max_height)
    private val differencesHorizontalChatMode = container.resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl6)

    private var sendChatTopNonChatMode: Int? = null
    private var immersiveBoxBottomNonChatMode: Int? = null

    override suspend fun invalidateHeightNonChatMode(
            videoOrientation: VideoOrientation,
            videoPlayer: PlayVideoPlayerUiModel,
            forceInvalidate: Boolean,
            hasProductFeatured: Boolean
    ) {
        val key = getKey(videoOrientation, null, null, hasProductFeatured)
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
        val key = getKey(videoOrientation, maxTopPosition, hasQuickReply, null)
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
        val immersiveBoxLayout = asyncCatchError(block = {
            if (immersiveBoxView.visibility == View.VISIBLE) measureWithTimeout { immersiveBoxView.awaitMeasured() }
        }) {}
        val pinnedViewLayout = asyncCatchError(block = {
            if (pinnedMessageView.visibility == View.VISIBLE) measureWithTimeout {
                if (forceInvalidation) pinnedMessageView.awaitNextGlobalLayout()
                pinnedMessageView.awaitMeasured()
            }
        }) {}
        val sendChatViewLayout = asyncCatchError(block = {
            if (sendChatView.visibility == View.VISIBLE) measureWithTimeout { sendChatView.awaitMeasured() }
        }) {}
        val productFeaturedViewLayout = asyncCatchError(block = {
            if (productFeaturedView.visibility == View.VISIBLE) measureWithTimeout { productFeaturedView.awaitMeasured() }
        }) {}

        awaitAll(immersiveBoxLayout, pinnedViewLayout, sendChatViewLayout, productFeaturedViewLayout)

        val suggestedBottomBounds = sendChatView.globalVisibleRect.top
        val suggestedTopBounds = immersiveBoxView.globalVisibleRect.bottom
        val (bottomBounds, topBounds) = if (suggestedBottomBounds < suggestedTopBounds) {
            val cachedBottomBounds = sendChatTopNonChatMode ?: error("Bottom bounds is invalid and no cache")
            val cachedTopBounds = immersiveBoxBottomNonChatMode ?: error("Top bounds is invalid and no cache")
            cachedBottomBounds to cachedTopBounds
        } else {
            if (sendChatTopNonChatMode == null) sendChatTopNonChatMode = suggestedBottomBounds
            if (immersiveBoxBottomNonChatMode == null) immersiveBoxBottomNonChatMode = suggestedTopBounds
            suggestedBottomBounds to suggestedTopBounds
        }

        val nonOffsetOccupiedHeight = productFeaturedView.visibleHeight + pinnedMessageView.visibleHeight
        val offsetOccupiedHeight = productFeaturedView.marginLp.bottomMargin + pinnedMessageView.marginLp.bottomMargin

        val maxHeight = (bottomBounds - topBounds) - nonOffsetOccupiedHeight - offsetOccupiedHeight - videoChatMargin - chatPinnedMargin - reservedMargin

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

    private fun getKey(videoOrientation: VideoOrientation, maxTop: Int?, hasQuickReply: Boolean?, hasProductFeatured: Boolean?)
            = ChatHeightMapKey(videoOrientation, maxTop, hasQuickReply, hasProductFeatured)

    companion object {
        private const val CONSISTENCY_THRESHOLD = 5
    }
}