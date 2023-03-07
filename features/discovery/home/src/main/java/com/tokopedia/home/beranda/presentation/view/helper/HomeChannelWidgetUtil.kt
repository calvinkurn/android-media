package com.tokopedia.home.beranda.presentation.view.helper

import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home_component.model.ChannelConfig
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.DividerUnify
import com.tokopedia.unifycomponents.dpToPx

object HomeChannelWidgetUtil {
    private const val DIVIDER_HEIGHT = 1f

    fun validateHomeComponentDivider(
        channelModel: DynamicHomeChannel.Channels?,
        dividerTop: DividerUnify?,
        dividerBottom: DividerUnify?
    ) {
        dividerTop?.layoutParams?.height = DIVIDER_HEIGHT.dpToPx().toInt()
        dividerBottom?.layoutParams?.height = DIVIDER_HEIGHT.dpToPx().toInt()
        when(channelModel?.dividerType) {
            ChannelConfig.DIVIDER_NO_DIVIDER -> {
                dividerTop?.invisible()
                dividerBottom?.gone()
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
}