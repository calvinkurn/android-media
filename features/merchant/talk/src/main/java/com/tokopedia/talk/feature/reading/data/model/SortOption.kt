package com.tokopedia.talk.feature.reading.data.model

sealed class SortOption(open val id: SortId, open val displayName: String) {

    companion object {
        const val INFORMATIVENESS_DISPLAY_NAME = "Pertanyaan Paling Relevan"
        const val TIME_DISPLAY_NAME = "Pertanyaan Terbaru"
        const val LIKE_DISPLAY_NAME = "Pertanyaan Terpopuler"
    }

    data class SortByInformativeness(override val displayName: String = INFORMATIVENESS_DISPLAY_NAME): SortOption(SortId.INFORMATIVENESS, displayName)
    data class SortByTime(override val displayName: String = TIME_DISPLAY_NAME): SortOption(SortId.TIME, displayName)
    data class SortByLike(override val displayName: String = LIKE_DISPLAY_NAME): SortOption(SortId.LIKE, displayName)

    enum class SortId {
        INFORMATIVENESS,
        TIME,
        LIKE
    }
}