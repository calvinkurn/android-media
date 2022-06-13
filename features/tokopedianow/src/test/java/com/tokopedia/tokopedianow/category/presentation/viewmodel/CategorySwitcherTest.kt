package com.tokopedia.tokopedianow.category.presentation.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.tokopedianow.category.domain.model.CategoryModel
import com.tokopedia.tokopedianow.searchcategory.jsonToObject
import com.tokopedia.tokopedianow.searchcategory.presentation.model.SwitcherWidgetDataView
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CategorySwitcherTest : BaseCategoryPageLoadTest() {

    private val localCacheModel15m = LocalCacheModel(
        address_id = "12257",
        city_id = "12345",
        district_id = "2274",
        lat = "1.1000",
        long = "37.002",
        postal_code = "15123",
        shop_id = "549621",
        warehouse_id = "11299001123",
        service_type = "15m"
    )

    private val localCacheModel2h = LocalCacheModel(
        address_id = "12257",
        city_id = "12345",
        district_id = "2274",
        lat = "1.1000",
        long = "37.002",
        postal_code = "15123",
        shop_id = "549621",
        warehouse_id = "11299001123",
        service_type = "2h"
    )

    @Before
    override fun setUp() {
        `Given choose address data`(localCacheModel2h)
        `Given category view model`()
    }

    @Test
    fun `test category switcher 2h service type`() {
        val categoryModel = "category/first-page-8-products.json".jsonToObject<CategoryModel>()
        `Given get category first page use case will be successful`(
            categoryModel,
            requestParamsSlot
        )

        `When view created`()

        val visitableList = tokoNowCategoryViewModel.visitableListLiveData.value!!
        val mandatoryParams = createExpectedMandatoryTokonowQueryParams(1, localCacheModel2h)

        `Then assert request params map`(mandatoryParams, localCacheModel2h)
        `Then assert visitable list does not contain switcher`(visitableList)
    }

    @Test
    fun `test category switcher 15m service type`() {
        val categoryModel = "category/first-page-8-products.json".jsonToObject<CategoryModel>()
        `Given get category first page use case will be successful`(
            categoryModel,
            requestParamsSlot
        )
        `Given choose address data`(localCacheModel15m)

        `When view reload page`()

        val visitableList = tokoNowCategoryViewModel.visitableListLiveData.value!!
        val mandatoryParams = createExpectedMandatoryTokonowQueryParams(1, localCacheModel15m)

        `Then assert request params map`(mandatoryParams, localCacheModel15m)
        `Then assert visitable list contains switcher`(visitableList)
    }

    private fun `Then assert visitable list does not contain switcher`(visitableList: List<Visitable<*>>) {
        assertTrue(visitableList.find { it is SwitcherWidgetDataView } == null)
    }

    private fun `Then assert visitable list contains switcher`(visitableList: List<Visitable<*>>) {
        assertTrue(visitableList.find { it is SwitcherWidgetDataView } != null)
    }
}