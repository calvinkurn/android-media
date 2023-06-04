package com.tokopedia.tokopedianow.category.presentation.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.newdynamicfilter.helper.OptionHelper
import com.tokopedia.tokopedianow.oldcategory.domain.model.CategoryModel
import com.tokopedia.tokopedianow.searchcategory.data.getTokonowQueryParam
import com.tokopedia.tokopedianow.searchcategory.jsonToObject
import com.tokopedia.tokopedianow.searchcategory.presentation.model.CategoryFilterDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.CategoryFilterItemDataView
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
    fun `when view created, category filter isSelected should be based on category L2 value`() {
        val selectedFilterOption =
                OptionHelper.copyOptionAsExclude(categoryModel.categoryFilter.filter[0].options[1])

        `Given category view model`(defaultCategoryL1, selectedFilterOption.value, defaultExternalServiceType, mapOf())
        `Given get category first page use case will be successful`(categoryModel)

        `When view created`()

        val categoryFilterVisitable = tokoNowCategoryViewModel.visitableListLiveData.value.getCategoryFilterDataView()
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
        val filterParam = createMockFilterParam()
        val queryParamWithFilter = defaultQueryParamMap + filterParam

        `Given get category first page use case will be successful`(categoryModel, requestParamsSlot)
        `Given category view model`(defaultCategoryL1, defaultCategoryL2, defaultExternalServiceType, queryParamWithFilter)
        `Given view already created`()

        val categoryFilterVisitable = tokoNowCategoryViewModel.visitableListLiveData.value.getCategoryFilterDataView()
        val selectedCategoryFilter = categoryFilterVisitable.categoryFilterItemList[1]

        `When category filter selected`(selectedCategoryFilter, true)

        val requestParams = requestParamsSlot.captured
        `Then verify get search first page is called twice`()
        `Then verify request params contains the applied category filter`(
                requestParams,
                selectedCategoryFilter
        )
        `Then verify request params reset filter and sort`(requestParams, filterParam)
    }

    private fun createMockFilterParam() = mapOf(
            SearchApiConst.OFFICIAL to "true",
            SearchApiConst.FCITY to "1,2#3"
    )

    private fun `When category filter selected`(
            selectedCategoryFilter: CategoryFilterItemDataView,
            isSelected: Boolean
    ) {
        tokoNowCategoryViewModel.onViewClickCategoryFilterChip(selectedCategoryFilter.option, isSelected)
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

    private fun `Then verify request params reset filter and sort`(
            requestParams: RequestParams,
            filterParam: Map<String, String>,
    ) {
        val tokonowQueryParam = getTokonowQueryParam(requestParams)

        filterParam.forEach { (key, _) ->
            assertThat(
                    "Query param should not contain filter key $key",
                    tokonowQueryParam[key],
                    nullValue(),
            )
        }

        assertThat(tokonowQueryParam[SearchApiConst.OB].toString(), shouldBe(23.toString()))
    }

    @Test
    fun `click category filter to un-apply filter`() {
        val requestParamsSlot = slot<RequestParams>()
        val selectedCategoryFilterIndex = 2
        val previouslySelectedFilterOption =
                categoryModel.categoryFilter.filter[0].options[selectedCategoryFilterIndex]
        val filterParam = createMockFilterParam()
        val queryParamWithFilter = mapOf(
                previouslySelectedFilterOption.key to previouslySelectedFilterOption.value,
        ) + filterParam

        `Given category view model`(defaultCategoryL1, defaultCategoryL2, defaultExternalServiceType, queryParamWithFilter)
        `Given get category first page use case will be successful`(categoryModel, requestParamsSlot)
        `Given view already created`()

        val categoryFilterVisitable = tokoNowCategoryViewModel.visitableListLiveData.value.getCategoryFilterDataView()
        val selectedQuickFilter = categoryFilterVisitable.categoryFilterItemList[selectedCategoryFilterIndex]

        `When category filter selected`(selectedQuickFilter, false)

        val requestParams = requestParamsSlot.captured
        `Then verify get search first page is called twice`()
        `Then verify request params does not contain unapplied filter`(
                requestParams,
                selectedQuickFilter
        )
        `Then verify request params reset filter and sort`(requestParams, filterParam)
    }

    private fun `Then verify request params does not contain unapplied filter`(
            requestParams: RequestParams,
            selectedCategoryFilter: CategoryFilterItemDataView
    ) {
        val selectedQuickFilterKey = selectedCategoryFilter.option.key
        val tokonowQueryParam = getTokonowQueryParam(requestParams)
        val actualParamsValue = tokonowQueryParam[selectedQuickFilterKey]

        assertThat(actualParamsValue, nullValue())
    }

    @Test
    fun `hide L2 category filter if only 1 filter option`() {
        val categoryModel = "category/categoryfilter/only-one-category-filter.json".jsonToObject<CategoryModel>()

        `Given get category first page use case will be successful`(categoryModel)

        `When view created`()

        val visitableList = tokoNowCategoryViewModel.visitableListLiveData.value!!
        val hasCategoryFilter = visitableList.find { it is CategoryFilterDataView } != null

        assertThat(
                "Should not show category filter if only 1 filter option",
                hasCategoryFilter,
                shouldBe(false)
        )
    }
}
