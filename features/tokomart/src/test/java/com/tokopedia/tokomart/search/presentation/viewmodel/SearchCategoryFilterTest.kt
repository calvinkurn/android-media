package com.tokopedia.tokomart.search.presentation.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.newdynamicfilter.helper.OptionHelper
import com.tokopedia.tokomart.search.domain.model.SearchModel
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

class SearchCategoryFilterTest: SearchTestFixtures() {

    private val searchModel = "search/first-page-8-products.json".jsonToObject<SearchModel>()

    @Test
    fun `when view created, category filter isSelected should be based on query params`() {
        val selectedFilterOption = OptionHelper.copyOptionAsExclude(
                searchModel.categoryFilter.filter[0].options[1]
        )
        val queryParamWithFilter = mapOf(
                SearchApiConst.Q to defaultKeyword,
                selectedFilterOption.key to selectedFilterOption.value,
        )

        `Given search view model`(queryParamWithFilter)
        `Given get search first page use case will be successful`(searchModel)

        `When view created`()

        val visitableList = searchViewModel.visitableListLiveData.value
        val quickFilterVisitable = visitableList.getCategoryFilterDataView()
        `Then assert category filter isSelected`(selectedFilterOption, quickFilterVisitable)
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
        `Given get search first page use case will be successful`(searchModel, requestParamsSlot)
        `Given view already created`()

        val visitableList = searchViewModel.visitableListLiveData.value
        val categoryFilterVisitable = visitableList.getCategoryFilterDataView()
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
        searchViewModel.onViewClickCategoryFilterChip(selectedCategoryFilter.option, isSelected)
    }

    private fun `Then verify get search first page is called twice`() {
        verify(exactly = 2) {
            getSearchFirstPageUseCase.cancelJobs()
            getSearchFirstPageUseCase.execute(any(), any(), any())
        }
    }

    private fun `Then verify request params contains the applied category filter`(
            requestParams: RequestParams,
            selectedCategoryFilter: CategoryFilterItemDataView
    ) {
        val selectedCategoryFilterKey = OptionHelper.getKeyRemoveExclude(selectedCategoryFilter.option)
        val tokonowQueryParam = getTokonowQueryParam(requestParams)
        val actualParamsValue = tokonowQueryParam[selectedCategoryFilterKey].toString()

        assertThat(actualParamsValue, shouldBe(selectedCategoryFilter.option.value))
    }

    @Test
    fun `click category filter to un-apply filter`() {
        val requestParamsSlot = slot<RequestParams>()
        val selectedCategoryFilterIndex = 2
        val previouslySelectedFilterOption =
                searchModel.quickFilter.filter[selectedCategoryFilterIndex].options[0]
        val queryParamWithFilter = mapOf(
                SearchApiConst.Q to defaultKeyword,
                previouslySelectedFilterOption.key to previouslySelectedFilterOption.value,
        )

        `Given search view model`(queryParamWithFilter)
        `Given get search first page use case will be successful`(searchModel, requestParamsSlot)
        `Given view already created`()

        val visitableList = searchViewModel.visitableListLiveData.value
        val categoryFilterVisitable = visitableList.getCategoryFilterDataView()
        val categoryFilterItemList = categoryFilterVisitable.categoryFilterItemList
        val selectedQuickFilter = categoryFilterItemList[selectedCategoryFilterIndex]

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
        val selectedQuickFilterKey = OptionHelper.getKeyRemoveExclude(selectedCategoryFilter.option)
        val actualParamsValue = requestParams.parameters[selectedQuickFilterKey]

        assertThat(actualParamsValue, nullValue())
    }

    @Test
    fun `apply new click category filter should remove existing category filter`() {
        val requestParamsSlot = slot<RequestParams>()
        val queryParamWithCategoryFilter = mapOf(
                SearchApiConst.Q to defaultKeyword,
                SearchApiConst.SC to "1324",
                OptionHelper.EXCLUDE_PREFIX + SearchApiConst.SC to "1333",
        )

        `Given search view model`(queryParamWithCategoryFilter)
        `Given get search first page use case will be successful`(searchModel, requestParamsSlot)
        `Given view already created`()

        val visitableList = searchViewModel.visitableListLiveData.value
        val categoryFilterVisitable = visitableList.getCategoryFilterDataView()
        val selectedCategoryFilter = categoryFilterVisitable.categoryFilterItemList[1]

        `When category filter selected`(selectedCategoryFilter, true)

        `Then verify get search first page is called twice`()
        `Then verify request params contains the applied category filter`(
                requestParamsSlot.captured,
                selectedCategoryFilter
        )
    }
}