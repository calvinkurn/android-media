package com.tokopedia.talk.feature.reading.data.model

sealed class SortOption(open val id: SortId, open val displayName: String, open var isSelected: Boolean) {

    companion object {
        const val INFORMATIVENESS_DISPLAY_NAME = "Pertanyaan Paling Relevan"
        const val TIME_DISPLAY_NAME = "Pertanyaan Terbaru"
    }

    data class SortByInformativeness(override val displayName: String = INFORMATIVENESS_DISPLAY_NAME, override var isSelected: Boolean = false): SortOption(SortId.INFORMATIVENESS, displayName, isSelected)
    data class SortByTime(override val displayName: String = TIME_DISPLAY_NAME, override var isSelected: Boolean = true): SortOption(SortId.TIME, displayName, isSelected)

    enum class SortId {
        INFORMATIVENESS,
        TIME
    }
}