package com.tokopedia.tokopedianow.category.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.tokopedianow.category.domain.response.GetCategoryLayoutResponse.BasicInfo
import com.tokopedia.tokopedianow.category.domain.response.GetCategoryLayoutResponse.Component
import com.tokopedia.tokopedianow.category.presentation.constant.CategoryComponentName.Companion.TABS_HORIZONTAL_SCROLL
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryHeaderSpaceUiModel
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryL2HeaderUiModel
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryL2TabUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowChooseAddressWidgetUiModel

object CategoryL2Mapper {

    fun MutableList<Visitable<*>>.addChooseAddress(addressData: LocalCacheModel)  {
        add(TokoNowChooseAddressWidgetUiModel(addressData = addressData))
    }

    fun MutableList<Visitable<*>>.addHeaderSpace(space: Int) {
        add(CategoryHeaderSpaceUiModel(space = space))
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
        response: Component,
        categoryNameList: List<String>
    ) {
        add(CategoryL2TabUiModel(categoryNameList))
    }

    fun MutableList<Visitable<*>>.mapToCategoryUiModel(
        components: List<Component>,
        categoryNameList: List<String>
    ) {
        components.forEach {
            when(it.type) {
                TABS_HORIZONTAL_SCROLL -> addCategoryTab(it, categoryNameList)
            }
        }
    }
}
