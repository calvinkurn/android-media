package com.tokopedia.talk.feature.reading.data.model

sealed class SortOption(open val id: SortId, open val displayName: String) {

    data class SortByInformativeness(override val displayName: String): SortOption(SortId.INFORMATIVENESS, displayName)
    data class SortByTime(override val displayName: String): SortOption(SortId.TIME, displayName)
    data class SortByLike(override val displayName: String): SortOption(SortId.LIKE, displayName)

    enum class SortId {
        INFORMATIVENESS,
        TIME,
        LIKE
    }
}