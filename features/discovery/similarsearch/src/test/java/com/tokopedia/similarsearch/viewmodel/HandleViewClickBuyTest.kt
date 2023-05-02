package com.tokopedia.similarsearch.viewmodel

import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase.Companion.REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST
import com.tokopedia.similarsearch.getsimilarproducts.model.SimilarProductModel
import com.tokopedia.similarsearch.testutils.TestException
import com.tokopedia.similarsearch.testutils.shouldBe
import com.tokopedia.similarsearch.utils.asObjectDataLayerAddToCart
import com.tokopedia.similarsearch.viewmodel.testinstance.getAddToCartFailedModel
import com.tokopedia.similarsearch.viewmodel.testinstance.getAddToCartSuccessModel
import com.tokopedia.similarsearch.viewmodel.testinstance.getSimilarProductModelCommon
import com.tokopedia.usecase.RequestParams
import io.mockk.CapturingSlot
import io.mockk.every
import io.mockk.slot
import org.junit.Test
import rx.Subscriber

internal class HandleViewClickBuyTest: SimilarSearchTestFixtures() {
    
    @Test
    fun `Click Buy, Not Logged In`() {
        val similarProductModelCommon = getSimilarProductModelCommon()

        `Given user is not logged in`()
        `Given view already created and has similar search data`(similarProductModelCommon)

        `When handle view click buy`()

        `Then should post event go to login page`()
        `Then assert tracking click buy event should be null`()
        `Then assert add to cart event should be null`()
        `Then assert route to cart page event is null (do not route to cart page)`()
    }

    private fun `When handle view click buy`() {
        similarSearchViewModel.onViewClickBuy()
    }

    private fun `Then should post event go to login page`() {
        val routeToLoginEvent = similarSearchViewModel.getRouteToLoginPageEventLiveData().value

        routeToLoginEvent?.getContentIfNotHandled().shouldBe(true,
            "Route to login page should be true")
    }

    private fun `Then assert tracking click buy event should be null`() {
        val trackingBuyEventLiveData = similarSearchViewModel.getTrackingBuyEventLiveData().value

        trackingBuyEventLiveData?.getContentIfNotHandled() shouldBe null
    }

    private fun `Then assert add to cart event should be null`() {
        val addToCartEventLiveData = similarSearchViewModel.getAddToCartEventLiveData().value

        addToCartEventLiveData?.getContentIfNotHandled() shouldBe null
    }

    private fun `Then assert route to cart page event is null (do not route to cart page)`() {
        val routeToCartPageEventLiveData = similarSearchViewModel.getRouteToCartPageEventLiveData().value

        routeToCartPageEventLiveData?.getContentIfNotHandled() shouldBe null
    }

    @Test
    fun `Click Buy with Add To Cart API success`() {
        val similarProductModelCommon = getSimilarProductModelCommon()
        val addToCartSuccessModel = getAddToCartSuccessModel()
        val mockUserId = "1"
        val addToCartUseCaseInputSlot = slot<RequestParams>()

        `Given user is logged in`()
        `Given user id`(mockUserId)
        `Given view already created and has similar search data`(similarProductModelCommon)
        `Given add to cart use case will return add to cart model with success status`(
            addToCartUseCaseInputSlot,
            addToCartSuccessModel
        )

        `When handle view click buy`()

        `Then should not post event to login page`()
        `Then assert addToCartUseCase is executed with the correct input`(
            addToCartUseCaseInputSlot,
            similarProductModelCommon,
            mockUserId
        )
        `Then assert tracking click buy event should contain original product as object data layer`(
            similarProductModelCommon,
            addToCartSuccessModel
        )
        `Then assert add to cart event should be null (no popup dialog)`()
        `Then assert route to cart page event is true`()
    }

    private fun `Given add to cart use case will return add to cart model with success status`(
        addToCartUseCaseInputSlot: CapturingSlot<RequestParams>,
        addToCartSuccessModel: AddToCartDataModel
    ) {
        every { addToCartUseCase.execute(capture(addToCartUseCaseInputSlot), any()) }.answers {
            secondArg<Subscriber<AddToCartDataModel>>().onStart()
            secondArg<Subscriber<AddToCartDataModel>>().onNext(addToCartSuccessModel)
            secondArg<Subscriber<AddToCartDataModel>>().onCompleted()
        }
    }

    private fun `Then should not post event to login page`() {
        val routeToLoginEvent = similarSearchViewModel.getRouteToLoginPageEventLiveData().value

        routeToLoginEvent?.getContentIfNotHandled().shouldBe(null,
            "Route to login page should be null")
    }

    private fun `Then assert addToCartUseCase is executed with the correct input`(
        addToCartUseCaseInputSlot: CapturingSlot<RequestParams>,
        similarProductModel: SimilarProductModel,
        userId: String
    ) {
        val addToCartUseCaseInput = addToCartUseCaseInputSlot.captured.parameters
        addToCartUseCaseInput.size shouldBe 1

        val addToCartRequestParams = addToCartUseCaseInput[REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST]
            as AddToCartRequestParams

        addToCartRequestParams.productId shouldBe similarProductModel.getOriginalProduct().id
        addToCartRequestParams.shopId shouldBe similarProductModel.getOriginalProduct().shop.id.toString()
        addToCartRequestParams.quantity shouldBe similarProductModel.getOriginalProduct().minOrder
        addToCartRequestParams.productName shouldBe similarProductModel.getOriginalProduct().name
        addToCartRequestParams.category shouldBe similarProductModel.getOriginalProduct().categoryName
        addToCartRequestParams.price shouldBe similarProductModel.getOriginalProduct().price
        addToCartRequestParams.userId shouldBe userId
    }

    private fun `Then assert tracking click buy event should contain original product as object data layer`(
        similarProductModelCommon: SimilarProductModel,
        addToCartSuccessModel: AddToCartDataModel
    ) {
        val expectedTrackingData = similarProductModelCommon.getOriginalProduct()
            .asObjectDataLayerAddToCart(addToCartSuccessModel.data.cartId)

        val trackingBuyEventLiveData = similarSearchViewModel.getTrackingBuyEventLiveData().value

        trackingBuyEventLiveData?.getContentIfNotHandled() shouldBe expectedTrackingData
    }

    private fun `Then assert add to cart event should be null (no popup dialog)`() {
        val addToCartEventLiveData = similarSearchViewModel.getAddToCartEventLiveData().value

        addToCartEventLiveData?.getContentIfNotHandled() shouldBe null
    }

    private fun `Then assert route to cart page event is true`() {
        val routeToCartPageEventLiveData = similarSearchViewModel.getRouteToCartPageEventLiveData().value

        routeToCartPageEventLiveData?.getContentIfNotHandled() shouldBe true
    }

    @Test
    fun `Add to Cart Status Error`() {
        val similarProductModelCommon = getSimilarProductModelCommon()
        val addToCartErrorModel = getAddToCartFailedModel()
        val mockUserId = "1"
        val addToCartUseCaseInputSlot = slot<RequestParams>()

        `Given user is logged in`()
        `Given user id`(mockUserId)
        `Given view already created and has similar search data`(similarProductModelCommon)
        `Given add to cart use case will return add to cart model with error status`(
            addToCartUseCaseInputSlot,
            addToCartErrorModel
        )

        `When handle view click buy`()

        `Then should not post event to login page`()
        `Then assert addToCartUseCase is executed with the correct input`(
            addToCartUseCaseInputSlot,
            similarProductModelCommon,
            mockUserId
        )
        `Then assert tracking click buy event should be null`()
        `Then assert add to cart event should be false (failed)`()
        `Then assert get add to cart failed message is the failed message from addToCartUseCase`(
            addToCartErrorModel
        )
        `Then assert route to cart page event is null (do not route to cart page)`()
    }

    private fun `Given add to cart use case will return add to cart model with error status`(
        addToCartUseCaseInputSlot: CapturingSlot<RequestParams>,
        addToCartErrorModel: AddToCartDataModel
    ) {
        every { addToCartUseCase.execute(capture(addToCartUseCaseInputSlot), any()) }.answers {
            secondArg<Subscriber<AddToCartDataModel>>().onStart()
            secondArg<Subscriber<AddToCartDataModel>>().onNext(addToCartErrorModel)
            secondArg<Subscriber<AddToCartDataModel>>().onCompleted()
        }
    }

    private fun `Then assert add to cart event should be false (failed)`() {
        val addToCartEventLiveData = similarSearchViewModel.getAddToCartEventLiveData().value

        addToCartEventLiveData?.getContentIfNotHandled() shouldBe false
    }

    private fun `Then assert get add to cart failed message is the failed message from addToCartUseCase`(
        addToCartErrorModel: AddToCartDataModel
    ) {
        val addToCartFailedMessage = similarSearchViewModel.getAddToCartFailedMessage()

        addToCartFailedMessage shouldBe addToCartErrorModel.errorMessage[0]
    }

    @Test
    fun `Add to Cart Error with Exception`() {
        val similarProductModelCommon = getSimilarProductModelCommon()
        val testException = TestException()
        val mockUserId = "1"
        val addToCartUseCaseInputSlot = slot<RequestParams>()

        `Given user is logged in`()
        `Given user id`(mockUserId)
        `Given view already created and has similar search data`(similarProductModelCommon)
        `Given add to cart use case will return add to cart model with error status`(
            addToCartUseCaseInputSlot,
            testException
        )

        `When handle view click buy`()

        `Then should not post event to login page`()
        `Then assert addToCartUseCase is executed with the correct input`(
            addToCartUseCaseInputSlot,
            similarProductModelCommon,
            mockUserId
        )
        `Then assert tracking click add to cart event should be null`()
        `Then assert add to cart event should be false (failed)`()
        `Then assert route to cart page event is null (do not route to cart page)`()
    }

    private fun `Given add to cart use case will return add to cart model with error status`(
        addToCartUseCaseInputSlot: CapturingSlot<RequestParams>,
        testException: Throwable
    ) {
        every { addToCartUseCase.execute(capture(addToCartUseCaseInputSlot), any()) }.answers {
            secondArg<Subscriber<AddToCartDataModel>>().onStart()
            secondArg<Subscriber<AddToCartDataModel>>().onError(testException)
        }
    }

    private fun `Then assert tracking click add to cart event should be null`() {
        val trackingAddToCartEventLiveData = similarSearchViewModel.getTrackingAddToCartEventLiveData().value

        trackingAddToCartEventLiveData?.getContentIfNotHandled() shouldBe null
    }
}
