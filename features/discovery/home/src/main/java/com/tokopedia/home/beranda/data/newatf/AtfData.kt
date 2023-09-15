package com.tokopedia.home.beranda.data.newatf

data class AtfData(
    val atfMetadata: AtfMetadata,
    val atfContent: AtfContent? = null,
    val isCache: Boolean,
)
