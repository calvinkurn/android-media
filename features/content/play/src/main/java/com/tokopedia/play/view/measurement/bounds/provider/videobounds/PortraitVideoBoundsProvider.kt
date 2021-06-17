package com.tokopedia.play.view.measurement.bounds.provider.videobounds

import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.common.utils.DisplayMetricUtils
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.play.R
import com.tokopedia.play.util.measureWithTimeout
import com.tokopedia.play.view.type.VideoOrientation
import com.tokopedia.play_common.util.extension.awaitMeasured
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

/**
 * Created by jegul on 04/08/20
 */
class PortraitVideoBoundsProvider(
        private val container: ViewGroup
) : VideoBoundsProvider {

    private val toolbarView = container.findViewById<View>(R.id.view_toolbar)
    private val statsInfoView = container.findViewById<View>(R.id.view_stats_info)
    private val sendChatView = container.findViewById<View>(R.id.view_send_chat)
    private val quickReplyView = container.findViewById<View>(R.id.rv_quick_reply)
    private val chatListView = container.findViewById<View>(R.id.view_chat_list)
    private val offset16 = container.resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4)

    override suspend fun getVideoTopBounds(videoOrientation: VideoOrientation): Int = coroutineScope {
        return@coroutineScope if (videoOrientation.isHorizontal) {
            val toolbarLayout = asyncCatchError(block = { measureWithTimeout { toolbarView.awaitMeasured() } }) {}
            val statsInfoLayout = asyncCatchError(block = { measureWithTimeout { statsInfoView.awaitMeasured() } }) {}

            awaitAll(toolbarLayout, statsInfoLayout)

            val toolbarViewTotalHeight = run {
                val marginLp = toolbarView.layoutParams as ViewGroup.MarginLayoutParams
                toolbarView.height + marginLp.bottomMargin + marginLp.topMargin
            }

            val statsInfoTotalHeight = run {
                val marginLp = statsInfoView.layoutParams as ViewGroup.MarginLayoutParams
                statsInfoView.height + marginLp.bottomMargin + marginLp.topMargin
            }

            val statusBarHeight = container.let { DisplayMetricUtils.getStatusBarHeight(it.context) }.orZero()

            toolbarViewTotalHeight + statsInfoTotalHeight + statusBarHeight
        } else 0
    }

    override suspend fun getVideoBottomBoundsOnKeyboardShown(estimatedKeyboardHeight: Int, hasQuickReply: Boolean): Int = coroutineScope {
        val sendChatViewTotalHeight = run {
            val height = sendChatView.height
            val marginLp = sendChatView.layoutParams as ViewGroup.MarginLayoutParams
            height + marginLp.bottomMargin + marginLp.topMargin
        }

        val chatListViewTotalHeight = run {
            val height = container.resources.getDimensionPixelSize(R.dimen.play_chat_vertical_max_height)
            val marginLp = chatListView.layoutParams as ViewGroup.MarginLayoutParams
            height + marginLp.bottomMargin + marginLp.topMargin
        }

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

        val statusBarHeight = DisplayMetricUtils.getStatusBarHeight(container.context)
        val requiredMargin = offset16

        val interactionTopmostY = getScreenHeight() - (estimatedKeyboardHeight + sendChatViewTotalHeight + chatListViewTotalHeight + quickReplyViewTotalHeight + statusBarHeight + requiredMargin)

        return@coroutineScope interactionTopmostY
    }
}