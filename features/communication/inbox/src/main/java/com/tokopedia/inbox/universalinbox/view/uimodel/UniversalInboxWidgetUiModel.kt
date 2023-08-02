package com.tokopedia.inbox.universalinbox.view.uimodel

import com.tokopedia.kotlin.extensions.view.ZERO

data class UniversalInboxWidgetUiModel (
    val icon: Int = Int.ZERO,
    val title: String = "",
    val subtext: String = "",
    val applink: String = "",
    var counter: Int = Int.ZERO,
    val type: Int = Int.ZERO,
    val isError: Boolean = false
)
