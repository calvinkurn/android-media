package com.tokopedia.play.view.measurement.bounds.manager

import android.view.View
import android.view.ViewGroup
import com.tokopedia.play.R
import com.tokopedia.play.view.custom.MaximumHeightRecyclerView
import com.tokopedia.play.view.type.VideoOrientation
import com.tokopedia.play.view.uimodel.Unknown
import com.tokopedia.play.view.uimodel.VideoPlayerUiModel
import com.tokopedia.play_common.util.extension.awaitMeasured
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

/**
 * Created by jegul on 01/09/20
 */
class PlayPinnedBoundsManager(
        container: ViewGroup
) : PinnedBoundsManager {

    private val pinnedView: View = container.findViewById(R.id.view_pinned)
    private val rvChatList: MaximumHeightRecyclerView = container.findViewById(R.id.rv_chat_list)
    private val immersiveBoxView: View = container.findViewById(R.id.v_immersive_box)

    private val offset16 = container.resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4)
    private val maxVerticalChatHeight = container.resources.getDimension(R.dimen.play_chat_vertical_max_height)

    private var mVideoOrientation: VideoOrientation = VideoOrientation.Unknown
    private var mVideoPlayer: VideoPlayerUiModel = Unknown

    override suspend fun invalidatePinnedBounds(
            videoOrientation: VideoOrientation,
            videoPlayer: VideoPlayerUiModel,
            isChatMode: Boolean
    ) = coroutineScope {
        if (videoOrientation == mVideoOrientation && videoPlayer == mVideoPlayer) return@coroutineScope

        mVideoOrientation = videoOrientation
        mVideoPlayer = videoPlayer

        if (videoOrientation.isHorizontal) layoutPinnedHorizontalVideo()
        else layoutPinnedVerticalVideo()
    }

    private suspend fun layoutPinnedHorizontalVideo() = coroutineScope {
        val immersiveBoxLayout = async { immersiveBoxView.awaitMeasured() }
        val pinnedViewLayout = async { pinnedView.awaitMeasured() }
        val rvChatLayout = async { rvChatList.awaitMeasured() }

        awaitAll(immersiveBoxLayout, pinnedViewLayout, rvChatLayout)

        val immersiveBoxBottom = immersiveBoxView.bottom
        val pinnedViewHeight = pinnedView.height

        val maxHeight =  immersiveBoxBottom + pinnedViewHeight - offset16 - rvChatList.bottom

        rvChatList.setMaxHeight(maxHeight.toFloat())
    }

    private suspend fun layoutPinnedVerticalVideo() = coroutineScope {
        rvChatList.setMaxHeight(maxVerticalChatHeight)
    }
}