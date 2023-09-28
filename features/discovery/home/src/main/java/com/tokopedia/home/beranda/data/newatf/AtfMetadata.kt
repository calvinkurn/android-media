package com.tokopedia.home.beranda.data.newatf

data class AtfMetadata(
    val id: Int = 0,
    val position: Int = 0,
    val name: String = "",
    val component: String = "",
    val param: String = "",
    val isOptional: Boolean = false,
    val isShimmer: Boolean = true,
)
