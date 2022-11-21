package com.tokopedia.home.beranda.presentation.view.helper

import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home_component.model.ChannelConfig
import com.tokopedia.home_component.util.ChannelStyleUtil.parseDividerSize
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.DividerUnify
import com.tokopedia.unifycomponents.toPx

object HomeChannelWidgetUtil {
    private const val DEFAULT_DIVIDER_HEIGHT = 1
    private const val DEFAULT_BOTTOM_PADDING = 8

    fun validateHomeComponentDivider(
        channelModel: DynamicHomeChannel.Channels?,
        dividerTop: DividerUnify?,
        dividerBottom: DividerUnify?,
        useBottomPadding: Boolean = false
    ) {
        val dividerSize = channelModel?.styleParam?.parseDividerSize()?.toPx()
            ?: DEFAULT_DIVIDER_HEIGHT.toPx()
        dividerTop?.layoutParams?.height = dividerSize
        dividerBottom?.layoutParams?.height = dividerSize
        when(ChannelConfig.DIVIDER_TOP) {
            ChannelConfig.DIVIDER_NO_DIVIDER -> {
                dividerTop?.invisible()
                if(useBottomPadding) setBottomPadding(dividerBottom)
                else dividerBottom?.gone()
            }
            ChannelConfig.DIVIDER_TOP -> {
                dividerTop?.visible()
                dividerBottom?.gone()
            }
            ChannelConfig.DIVIDER_BOTTOM -> {
                dividerTop?.invisible()
                dividerBottom?.visible()
            }
            ChannelConfig.DIVIDER_TOP_AND_BOTTOM -> {
                dividerTop?.visible()
                dividerBottom?.visible()
            }
        }
    }

    private fun setBottomPadding(dividerBottom: DividerUnify?) {
        dividerBottom?.layoutParams?.height = DEFAULT_BOTTOM_PADDING.toPx()
        dividerBottom?.setBackgroundResource(android.R.color.transparent)
        dividerBottom?.visible()
    }
}
