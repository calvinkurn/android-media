package com.tokopedia.shop_widget.mvc_locked_to_product.view.uimodel

object MvcLockedToProductSortListFactory {

    private const val SORT_NAME_DEFAULT = "Urutkan"
    private const val SORT_NAME_MOST_SUITABLE = "Paling Sesuai"
    private const val SORT_VALUE_MOST_SUITABLE = "23"
    private const val SORT_NAME_REVIEW = "Ulasan"
    private const val SORT_VALUE_REVIEW = "5"
    private const val SORT_NAME_LATEST = "Terbaru"
    private const val SORT_VALUE_LATEST = "9"
    private const val SORT_NAME_HIGHEST_PRICE = "Harga Tertinggi"
    private const val SORT_VALUE_HIGHEST_PRICE = "4"
    private const val SORT_NAME_LOWEST_PRICE = "Harga Terendah"
    private const val SORT_VALUE_LOWEST_PRICE = "3"


    private val sortMap = mapOf(
        SORT_NAME_MOST_SUITABLE to SORT_VALUE_MOST_SUITABLE,
        SORT_NAME_REVIEW to SORT_VALUE_REVIEW,
        SORT_NAME_LATEST to SORT_VALUE_LATEST,
        SORT_NAME_HIGHEST_PRICE to SORT_VALUE_HIGHEST_PRICE,
        SORT_NAME_LOWEST_PRICE to SORT_VALUE_LOWEST_PRICE
    )

    fun getSortListData(selectedSortId: String): List<MvcLockedToProductSortUiModel> {
        return sortMap.map {
            val isSelected = it.value == selectedSortId
            MvcLockedToProductSortUiModel(it.key, it.value, isSelected)
        }
    }

    fun getDefaultSortData(): MvcLockedToProductSortUiModel {
        return MvcLockedToProductSortUiModel(
            SORT_NAME_DEFAULT,
            SORT_VALUE_LOWEST_PRICE,
            true
        )
    }

    fun isDefaultSortName(sortName: String): Boolean {
        return sortName == SORT_NAME_DEFAULT
    }

}