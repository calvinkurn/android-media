package com.tokopedia.tokomart.category.presentation.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.tokomart.category.domain.model.CategoryModel
import com.tokopedia.tokomart.searchcategory.data.getTokonowQueryParam
import com.tokopedia.tokomart.searchcategory.jsonToObject
import com.tokopedia.tokomart.searchcategory.presentation.model.QuickFilterDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.SortFilterItemDataView
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.usecase.RequestParams
import io.mockk.slot
import io.mockk.verify
import org.hamcrest.CoreMatchers.nullValue
import org.junit.Assert.assertThat
import org.junit.Test
import org.hamcrest.CoreMatchers.`is` as shouldBe

class CategoryQuickFilterTest: CategoryTestFixtures() {

    private val categoryModel = "category/first-page-8-products.json".jsonToObject<CategoryModel>()

    @Test
    fun `when view created, quick filter type selected should be based on query params`() {
        val selectedFilterOption = categoryModel.quickFilter.filter[2].options[0]
        val queryParamWithFilter = mapOf(
                selectedFilterOption.key to selectedFilterOption.value,
        )

        `Given category view model`(defaultCategoryId, queryParamWithFilter)
        `Given get category first page use case will be successful`(categoryModel)

        `When view created`()

        val quickFilterVisitable = categoryViewModel.visitableListLiveData.value.getQuickFilterDataView()
        `Then assert quick filter selected type`(selectedFilterOption, quickFilterVisitable)
    }

    private fun List<Visitable<*>>?.getQuickFilterDataView(): QuickFilterDataView {
        return this?.find { it is QuickFilterDataView } as? QuickFilterDataView ?:
        throw AssertionError("Quick filter data view not found")
    }

    private fun `Then assert quick filter selected type`(
            selectedFilterOption: Option,
            quickFilterVisitable: QuickFilterDataView,
    ) {
        quickFilterVisitable.quickFilterItemList.forEach {
            val failedReason = "Quick filter option \"${it.firstOption!!.key}\" type is incorrect."
            val expectedType = getExpectedChipsUnifyType(it.firstOption!!, selectedFilterOption)

            assertThat(failedReason, it.sortFilterItem.type, shouldBe(expectedType))
        }
    }

    private fun getExpectedChipsUnifyType(actualFilterOption: Option, selectedFilterOption: Option) =
            if (actualFilterOption == selectedFilterOption)
                ChipsUnify.TYPE_SELECTED
            else
                ChipsUnify.TYPE_NORMAL

    @Test
    fun `click quick filter to apply filter`() {
        val requestParamsSlot = slot<RequestParams>()
        `Given get category first page use case will be successful`(categoryModel, requestParamsSlot)
        `Given view already created`()

        val quickFilterVisitable = categoryViewModel.visitableListLiveData.value.getQuickFilterDataView()
        val selectedQuickFilter = quickFilterVisitable.quickFilterItemList[1]

        `When quick filter selected`(selectedQuickFilter)

        `Then verify get category first page is called twice`()
        `Then verify request params contains the applied quick filter`(
                requestParamsSlot.captured,
                selectedQuickFilter
        )
    }

    private fun `When quick filter selected`(selectedQuickFilter: SortFilterItemDataView) {
        selectedQuickFilter.sortFilterItem.listener()
    }

    private fun `Then verify get category first page is called twice`() {
        verify(exactly = 2) {
            getCategoryFirstPageUseCase.cancelJobs()
            getCategoryFirstPageUseCase.execute(any(), any(), any())
        }
    }

    private fun `Then verify request params contains the applied quick filter`(
            requestParams: RequestParams,
            selectedQuickFilter: SortFilterItemDataView
    ) {
        val selectedQuickFilterKey = selectedQuickFilter.firstOption!!.key
        val actualParamsValue = getTokonowQueryParam(requestParams)[selectedQuickFilterKey].toString()

        assertThat(actualParamsValue, shouldBe(selectedQuickFilter.firstOption!!.value))
    }

    @Test
    fun `click quick filter to un-apply filter`() {
        val requestParamsSlot = slot<RequestParams>()
        val selectedQuickFilterIndex = 2
        val previouslySelectedFilterOption = categoryModel.quickFilter.filter[selectedQuickFilterIndex].options[0]
        val queryParamWithFilter = mapOf(
                previouslySelectedFilterOption.key to previouslySelectedFilterOption.value,
        )

        `Given category view model`(defaultCategoryId, queryParamWithFilter)
        `Given get category first page use case will be successful`(categoryModel, requestParamsSlot)
        `Given view already created`()

        val quickFilterVisitable = categoryViewModel.visitableListLiveData.value.getQuickFilterDataView()
        val selectedQuickFilter = quickFilterVisitable.quickFilterItemList[selectedQuickFilterIndex]

        `When quick filter selected`(selectedQuickFilter)

        `Then verify get category first page is called twice`()
        `Then verify quick filter does not contain unapplied filter`(
                requestParamsSlot.captured,
                selectedQuickFilter
        )
    }

    private fun `Then verify quick filter does not contain unapplied filter`(
            requestParams: RequestParams,
            selectedQuickFilter: SortFilterItemDataView
    ) {
        val selectedQuickFilterKey = selectedQuickFilter.firstOption!!.key
        val actualParamsValue = requestParams.parameters[selectedQuickFilterKey]

        assertThat(actualParamsValue, nullValue())
    }

    @Test
    fun `click quick filter to open L3 filter bottom sheet`() {
        val requestParamsSlot = slot<RequestParams>()
        `Given get category first page use case will be successful`(categoryModel, requestParamsSlot)
        `Given view already created`()

        val quickFilterVisitable = categoryViewModel.visitableListLiveData.value.getQuickFilterDataView()
        val selectedQuickFilter = quickFilterVisitable.quickFilterItemList[0]

        `When quick filter selected`(selectedQuickFilter)

        `Then assert L3 Bottomsheet filter is open with filter`(selectedQuickFilter.filter)
    }

    private fun `Then assert L3 Bottomsheet filter is open with filter`(selectedFilter: Filter) {
        val isL3FilterPageOpen = categoryViewModel.isL3FilterPageOpenLiveData.value

        assertThat(isL3FilterPageOpen, shouldBe(selectedFilter))
    }
}