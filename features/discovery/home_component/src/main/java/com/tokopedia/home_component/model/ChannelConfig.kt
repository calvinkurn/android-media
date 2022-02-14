package com.tokopedia.home_component.model

data class ChannelConfig (
        val layout: String = "",
        val showPromoBadge: Boolean = false,
        val hasCloseButton: Boolean = false,
        var serverTimeOffset: Long = 0,
        val createdTimeMillis: String = "",
        val isAutoRefreshAfterExpired: Boolean = false,
        val enableTimeDiffMoreThan24h: Boolean = false,
        val dividerType: Int = DIVIDER_NO_DIVIDER
) {
        companion object {
                const val DIVIDER_NO_DIVIDER = 0
                const val DIVIDER_TOP = 1
                const val DIVIDER_BOTTOM = 2
                const val DIVIDER_TOP_AND_BOTTOM = 3
        }
}