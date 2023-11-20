package com.tokopedia.universal_sharing.view.model

data class ChipProperties(
    val id: Int = -1,
    val title: String = "",
    var isSelected: Boolean = false,
    var shareText: String = "",
    val properties: LinkProperties
)
