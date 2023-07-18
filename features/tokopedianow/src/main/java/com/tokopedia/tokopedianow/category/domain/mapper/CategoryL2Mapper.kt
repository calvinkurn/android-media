package com.tokopedia.tokopedianow.category.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryL2TabMapper.filterTabComponents
import com.tokopedia.tokopedianow.category.domain.response.GetCategoryLayoutResponse.BasicInfo
import com.tokopedia.tokopedianow.category.domain.response.GetCategoryLayoutResponse.Component
import com.tokopedia.tokopedianow.category.presentation.constant.CategoryComponentType.Companion.TABS_HORIZONTAL_SCROLL
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryL2HeaderUiModel
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryL2TabUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowChooseAddressWidgetUiModel

object CategoryL2Mapper {

    private const val DEFAULT_INDEX = 0

    private val SUPPORTED_LAYOUT_TYPES = listOf(
        TABS_HORIZONTAL_SCROLL
    )

    fun MutableList<Visitable<*>>.mapToCategoryUiModel(
        componentsResponse: List<Component>,
        categoryIdL2: String
    ) {
        componentsResponse.filter { SUPPORTED_LAYOUT_TYPES.contains(it.type) }.forEach { componentResponse ->
            when(componentResponse.type) {
                TABS_HORIZONTAL_SCROLL -> addCategoryTab(
                    componentsResponse,
                    componentResponse,
                    categoryIdL2
                )
            }
        }
    }

    fun MutableList<Visitable<*>>.addChooseAddress(addressData: LocalCacheModel)  {
        add(TokoNowChooseAddressWidgetUiModel(addressData = addressData))
    }

    fun MutableList<Visitable<*>>.addHeader(basicInfoResponse: BasicInfo) {
        val headerUiModel = CategoryL2HeaderUiModel(
            id = basicInfoResponse.id,
            name = basicInfoResponse.name,
            appLink = basicInfoResponse.applinks
        )
        add(headerUiModel)
    }

    fun MutableList<Visitable<*>>.addCategoryTab(
        componentListResponse: List<Component>,
        componentResponse: Component,
        categoryIdL2: String
    ) {
        val categoryNameList = componentResponse.getTabCategoryNameList()
        val tabComponents = componentListResponse.filterTabComponents()
        val categoryL2Ids = componentResponse.data.map { it.id }
        val selectedTabPosition = if(categoryL2Ids.contains(categoryIdL2)) {
            categoryL2Ids.indexOf(categoryIdL2)
        } else {
            DEFAULT_INDEX
        }

        add(CategoryL2TabUiModel(
            id = componentResponse.id,
            titleList = categoryNameList,
            componentList = tabComponents,
            categoryL2Ids = categoryL2Ids,
            selectedTabPosition = selectedTabPosition
        ))
    }
}
