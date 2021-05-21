package com.tokopedia.tokomart.searchcategory

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheet.ApplySortFilterModel
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.tokomart.searchcategory.data.getTokonowQueryParam
import com.tokopedia.tokomart.searchcategory.presentation.viewmodel.BaseSearchCategoryViewModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import io.mockk.CapturingSlot
import io.mockk.every
import io.mockk.slot
import io.mockk.verify
import org.junit.Assert.assertThat
import java.lang.AssertionError
import org.hamcrest.CoreMatchers.`is` as shouldBe

class FilterPageTestHelper(
        private val baseViewModel: BaseSearchCategoryViewModel,
        private val getFilterUseCase: UseCase<DynamicFilterModel>,
        private val getProductCountUseCase: UseCase<String>,
) {
    private val dynamicFilterModel = "filter/filter.json".jsonToObject<DynamicFilterModel>()
    private val mockApplyFilterMapParam = mutableMapOf<String, String>()
    private val selectedFilterMap = mutableMapOf<String, String>()
    private var applySortFilterModel: ApplySortFilterModel? = null

    fun `test open filter page first time`(expectedQueryParamMap: Map<String, String>) {
        val filterRequestParamsSlot = slot<RequestParams>()
        val filterRequestParams by lazy { filterRequestParamsSlot.captured }

        `Given view already created`()
        `Given get filter API will be successful`(dynamicFilterModel, filterRequestParamsSlot)

        `When view open filter page`()

        `Then assert filter is open live data`(true)
        `Then assert filter request params`(expectedQueryParamMap, filterRequestParams)
        `Then assert dynamic filter model live data is updated`(dynamicFilterModel)
    }

    private fun `Given view already created`() {
        baseViewModel.onViewCreated()
    }

    private fun `Given get filter API will be successful`(
            dynamicFilterModel: DynamicFilterModel,
            filterRequestParamsSlot: CapturingSlot<RequestParams> = slot()
    ) {
        every {
            getFilterUseCase.execute(any(), any(), capture(filterRequestParamsSlot))
        } answers {
            firstArg<(DynamicFilterModel) -> Unit>().invoke(dynamicFilterModel)
        }
    }

    private fun `When view open filter page`() {
        baseViewModel.onViewOpenFilterPage()
    }

    private fun `Then assert filter is open live data`(expectedIsOpen: Boolean) {
        val isFilterPageOpen = baseViewModel.isFilterPageOpenLiveData.value

        assertThat(isFilterPageOpen, shouldBe(expectedIsOpen))
    }

    private fun `Then assert filter request params`(
            expectedQueryParamMap: Map<String, String>,
            filterRequestParams: RequestParams,
    ) {
        val actualRequestParams = filterRequestParams.parameters
        expectedQueryParamMap.forEach { (key, value) ->
            assertThat(actualRequestParams[key], shouldBe(value))
        }
    }

    private fun `Then assert dynamic filter model live data is updated`(
            dynamicFilterModel: DynamicFilterModel
    ) {
        assertThat(baseViewModel.dynamicFilterModelLiveData.value, shouldBe(dynamicFilterModel))
    }

    fun `test open filter page cannot be spammed`() {
        `Given view already created`()
        `Given get filter API will be successful`(dynamicFilterModel)

        `When view open filter page`()
        `When view open filter page`()
        `When view open filter page`()

        `Then verify get filter API only called once`()
    }

    private fun `Then verify get filter API only called once`() {
        verify(exactly = 1) {
            getFilterUseCase.execute(any(), any(), any())
        }
    }

    fun `test dismiss filter page`() {
        `Given view already created`()
        `Given get filter API will be successful`(dynamicFilterModel)
        `Given view open filter page`()

        `When view dismiss filter page`()

        `Then assert filter is open live data`(false)
    }

    private fun `Given view open filter page`() {
        baseViewModel.onViewOpenFilterPage()
    }

    private fun `When view dismiss filter page`() {
        baseViewModel.onViewDismissFilterPage()
    }

    fun `test open filter page second time should not call API again`() {
        `Given view already created`()
        `Given get filter API will be successful`(dynamicFilterModel)
        `Given view open filter page`()
        `Given view dismiss filter page`()

        `When view open filter page`()

        `Then verify get filter API only called once`()
    }

    private fun `Given view dismiss filter page`() {
        baseViewModel.onViewDismissFilterPage()
    }

    fun `test apply filter from filter page`(testInterface: ApplyFilterTestInterface) {
        val requestParamsSlot = slot<RequestParams>()
        val requestParams by lazy { requestParamsSlot.captured }

        `Given view already created`()
        `Given get filter API will be successful`(dynamicFilterModel)
        `Given view open filter page`()
        `Given mock apply filter param`(dynamicFilterModel)

        `When view apply filter`()

        testInterface.`Then assert first page use case is called twice`(requestParamsSlot)
        `Then verify query params is updated from filter`(requestParams)
    }

    private fun `Given mock apply filter param`(dynamicFilterModel: DynamicFilterModel) {
        val selectedFilterOption = dynamicFilterModel.data.filter[0].options[3]

        selectedFilterMap.clear()
        selectedFilterMap[selectedFilterOption.key] = selectedFilterOption.value

        mockApplyFilterMapParam.clear()
        mockApplyFilterMapParam.putAll(baseViewModel.queryParam)
        mockApplyFilterMapParam.putAll(selectedFilterMap)

        applySortFilterModel = ApplySortFilterModel(
                mapParameter = mockApplyFilterMapParam,
                selectedFilterMapParameter = selectedFilterMap,
                selectedSortMapParameter = mapOf(),
                selectedSortName = "",
        )
    }

    private fun `When view apply filter`() {
        val applySortFilterModel = applySortFilterModel ?:
        throw AssertionError("Apply Sort Filter Model is null")

        baseViewModel.onViewApplySortFilter(applySortFilterModel)
    }

    private fun `Then verify query params is updated from filter`(requestParams: RequestParams) {
        val queryParams = getTokonowQueryParam(requestParams)

        mockApplyFilterMapParam.forEach { (key, value) ->
            assertThat("Query param with key $key is incorrect", queryParams[key], shouldBe(value))
        }
    }

    fun `test get filter count success when choosing filter`(mandatoryParams: Map<String, Any>) {
        val requestParamsSlot = slot<RequestParams>()
        val requestParams by lazy { requestParamsSlot.captured }
        val successResponse = "10rb+"

        `Given view already created`()
        `Given get filter API will be successful`(dynamicFilterModel)
        `Given get product count API will be successful`(requestParamsSlot, successResponse)
        `Given view open filter page`()
        `Given mock apply filter param`(dynamicFilterModel)

        `When view get product count`()

        `Then assert params for get product count`(mandatoryParams, requestParams)
        `Then assert product count live data`(successResponse)
    }

    private fun `Given get product count API will be successful`(
            requestParamsSlot: CapturingSlot<RequestParams>,
            successResponse: String,
    ) {
        every {
            getProductCountUseCase.execute(any(), any(), capture(requestParamsSlot))
        } answers {
            firstArg<(String) -> Unit>().invoke(successResponse)
        }
    }

    private fun `When view get product count`() {
        baseViewModel.onViewGetProductCount(mockApplyFilterMapParam)
    }

    private fun `Then assert params for get product count`(
            mandatoryParams: Map<String, Any>,
            getProductCountRequestParams: RequestParams
    ) {
        val expectedGetProductCountParams =
                mockApplyFilterMapParam + mandatoryParams + mapOf(SearchApiConst.ROWS to 0)

        val getProductCountParams = getProductCountRequestParams.parameters

        expectedGetProductCountParams.forEach { (key, value) ->
            val reason = "Get product count params key \"$key\" value is invalid"
            assertThat(reason, getProductCountParams[key].toString(), shouldBe(value.toString()))
        }
    }

    private fun `Then assert product count live data`(successResponse: String) {
        assertThat(baseViewModel.productCountAfterFilterLiveData.value, shouldBe(successResponse))
    }

    fun `test get filter count fail when choosing filter`(mandatoryParams: Map<String, Any>) {
        val requestParamsSlot = slot<RequestParams>()
        val requestParams by lazy { requestParamsSlot.captured }

        `Given view already created`()
        `Given get filter API will be successful`(dynamicFilterModel)
        `Given get product count API will fail`(requestParamsSlot)
        `Given view open filter page`()
        `Given mock apply filter param`(dynamicFilterModel)

        `When view get product count`()

        `Then assert params for get product count`(mandatoryParams, requestParams)
        `Then assert product count live data`("0")
    }

    private fun `Given get product count API will fail`(requestParamsSlot: CapturingSlot<RequestParams>) {
        every {
            getProductCountUseCase.execute(any(), any(), capture(requestParamsSlot))
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(Throwable())
        }
    }

    interface ApplyFilterTestInterface {
        fun `Then assert first page use case is called twice`(requestParamsSlot: CapturingSlot<RequestParams>)
    }
}