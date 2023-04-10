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

internal class HandleViewClickAddToCartTest: SimilarSearchTestFixtures() {

    @Test
    fun `Add to Cart Not Logged In`() {
        val similarProductModelCommon = getSimilarProductModelCommon()

        `Given user is not logged in`()
        `Given view already created and has similar search data`(similarProductModelCommon)

        `When handle view click add to cart`()

        `Then should post event go to login page`()
        `Then assert tracking click add to cart event should be null`()
        `Then assert add to cart event should be null`()
    }

    private fun `Then should post event go to login page`() {
        val routeToLoginEvent = similarSearchViewModel.getRouteToLoginPageEventLiveData().value

        routeToLoginEvent?.getContentIfNotHandled().shouldBe(
            true,
            "Route to login page should be true"
        )
    }

    private fun `Then assert tracking click add to cart event should be null`() {
        val trackingAddToCartEventLiveData =
            similarSearchViewModel.getTrackingAddToCartEventLiveData().value

        trackingAddToCartEventLiveData?.getContentIfNotHandled() shouldBe null
    }

    private fun `Then assert add to cart event should be null`() {
        val addToCartEventLiveData = similarSearchViewModel.getAddToCartEventLiveData().value

        addToCartEventLiveData?.getContentIfNotHandled() shouldBe null
    }

    @Test
    fun `Add to Cart Status Success`() {
        val similarProductModelCommon = getSimilarProductModelCommon()
        val addToCartSuccessModel = getAddToCartSuccessModel()
        val mockUserId = "1"
        val addToCartUseCaseInputSlot = slot<RequestParams>()

        `Given user is logged in`()
        `Given user id`(mockUserId)
        `Given view already created and has similar search data`(similarProductModelCommon)
        `Given add to cart use case will return add to cart model with success status`(
            addToCartUseCaseInputSlot, addToCartSuccessModel
        )

        `When handle view click add to cart`()

        `Then should not post event to login page`()
        `Then assert addToCartUseCase is executed with the correct input`(
            addToCartUseCaseInputSlot,
            similarProductModelCommon,
            mockUserId
        )
        `Then assert tracking click add to cart event should contain original product as object data layer`(
            similarProductModelCommon, addToCartSuccessModel
        )
        `Then assert add to cart event should be true (success)`()
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

    private fun `When handle view click add to cart`() {
        similarSearchViewModel.onViewClickAddToCart()
    }

    private fun `Then should not post event to login page`() {
        val routeToLoginEvent = similarSearchViewModel.getRouteToLoginPageEventLiveData().value

        routeToLoginEvent?.getContentIfNotHandled().shouldBe(
            null,
            "Route to login page should be null"
        )
    }

    private fun `Then assert addToCartUseCase is executed with the correct input`(
        addToCartUseCaseInputSlot: CapturingSlot<RequestParams>,
        similarProductModelCommon: SimilarProductModel,
        userId: String
    ) {
        val addToCartUseCaseInput = addToCartUseCaseInputSlot.captured.parameters
        addToCartUseCaseInput.size shouldBe 1

        val addToCartRequestParams =
            addToCartUseCaseInput[REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST] as AddToCartRequestParams

        addToCartRequestParams.productId shouldBe similarProductModelCommon.getOriginalProduct().id
        addToCartRequestParams.shopId shouldBe similarProductModelCommon.getOriginalProduct().shop.id.toString()
        addToCartRequestParams.quantity shouldBe similarProductModelCommon.getOriginalProduct().minOrder
        addToCartRequestParams.productName shouldBe similarProductModelCommon.getOriginalProduct().name
        addToCartRequestParams.category shouldBe similarProductModelCommon.getOriginalProduct().categoryName
        addToCartRequestParams.price shouldBe similarProductModelCommon.getOriginalProduct().price
        addToCartRequestParams.userId shouldBe userId
    }

    private fun `Then assert tracking click add to cart event should contain original product as object data layer`(
        similarProductModelCommon: SimilarProductModel,
        addToCartSuccessModel: AddToCartDataModel
    ) {
        val expectedTrackingData = similarProductModelCommon.getOriginalProduct()
            .asObjectDataLayerAddToCart(addToCartSuccessModel.data.cartId)

        val trackingAddToCartEventLiveData =
            similarSearchViewModel.getTrackingAddToCartEventLiveData().value

        trackingAddToCartEventLiveData?.getContentIfNotHandled() shouldBe expectedTrackingData
    }

    private fun `Then assert add to cart event should be true (success)`() {
        val addToCartEventLiveData = similarSearchViewModel.getAddToCartEventLiveData().value

        addToCartEventLiveData?.getContentIfNotHandled() shouldBe true
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
            addToCartUseCaseInputSlot, addToCartErrorModel
        )

        `When handle view click add to cart`()

        `Then should not post event to login page`()
        `Then assert addToCartUseCase is executed with the correct input`(
            addToCartUseCaseInputSlot,
            similarProductModelCommon,
            mockUserId
        )
        `Then assert tracking click add to cart event should be null`()
        `Then assert add to cart event should be false (failed)`()
        `Then assert get add to cart failed message is the failed message from addToCartUseCase`(
            addToCartErrorModel
        )
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
            addToCartUseCaseInputSlot, testException
        )

        `When handle view click add to cart`()
        `Then should not post event to login page`()
        `Then assert addToCartUseCase is executed with the correct input`(
            addToCartUseCaseInputSlot,
            similarProductModelCommon,
            mockUserId
        )
        `Then assert tracking click add to cart event should be null`()
        `Then assert add to cart event should be false (failed)`()
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
}
