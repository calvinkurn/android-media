package com.tokopedia.tokopedianow.category.presentation.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.applink.internal.ApplinkConstInternalTokopediaNow
import com.tokopedia.tokopedianow.category.domain.model.CategoryModel
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.domain.mapper.CategoryMenuMapper
import com.tokopedia.tokopedianow.common.domain.model.GetCategoryListResponse
import com.tokopedia.tokopedianow.common.domain.model.WarehouseData
import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuUiModel
import com.tokopedia.tokopedianow.searchcategory.jsonToObject
import com.tokopedia.tokopedianow.searchcategory.utils.CATEGORY_LIST_DEPTH
import com.tokopedia.tokopedianow.util.SearchCategoryDummyUtils.dummyChooseAddressData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.slot
import org.junit.Assert.assertThat
import org.junit.Test
import org.hamcrest.CoreMatchers.`is` as shouldBe

class CategoryGetCategoryGridTest : CategoryTestFixtures() {

    private val emptyProductCategoryModel = "category/emptyproduct/empty-product.json".jsonToObject<CategoryModel>()
    private val categoryList = "category/categorylist/category-list.json".jsonToObject<GetCategoryListResponse>()

    private val warehousesSlot = slot<List<WarehouseData>>()
    private val warehouses by lazy { warehousesSlot.captured }

    private val depthSlot = slot<Int>()
    private val depth by lazy { depthSlot.captured }

    @Test
    fun `show category grid on empty product list`() {
        `Given get category first page use case will be successful`(emptyProductCategoryModel)
        `Given get category list use case will return category list`()

        `When view created`()

        `Then assert get category list params`()
        `Then assert category menu ui model state show`()
    }

    private fun `Given get category list use case will return category list`() {
        coEvery {
            getCategoryListUseCase.execute(capture(warehousesSlot), capture(depthSlot))
        } returns categoryList.response
    }

    private fun `Then assert get category list params`() {
        val expectedWarehouses = dummyChooseAddressData.warehouses
            .map { WarehouseData(it.warehouse_id.toString(), it.service_type) }
        assertThat(warehouses, shouldBe(expectedWarehouses))
        assertThat(depth, shouldBe(CATEGORY_LIST_DEPTH))
    }

    private fun `Then assert category menu ui model state show`() {
        val expectedCategoryList = CategoryMenuMapper.mapToCategoryList(
            response = categoryList.response.data,
            seeAllAppLink = ApplinkConstInternalTokopediaNow.SEE_ALL_CATEGORY + CategoryMenuMapper.APPLINK_PARAM_WAREHOUSE_ID
        )
        val visitableList = tokoNowCategoryViewModel.visitableListLiveData.value!!
        val categoryGridUiModel = visitableList.findIndexedCategoryMenuUIModel()

        assertThat(categoryGridUiModel.categoryListUiModel, shouldBe(expectedCategoryList))
        assertThat(categoryGridUiModel.state, shouldBe(TokoNowLayoutState.SHOW))
    }

    private fun List<Visitable<*>>.findIndexedCategoryMenuUIModel() =
        find { it is TokoNowCategoryMenuUiModel }
            as? TokoNowCategoryMenuUiModel
            ?: throw AssertionError("Cannot find category grid ui model")

    @Test
    fun `do not show category grid if has product list`() {
        val categoryModel = "category/first-page-8-products.json".jsonToObject<CategoryModel>()
        `Given get category first page use case will be successful`(categoryModel)
        `Given get category list use case will return category list`()

        `When view created`()

        `Then verify get category list use case is not called`()
    }

    private fun `Then verify get category list use case is not called`() {
        coVerify(exactly = 0) {
            getCategoryListUseCase.execute(any(), any<Int>())
        }
    }

    @Test
    fun `get category list error`() {
        `Given get category first page use case will be successful`(emptyProductCategoryModel)
        `Given get category list use case will throw exception`()

        `When view created`()

        `Then assert category grid ui model state hide`()
    }

    private fun `Given get category list use case will throw exception`() {
        coEvery {
            getCategoryListUseCase.execute(capture(warehousesSlot), capture(depthSlot))
        } throws Exception()
    }

    private fun `Then assert category grid ui model state hide`() {
        val visitableList = tokoNowCategoryViewModel.visitableListLiveData.value!!
        val categoryGridUiModel = visitableList.findIndexedCategoryMenuUIModel()

        assertThat(categoryGridUiModel.state, shouldBe(TokoNowLayoutState.HIDE))
    }

    @Test
    fun `retry get category list after error`() {
        `Given get category first page use case will be successful`(emptyProductCategoryModel)
        `Given get category list use case will throw exception`()
        `Given view already created`()
        `Given get category list use case will return category list`()

        `When view retry load category grid`()

        `Then assert category menu ui model state show`()
    }

    private fun `When view retry load category grid`() {
        tokoNowCategoryViewModel.onCategoryMenuRetry()
    }
}
