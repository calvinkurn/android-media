package com.tokopedia.similarsearch.viewmodel

import com.tokopedia.discovery.common.State
import com.tokopedia.similarsearch.*
import com.tokopedia.similarsearch.getsimilarproducts.model.SimilarProductModel
import com.tokopedia.similarsearch.testutils.*
import com.tokopedia.similarsearch.viewmodel.testinstance.getSimilarProductModelCommon
import com.tokopedia.similarsearch.viewmodel.testinstance.getSimilarProductModelEmptyResult
import com.tokopedia.similarsearch.viewmodel.testinstance.getSimilarProductModelOnePage
import com.tokopedia.usecase.coroutines.UseCase
import org.junit.Test

internal class HandleViewCreatedTest: SimilarSearchTestFixtures() {
    
    @Test
    fun `View is created`() {
        val similarProductModelCommon = getSimilarProductModelCommon()

        `Given get similar product will be successful`(similarProductModelCommon)

        `When handle view is created`()

        `Then verify get similar product API is called`()
    }

    private fun `Then verify get similar product API is called`() {
        getSimilarProductsUseCase.isExecuted()
    }

    @Test
    fun `View is created multiple times`() {
        val similarProductModelCommon = getSimilarProductModelCommon()

        `Given get similar product will be successful`(similarProductModelCommon)

        `When handle view is created multiple times`()

        `Then verify get similar product API is called only once`()
    }

    private fun `When handle view is created multiple times`() {
        similarSearchViewModel.onViewCreated()
        similarSearchViewModel.onViewCreated()
        similarSearchViewModel.onViewCreated()
    }

    private fun `Then verify get similar product API is called only once`() {
        getSimilarProductsUseCase.isExecuted(1)
    }
    
    @Test
    fun `Get Similar Products Successful`() {
        val similarProductModelCommon = getSimilarProductModelCommon()

        `Given get similar product will be successful`(similarProductModelCommon)

        `When handle view is created`()

        `Then assert original product state is success and contains original product data`(
            similarProductModelCommon
        )
        `Then assert similar search state is success and contains similar search data`(
            similarProductModelCommon
        )
        `Then assert has next page is true`()
        `Then assert tracking impression similar product event for all products`(
            similarProductModelCommon
        )
    }

    private fun `Then assert original product state is success and contains original product data`(
        similarProductModel: SimilarProductModel
    ) {
        val originalProductData = similarSearchViewModel.getOriginalProductLiveData().value

        originalProductData.shouldBe(
            similarProductModel.getOriginalProduct(),
            "Original Product data should be equal to original product data from API"
        )
    }

    private fun `Then assert similar search state is success and contains similar search data`(
        similarProductModel: SimilarProductModel
    ) {
        val similarSearchLiveData = similarSearchViewModel.getSimilarSearchLiveData().value
        val expectedSimilarProductList = similarProductModel.getSimilarProductList().subList(
            0,
            SIMILAR_PRODUCT_ITEM_SIZE_PER_PAGE
        )

        similarSearchLiveData.shouldBeInstanceOf<State.Success<*>>()
        similarSearchLiveData.shouldHaveCorrectViewModelListWithLoadingMore(expectedSimilarProductList)
    }

    private fun `Then assert has next page is true`() {
        similarSearchViewModel.getHasNextPage().shouldBe(true,
            "Has next page should be true")
    }

    private fun `Then assert tracking impression similar product event for all products`(
        similarProductModel: SimilarProductModel
    ) {
        val trackingImpressionSimilarProductEvent = similarSearchViewModel.getTrackingImpressionSimilarProductEventLiveData().value
        val trackingSimilarProductContent = trackingImpressionSimilarProductEvent?.getContentIfNotHandled()

        trackingSimilarProductContent.shouldBeListOfMapOfProductItemAsObjectDataLayer(
            similarProductModel.getSimilarProductList()
        )
    }

    @Test
    fun `Get Similar Products Failed with exception`() {
        val exception = TestException()

        `Given get similar product will fail with exception`(exception)

        `When handle view is created`()

        `Then assert stack trace is printed`(exception)
        `Then assert original product live data value should be null`()
        `Then assert similar search state is error and contain empty search data`()
        `Then assert has next page is false`()
        `Then assert tracking empty result event is true`()
        `Then assert tracking impression similar product event is null`()
    }

    private fun `Then assert stack trace is printed`(
        exception: TestException
    ) {
        exception.isStackTracePrinted.shouldBe(true, "Exception stack trace should be printed")
    }

    private fun `Then assert original product live data value should be null`() {
        val originalProductData = similarSearchViewModel.getOriginalProductLiveData().value

        originalProductData.shouldBe(
            null,
            "Original product data should be null"
        )
    }

    private fun `Then assert similar search state is error and contain empty search data`() {
        val similarSearchLiveData = similarSearchViewModel.getSimilarSearchLiveData().value

        similarSearchLiveData.shouldBeInstanceOf<State.Error<*>>()
        similarSearchLiveData.shouldHaveCorrectEmptyResultView()
    }

    private fun `Then assert has next page is false`() {
        similarSearchViewModel.getHasNextPage().shouldBe(false,
            "Has next page should be false")
    }

    private fun `Then assert tracking empty result event is true`() {
        val trackingEmptyResultEvent = similarSearchViewModel.getTrackingEmptyResultEventLiveData().value
        val trackingEmptyResultContent = trackingEmptyResultEvent?.getContentIfNotHandled()

        trackingEmptyResultContent shouldBe true
    }

    private fun `Then assert tracking impression similar product event is null`() {
        val trackingImpressionSimilarProductEvent = similarSearchViewModel.getTrackingImpressionSimilarProductEventLiveData().value
        val trackingSimilarProductContent = trackingImpressionSimilarProductEvent?.getContentIfNotHandled()

        trackingSimilarProductContent.shouldBe(null,
            "Tracking similar product event should be null")
    }

    @Test
    fun `Get Similar Products Failed because of null SimilarSearchModel`() {
        `Given get similar product will fail by returning null object`()

        `When handle view is created`()

        `Then assert original product live data value should be null`()
        `Then assert similar search state is error and contains empty search data`()
        `Then assert has next page is false`()
        `Then assert tracking empty result event is true`()
        `Then assert tracking impression similar product event is null`()
    }

    private fun `Given get similar product will fail by returning null object`() {
        getSimilarProductsUseCase.stubExecute().returns(null)
    }

    private fun `Then assert similar search state is error and contains empty search data`() {
        val similarSearchLiveData = similarSearchViewModel.getSimilarSearchLiveData().value

        similarSearchLiveData.shouldBeInstanceOf<State.Error<*>>()
        similarSearchLiveData.shouldHaveCorrectEmptyResultView()
    }

    @Test
    fun `Get Similar Products returns empty result`() {
        val similarProductModelEmptyResult = getSimilarProductModelEmptyResult()
        `Given get similar product will be successful`(similarProductModelEmptyResult)

        `When handle view is created`()

        `Then assert original product live data value should be null`()
        `Then assert similar search state is success and contains empty search model`()
        `Then assert has next page is false`()
        `Then assert tracking empty result event is true`()
        `Then assert tracking impression similar product event is null`()
    }

    private fun `Then assert similar search state is success and contains empty search model`() {
        val similarSearchLiveData = similarSearchViewModel.getSimilarSearchLiveData().value

        similarSearchLiveData.shouldBeInstanceOf<State.Success<*>>()
        similarSearchLiveData.shouldHaveCorrectEmptyResultView()
    }

    @Test
    fun `Get Similar Products only returns one page of data`() {
        val similarProductModelOnePage = getSimilarProductModelOnePage()
        `Given get similar product will be successful`(
            similarProductModelOnePage
        )

        `When handle view is created`()

        `Then assert original product state is success and contains original product data`(
            similarProductModelOnePage
        )
        `Then assert similar search state is success and contains one page similar search data without loading more model`(
            similarProductModelOnePage
        )
        `Then assert has next page is false`()
        `Then assert tracking impression similar product event for all products`(similarProductModelOnePage)
    }

    private fun `Then assert similar search state is success and contains one page similar search data without loading more model`(
        similarProductModel: SimilarProductModel
    ) {
        val similarSearchLiveData = similarSearchViewModel.getSimilarSearchLiveData().value
        val expectedSimilarProductList = similarProductModel.getSimilarProductList().subList(0, similarProductModel.getSimilarProductList().size)

        similarSearchLiveData.shouldBeInstanceOf<State.Success<*>>()
        similarSearchLiveData.shouldHaveCorrectViewModelListWithoutLoadingMore(expectedSimilarProductList)
    }
}