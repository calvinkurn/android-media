package com.tokopedia.tokomart.home.domain.mapper

import com.tokopedia.tokomart.home.presentation.uimodel.HomeLayoutUiModel
import com.tokopedia.tokomart.home.constant.HomeLayoutType
import com.tokopedia.tokomart.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokomart.home.presentation.uimodel.HomeCategoryItemUiModel
import com.tokopedia.tokomart.home.presentation.uimodel.HomeCategoryGridUiModel
import com.tokopedia.tokomart.home.presentation.uimodel.HomeSectionUiModel

object HomeLayoutMapper {

    private val SUPPORTED_TYPE = listOf(
        HomeLayoutType.SECTION,
        HomeLayoutType.ALL_CATEGORY,
        HomeLayoutType.DYNAMIC_CHANNEL
    )

    fun mapToHomeUiModel(response: List<HomeLayoutResponse>): List<HomeLayoutUiModel> {
        return response.filter { SUPPORTED_TYPE.contains(it.type) }.map {
            when(it.type) {
                HomeLayoutType.ALL_CATEGORY -> mapToCategoryUiModel(it)
                else -> HomeSectionUiModel(it.id, it.title)
            }
        }
    }

    private fun mapToCategoryUiModel(it: HomeLayoutResponse): HomeCategoryGridUiModel {
        val menuList = listOf(
            HomeCategoryItemUiModel("1", "Daging", "https://img.icons8.com/cotton/2x/circled-down--v2.png"),
            HomeCategoryItemUiModel("2", "Makanan Laut", "https://img.icons8.com/cotton/2x/circled-down--v2.png"),
            HomeCategoryItemUiModel("3", "Frozen Food", "https://img.icons8.com/cotton/2x/circled-down--v2.png"),
            HomeCategoryItemUiModel("4", "Sayuran Segar", "https://img.icons8.com/cotton/2x/circled-down--v2.png"),
            HomeCategoryItemUiModel("5", "Category 1", "https://img.icons8.com/cotton/2x/circled-down--v2.png"),
            HomeCategoryItemUiModel("6", "Category 2", "https://img.icons8.com/cotton/2x/circled-down--v2.png"),
            HomeCategoryItemUiModel("7", "Category 3", "https://img.icons8.com/cotton/2x/circled-down--v2.png"),
            HomeCategoryItemUiModel("8", "Category 4", "https://img.icons8.com/cotton/2x/circled-down--v2.png"),
            HomeCategoryItemUiModel("9", "Category 5", "https://img.icons8.com/cotton/2x/circled-down--v2.png"),
            HomeCategoryItemUiModel("10", "Kategori Lainnya", "https://img.icons8.com/cotton/2x/circled-down--v2.png")
        )
        return HomeCategoryGridUiModel(it.id, it.title, menuList)
    }
}
