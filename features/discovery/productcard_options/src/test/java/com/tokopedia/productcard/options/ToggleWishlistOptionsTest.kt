package com.tokopedia.productcard.options

import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.discovery.common.model.WishlistTrackingModel
import com.tokopedia.productcard.options.testutils.InstantTaskExecutorRuleSpek
import com.tokopedia.productcard.options.testutils.TestException
import com.tokopedia.productcard.options.testutils.complete
import com.tokopedia.productcard.options.testutils.error
import com.tokopedia.topads.sdk.domain.interactor.TopAdsWishlishedUseCase.WISHSLIST_URL
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import io.mockk.every
import io.mockk.slot
import io.mockk.verify
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Subscriber

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

            Then("should post wishlist event") {
                val wishlistEventLiveData = productCardOptionsViewModel.getWishlistEventLiveData().value

                wishlistEventLiveData?.getContentIfNotHandled().shouldBe(
                        true,
                        "Wishlist event should be true"
                )
            }

            Then("should post event close product card options") {
                val closeProductCardOptionsEvent = productCardOptionsViewModel.getCloseProductCardOptionsEventLiveData().value

                closeProductCardOptionsEvent?.getContentIfNotHandled() shouldBe true
            }

            Then("assert product card options model wishlist result isUserLoggedIn is false") {
                val wishlistResult = productCardOptionsViewModel.productCardOptionsModel?.wishlistResult!!

                wishlistResult.isUserLoggedIn shouldBe false
            }
        }

        Scenario("Add Wishlist Non-TopAds Product Success") {
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

            Then("should post wishlist event") {
                val wishlistEventLiveData = productCardOptionsViewModel.getWishlistEventLiveData().value

                wishlistEventLiveData?.getContentIfNotHandled().shouldBe(
                        true,
                        "Wishlist event should be true"
                )
            }

            Then("assert product card options model has wishlist result with isUserLoggedIn = true, isAddWishlist = true, and isSuccess = true") {
                val wishlistResult = productCardOptionsViewModel.productCardOptionsModel?.wishlistResult!!

                wishlistResult.isUserLoggedIn.shouldBe(
                        true,
                        "Wishlist result isUserLoggedIn should be true"
                )

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

        Scenario("Add Wishlist Non-TopAds Product Failed and handled in onErrorAddWishlist") {
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

            Then("should post wishlist event") {
                val wishlistEventLiveData = productCardOptionsViewModel.getWishlistEventLiveData().value

                wishlistEventLiveData?.getContentIfNotHandled().shouldBe(
                        true,
                        "Wishlist event should be true"
                )
            }

            Then("assert product card options model has wishlist result with isUserLoggin = true, isAddWishlist = true, and isSuccess = false") {
                val wishlistResult = productCardOptionsViewModel.productCardOptionsModel?.wishlistResult!!

                wishlistResult.isUserLoggedIn.shouldBe(
                        true,
                        "Wishlist result isUserLoggedIn should be true"
                )
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

        Scenario("Add Wishlist Non-TopAds Product Failed and throw Exception") {
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

            Then("should post wishlist event") {
                val wishlistEventLiveData = productCardOptionsViewModel.getWishlistEventLiveData().value

                wishlistEventLiveData?.getContentIfNotHandled().shouldBe(
                        true,
                        "Wishlist event should be true"
                )
            }

            Then("assert product card options model has wishlist result with isUserLoggedIn = true, isAddWishlist = true and isSuccess = false") {
                val wishlistResult = productCardOptionsViewModel.productCardOptionsModel?.wishlistResult!!

                wishlistResult.isUserLoggedIn.shouldBe(
                        true,
                        "Wishlist result isUserLoggedIn should be true"
                )

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

        Scenario("Add Wishlist TopAds Product Success") {
            val userId = "123456"
            val topAdsWishlistUrl = "https://dummy_topads_url_for_wishlist"
            val topAdsWishlistUseCase by memoized<UseCase<Boolean>>()
            val userSession by memoized<UserSessionInterface>()
            val topAdsWishlistRequestParams = slot<RequestParams>()

            lateinit var productCardOptionsViewModel: ProductCardOptionsViewModel

            Given("Product Card Options View Model") {
                val productCardOptionsModelNotWishlisted = ProductCardOptionsModel(
                        hasWishlist = true,
                        isWishlisted = false,
                        productId = "12345",
                        isTopAds = true,
                        topAdsWishlistUrl = topAdsWishlistUrl
                )
                productCardOptionsViewModel = createProductCardOptionsViewModel(productCardOptionsModelNotWishlisted)
            }

            Given("user is logged in") {
                every { userSession.isLoggedIn }.returns(true)
                every { userSession.userId }.returns(userId)
            }

            Given("add TopAds wishlist API will be successful") {
                every {
                    topAdsWishlistUseCase.execute(capture(topAdsWishlistRequestParams), any())
                } answers {
                    secondArg<Subscriber<Boolean>>().complete(true)
                }
            }

            When("Click save to wishlist") {
                productCardOptionsViewModel.getOption(SAVE_TO_WISHLIST).onClick()
            }

            Then("verify TopAds wishlist API is called with correct parameters") {
                val requestParams = topAdsWishlistRequestParams.captured
                val wishlistUrl = requestParams.getString(WISHSLIST_URL, "")

                wishlistUrl shouldBe topAdsWishlistUrl
            }

            Then("should post wishlist event") {
                val wishlistEventLiveData = productCardOptionsViewModel.getWishlistEventLiveData().value

                wishlistEventLiveData?.getContentIfNotHandled().shouldBe(
                        true,
                        "Wishlist event should be true"
                )
            }

            Then("assert product card options model has wishlist result with isUserLoggedIn = true, isAddWishlist = true, and isSuccess = true") {
                val wishlistResult = productCardOptionsViewModel.productCardOptionsModel?.wishlistResult!!

                wishlistResult.isUserLoggedIn.shouldBe(
                        true,
                        "Wishlist result isUserLoggedIn should be true"
                )
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

        Scenario("Add Wishlist Non-TopAds Product Failed and throw Exception") {
            val userId = "123456"
            val topAdsWishlistUrl = "https://dummy_topads_url_for_wishlist"
            val topAdsWishlistUseCase by memoized<UseCase<Boolean>>()
            val testException = TestException()
            val topAdsWishlistRequestParams = slot<RequestParams>()
            val userSession by memoized<UserSessionInterface>()

            lateinit var productCardOptionsViewModel: ProductCardOptionsViewModel

            Given("Product Card Options View Model") {
                val productCardOptionsModelNotWishlisted = ProductCardOptionsModel(
                        hasWishlist = true,
                        isWishlisted = false,
                        productId = "12345",
                        isTopAds = true,
                        topAdsWishlistUrl = topAdsWishlistUrl
                )
                productCardOptionsViewModel = createProductCardOptionsViewModel(productCardOptionsModelNotWishlisted)
            }

            Given("user is logged in") {
                every { userSession.isLoggedIn }.returns(true)
                every { userSession.userId }.returns(userId)
            }

            Given("add TopAds wishlist API will fail") {
                every {
                    topAdsWishlistUseCase.execute(capture(topAdsWishlistRequestParams), any())
                } answers {
                    secondArg<Subscriber<Boolean>>().error(testException)
                }
            }

            When("Click save to wishlist") {
                productCardOptionsViewModel.getOption(SAVE_TO_WISHLIST).onClick()
            }

            Then("assert error stack trace is printed") {
                testException.isStackTracePrinted.shouldBe(true,
                        "Exception stack trace printed should be true")
            }

            Then("should post wishlist event") {
                val wishlistEventLiveData = productCardOptionsViewModel.getWishlistEventLiveData().value

                wishlistEventLiveData?.getContentIfNotHandled().shouldBe(
                        true,
                        "Wishlist event should be true"
                )
            }

            Then("assert product card options model has wishlist result with isUserLoggedIn = true, isAddWishlist = true and isSuccess = false") {
                val wishlistResult = productCardOptionsViewModel.productCardOptionsModel?.wishlistResult!!

                wishlistResult.isUserLoggedIn.shouldBe(
                        true,
                        "Wishlist result isUserLoggedIn should be true"
                )

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

            Then("should post event close product card options") {
                val closeProductCardOptionsEvent = productCardOptionsViewModel.getCloseProductCardOptionsEventLiveData().value

                closeProductCardOptionsEvent?.getContentIfNotHandled() shouldBe true
            }

            Then("assert product card options model wishlist result isUserLoggedIn is false") {
                val wishlistResult = productCardOptionsViewModel.productCardOptionsModel?.wishlistResult!!

                wishlistResult.isUserLoggedIn shouldBe false
            }
        }

        Scenario("Delete Product from Wishlist Success") {
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

            Then("should post wishlist event") {
                val wishlistEventLiveData = productCardOptionsViewModel.getWishlistEventLiveData().value

                wishlistEventLiveData?.getContentIfNotHandled().shouldBe(
                        true,
                        "Wishlist event should be true"
                )
            }

            Then("assert product card options model has wishlist result with isAddWishlist = false and isSuccess = true") {
                val wishlistResult = productCardOptionsViewModel.productCardOptionsModel?.wishlistResult!!

                wishlistResult.isUserLoggedIn.shouldBe(
                        true,
                        "Wishlist result isUserLoggedIn should be true"
                )

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

            Then("should post wishlist event") {
                val wishlistEventLiveData = productCardOptionsViewModel.getWishlistEventLiveData().value

                wishlistEventLiveData?.getContentIfNotHandled().shouldBe(
                        true,
                        "Wishlist event should be true"
                )
            }

            Then("assert product card options model has wishlist result with isAddWishlist = false and isSuccess = false") {
                val wishlistResult = productCardOptionsViewModel.productCardOptionsModel?.wishlistResult!!

                wishlistResult.isUserLoggedIn.shouldBe(
                        true,
                        "Wishlist result isUserLoggedIn should be true"
                )

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

            Then("assert error stack trace is printed") {
                testException.isStackTracePrinted.shouldBe(true,
                        "Exception stack trace printed should be true")
            }

            Then("should post wishlist event") {
                val wishlistEventLiveData = productCardOptionsViewModel.getWishlistEventLiveData().value

                wishlistEventLiveData?.getContentIfNotHandled().shouldBe(
                        true,
                        "Wishlist event should be true"
                )
            }

            Then("assert product card options model has wishlist result with isAddWishlist = false and isSuccess = false") {
                val wishlistResult = productCardOptionsViewModel.productCardOptionsModel?.wishlistResult!!

                wishlistResult.isUserLoggedIn.shouldBe(
                        true,
                        "Wishlist result isUserLoggedIn should be true"
                )

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