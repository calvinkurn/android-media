package com.tokopedia.home.beranda.presentation.view.helper

import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import com.tokopedia.home.R
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home_component.model.ChannelConfig
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.DividerUnify

object HomeChannelWidgetUtil {
    fun validateHomeComponentDivider(
        channelModel: DynamicHomeChannel.Channels?,
        dividerTop: DividerUnify?,
        dividerBottom: DividerUnify?
    ) {
//        dividerBottom?.let {
//            it.background =
//                ColorDrawable(
//                    ContextCompat.getColor(it.context, R.color.Blue_B500)
//                )
//        }
//
//        dividerTop?.let {
//            it.background =
//                ColorDrawable(
//                    ContextCompat.getColor(it.context, R.color.Unify_G500_96)
//                )
//        }
        when(channelModel?.dividerType) {
            ChannelConfig.DIVIDER_NO_DIVIDER -> {
                dividerTop?.gone()
                dividerBottom?.gone()
            }
            ChannelConfig.DIVIDER_TOP -> {
                dividerTop?.visible()
                dividerBottom?.gone()
            }
            ChannelConfig.DIVIDER_BOTTOM -> {
                dividerTop?.gone()
                dividerBottom?.visible()
            }
            ChannelConfig.DIVIDER_TOP_AND_BOTTOM -> {
                dividerTop?.visible()
                dividerBottom?.visible()
            }
        }
    }
}