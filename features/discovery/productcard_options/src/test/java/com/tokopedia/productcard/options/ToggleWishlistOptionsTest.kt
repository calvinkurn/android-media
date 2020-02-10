package com.tokopedia.productcard.options

import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.discovery.common.model.WishlistTrackingModel
import com.tokopedia.productcard.options.testutils.InstantTaskExecutorRuleSpek
import com.tokopedia.productcard.options.testutils.TestException
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import io.mockk.every
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

internal class ToggleWishlistOptionsTest: Spek({

    InstantTaskExecutorRuleSpek(this)

    Feature("Click save to wishlist") {
        createTestInstance()

        Scenario("Save to Wishlist for non login user") {
            val userSession by memoized<UserSessionInterface>()

            lateinit var productCardOptionsViewModel: ProductCardOptionsViewModel

            Given("Product Card Options View Model") {
                val productCardOptionsModelNotWishlisted = ProductCardOptionsModel(hasWishlist = true, isWishlisted = false, productId = "12345")
                productCardOptionsViewModel = createProductCardOptionsViewModel(productCardOptionsModelNotWishlisted)
            }

            Given("User is not logged in") {
                every { userSession.isLoggedIn }.returns(false)
            }

            When("Click save to wishlist") {
                productCardOptionsViewModel.getOption(SAVE_TO_WISHLIST).onClick()
            }

            Then("assert tracking toggle wishlist event has correct WishlistTrackingModel") {
                val trackingWishlistEventLiveData = productCardOptionsViewModel.getTrackingWishlistEventLiveData().value
                val expectedWishlistTrackingModel = WishlistTrackingModel(
                        isAddWishlist = true,
                        productId = productCardOptionsViewModel.productCardOptionsModel?.productId!!,
                        isTopAds = productCardOptionsViewModel.productCardOptionsModel?.isTopAds!!,
                        keyword = productCardOptionsViewModel.productCardOptionsModel?.keyword!!,
                        isUserLoggedIn = false
                )

                trackingWishlistEventLiveData?.getContentIfNotHandled() shouldBe expectedWishlistTrackingModel
            }

            Then("should post event go to login page") {
                val routeToLoginEvent = productCardOptionsViewModel.getRouteToLoginPageEventLiveData().value

                routeToLoginEvent?.getContentIfNotHandled().shouldBe(true,
                        "Route to login page should be true")
            }

            Then("should post event close product card options") {
                val closeProductCardOptionsEvent = productCardOptionsViewModel.getCloseProductCardOptionsEventLiveData().value

                closeProductCardOptionsEvent?.getContentIfNotHandled() shouldBe true
            }

            Then("assert product card options model wishlist result is null") {
                val wishlistResult = productCardOptionsViewModel.productCardOptionsModel?.wishlistResult

                wishlistResult.shouldBe(null,
                        "Wishlist result should be null"
                )
            }
        }

        Scenario("Add Wishlist Product Success") {
            val userId = "123456"
            val addWishListUseCase by memoized<AddWishListUseCase>()
            val userSession by memoized<UserSessionInterface>()

            lateinit var productCardOptionsViewModel: ProductCardOptionsViewModel

            Given("Product Card Options View Model") {

                val productCardOptionsModelNotWishlisted = ProductCardOptionsModel(hasWishlist = true, isWishlisted = false, productId = "12345")
                productCardOptionsViewModel = createProductCardOptionsViewModel(productCardOptionsModelNotWishlisted)
            }

            Given("user is logged in") {
                every { userSession.isLoggedIn }.returns(true)
                every { userSession.userId }.returns(userId)
            }

            Given("add wishlist API will be successful") {
                val productId = productCardOptionsViewModel.productCardOptionsModel?.productId ?: "0"

                every {
                    addWishListUseCase.createObservable(productId, userId, any())
                }.answers {
                    thirdArg<WishListActionListener>().onSuccessAddWishlist(firstArg())
                }
            }

            When("Click save to wishlist") {
                productCardOptionsViewModel.getOption(SAVE_TO_WISHLIST).onClick()
            }

            Then("assert route to login event is null") {
                val routeToLoginEvent = productCardOptionsViewModel.getRouteToLoginPageEventLiveData().value

                routeToLoginEvent?.getContentIfNotHandled().shouldBe(null,
                        "Route to login page should be null")
            }

            Then("assert tracking toggle wishlist event has correct WishlistTrackingModel") {
                val trackingWishlistEventLiveData = productCardOptionsViewModel.getTrackingWishlistEventLiveData().value
                val expectedWishlistTrackingModel = WishlistTrackingModel(
                        isAddWishlist = true,
                        productId = productCardOptionsViewModel.productCardOptionsModel?.productId!!,
                        isTopAds = productCardOptionsViewModel.productCardOptionsModel?.isTopAds!!,
                        keyword = productCardOptionsViewModel.productCardOptionsModel?.keyword!!,
                        isUserLoggedIn = true
                )

                trackingWishlistEventLiveData?.getContentIfNotHandled() shouldBe expectedWishlistTrackingModel
            }

            Then("assert wishlist event is true") {
                val wishlistEventLiveData = productCardOptionsViewModel.getWishlistEventLiveData().value

                wishlistEventLiveData?.getContentIfNotHandled().shouldBe(
                        true,
                        "Wishlist event should be true"
                )
            }

            Then("assert product card options model has wishlist result with isAddWishlist = true and isSuccess = true") {
                val wishlistResult = productCardOptionsViewModel.productCardOptionsModel?.wishlistResult!!

                wishlistResult.isSuccess.shouldBe(
                        true,
                        "Wishlist result isSuccess should be true"
                )

                wishlistResult.isAddWishlist.shouldBe(
                        true,
                        "Wishlist result isAddWishlist should be true"
                )
            }
        }

        Scenario("Add Wishlist Product Failed and handled in onErrorAddWishlist") {
            val userId = "123456"
            val addWishListUseCase by memoized<AddWishListUseCase>()
            val userSession by memoized<UserSessionInterface>()

            lateinit var productCardOptionsViewModel: ProductCardOptionsViewModel

            Given("Product Card Options View Model") {
                val productCardOptionsModelNotWishlisted = ProductCardOptionsModel(hasWishlist = true, isWishlisted = false, productId = "12345")
                productCardOptionsViewModel = createProductCardOptionsViewModel(productCardOptionsModelNotWishlisted)
            }

            Given("user is logged in") {
                every { userSession.isLoggedIn }.returns(true)
                every { userSession.userId }.returns(userId)
            }

            Given("add wishlist API will fail") {
                val productId = productCardOptionsViewModel.productCardOptionsModel?.productId ?: "0"

                every {
                    addWishListUseCase.createObservable(productId, userId, any())
                }.answers {
                    thirdArg<WishListActionListener>().onErrorAddWishList("error from backend", firstArg())
                }
            }

            When("Click save to wishlist") {
                productCardOptionsViewModel.getOption(SAVE_TO_WISHLIST).onClick()
            }

            Then("assert route to login event is null") {
                val routeToLoginEvent = productCardOptionsViewModel.getRouteToLoginPageEventLiveData().value

                routeToLoginEvent?.getContentIfNotHandled().shouldBe(null,
                        "Route to login page should be null")
            }

            Then("assert tracking toggle wishlist event is null") {
                val trackingWishlistEventLiveData = productCardOptionsViewModel.getTrackingWishlistEventLiveData().value

                trackingWishlistEventLiveData?.getContentIfNotHandled() shouldBe null
            }

            Then("assert wishlist event is true") {
                val wishlistEventLiveData = productCardOptionsViewModel.getWishlistEventLiveData().value

                wishlistEventLiveData?.getContentIfNotHandled().shouldBe(
                        true,
                        "Wishlist event should be true"
                )
            }

            Then("assert product card options model has wishlist result with isAddWishlist = true and isSuccess = false") {
                val wishlistResult = productCardOptionsViewModel.productCardOptionsModel?.wishlistResult!!

                wishlistResult.isSuccess.shouldBe(
                        false,
                        "Wishlist result isSuccess should be false"
                )

                wishlistResult.isAddWishlist.shouldBe(
                        true,
                        "Wishlist result isAddWishlist should be true"
                )
            }
        }

        Scenario("Add Wishlist Product Failed and throw Exception") {
            val userId = "123456"
            val testException = TestException()
            val addWishListUseCase by memoized<AddWishListUseCase>()
            val userSession by memoized<UserSessionInterface>()

            lateinit var productCardOptionsViewModel: ProductCardOptionsViewModel

            Given("Product Card Options View Model") {
                val productCardOptionsModelNotWishlisted = ProductCardOptionsModel(hasWishlist = true, isWishlisted = false, productId = "12345")
                productCardOptionsViewModel = createProductCardOptionsViewModel(productCardOptionsModelNotWishlisted)
            }

            Given("user is logged in") {
                every { userSession.isLoggedIn }.returns(true)
                every { userSession.userId }.returns(userId)
            }

            Given("add wishlist API will fail with exception") {
                val productId = productCardOptionsViewModel.productCardOptionsModel?.productId ?: "0"

                every {
                    addWishListUseCase.createObservable(productId, userId, any())
                }.throws(testException)
            }

            When("Click save to wishlist") {
                productCardOptionsViewModel.getOption(SAVE_TO_WISHLIST).onClick()
            }

            Then("assert error stack trace is printed") {
                testException.isStackTracePrinted.shouldBe(true,
                        "Exception stack trace printed should be true")
            }

            Then("assert route to login event is null") {
                val routeToLoginEvent = productCardOptionsViewModel.getRouteToLoginPageEventLiveData().value

                routeToLoginEvent?.getContentIfNotHandled().shouldBe(null,
                        "Route to login page should be null")
            }

            Then("assert tracking toggle wishlist event is null") {
                val trackingWishlistEventLiveData = productCardOptionsViewModel.getTrackingWishlistEventLiveData().value

                trackingWishlistEventLiveData?.getContentIfNotHandled() shouldBe null
            }

            Then("assert wishlist event is true") {
                val wishlistEventLiveData = productCardOptionsViewModel.getWishlistEventLiveData().value

                wishlistEventLiveData?.getContentIfNotHandled().shouldBe(
                        true,
                        "Wishlist event should be true"
                )
            }

            Then("assert product card options model has wishlist result with isAddWishlist = true and isSuccess = false") {
                val wishlistResult = productCardOptionsViewModel.productCardOptionsModel?.wishlistResult!!

                wishlistResult.isSuccess.shouldBe(
                        false,
                        "Wishlist result isSuccess should be false"
                )

                wishlistResult.isAddWishlist.shouldBe(
                        true,
                        "Wishlist result isAddWishlist should be true"
                )
            }
        }
    }

    Feature("Click delete from wishlist") {
        createTestInstance()

        Scenario("Delete from Wishlist for non login user") {
            val userSession by memoized<UserSessionInterface>()

            lateinit var productCardOptionsViewModel: ProductCardOptionsViewModel

            Given("Product Card Options View Model") {
                val productCardOptionsModelWishlisted = ProductCardOptionsModel(hasWishlist = true, isWishlisted = true, productId = "12345")
                productCardOptionsViewModel = createProductCardOptionsViewModel(productCardOptionsModelWishlisted)
            }

            Given("User is not logged in") {
                every { userSession.isLoggedIn }.returns(false)
            }

            When("Click delete from wishlist") {
                productCardOptionsViewModel.getOption(DELETE_FROM_WISHLIST).onClick()
            }

            Then("assert tracking toggle wishlist event has correct WishlistTrackingModel") {
                val trackingWishlistEventLiveData = productCardOptionsViewModel.getTrackingWishlistEventLiveData().value
                val expectedWishlistTrackingModel = WishlistTrackingModel(
                        isAddWishlist = false,
                        productId = productCardOptionsViewModel.productCardOptionsModel?.productId!!,
                        isTopAds = productCardOptionsViewModel.productCardOptionsModel?.isTopAds!!,
                        keyword = productCardOptionsViewModel.productCardOptionsModel?.keyword!!,
                        isUserLoggedIn = false
                )

                trackingWishlistEventLiveData?.getContentIfNotHandled() shouldBe expectedWishlistTrackingModel
            }

            Then("should post event go to login page") {
                val routeToLoginEvent = productCardOptionsViewModel.getRouteToLoginPageEventLiveData().value

                routeToLoginEvent?.getContentIfNotHandled().shouldBe(true,
                        "Route to login page should be true")
            }

            Then("should post event close product card options") {
                val closeProductCardOptionsEvent = productCardOptionsViewModel.getCloseProductCardOptionsEventLiveData().value

                closeProductCardOptionsEvent?.getContentIfNotHandled() shouldBe true
            }

            Then("assert product card options model wishlist result is null") {
                val wishlistResult = productCardOptionsViewModel.productCardOptionsModel?.wishlistResult

                wishlistResult.shouldBe(null,
                        "Wishlist result should be null"
                )
            }
        }

        Scenario("Delete Wishlist Product Success") {
            val userId = "123456"
            val removeWishListUseCase by memoized<RemoveWishListUseCase>()
            val userSession by memoized<UserSessionInterface>()

            lateinit var productCardOptionsViewModel: ProductCardOptionsViewModel

            Given("Product Card Options View Model") {
                val productCardOptionsModelWishlisted = ProductCardOptionsModel(hasWishlist = true, isWishlisted = true, productId = "12345")
                productCardOptionsViewModel = createProductCardOptionsViewModel(productCardOptionsModelWishlisted)
            }

            Given("user is logged in") {
                every { userSession.isLoggedIn }.returns(true)
                every { userSession.userId }.returns(userId)
            }

            Given("remove wishlist API will be successful") {
                val productId = productCardOptionsViewModel.productCardOptionsModel?.productId ?: "0"

                every {
                    removeWishListUseCase.createObservable(productId, userId, any())
                }.answers {
                    thirdArg<WishListActionListener>().onSuccessRemoveWishlist(firstArg())
                }
            }

            When("Click delete from wishlist") {
                productCardOptionsViewModel.getOption(DELETE_FROM_WISHLIST).onClick()
            }

            Then("assert tracking toggle wishlist event has correct WishlistTrackingModel") {
                val trackingWishlistEventLiveData = productCardOptionsViewModel.getTrackingWishlistEventLiveData().value
                val expectedWishlistTrackingModel = WishlistTrackingModel(
                        isAddWishlist = false,
                        productId = productCardOptionsViewModel.productCardOptionsModel?.productId!!,
                        isTopAds = productCardOptionsViewModel.productCardOptionsModel?.isTopAds!!,
                        keyword = productCardOptionsViewModel.productCardOptionsModel?.keyword!!,
                        isUserLoggedIn = true
                )

                trackingWishlistEventLiveData?.getContentIfNotHandled() shouldBe expectedWishlistTrackingModel
            }

            Then("assert route to login event is null") {
                val routeToLoginEvent = productCardOptionsViewModel.getRouteToLoginPageEventLiveData().value

                routeToLoginEvent?.getContentIfNotHandled().shouldBe(null,
                        "Route to login page should be null")
            }

            Then("assert wishlist event is true") {
                val wishlistEventLiveData = productCardOptionsViewModel.getWishlistEventLiveData().value

                wishlistEventLiveData?.getContentIfNotHandled().shouldBe(
                        true,
                        "Wishlist event should be true"
                )
            }

            Then("assert product card options model has wishlist result with isAddWishlist = false and isSuccess = true") {
                val wishlistResult = productCardOptionsViewModel.productCardOptionsModel?.wishlistResult!!

                wishlistResult.isSuccess.shouldBe(
                        true,
                        "Wishlist result isSuccess should be true"
                )

                wishlistResult.isAddWishlist.shouldBe(
                        false,
                        "Wishlist result isAddWishlist should be false"
                )
            }
        }

        Scenario("Delete Wishlist Product Failed and handled in onErrorRemoveWishlist") {
            val userId = "123456"
            val removeWishListUseCase by memoized<RemoveWishListUseCase>()
            val userSession by memoized<UserSessionInterface>()

            lateinit var productCardOptionsViewModel: ProductCardOptionsViewModel

            Given("Product Card Options View Model") {
                val productCardOptionsModelWishlisted = ProductCardOptionsModel(hasWishlist = true, isWishlisted = true, productId = "12345")
                productCardOptionsViewModel = createProductCardOptionsViewModel(productCardOptionsModelWishlisted)
            }

            Given("user is logged in") {
                every { userSession.isLoggedIn }.returns(true)
                every { userSession.userId }.returns(userId)
            }

            Given("remove wishlist API will fail") {
                val productId = productCardOptionsViewModel.productCardOptionsModel?.productId ?: "0"

                every {
                    removeWishListUseCase.createObservable(productId, userId, any())
                }.answers {
                    thirdArg<WishListActionListener>().onErrorRemoveWishlist("error from backedn", firstArg())
                }
            }

            When("Click delete from wishlist") {
                productCardOptionsViewModel.getOption(DELETE_FROM_WISHLIST).onClick()
            }

            Then("assert tracking toggle wishlist event has correct WishlistTrackingModel") {
                val trackingWishlistEventLiveData = productCardOptionsViewModel.getTrackingWishlistEventLiveData().value

                trackingWishlistEventLiveData?.getContentIfNotHandled() shouldBe null
            }

            Then("assert route to login event is null") {
                val routeToLoginEvent = productCardOptionsViewModel.getRouteToLoginPageEventLiveData().value

                routeToLoginEvent?.getContentIfNotHandled().shouldBe(null,
                        "Route to login page should be null")
            }

            Then("assert wishlist event is true") {
                val wishlistEventLiveData = productCardOptionsViewModel.getWishlistEventLiveData().value

                wishlistEventLiveData?.getContentIfNotHandled().shouldBe(
                        true,
                        "Wishlist event should be true"
                )
            }

            Then("assert product card options model has wishlist result with isAddWishlist = false and isSuccess = false") {
                val wishlistResult = productCardOptionsViewModel.productCardOptionsModel?.wishlistResult!!

                wishlistResult.isSuccess.shouldBe(
                        false,
                        "Wishlist result isSuccess should be false"
                )

                wishlistResult.isAddWishlist.shouldBe(
                        false,
                        "Wishlist result isAddWishlist should be false"
                )
            }
        }

        Scenario("Delete Wishlist Product Failed") {
            val userId = "123456"
            val testException = TestException()
            val removeWishListUseCase by memoized<RemoveWishListUseCase>()
            val userSession by memoized<UserSessionInterface>()

            lateinit var productCardOptionsViewModel: ProductCardOptionsViewModel

            Given("Product Card Options View Model") {
                val productCardOptionsModelWishlisted = ProductCardOptionsModel(hasWishlist = true, isWishlisted = true, productId = "12345")
                productCardOptionsViewModel = createProductCardOptionsViewModel(productCardOptionsModelWishlisted)
            }

            Given("user is logged in") {
                every { userSession.isLoggedIn }.returns(true)
                every { userSession.userId }.returns(userId)
            }

            Given("remove wishlist API will fail") {
                val productId = productCardOptionsViewModel.productCardOptionsModel?.productId ?: "0"

                every {
                    removeWishListUseCase.createObservable(productId, userId, any())
                }.throws(testException)
            }

            When("Click delete from wishlist") {
                productCardOptionsViewModel.getOption(DELETE_FROM_WISHLIST).onClick()
            }

            Then("assert tracking toggle wishlist event has correct WishlistTrackingModel") {
                val trackingWishlistEventLiveData = productCardOptionsViewModel.getTrackingWishlistEventLiveData().value

                trackingWishlistEventLiveData?.getContentIfNotHandled() shouldBe null
            }

            Then("assert error stack trace is printed") {
                testException.isStackTracePrinted.shouldBe(true,
                        "Exception stack trace printed should be true")
            }

            Then("assert route to login event is null") {
                val routeToLoginEvent = productCardOptionsViewModel.getRouteToLoginPageEventLiveData().value

                routeToLoginEvent?.getContentIfNotHandled().shouldBe(null,
                        "Route to login page should be null")
            }

            Then("assert wishlist event is true") {
                val wishlistEventLiveData = productCardOptionsViewModel.getWishlistEventLiveData().value

                wishlistEventLiveData?.getContentIfNotHandled().shouldBe(
                        true,
                        "Wishlist event should be true"
                )
            }

            Then("assert product card options model has wishlist result with isAddWishlist = false and isSuccess = false") {
                val wishlistResult = productCardOptionsViewModel.productCardOptionsModel?.wishlistResult!!

                wishlistResult.isSuccess.shouldBe(
                        false,
                        "Wishlist result isSuccess should be false"
                )

                wishlistResult.isAddWishlist.shouldBe(
                        false,
                        "Wishlist result isAddWishlist should be false"
                )
            }
        }
    }
})