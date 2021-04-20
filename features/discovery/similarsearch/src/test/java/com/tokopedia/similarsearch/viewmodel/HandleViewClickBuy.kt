package com.tokopedia.similarsearch.viewmodel

import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase.Companion.REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST
import com.tokopedia.similarsearch.SimilarSearchViewModel
import com.tokopedia.similarsearch.getsimilarproducts.model.SimilarProductModel
import com.tokopedia.similarsearch.testutils.InstantTaskExecutorRuleSpek
import com.tokopedia.similarsearch.testutils.TestException
import com.tokopedia.similarsearch.testutils.shouldBe
import com.tokopedia.similarsearch.testutils.stubExecute
import com.tokopedia.similarsearch.utils.asObjectDataLayerAddToCart
import com.tokopedia.similarsearch.viewmodel.testinstance.getAddToCartFailedModel
import com.tokopedia.similarsearch.viewmodel.testinstance.getAddToCartSuccessModel
import com.tokopedia.similarsearch.viewmodel.testinstance.getSimilarProductModelCommon
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.every
import io.mockk.slot
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Subscriber
import com.tokopedia.usecase.UseCase as RxUseCase

internal class HandleViewClickBuy: Spek({

    InstantTaskExecutorRuleSpek(this)

    Feature("Original Product Click Buy") {
        createTestInstance()

        Scenario("Click Buy, Not Logged In") {
            val similarProductModelCommon = getSimilarProductModelCommon()
            val userSession by memoized<UserSessionInterface>()

            lateinit var similarSearchViewModel: SimilarSearchViewModel

            Given("similar search view model") {
                similarSearchViewModel = createSimilarSearchViewModel()
            }

            Given("user is not logged in") {
                every { userSession.isLoggedIn }.returns(false)
            }

            Given("view already created and has similar search data") {
                val getSimilarProductsUseCase by memoized<UseCase<SimilarProductModel>>()
                getSimilarProductsUseCase.stubExecute().returns(similarProductModelCommon)
                similarSearchViewModel.onViewCreated()
            }

            When("handle view click buy") {
                similarSearchViewModel.onViewClickBuy()
            }

            Then("should post event go to login page") {
                val routeToLoginEvent = similarSearchViewModel.getRouteToLoginPageEventLiveData().value

                routeToLoginEvent?.getContentIfNotHandled().shouldBe(true,
                        "Route to login page should be true")
            }

            Then("assert tracking click buy event should be null") {
                val trackingBuyEventLiveData = similarSearchViewModel.getTrackingBuyEventLiveData().value

                trackingBuyEventLiveData?.getContentIfNotHandled() shouldBe null
            }

            Then("assert add to cart event should be null") {
                val addToCartEventLiveData = similarSearchViewModel.getAddToCartEventLiveData().value

                addToCartEventLiveData?.getContentIfNotHandled() shouldBe null
            }

            Then("assert route to cart page event is null (do not route to cart page)") {
                val routeToCartPageEventLiveData = similarSearchViewModel.getRouteToCartPageEventLiveData().value

                routeToCartPageEventLiveData?.getContentIfNotHandled() shouldBe null
            }
        }

        Scenario("Click Buy with Add To Cart API success") {
            val similarProductModelCommon = getSimilarProductModelCommon()
            val addToCartSuccessModel = getAddToCartSuccessModel()
            val mockUserId = "1"

            val addToCartUseCase by memoized<RxUseCase<AddToCartDataModel>>()
            val userSession by memoized<UserSessionInterface>()

            val addToCartUseCaseInputSlot = slot<RequestParams>()

            lateinit var similarSearchViewModel: SimilarSearchViewModel

            Given("similar search view model") {
                similarSearchViewModel = createSimilarSearchViewModel()
            }

            Given("user is logged in") {
                every { userSession.isLoggedIn }.returns(true)
            }

            Given("user id") {
                every { userSession.userId } returns(mockUserId)
            }

            Given("view already created and has similar search data") {
                val getSimilarProductsUseCase by memoized<UseCase<SimilarProductModel>>()
                getSimilarProductsUseCase.stubExecute().returns(similarProductModelCommon)
                similarSearchViewModel.onViewCreated()
            }

            Given("add to cart use case will return add to cart model with success status") {
                every { addToCartUseCase.execute(capture(addToCartUseCaseInputSlot), any()) }.answers {
                    secondArg<Subscriber<AddToCartDataModel>>().onStart()
                    secondArg<Subscriber<AddToCartDataModel>>().onNext(addToCartSuccessModel)
                    secondArg<Subscriber<AddToCartDataModel>>().onCompleted()
                }
            }

            When("handle view click buy") {
                similarSearchViewModel.onViewClickBuy()
            }

            Then("should not post event to login page") {
                val routeToLoginEvent = similarSearchViewModel.getRouteToLoginPageEventLiveData().value

                routeToLoginEvent?.getContentIfNotHandled().shouldBe(null,
                        "Route to login page should be null")
            }

            Then("assert addToCartUseCase is executed with the correct input") {
                val addToCartUseCaseInput = addToCartUseCaseInputSlot.captured.parameters
                addToCartUseCaseInput.size shouldBe 1

                val addToCartRequestParams = addToCartUseCaseInput[REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST] as AddToCartRequestParams

                addToCartRequestParams.productId shouldBe similarProductModelCommon.getOriginalProduct().id.toLong()
                addToCartRequestParams.shopId shouldBe similarProductModelCommon.getOriginalProduct().shop.id
                addToCartRequestParams.quantity shouldBe similarProductModelCommon.getOriginalProduct().minOrder
                addToCartRequestParams.productName shouldBe similarProductModelCommon.getOriginalProduct().name
                addToCartRequestParams.category shouldBe similarProductModelCommon.getOriginalProduct().categoryName
                addToCartRequestParams.price shouldBe similarProductModelCommon.getOriginalProduct().price
                addToCartRequestParams.userId shouldBe mockUserId
            }

            Then("assert tracking click buy event should contain original product as object data layer") {
                val expectedTrackingData = similarProductModelCommon.getOriginalProduct()
                        .asObjectDataLayerAddToCart(addToCartSuccessModel.data.cartId)

                val trackingBuyEventLiveData = similarSearchViewModel.getTrackingBuyEventLiveData().value

                trackingBuyEventLiveData?.getContentIfNotHandled() shouldBe expectedTrackingData
            }

            Then("assert add to cart event should be null (no popup dialog)") {
                val addToCartEventLiveData = similarSearchViewModel.getAddToCartEventLiveData().value

                addToCartEventLiveData?.getContentIfNotHandled() shouldBe null
            }

            Then("assert route to cart page event is true") {
                val routeToCartPageEventLiveData = similarSearchViewModel.getRouteToCartPageEventLiveData().value

                routeToCartPageEventLiveData?.getContentIfNotHandled() shouldBe true
            }
        }

        Scenario("Add to Cart Status Error") {
            val similarProductModelCommon = getSimilarProductModelCommon()
            val addToCartErrorModel = getAddToCartFailedModel()
            val mockUserId = "1"

            val addToCartUseCase by memoized<RxUseCase<AddToCartDataModel>>()
            val userSession by memoized<UserSessionInterface>()

            val addToCartUseCaseInputSlot = slot<RequestParams>()

            lateinit var similarSearchViewModel: SimilarSearchViewModel

            Given("similar search view model") {
                similarSearchViewModel = createSimilarSearchViewModel()
            }

            Given("user is logged in") {
                every { userSession.isLoggedIn }.returns(true)
            }

            Given("user id") {
                every { userSession.userId } returns(mockUserId)
            }

            Given("view already created and has similar search data") {
                val getSimilarProductsUseCase by memoized<UseCase<SimilarProductModel>>()
                getSimilarProductsUseCase.stubExecute().returns(similarProductModelCommon)
                similarSearchViewModel.onViewCreated()
            }

            Given("add to cart use case will return add to cart model with error status") {
                every { addToCartUseCase.execute(capture(addToCartUseCaseInputSlot), any()) }.answers {
                    secondArg<Subscriber<AddToCartDataModel>>().onStart()
                    secondArg<Subscriber<AddToCartDataModel>>().onNext(addToCartErrorModel)
                    secondArg<Subscriber<AddToCartDataModel>>().onCompleted()
                }
            }

            When("handle view click buy") {
                similarSearchViewModel.onViewClickBuy()
            }

            Then("should not post event to login page") {
                val routeToLoginEvent = similarSearchViewModel.getRouteToLoginPageEventLiveData().value

                routeToLoginEvent?.getContentIfNotHandled().shouldBe(null,
                        "Route to login page should be null")
            }

            Then("assert addToCartUseCase is executed with the correct input") {
                val addToCartUseCaseInput = addToCartUseCaseInputSlot.captured.parameters
                addToCartUseCaseInput.size shouldBe 1

                val addToCartRequestParams = addToCartUseCaseInput[REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST] as AddToCartRequestParams

                addToCartRequestParams.productId shouldBe similarProductModelCommon.getOriginalProduct().id.toLong()
                addToCartRequestParams.shopId shouldBe similarProductModelCommon.getOriginalProduct().shop.id
                addToCartRequestParams.quantity shouldBe similarProductModelCommon.getOriginalProduct().minOrder
                addToCartRequestParams.productName shouldBe similarProductModelCommon.getOriginalProduct().name
                addToCartRequestParams.category shouldBe similarProductModelCommon.getOriginalProduct().categoryName
                addToCartRequestParams.price shouldBe similarProductModelCommon.getOriginalProduct().price
                addToCartRequestParams.userId shouldBe mockUserId
            }

            Then("assert tracking click buy event should be null") {
                val trackingBuyEventLiveData = similarSearchViewModel.getTrackingBuyEventLiveData().value

                trackingBuyEventLiveData?.getContentIfNotHandled() shouldBe null
            }

            Then("assert add to cart event should be false (failed)") {
                val addToCartEventLiveData = similarSearchViewModel.getAddToCartEventLiveData().value

                addToCartEventLiveData?.getContentIfNotHandled() shouldBe false
            }

            Then("assert get add to cart failed message is the failed message from addToCartUseCase") {
                val addToCartFailedMessage = similarSearchViewModel.getAddToCartFailedMessage()

                addToCartFailedMessage shouldBe addToCartErrorModel.errorMessage[0]
            }

            Then("assert route to cart page event is null (do not route to cart page)") {
                val routeToCartPageEventLiveData = similarSearchViewModel.getRouteToCartPageEventLiveData().value

                routeToCartPageEventLiveData?.getContentIfNotHandled() shouldBe null
            }
        }

        Scenario("Add to Cart Error with Exception") {
            val similarProductModelCommon = getSimilarProductModelCommon()
            val testException = TestException()
            val mockUserId = "1"

            val addToCartUseCase by memoized<RxUseCase<AddToCartDataModel>>()
            val userSession by memoized<UserSessionInterface>()

            val addToCartUseCaseInputSlot = slot<RequestParams>()

            lateinit var similarSearchViewModel: SimilarSearchViewModel

            Given("similar search view model") {
                similarSearchViewModel = createSimilarSearchViewModel()
            }

            Given("user is logged in") {
                every { userSession.isLoggedIn }.returns(true)
            }

            Given("user id") {
                every { userSession.userId } returns(mockUserId)
            }

            Given("view already created and has similar search data") {
                val getSimilarProductsUseCase by memoized<UseCase<SimilarProductModel>>()
                getSimilarProductsUseCase.stubExecute().returns(similarProductModelCommon)
                similarSearchViewModel.onViewCreated()
            }

            Given("add to cart use case will return add to cart model with error status") {
                every { addToCartUseCase.execute(capture(addToCartUseCaseInputSlot), any()) }.answers {
                    secondArg<Subscriber<AddToCartDataModel>>().onStart()
                    secondArg<Subscriber<AddToCartDataModel>>().onError(testException)
                }
            }

            When("handle view click buy") {
                similarSearchViewModel.onViewClickBuy()
            }

            Then("should not post event to login page") {
                val routeToLoginEvent = similarSearchViewModel.getRouteToLoginPageEventLiveData().value

                routeToLoginEvent?.getContentIfNotHandled().shouldBe(null,
                        "Route to login page should be null")
            }

            Then("assert addToCartUseCase is executed with the correct input") {
                val addToCartUseCaseInput = addToCartUseCaseInputSlot.captured.parameters
                addToCartUseCaseInput.size shouldBe 1

                val addToCartRequestParams = addToCartUseCaseInput[REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST] as AddToCartRequestParams

                addToCartRequestParams.productId shouldBe similarProductModelCommon.getOriginalProduct().id.toLong()
                addToCartRequestParams.shopId shouldBe similarProductModelCommon.getOriginalProduct().shop.id
                addToCartRequestParams.quantity shouldBe similarProductModelCommon.getOriginalProduct().minOrder
                addToCartRequestParams.productName shouldBe similarProductModelCommon.getOriginalProduct().name
                addToCartRequestParams.category shouldBe similarProductModelCommon.getOriginalProduct().categoryName
                addToCartRequestParams.price shouldBe similarProductModelCommon.getOriginalProduct().price
                addToCartRequestParams.userId shouldBe mockUserId
            }

            Then("assert tracking click add to cart event should be null") {
                val trackingAddToCartEventLiveData = similarSearchViewModel.getTrackingAddToCartEventLiveData().value

                trackingAddToCartEventLiveData?.getContentIfNotHandled() shouldBe null
            }

            Then("assert add to cart event should be false (failed)") {
                val addToCartEventLiveData = similarSearchViewModel.getAddToCartEventLiveData().value

                addToCartEventLiveData?.getContentIfNotHandled() shouldBe false
            }

            Then("assert route to cart page event is null (do not route to cart page)") {
                val routeToCartPageEventLiveData = similarSearchViewModel.getRouteToCartPageEventLiveData().value

                routeToCartPageEventLiveData?.getContentIfNotHandled() shouldBe null
            }
        }
    }
})