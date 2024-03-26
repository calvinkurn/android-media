package com.tokopedia.discovery2.data

import com.tokopedia.kotlin.extensions.view.EMPTY

data class TopLevelTab(
    val name: String,
    val index: Int
)

val UnknownTab = TopLevelTab(String.EMPTY, -1)
