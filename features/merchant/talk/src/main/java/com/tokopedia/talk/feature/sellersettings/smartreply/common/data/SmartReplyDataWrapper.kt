package com.tokopedia.talk.feature.sellersettings.smartreply.common.data

data class SmartReplyDataWrapper(
        val isSmartReplyOn: Boolean = false,
        val messageReady: String = "",
        val messageNotReady: String = ""
)