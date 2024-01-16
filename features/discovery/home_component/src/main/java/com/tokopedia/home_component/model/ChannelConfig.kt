package com.tokopedia.home_component.model

import com.tokopedia.home_component.util.ChannelStyleUtil.isProductCardReimagine
import com.tokopedia.home_component.util.ChannelStyleUtil.parseBorderStyle
import com.tokopedia.home_component.util.ChannelStyleUtil.parseDividerSize
import com.tokopedia.home_component.util.ChannelStyleUtil.parseImageStyle

data class ChannelConfig(
    val layout: String = "",
    val showPromoBadge: Boolean = false,
    val hasCloseButton: Boolean = false,
    var serverTimeOffset: Long = 0,
    val createdTimeMillis: String = "",
    val isAutoRefreshAfterExpired: Boolean = false,
    val enableTimeDiffMoreThan24h: Boolean = false,
    val dividerType: Int = DIVIDER_NO_DIVIDER,
    val styleParam: Map<String, String> = mapOf()
) {
    companion object {
        const val DIVIDER_NO_DIVIDER = 0
        const val DIVIDER_TOP = 1
        const val DIVIDER_BOTTOM = 2
        const val DIVIDER_TOP_AND_BOTTOM = 3
    }

    val dividerSize: Int = styleParam.parseDividerSize()
    val borderStyle: String = styleParam.parseBorderStyle()
    val imageStyle: String = styleParam.parseImageStyle()
    val isProductCardReimagine: Boolean = styleParam.isProductCardReimagine()
}
