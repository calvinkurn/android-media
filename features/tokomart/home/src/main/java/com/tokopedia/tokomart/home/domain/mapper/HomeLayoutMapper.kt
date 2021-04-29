package com.tokopedia.tokomart.home.domain.mapper

import com.tokopedia.tokomart.home.base.uimodel.BaseHomeUiModel
import com.tokopedia.tokomart.home.constant.HomeLayoutType
import com.tokopedia.tokomart.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokomart.home.presentation.uimodel.HomeAllCategoryUiModel
import com.tokopedia.tokomart.home.presentation.uimodel.HomeSectionUiModel

object HomeLayoutMapper {

    private val SUPPORTED_TYPE = listOf(
        HomeLayoutType.SECTION,
        HomeLayoutType.ALL_CATEGORY,
        HomeLayoutType.DYNAMIC_CHANNEL
    )

    fun mapToHomeUiModel(response: List<HomeLayoutResponse>): List<BaseHomeUiModel> {
        return response.filter { SUPPORTED_TYPE.contains(it.type) }.map {
            when(it.type) {
                HomeLayoutType.ALL_CATEGORY -> HomeAllCategoryUiModel(it.id, it.title)
                else -> HomeSectionUiModel(it.id, it.title)
            }
        }
    }
}
