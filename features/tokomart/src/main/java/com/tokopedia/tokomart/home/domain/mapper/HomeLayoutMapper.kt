package com.tokopedia.tokomart.home.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_component.visitable.HomeComponentVisitable
import com.tokopedia.tokomart.categorylist.domain.model.CategoryResponse
import com.tokopedia.tokomart.home.constant.HomeLayoutType
import com.tokopedia.tokomart.home.domain.mapper.HomeCategoryMapper.mapToCategoryLayout
import com.tokopedia.tokomart.home.domain.mapper.HomeCategoryMapper.mapToCategoryList
import com.tokopedia.tokomart.home.domain.mapper.LegoBannerMapper.mapLegoBannerDataModel
import com.tokopedia.tokomart.home.domain.mapper.VisitableMapper.updateByChannelId
import com.tokopedia.tokomart.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokomart.home.presentation.uimodel.HomeCategoryGridUiModel

object HomeLayoutMapper {

    private val SUPPORTED_LAYOUT_TYPES = listOf(
        HomeLayoutType.CATEGORY,
        HomeLayoutType.LEGO_3_IMAGE
    )

    fun mapHomeLayoutList(response: List<HomeLayoutResponse>): List<Visitable<*>> {
        val layoutList = mutableListOf<Visitable<*>>()

        response.filter { SUPPORTED_LAYOUT_TYPES.contains(it.layout) }.forEach {
            mapToHomeUiModel(it)?.let { item ->
                layoutList.add(item)
            }
        }

        return layoutList
    }

    fun List<Visitable<*>>.mapGlobalHomeLayoutData(
        item: HomeComponentVisitable,
        response: HomeLayoutResponse
    ): List<Visitable<*>> {
        return updateByChannelId(item.visitableId()) {
            mapToHomeUiModel(response)
        }
    }

    fun List<Visitable<*>>.mapHomeCategoryGridData(
        item: HomeCategoryGridUiModel,
        response: List<CategoryResponse>
    ): List<Visitable<*>> {
        return updateByChannelId(item.channelId) {
            val categoryList = mapToCategoryList(response)
            item.copy(categoryList = categoryList)
        }
    }

    private fun mapToHomeUiModel(response: HomeLayoutResponse): Visitable<*>? {
        return when (response.layout) {
            HomeLayoutType.CATEGORY -> mapToCategoryLayout(response)
            HomeLayoutType.LEGO_3_IMAGE -> mapLegoBannerDataModel(response)
            else -> null
        }
    }
}
