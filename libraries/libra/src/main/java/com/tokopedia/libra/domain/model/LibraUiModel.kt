package com.tokopedia.libra.domain.model

data class LibraUiModel(
    val experiments: List<ItemLibraUiModel>
) {

    companion object {
        fun default() = LibraUiModel(listOf())
    }
}

data class ItemLibraUiModel(
    val experiment: String,
    val variant: String
)
