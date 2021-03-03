package com.tokopedia.play.view.measurement.bounds.manager.chatlistheight

import android.view.View
import android.view.ViewGroup
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.play.R
import com.tokopedia.play.view.custom.MaximumHeightRecyclerView
import com.tokopedia.play.view.type.VideoOrientation
import com.tokopedia.play.view.uimodel.recom.PlayVideoPlayerUiModel
import com.tokopedia.play_common.util.extension.awaitMeasured
import com.tokopedia.play_common.util.extension.globalVisibleRect
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlin.math.abs

/**
 * Created by jegul on 02/09/20
 */
class PortraitChatListHeightManager(
        container: ViewGroup,
        private val chatListHeightMap: MutableMap<ChatHeightMapKey, Float>
) : ChatListHeightManager {

    private val pinnedView: View = container.findViewById(R.id.view_pinned)
    private val rvChatList: MaximumHeightRecyclerView = container.findViewById(R.id.rv_chat_list)
    private val sendChatView: View = container.findViewById(R.id.view_send_chat)
    private val immersiveBoxView: View = container.findViewById(R.id.v_immersive_box)
    private val quickReplyView: View = container.findViewById(R.id.rv_quick_reply)

    private val videoChatMargin = container.resources.getDimensionPixelOffset(R.dimen.play_landscape_video_chat_margin)
    private val maxVerticalChatHeight = container.resources.getDimension(R.dimen.play_chat_vertical_max_height)

    override suspend fun invalidateHeightNonChatMode(
            videoOrientation: VideoOrientation,
            videoPlayer: PlayVideoPlayerUiModel
    ) {
        val key = getKey(videoOrientation, null, null)
        val value = chatListHeightMap[key]
        if (value.orZero() > 0f) {
            rvChatList.setMaxHeight(value!!)
            return
        }

        try {
            val measuredHeight = if (videoOrientation.isHorizontal) measureHorizontalVideoNonChatMode()
            else measurePinnedVerticalVideo()

            chatListHeightMap[key] = measuredHeight
            rvChatList.setMaxHeight(measuredHeight)
        } catch (e: Throwable) {}
    }

    override suspend fun invalidateHeightChatMode(videoOrientation: VideoOrientation, videoPlayer: PlayVideoPlayerUiModel, maxTopPosition: Int, hasQuickReply: Boolean) {
        val key = getKey(videoOrientation, maxTopPosition, hasQuickReply)
        val value = chatListHeightMap[key]
        if (value.orZero() > 0f) {
            rvChatList.setMaxHeight(value!!)
            return
        }

        try {
            val measuredHeight = if (videoOrientation.isHorizontal) measureHorizontalVideoChatMode(maxTopPosition, hasQuickReply)
            else measurePinnedVerticalVideo()

            chatListHeightMap[key] = measuredHeight
            rvChatList.setMaxHeight(measuredHeight)
        } catch (e: Throwable) {}
    }

    private suspend fun measureHorizontalVideoNonChatMode(): Float = coroutineScope {
        val immersiveBoxLayout = async { immersiveBoxView.awaitMeasured() }
        val pinnedViewLayout = async { pinnedView.awaitMeasured() }
        val rvChatLayout = async { sendChatView.awaitMeasured() }

        awaitAll(immersiveBoxLayout, pinnedViewLayout, rvChatLayout)

        val immersiveBoxBottom = immersiveBoxView.globalVisibleRect.bottom
        val pinnedViewHeight = pinnedView.height
        val pinnedMargin = pinnedView.layoutParams as ViewGroup.MarginLayoutParams

        val maxHeight = abs(sendChatView.globalVisibleRect.top - (immersiveBoxBottom + pinnedViewHeight + videoChatMargin + pinnedMargin.bottomMargin))

        maxHeight.toFloat()
    }

    private suspend fun measureHorizontalVideoChatMode(maxTopPosition: Int, hasQuickReply: Boolean) = coroutineScope {
        val rvChatLayout = async { sendChatView.awaitMeasured() }

        val quickReplyViewTotalHeight = run {
            val height = if (hasQuickReply) {
                if (quickReplyView.height <= 0) {
                    quickReplyView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
                    quickReplyView.measuredHeight
                } else quickReplyView.height
            } else 0
            val marginLp = quickReplyView.layoutParams as ViewGroup.MarginLayoutParams
            height + marginLp.bottomMargin + marginLp.topMargin
        }

        awaitAll(rvChatLayout)

        val maxHeight = abs(sendChatView.globalVisibleRect.top - (maxTopPosition + videoChatMargin + quickReplyViewTotalHeight))

        maxHeight.toFloat()
    }

    private suspend fun measurePinnedVerticalVideo(): Float = coroutineScope {
        rvChatList.setMaxHeight(maxVerticalChatHeight)
        maxVerticalChatHeight
    }

    private fun getKey(videoOrientation: VideoOrientation, maxTop: Int?, hasQuickReply: Boolean?)
            = ChatHeightMapKey(videoOrientation, maxTop, hasQuickReply)
}