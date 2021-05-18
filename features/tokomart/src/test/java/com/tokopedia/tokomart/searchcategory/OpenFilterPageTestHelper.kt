package com.tokopedia.tokomart.searchcategory

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.tokomart.searchcategory.presentation.viewmodel.BaseSearchCategoryViewModel
import com.tokopedia.tokomart.searchcategory.utils.TOKONOW
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import io.mockk.CapturingSlot
import io.mockk.every
import io.mockk.slot
import io.mockk.verify
import org.junit.Assert.assertThat
import org.hamcrest.CoreMatchers.`is` as shouldBe

class OpenFilterPageTestHelper(
        private val baseViewModel: BaseSearchCategoryViewModel,
        private val getFilterUseCase: UseCase<DynamicFilterModel>,
) {

    fun `test open filter page first time`(expectedQueryParamMap: Map<String, String>) {
        val filterRequestParamsSlot = slot<RequestParams>()
        val filterRequestParams by lazy { filterRequestParamsSlot.captured }
        val dynamicFilterModel = "filter/filter.json".jsonToObject<DynamicFilterModel>()

        `Given get filter API will be successful`(dynamicFilterModel, filterRequestParamsSlot)

        `When view open filter page`()

        `Then assert filter is open live data`(true)
        `Then assert filter request params`(expectedQueryParamMap, filterRequestParams)
        `Then assert dynamic filter model live data is updated`(dynamicFilterModel)
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

        assertThat(actualRequestParams[SearchApiConst.SOURCE], shouldBe(TOKONOW))
    }

    private fun `Then assert dynamic filter model live data is updated`(
            dynamicFilterModel: DynamicFilterModel
    ) {
        assertThat(baseViewModel.dynamicFilterModelLiveData.value, shouldBe(dynamicFilterModel))
    }

    fun `test open filter page cannot be spammed`() {
        val dynamicFilterModel = "filter/filter.json".jsonToObject<DynamicFilterModel>()

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
        val dynamicFilterModel = "filter/filter.json".jsonToObject<DynamicFilterModel>()

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
        val dynamicFilterModel = "filter/filter.json".jsonToObject<DynamicFilterModel>()

        `Given get filter API will be successful`(dynamicFilterModel)
        `Given view open filter page`()
        `Given view dismiss filter page`()

        `When view open filter page`()

        `Then verify get filter API only called once`()
    }

    private fun `Given view dismiss filter page`() {
        baseViewModel.onViewDismissFilterPage()
    }
}