package com.tokopedia.home.beranda.data.newatf

data class ListNewAtfData(
    val listAtfMetadata: List<AtfMetadata> = emptyList(),
    val source: AtfSource = AtfSource.NONE
)

enum class AtfSource {
    NONE,
    CACHE,
    REMOTE
}
