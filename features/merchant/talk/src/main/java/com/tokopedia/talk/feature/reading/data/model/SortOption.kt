package com.tokopedia.talk.feature.reading.data.model

sealed class SortOption(open val id: SortId, open val displayName: String, open var isSelected: Boolean) {

    companion object {
        const val INFORMATIVENESS_DISPLAY_NAME = "Pertanyaan Paling Relevan"
        const val TIME_DISPLAY_NAME = "Pertanyaan Terbaru"
        const val LIKE_DISPLAY_NAME = "Pertanyaan Terpopuler"
    }

    data class SortByInformativeness(override val displayName: String = INFORMATIVENESS_DISPLAY_NAME, override var isSelected: Boolean = true): SortOption(SortId.INFORMATIVENESS, displayName, isSelected)
    data class SortByTime(override val displayName: String = TIME_DISPLAY_NAME, override var isSelected: Boolean = false): SortOption(SortId.TIME, displayName, isSelected)
    data class SortByLike(override val displayName: String = LIKE_DISPLAY_NAME, override var isSelected: Boolean = false): SortOption(SortId.LIKE, displayName, isSelected)

    enum class SortId {
        INFORMATIVENESS,
        TIME,
        LIKE
    }
}