package com.tokopedia.play.view.measurement.bounds.provider.videobounds

import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.common.utils.DisplayMetricUtils
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.play.R
import com.tokopedia.play.util.measureWithTimeout
import com.tokopedia.play.view.type.VideoOrientation
import com.tokopedia.play_common.util.extension.awaitLayout
import com.tokopedia.play_common.util.extension.awaitMeasured
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

/**
 * Created by jegul on 04/08/20
 */
class PortraitVideoBoundsProvider(
    private val container: ViewGroup
) : VideoBoundsProvider {

    private val toolbarView = container.findViewById<View>(R.id.view_toolbar_room)
    private val toolbarCloseIcon = toolbarView.findViewById<View>(R.id.iv_back)
    private val statsInfoView = container.findViewById<View>(R.id.view_stats_info)
    private val partnerInfoView = container.findViewById<View>(R.id.view_partner_info)
    private val chatListView = container.findViewById<View>(R.id.view_chat_list)
    private val offset16 = container.resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4)
    private val verticalChatListHeight = container.resources.getDimensionPixelSize(
        R.dimen.play_chat_vertical_max_height
    )

    override suspend fun getVideoTopBounds(videoOrientation: VideoOrientation): Int = coroutineScope {
        return@coroutineScope if (videoOrientation.isHorizontal) {
            val toolbarLayout = asyncCatchError(block = { measureWithTimeout(MEASURE_TOP_BOUNDS_TIMEOUT, toolbarView::awaitMeasured) }) {}
            val statsInfoLayout = asyncCatchError(block = { measureWithTimeout(MEASURE_TOP_BOUNDS_TIMEOUT, statsInfoView::awaitMeasured) }) {}
            val partnerInfoLayout = asyncCatchError(block = { measureWithTimeout(MEASURE_TOP_BOUNDS_TIMEOUT, partnerInfoView::awaitMeasured) }) {}

            awaitAll(toolbarLayout, statsInfoLayout, partnerInfoLayout)

            val toolbarViewTotalHeight = run {
                val marginLp = toolbarView.layoutParams as ViewGroup.MarginLayoutParams
                toolbarView.height + marginLp.bottomMargin + marginLp.topMargin
            }

            val statsInfoTotalHeight = run {
                val marginLp = statsInfoView.layoutParams as ViewGroup.MarginLayoutParams
                statsInfoView.height + marginLp.bottomMargin + marginLp.topMargin
            }

            val partnerInfoTotalHeight = run {
                val marginLp = partnerInfoView.layoutParams as ViewGroup.MarginLayoutParams
                partnerInfoView.height + marginLp.bottomMargin + marginLp.topMargin
            }

            val statusBarHeight = container.let { DisplayMetricUtils.getStatusBarHeight(it.context) }.orZero()

            toolbarViewTotalHeight + statsInfoTotalHeight + partnerInfoTotalHeight + statusBarHeight
        } else 0
    }

    override suspend fun getVideoBottomBoundsOnKeyboardShown(
        view: View,
        estimatedKeyboardHeight: Int,
        videoOrientation: VideoOrientation,
    ): Int {
        view.awaitLayout()

        val viewBottom = view.bottom
        //View Bottom is still a full device height because it is not changed by the insets

        val destHeight = if (videoOrientation is VideoOrientation.Horizontal) {
            val dstStart = toolbarCloseIcon.right + offset16
            val dstEnd = view.right - dstStart
            val dstWidth = dstEnd - dstStart
            (1 / (videoOrientation.widthRatio / videoOrientation.heightRatio.toFloat()) * dstWidth).toInt()
        } else {
            viewBottom -
                    estimatedKeyboardHeight -
                    (viewBottom - estimatedKeyboardHeight - chatListView.bottom) -
                    verticalChatListHeight -
                    toolbarView.top -
                    offset16
        }

        return destHeight
    }

    companion object {
        private const val MEASURE_TOP_BOUNDS_TIMEOUT = 35000L
    }
}