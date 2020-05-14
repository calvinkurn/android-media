package com.tokopedia.similarsearch.viewmodel

import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.discovery.common.model.WishlistTrackingModel
import com.tokopedia.similarsearch.SimilarSearchViewModel
import com.tokopedia.similarsearch.getsimilarproducts.model.SimilarProductModel
import com.tokopedia.similarsearch.testutils.InstantTaskExecutorRuleSpek
import com.tokopedia.similarsearch.testutils.shouldBe
import com.tokopedia.similarsearch.testutils.stubExecute
import com.tokopedia.similarsearch.viewmodel.testinstance.getSimilarProductModelCommon
import com.tokopedia.similarsearch.viewmodel.testinstance.getSimilarSearchQuery
import com.tokopedia.usecase.coroutines.UseCase
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

internal class HandleReceiveProductCardOptionsWishlistResult: Spek({
    InstantTaskExecutorRuleSpek(this)

    Feature("Handle product card options wishlist result") {
        createTestInstance()

        Scenario("Receive wishlist result user not logged in") {
            val productCardOptionsModel = ProductCardOptionsModel().also {
                it.productId = "12345"
                it.isWishlisted = false
                it.wishlistResult = ProductCardOptionsModel.WishlistResult(
                        isUserLoggedIn = false
                )
            }

            lateinit var similarSearchViewModel: SimilarSearchViewModel

            Given("Similar search view model") {
                similarSearchViewModel = createSimilarSearchViewModel()
            }

            When("Receive wishlist result") {
                similarSearchViewModel.onReceiveProductCardOptionsWishlistResult(productCardOptionsModel)
            }

            Then("Verify route to login page event is true") {
                val routeToLoginEvent = similarSearchViewModel.getRouteToLoginPageEventLiveData().value

                routeToLoginEvent?.getContentIfNotHandled().shouldBe(
                        true,
                        "Route to login page event should be true"
                )
            }

            Then("Verify tracking wishlist event has correct WishlistTrackingModel") {
                val trackingWishlistEventLiveData = similarSearchViewModel.getTrackingWishlistEventLiveData().value
                val expectedWishlistTrackingModel = WishlistTrackingModel(
                        isAddWishlist = !productCardOptionsModel.isWishlisted,
                        productId = productCardOptionsModel.productId,
                        isTopAds = productCardOptionsModel.isTopAds,
                        keyword = getSimilarSearchQuery(),
                        isUserLoggedIn = productCardOptionsModel.wishlistResult.isUserLoggedIn
                )

                trackingWishlistEventLiveData?.getContentIfNotHandled() shouldBe expectedWishlistTrackingModel
            }

            Then("Verify other wishlist event should be null") {
                val addWishlistEvent = similarSearchViewModel.getAddWishlistEventLiveData().value
                val removeWishlistEvent = similarSearchViewModel.getRemoveWishlistEventLiveData().value


                addWishlistEvent?.getContentIfNotHandled().shouldBe(null, "Add wishlist event should be null")
                removeWishlistEvent?.getContentIfNotHandled().shouldBe(null, "Remove wishlist event should be null")
            }
        }

        Scenario("Receive wishlist result add to wishlist success") {
            val similarProductModel = getSimilarProductModelCommon()
            val productId = similarProductModel.getSimilarProductList()[0].id
            val productCardOptionsModel = ProductCardOptionsModel().also {
                it.productId = productId
                it.isWishlisted = false
                it.wishlistResult = ProductCardOptionsModel.WishlistResult(
                        isUserLoggedIn = true,
                        isSuccess = true,
                        isAddWishlist = true
                )
            }

            lateinit var similarSearchViewModel: SimilarSearchViewModel

            Given("Similar search view model") {
                similarSearchViewModel = createSimilarSearchViewModel()
            }

            Given("View is already created and has similar search data") {
                val getSimilarProductsUseCase by memoized<UseCase<SimilarProductModel>>()
                getSimilarProductsUseCase.stubExecute().returns(similarProductModel)
                similarSearchViewModel.onViewCreated()
            }

            When("Receive wishlist result") {
                similarSearchViewModel.onReceiveProductCardOptionsWishlistResult(productCardOptionsModel)
            }

            Then("Verify Add wishlist event should be true") {
                val addWishlistEvent = similarSearchViewModel.getAddWishlistEventLiveData().value
                addWishlistEvent?.getContentIfNotHandled().shouldBe(true, "Add wishlist event should be true")
            }

            Then("verify similar search view model list is updated with chosen similar product isWishlisted is true") {
                val similarSearchLiveData = similarSearchViewModel.getSimilarSearchLiveData().value
                val similarSearchViewModelList = similarSearchLiveData?.data ?: listOf()
                similarSearchViewModelList.shouldHaveSimilarProductWithExpectedWishlistStatus(productId, true)
            }

            Then("Verify tracking wishlist event has correct WishlistTrackingModel") {
                val trackingWishlistEventLiveData = similarSearchViewModel.getTrackingWishlistEventLiveData().value
                val expectedWishlistTrackingModel = WishlistTrackingModel(
                        isAddWishlist = !productCardOptionsModel.isWishlisted,
                        productId = productCardOptionsModel.productId,
                        isTopAds = productCardOptionsModel.isTopAds,
                        keyword = getSimilarSearchQuery(),
                        isUserLoggedIn = productCardOptionsModel.wishlistResult.isUserLoggedIn
                )

                trackingWishlistEventLiveData?.getContentIfNotHandled() shouldBe expectedWishlistTrackingModel
            }

            Then("Verify remove wishlist event should be null") {
                val removeWishlistEvent = similarSearchViewModel.getRemoveWishlistEventLiveData().value
                removeWishlistEvent?.getContentIfNotHandled().shouldBe(null, "Remove wishlist event should be null")
            }

            Then("Verify route to login page event should be null") {
                val routeToLoginEvent = similarSearchViewModel.getRouteToLoginPageEventLiveData().value
                routeToLoginEvent?.getContentIfNotHandled().shouldBe(null, "Route to login page event should be null")
            }
        }

        Scenario("Receive wishlist result add to wishlist failed") {
            val similarProductModel = getSimilarProductModelCommon()
            val productId = similarProductModel.getSimilarProductList()[0].id
            val productCardOptionsModel = ProductCardOptionsModel().also {
                it.productId = productId
                it.isWishlisted = false
                it.wishlistResult = ProductCardOptionsModel.WishlistResult(
                        isUserLoggedIn = true,
                        isSuccess = false,
                        isAddWishlist = true
                )
            }

            lateinit var similarSearchViewModel: SimilarSearchViewModel

            Given("Similar search view model") {
                similarSearchViewModel = createSimilarSearchViewModel()
            }

            Given("View is already created and has similar search data") {
                val getSimilarProductsUseCase by memoized<UseCase<SimilarProductModel>>()
                getSimilarProductsUseCase.stubExecute().returns(similarProductModel)
                similarSearchViewModel.onViewCreated()
            }

            When("Receive wishlist result") {
                similarSearchViewModel.onReceiveProductCardOptionsWishlistResult(productCardOptionsModel)
            }

            Then("Verify Add wishlist event should be false") {
                val addWishlistEvent = similarSearchViewModel.getAddWishlistEventLiveData().value
                addWishlistEvent?.getContentIfNotHandled().shouldBe(false, "Add wishlist event should be false")
            }

            Then("Verify tracking wishlist event is null") {
                val trackingWishlistEventLiveData = similarSearchViewModel.getTrackingWishlistEventLiveData().value
                trackingWishlistEventLiveData?.getContentIfNotHandled().shouldBe(null, "Tracking wishlist event should be null")
            }

            Then("Verify remove wishlist event should be null") {
                val removeWishlistEvent = similarSearchViewModel.getRemoveWishlistEventLiveData().value
                removeWishlistEvent?.getContentIfNotHandled().shouldBe(null, "Remove wishlist event should be null")
            }

            Then("Verify route to login page event should be null") {
                val routeToLoginEvent = similarSearchViewModel.getRouteToLoginPageEventLiveData().value
                routeToLoginEvent?.getContentIfNotHandled().shouldBe(null, "Route to login page event should be null")
            }
        }

        Scenario("Receive wishlist result remove wishlist success") {
            val similarProductModel = getSimilarProductModelCommon()
            val productId = similarProductModel.getSimilarProductList()[1].id
            val productCardOptionsModel = ProductCardOptionsModel().also {
                it.productId = productId
                it.isWishlisted = true
                it.wishlistResult = ProductCardOptionsModel.WishlistResult(
                        isUserLoggedIn = true,
                        isSuccess = true,
                        isAddWishlist = false
                )
            }

            lateinit var similarSearchViewModel: SimilarSearchViewModel

            Given("Similar search view model") {
                similarSearchViewModel = createSimilarSearchViewModel()
            }

            Given("View is already created and has similar search data") {
                val getSimilarProductsUseCase by memoized<UseCase<SimilarProductModel>>()
                getSimilarProductsUseCase.stubExecute().returns(similarProductModel)
                similarSearchViewModel.onViewCreated()
            }

            When("Receive wishlist result") {
                similarSearchViewModel.onReceiveProductCardOptionsWishlistResult(productCardOptionsModel)
            }

            Then("Verify remove wishlist event should be true") {
                val removeWishlistEvent = similarSearchViewModel.getRemoveWishlistEventLiveData().value
                removeWishlistEvent?.getContentIfNotHandled().shouldBe(true, "Remove wishlist event should be true")
            }

            Then("verify similar search view model list is updated with chosen similar product isWishlisted is false") {
                val similarSearchLiveData = similarSearchViewModel.getSimilarSearchLiveData().value
                val similarSearchViewModelList = similarSearchLiveData?.data ?: listOf()
                similarSearchViewModelList.shouldHaveSimilarProductWithExpectedWishlistStatus(productId, false)
            }

            Then("Verify tracking wishlist event has correct WishlistTrackingModel") {
                val trackingWishlistEventLiveData = similarSearchViewModel.getTrackingWishlistEventLiveData().value
                val expectedWishlistTrackingModel = WishlistTrackingModel(
                        isAddWishlist = !productCardOptionsModel.isWishlisted,
                        productId = productCardOptionsModel.productId,
                        isTopAds = productCardOptionsModel.isTopAds,
                        keyword = getSimilarSearchQuery(),
                        isUserLoggedIn = productCardOptionsModel.wishlistResult.isUserLoggedIn
                )

                trackingWishlistEventLiveData?.getContentIfNotHandled() shouldBe expectedWishlistTrackingModel
            }

            Then("Verify Add wishlist event should be null") {
                val addWishlistEvent = similarSearchViewModel.getAddWishlistEventLiveData().value
                addWishlistEvent?.getContentIfNotHandled().shouldBe(null, "Add wishlist event should be null")
            }

            Then("Verify route to login page event should be null") {
                val routeToLoginEvent = similarSearchViewModel.getRouteToLoginPageEventLiveData().value
                routeToLoginEvent?.getContentIfNotHandled().shouldBe(null, "Route to login page event should be null")
            }
        }

        Scenario("Receive wishlist result remove from wishlist failed") {
            val similarProductModel = getSimilarProductModelCommon()
            val productId = similarProductModel.getSimilarProductList()[1].id
            val productCardOptionsModel = ProductCardOptionsModel().also {
                it.productId = productId
                it.isWishlisted = true
                it.wishlistResult = ProductCardOptionsModel.WishlistResult(
                        isUserLoggedIn = true,
                        isSuccess = false,
                        isAddWishlist = false
                )
            }

            lateinit var similarSearchViewModel: SimilarSearchViewModel

            Given("Similar search view model") {
                similarSearchViewModel = createSimilarSearchViewModel()
            }

            Given("View is already created and has similar search data") {
                val getSimilarProductsUseCase by memoized<UseCase<SimilarProductModel>>()
                getSimilarProductsUseCase.stubExecute().returns(similarProductModel)
                similarSearchViewModel.onViewCreated()
            }

            When("Receive wishlist result") {
                similarSearchViewModel.onReceiveProductCardOptionsWishlistResult(productCardOptionsModel)
            }

            Then("Verify Remove wishlist event should be false") {
                val removeWishlistEvent = similarSearchViewModel.getRemoveWishlistEventLiveData().value
                removeWishlistEvent?.getContentIfNotHandled().shouldBe(false, "Remove wishlist event should be false")
            }

            Then("Verify tracking wishlist event is null") {
                val trackingWishlistEventLiveData = similarSearchViewModel.getTrackingWishlistEventLiveData().value
                trackingWishlistEventLiveData?.getContentIfNotHandled().shouldBe(null, "Tracking wishlist event should be null")
            }

            Then("Verify add wishlist event should be null") {
                val addWishlistEvent = similarSearchViewModel.getAddWishlistEventLiveData().value
                addWishlistEvent?.getContentIfNotHandled().shouldBe(null, "Add wishlist event should be null")
            }

            Then("Verify route to login page event should be null") {
                val routeToLoginEvent = similarSearchViewModel.getRouteToLoginPageEventLiveData().value
                routeToLoginEvent?.getContentIfNotHandled().shouldBe(null, "Route to login page event should be null")
            }
        }
    }
})