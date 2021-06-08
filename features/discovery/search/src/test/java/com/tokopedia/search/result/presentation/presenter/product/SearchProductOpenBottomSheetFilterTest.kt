package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.search.TestException
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.error
import com.tokopedia.search.shouldBe
import com.tokopedia.search.shouldNotContain
import com.tokopedia.search.utils.createSearchProductDefaultFilter
import com.tokopedia.usecase.RequestParams
import io.mockk.*
import org.junit.Test
import rx.Subscriber
import java.util.HashMap

internal class SearchProductOpenBottomSheetFilterTest: ProductListPresenterTestFixtures() {

    @Test
    fun `Open filter page without parameter (edge cases)`() {
        productListPresenter.openFilterPage(null)

        confirmVerified(productListView)
        confirmVerified(getDynamicFilterUseCase)
    }

    @Test
    fun `Open filter page for the first time with get dynamic filter API success`() {
        val dynamicFilterModel = "searchproduct/dynamicfilter/dynamic-filter-model-common.json".jsonToObject<DynamicFilterModel>()
        val getDynamicFilterRequestParamSlot = slot<RequestParams>()
        val mapParameter = mapOf(SearchApiConst.Q to "samsung", SearchApiConst.OFFICIAL to true, SearchApiConst.USER_WAREHOUSE_ID to warehouseId)

        `Setup choose address`(dummyChooseAddressData)
        `Given get dynamic filter model API will success`(getDynamicFilterRequestParamSlot, dynamicFilterModel)

        `When open filter page`(mapParameter)

        val getDynamicFilterRequestParams = getDynamicFilterRequestParamSlot.captured.parameters
        `Then verify view interactions for open filter page first time`(dynamicFilterModel)
        `Then assert Get Dynamic Filter Request Params`(getDynamicFilterRequestParams, mapParameter)
    }

    private fun `Given get dynamic filter model API will success`(
            getDynamicFilterRequestParamSlot: CapturingSlot<RequestParams>,
            dynamicFilterModel: DynamicFilterModel
    ) {
        every {
            getDynamicFilterUseCase.execute(capture(getDynamicFilterRequestParamSlot), any())
        } answers {
            secondArg<Subscriber<DynamicFilterModel>>().complete(dynamicFilterModel)
        }
    }

    private fun `When open filter page`(mapParameter: Map<String, Any>) {
        productListPresenter.openFilterPage(mapParameter)
    }

    private fun `Then verify view interactions for open filter page first time`(dynamicFilterModel: DynamicFilterModel) {
        verify {
            productListView.sendTrackingOpenFilterPage()
            productListView.openBottomSheetFilter(null)
            productListView.setDynamicFilter(dynamicFilterModel)
        }
    }

    private fun `Then assert Get Dynamic Filter Request Params`(
            getDynamicFilterRequestParams: Map<String, Any>,
            mapParameter: Map<String, Any>
    ) {
        mapParameter.forEach { (key, value) ->
            val actualValue = getDynamicFilterRequestParams[key]
            actualValue.shouldBe(
                    value,
                    "Request params key $key value is $actualValue, should be $value."
            )
        }

        getDynamicFilterRequestParams[SearchApiConst.SOURCE] shouldBe SearchApiConst.DEFAULT_VALUE_SOURCE_PRODUCT
        getDynamicFilterRequestParams[SearchApiConst.DEVICE] shouldBe SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_DEVICE
    }

    @Test
    fun `Open filter page for the first time with get dynamic filter API fail`() {
        val mapParameter = mapOf(SearchApiConst.Q to "samsung", SearchApiConst.OFFICIAL to true)
        val dynamicFilterModelSlot = slot<DynamicFilterModel>()

        `Given get dynamic filter model API will fail`()
        `Given view will set default dynamic filter`(dynamicFilterModelSlot)

        `When open filter page`(mapParameter)

        val defaultDynamicFilterModel = dynamicFilterModelSlot.captured
        `Then verify view interactions for open filter page first time`(defaultDynamicFilterModel)
        `Then verify default filter`(defaultDynamicFilterModel)
    }

    private fun `Given get dynamic filter model API will fail`() {
        every {
            getDynamicFilterUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<DynamicFilterModel>>().error(TestException())
        }
    }

    private fun `Given view will set default dynamic filter`(dynamicFilterModelSlot: CapturingSlot<DynamicFilterModel>) {
        every {
            productListView.setDynamicFilter(capture(dynamicFilterModelSlot))
        } just runs
    }

    private fun `Then verify default filter`(defaultDynamicFilterModel: DynamicFilterModel) {
        val expectedDefaultDynamicFilterModel = createSearchProductDefaultFilter()

        val expectedSort = expectedDefaultDynamicFilterModel.data.sort
        val actualSort = defaultDynamicFilterModel.data.sort
        actualSort.size.shouldBe(expectedSort.size, "Default sort size should be ${expectedSort.size}.")
        actualSort.forEachIndexed { index, sort ->
            sort.key.shouldBe(expectedSort[index].key, "Default sort index $index key should be ${expectedSort[index].key}.")
            sort.name.shouldBe(expectedSort[index].name, "Default sort index $index name should be ${expectedSort[index].name}.")
            sort.value.shouldBe(expectedSort[index].value, "Default sort index $index value should be ${expectedSort[index].value}.")
        }

        val expectedFilter = expectedDefaultDynamicFilterModel.data.filter
        val actualFilter = defaultDynamicFilterModel.data.filter
        actualFilter.size.shouldBe(expectedFilter.size, "Default filter size should be ${expectedFilter.size}.")
        actualFilter.forEachIndexed { index, filter ->
            filter.title.shouldBe(expectedFilter[index].title, "Default filter index $index title should be ${expectedFilter[index].title}.")
            filter.templateName.shouldBe(expectedFilter[index].templateName, "Default filter index $index template name should be ${expectedFilter[index].templateName}.")
        }
    }

    @Test
    fun `Open filter page for the first time with get dynamic filter API returns empty filter`() {
        val mapParameter = mapOf(SearchApiConst.Q to "samsung", SearchApiConst.OFFICIAL to true)
        val dynamicFilterModelSlot = slot<DynamicFilterModel>()

        `Given get dynamic filter model API will success`(slot(), DynamicFilterModel())
        `Given view will set default dynamic filter`(dynamicFilterModelSlot)

        `When open filter page`(mapParameter)

        val defaultDynamicFilterModel = dynamicFilterModelSlot.captured
        `Then verify view interactions for open filter page first time`(defaultDynamicFilterModel)
        `Then verify default filter`(defaultDynamicFilterModel)
    }

    @Test
    fun `Open filter page second time after first time success`() {
        val dynamicFilterModel = "searchproduct/dynamicfilter/dynamic-filter-model-common.json".jsonToObject<DynamicFilterModel>()
        val mapParameter = mapOf(SearchApiConst.Q to "samsung", SearchApiConst.OFFICIAL to true)

        `Given get dynamic filter model API will success`(slot(), dynamicFilterModel)

        `When open filter page`(mapParameter)
        `When bottomsheet filter is dismissed`()
        `When open filter page`(mapParameter)

        `Then verify interactions open filter page second time after first time success`(dynamicFilterModel)
    }

    private fun `When bottomsheet filter is dismissed`() {
        productListPresenter.onBottomSheetFilterDismissed()
    }

    private fun `Then verify interactions open filter page second time after first time success`(dynamicFilterModel: DynamicFilterModel) {
        verifyOrder {
            productListView.sendTrackingOpenFilterPage()
            productListView.openBottomSheetFilter(null)
            getDynamicFilterUseCase.execute(any(), any())
            productListView.setDynamicFilter(dynamicFilterModel)

            productListView.sendTrackingOpenFilterPage()
            productListView.openBottomSheetFilter(dynamicFilterModel)
        }
    }

    @Test
    fun `Open filter page second time after first time failed`() {
        val dynamicFilterModel = "searchproduct/dynamicfilter/dynamic-filter-model-common.json".jsonToObject<DynamicFilterModel>()
        val mapParameter = mapOf(SearchApiConst.Q to "samsung", SearchApiConst.OFFICIAL to true)
        val dynamicFilterModelSlot = slot<DynamicFilterModel>()

        `Given get dynamic filter model API will fail and then success`(dynamicFilterModel)

        `When open filter page`(mapParameter)
        `When bottomsheet filter is dismissed`()
        `When open filter page`(mapParameter)

        `Then verify interactions for open filter page second time after first time failed`(dynamicFilterModelSlot, dynamicFilterModel)
        `Then verify default filter`(dynamicFilterModelSlot.captured)
    }

    private fun `Given get dynamic filter model API will fail and then success`(dynamicFilterModel: DynamicFilterModel) {
        every {
            getDynamicFilterUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<DynamicFilterModel>>().error(TestException())
        } andThen {
            secondArg<Subscriber<DynamicFilterModel>>().complete(dynamicFilterModel)
        }
    }

    private fun `Then verify interactions for open filter page second time after first time failed`(dynamicFilterModelSlot: CapturingSlot<DynamicFilterModel>, dynamicFilterModel: DynamicFilterModel) {
        verifyOrder {
            productListView.sendTrackingOpenFilterPage()
            productListView.openBottomSheetFilter(null)
            getDynamicFilterUseCase.execute(any(), any())
            productListView.setDynamicFilter(capture(dynamicFilterModelSlot))

            productListView.sendTrackingOpenFilterPage()
            productListView.openBottomSheetFilter(null)
            getDynamicFilterUseCase.execute(any(), any())
            productListView.setDynamicFilter(dynamicFilterModel)
        }
    }

    @Test
    fun `User cannot spam bottom sheet filter`() {
        val dynamicFilterModel = "searchproduct/dynamicfilter/dynamic-filter-model-common.json".jsonToObject<DynamicFilterModel>()
        val mapParameter = mapOf(SearchApiConst.Q to "samsung", SearchApiConst.OFFICIAL to true)

        `Given get dynamic filter model API will success`(slot(), dynamicFilterModel)

        `When open filter page`(mapParameter)
        `When open filter page`(mapParameter)

        `Then verify interactions bottom sheet filter opened only once`(dynamicFilterModel)
    }

    private fun `Then verify interactions bottom sheet filter opened only once`(dynamicFilterModel: DynamicFilterModel) {
        verify(exactly = 1) {
            productListView.sendTrackingOpenFilterPage()
            productListView.openBottomSheetFilter(null)
            getDynamicFilterUseCase.execute(any(), any())
            productListView.setDynamicFilter(dynamicFilterModel)
        }
    }
}