package com.tokopedia.tokomart.category.presentation.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.newdynamicfilter.helper.OptionHelper
import com.tokopedia.tokomart.category.domain.model.CategoryModel
import com.tokopedia.tokomart.searchcategory.data.getTokonowQueryParam
import com.tokopedia.tokomart.searchcategory.jsonToObject
import com.tokopedia.tokomart.searchcategory.presentation.model.CategoryFilterDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.CategoryFilterItemDataView
import com.tokopedia.usecase.RequestParams
import io.mockk.slot
import io.mockk.verify
import org.hamcrest.CoreMatchers.nullValue
import org.junit.Assert.assertThat
import org.junit.Test
import org.hamcrest.CoreMatchers.`is` as shouldBe

class CategoryCategoryFilterTest: CategoryTestFixtures() {

    private val categoryModel = "category/first-page-8-products.json".jsonToObject<CategoryModel>()

    @Test
    fun `when view created, category filter isSelected should be based on query params`() {
        val selectedFilterOption =
                OptionHelper.copyOptionAsExclude(categoryModel.categoryFilter.filter[0].options[1])
        val queryParamWithFilter = mapOf(
                selectedFilterOption.key to selectedFilterOption.value,
        )

        `Given category view model`(defaultCategoryId, queryParamWithFilter)
        `Given get category first page use case will be successful`(categoryModel)

        `When view created`()

        val categoryFilterVisitable = categoryViewModel.visitableListLiveData.value.getCategoryFilterDataView()
        `Then assert category filter isSelected`(selectedFilterOption, categoryFilterVisitable)
    }

    private fun List<Visitable<*>>?.getCategoryFilterDataView(): CategoryFilterDataView {
        return this?.find { it is CategoryFilterDataView } as? CategoryFilterDataView ?:
        throw AssertionError("Category filter data view not found")
    }

    private fun `Then assert category filter isSelected`(
            selectedFilterOption: Option,
            categoryFilterVisitable: CategoryFilterDataView,
    ) {
        categoryFilterVisitable.categoryFilterItemList.forEach {
            val failedReason = "Category filter option \"${it.option.key}\" - \"${it.option.name}\" " +
                    "isSelected is incorrect."
            val expectedIsSelected = it.option == selectedFilterOption

            assertThat(failedReason, it.isSelected, shouldBe(expectedIsSelected))
        }
    }

    @Test
    fun `click category filter to apply filter`() {
        val requestParamsSlot = slot<RequestParams>()
        `Given get category first page use case will be successful`(categoryModel, requestParamsSlot)
        `Given view already created`()

        val categoryFilterVisitable = categoryViewModel.visitableListLiveData.value.getCategoryFilterDataView()
        val selectedCategoryFilter = categoryFilterVisitable.categoryFilterItemList[1]

        `When category filter selected`(selectedCategoryFilter, true)

        `Then verify get search first page is called twice`()
        `Then verify request params contains the applied category filter`(
                requestParamsSlot.captured,
                selectedCategoryFilter
        )
    }

    private fun `When category filter selected`(
            selectedCategoryFilter: CategoryFilterItemDataView,
            isSelected: Boolean
    ) {
        categoryViewModel.onViewClickCategoryFilterChip(selectedCategoryFilter.option, isSelected)
    }

    private fun `Then verify get search first page is called twice`() {
        verify(exactly = 2) {
            getCategoryFirstPageUseCase.cancelJobs()
            getCategoryFirstPageUseCase.execute(any(), any(), any())
        }
    }

    private fun `Then verify request params contains the applied category filter`(
            requestParams: RequestParams,
            selectedCategoryFilter: CategoryFilterItemDataView
    ) {
        val selectedCategoryFilterKey = OptionHelper.getKeyRemoveExclude(selectedCategoryFilter.option)
        val actualParamsValue = getTokonowQueryParam(requestParams)[selectedCategoryFilterKey].toString()

        val reason = "Query param with key \"$selectedCategoryFilterKey\" value is incorrect"
        assertThat(reason, actualParamsValue, shouldBe(selectedCategoryFilter.option.value))
    }

    @Test
    fun `click category filter to un-apply filter`() {
        val requestParamsSlot = slot<RequestParams>()
        val selectedCategoryFilterIndex = 2
        val previouslySelectedFilterOption =
                categoryModel.categoryFilter.filter[0].options[selectedCategoryFilterIndex]
        val queryParamWithFilter = mapOf(
                previouslySelectedFilterOption.key to previouslySelectedFilterOption.value,
        )

        `Given category view model`(defaultCategoryId, queryParamWithFilter)
        `Given get category first page use case will be successful`(categoryModel, requestParamsSlot)
        `Given view already created`()

        val categoryFilterVisitable = categoryViewModel.visitableListLiveData.value.getCategoryFilterDataView()
        val selectedQuickFilter = categoryFilterVisitable.categoryFilterItemList[selectedCategoryFilterIndex]

        `When category filter selected`(selectedQuickFilter, false)

        `Then verify get search first page is called twice`()
        `Then verify category filter does not contain unapplied filter`(
                requestParamsSlot.captured,
                selectedQuickFilter
        )
    }

    private fun `Then verify category filter does not contain unapplied filter`(
            requestParams: RequestParams,
            selectedCategoryFilter: CategoryFilterItemDataView
    ) {
        val selectedQuickFilterKey = selectedCategoryFilter.option.key
        val actualParamsValue = requestParams.parameters[selectedQuickFilterKey]

        assertThat(actualParamsValue, nullValue())
    }

    @Test
    fun `apply new click category filter should remove existing category filter`() {
        val requestParamsSlot = slot<RequestParams>()
        val queryParamWithCategoryFilter = mapOf(
                SearchApiConst.SC to "1324",
        )

        `Given category view model`(defaultCategoryId, queryParamWithCategoryFilter)
        `Given get category first page use case will be successful`(categoryModel, requestParamsSlot)
        `Given view already created`()

        val categoryFilterVisitable = categoryViewModel.visitableListLiveData.value.getCategoryFilterDataView()
        val selectedCategoryFilter = categoryFilterVisitable.categoryFilterItemList[1]

        `When category filter selected`(selectedCategoryFilter, true)

        `Then verify get search first page is called twice`()
        `Then verify request params contains the applied category filter`(
                requestParamsSlot.captured,
                selectedCategoryFilter
        )
    }
}