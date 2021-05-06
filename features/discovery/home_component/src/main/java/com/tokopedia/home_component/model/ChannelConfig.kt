package com.tokopedia.home_component.model

data class ChannelConfig (
        val layout: String = "",
        val showPromoBadge: Boolean = false,
        val hasCloseButton: Boolean = false,
        var serverTimeOffset: Long = 0,
        val createdTimeMillis: String = "",
        val isAutoRefreshAfterExpired: Boolean = false,
        val enableTimeDiffMoreThan24h: Boolean = false
)