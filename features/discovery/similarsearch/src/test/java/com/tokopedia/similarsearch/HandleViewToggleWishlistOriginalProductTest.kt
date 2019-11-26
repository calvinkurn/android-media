package com.tokopedia.similarsearch

import com.tokopedia.similarsearch.testinstance.getSimilarProductModelCommon
import com.tokopedia.similarsearch.testinstance.getSimilarProductModelOriginalProductWishlisted
import com.tokopedia.similarsearch.getsimilarproducts.GetSimilarProductsUseCase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import io.mockk.every
import io.mockk.verify
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

internal class HandleViewToggleWishlistOriginalProductTest: Spek({

    InstantTaskExecutorRuleSpek(this)

    Feature("Handle View Toggle Wishlist for Original Product") {
        createTestInstance()

        Scenario("Handle View Toggle Wishlist Original Product for non-login user") {
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
                val getSimilarProductsUseCase by memoized<GetSimilarProductsUseCase>()
                getSimilarProductsUseCase.stubExecute().returns(similarProductModelCommon)
                similarSearchViewModel.onViewCreated()
            }

            When("handle view toggle wishlist Original product") {
                similarSearchViewModel.onViewToggleWishlistOriginalProduct()
            }

            Then("should post event go to login page") {
                val routeToLoginEvent = similarSearchViewModel.getRouteToLoginPageEventLiveData().value

                routeToLoginEvent?.getContentIfNotHandled().shouldBe(true,
                        "Route to login page should be true")
            }
        }

        Scenario("Handle View Toggle Wishlist Original Product for logged in user and product is not wishlisted") {
            val userId = "123456"
            val similarProductModelCommon = getSimilarProductModelCommon()
            val originalProduct = similarProductModelCommon.getOriginalProduct()
            val addWishListUseCase by memoized<AddWishListUseCase>()
            val userSession by memoized<UserSessionInterface>()

            lateinit var similarSearchViewModel: SimilarSearchViewModel

            Given("similar search view model") {
                similarSearchViewModel = createSimilarSearchViewModel()
            }

            Given("view already created and has similar search data with original product not wishlisted") {
                val getSimilarProductsUseCase by memoized<GetSimilarProductsUseCase>()
                getSimilarProductsUseCase.stubExecute().returns(similarProductModelCommon)
                similarSearchViewModel.onViewCreated()
            }

            Given("user is logged in") {
                every { userSession.isLoggedIn }.returns(true)
                every { userSession.userId }.returns(userId)
            }

            When("handle view toggle wishlist Original product") {
                similarSearchViewModel.onViewToggleWishlistOriginalProduct()
            }

            Then("verify add wishlist API is called with product id equals to Original product id") {
                verify(exactly = 1) { addWishListUseCase.createObservable(originalProduct.id, userId, any()) }
            }
        }

        Scenario("Handle View Toggle Wishlist Original Product for logged in user and product is wishlisted") {
            val userId = "123456"
            val similarProductModelOriginalProductWishlisted = getSimilarProductModelOriginalProductWishlisted()
            val originalProduct = similarProductModelOriginalProductWishlisted.getOriginalProduct()
            val removeWishListUseCase by memoized<RemoveWishListUseCase>()
            val userSession by memoized<UserSessionInterface>()

            lateinit var similarSearchViewModel: SimilarSearchViewModel

            Given("similar search view model with wishlisted product") {
                similarSearchViewModel = createSimilarSearchViewModel()
            }

            Given("user is logged in") {
                every { userSession.isLoggedIn }.returns(true)
                every { userSession.userId }.returns(userId)
            }

            Given("view already created and has similar search data with original product wishlisted") {
                val getSimilarProductsUseCase by memoized<GetSimilarProductsUseCase>()
                getSimilarProductsUseCase.stubExecute().returns(similarProductModelOriginalProductWishlisted)
                similarSearchViewModel.onViewCreated()
            }

            When("handle view toggle wishlist Original product") {
                similarSearchViewModel.onViewToggleWishlistOriginalProduct()
            }

            Then("verify remove wishlist API is called with product id equals to Original product id") {
                verify(exactly = 1) { removeWishListUseCase.createObservable(originalProduct.id, userId, any()) }
            }
        }
    }

    Feature("Add Wishlist Original Product") {
        createTestInstance()

        Scenario("Add Wishlist Original Product Success") {
            val userId = "123456"
            val similarProductModelCommon = getSimilarProductModelCommon()
            val originalProduct = similarProductModelCommon.getOriginalProduct()
            val addWishListUseCase by memoized<AddWishListUseCase>()
            val userSession by memoized<UserSessionInterface>()

            lateinit var similarSearchViewModel: SimilarSearchViewModel

            Given("similar search view model") {
                similarSearchViewModel = createSimilarSearchViewModel()
            }

            Given("user is logged in") {
                every { userSession.isLoggedIn }.returns(true)
                every { userSession.userId }.returns(userId)
            }

            Given("view already created and has similar search data with original product not wishlisted") {
                val getSimilarProductsUseCase by memoized<GetSimilarProductsUseCase>()
                getSimilarProductsUseCase.stubExecute().returns(similarProductModelCommon)
                similarSearchViewModel.onViewCreated()
            }

            Given("add wishlist API will be successful") {
                every {
                    addWishListUseCase.createObservable(originalProduct.id, userId, any())
                }.answers {
                    thirdArg<WishListActionListener>().onSuccessAddWishlist(firstArg())
                }
            }

            When("handle view toggle wishlist Original product") {
                similarSearchViewModel.onViewToggleWishlistOriginalProduct()
            }

            Then("assert add wishlist event is true") {
                val addWishlistEventLiveData = similarSearchViewModel.getAddWishlistEventLiveData().value

                addWishlistEventLiveData?.getContentIfNotHandled().shouldBe(
                        true,
                        "Add wishlist event should be true"
                )
            }

            Then("assert Original product is wishlisted is true, and update wishlist Original product event is true") {
                val similarSearchOriginalProduct = similarSearchViewModel.getOriginalProductLiveData().value

                similarSearchOriginalProduct?.isWishlisted.shouldBe(
                        true,
                        "Original Product is wishlisted should be true"
                )

                val updateWishlistOriginalProductEventLiveData = similarSearchViewModel.getUpdateWishlistOriginalProductEventLiveData().value

                updateWishlistOriginalProductEventLiveData?.getContentIfNotHandled().shouldBe(
                        true,
                        "Update wishlist Original product event should be true"
                )
            }
        }

        Scenario("Add Wishlist Original Product Failed") {
            val userId = "123456"
            val similarProductModelCommon = getSimilarProductModelCommon()
            val originalProduct = similarProductModelCommon.getOriginalProduct()
            val addWishListUseCase by memoized<AddWishListUseCase>()
            val userSession by memoized<UserSessionInterface>()

            lateinit var similarSearchViewModel: SimilarSearchViewModel

            Given("similar search view model") {
                similarSearchViewModel = createSimilarSearchViewModel()
            }

            Given("user is logged in") {
                every { userSession.isLoggedIn }.returns(true)
                every { userSession.userId }.returns(userId)
            }

            Given("view already created and has similar search data with original product not wishlisted") {
                val getSimilarProductsUseCase by memoized<GetSimilarProductsUseCase>()
                getSimilarProductsUseCase.stubExecute().returns(similarProductModelCommon)
                similarSearchViewModel.onViewCreated()
            }

            Given("add wishlist API will fail") {
                every {
                    addWishListUseCase.createObservable(originalProduct.id, userId, any())
                }.answers {
                    thirdArg<WishListActionListener>().onErrorAddWishList("error from backend", firstArg())
                }
            }

            When("handle view toggle wishlist Original product") {
                similarSearchViewModel.onViewToggleWishlistOriginalProduct()
            }

            Then("assert add wishlist event is false") {
                val addWishlistEventLiveData = similarSearchViewModel.getAddWishlistEventLiveData().value

                addWishlistEventLiveData?.getContentIfNotHandled().shouldBe(
                        false,
                        "Add wishlist event should be false"
                )
            }

            Then("assert Original product is wishlisted stays false, and update wishlist Original product event is null") {
                val similarSearchOriginalProduct = similarSearchViewModel.getOriginalProductLiveData().value

                similarSearchOriginalProduct?.isWishlisted.shouldBe(
                        false,
                        "Original Product is wishlisted should be false"
                )

                val updateWishlistOriginalProductEventLiveData = similarSearchViewModel.getUpdateWishlistOriginalProductEventLiveData().value

                updateWishlistOriginalProductEventLiveData?.getContentIfNotHandled().shouldBe(
                        null,
                        "Update wishlist Original product event should be null"
                )
            }
        }
    }

    Feature("Remove Wishlist Original Product") {
        createTestInstance()

        Scenario("Remove Wishlist Original Product Success") {
            val userId = "123456"
            val similarProductModelOriginalProductWishlisted = getSimilarProductModelOriginalProductWishlisted()
            val originalProduct = similarProductModelOriginalProductWishlisted.getOriginalProduct()
            val removeWishListUseCase by memoized<RemoveWishListUseCase>()
            val userSession by memoized<UserSessionInterface>()

            lateinit var similarSearchViewModel: SimilarSearchViewModel

            Given("similar search view model") {
                similarSearchViewModel = createSimilarSearchViewModel()
            }

            Given("user is logged in") {
                every { userSession.isLoggedIn }.returns(true)
                every { userSession.userId }.returns(userId)
            }

            Given("view already created and has similar search data with original product wishlisted") {
                val getSimilarProductsUseCase by memoized<GetSimilarProductsUseCase>()
                getSimilarProductsUseCase.stubExecute().returns(similarProductModelOriginalProductWishlisted)
                similarSearchViewModel.onViewCreated()
            }

            Given("remove wishlist API will be successful") {
                every {
                    removeWishListUseCase.createObservable(originalProduct.id, userId, any())
                }.answers {
                    thirdArg<WishListActionListener>().onSuccessRemoveWishlist(firstArg())
                }
            }

            When("handle view toggle wishlist Original product") {
                similarSearchViewModel.onViewToggleWishlistOriginalProduct()
            }

            Then("assert remove wishlist event is true") {
                val removeWishlistEventLiveData = similarSearchViewModel.getRemoveWishlistEventLiveData().value

                removeWishlistEventLiveData?.getContentIfNotHandled().shouldBe(
                        true,
                        "Remove wishlist event should be true"
                )
            }

            Then("assert Original product is wishlisted is false, and update wishlist Original product event is false") {
                val similarSearchOriginalProduct = similarSearchViewModel.getOriginalProductLiveData().value

                similarSearchOriginalProduct?.isWishlisted.shouldBe(
                        false,
                        "Original Product is wishlisted should be false"
                )

                val updateWishlistOriginalProductEventLiveData = similarSearchViewModel.getUpdateWishlistOriginalProductEventLiveData().value

                updateWishlistOriginalProductEventLiveData?.getContentIfNotHandled().shouldBe(
                        false,
                        "Update wishlist Original product event should be false"
                )
            }
        }

        Scenario("Remove Wishlist Original Product Failed") {
            val userId = "123456"
            val similarProductModelOriginalProductWishlisted = getSimilarProductModelOriginalProductWishlisted()
            val originalProduct = similarProductModelOriginalProductWishlisted.getOriginalProduct()
            val removeWishListUseCase by memoized<RemoveWishListUseCase>()
            val userSession by memoized<UserSessionInterface>()

            lateinit var similarSearchViewModel: SimilarSearchViewModel

            Given("similar search view model") {
                similarSearchViewModel = createSimilarSearchViewModel()
            }

            Given("user is logged in") {
                every { userSession.isLoggedIn }.returns(true)
                every { userSession.userId }.returns(userId)
            }

            Given("view already created and has similar search data with original product wishlisted") {
                val getSimilarProductsUseCase by memoized<GetSimilarProductsUseCase>()
                getSimilarProductsUseCase.stubExecute().returns(similarProductModelOriginalProductWishlisted)
                similarSearchViewModel.onViewCreated()
            }

            Given("remove wishlist API will fail") {
                every {
                    removeWishListUseCase.createObservable(originalProduct.id, userId, any())
                }.answers {
                    thirdArg<WishListActionListener>().onErrorRemoveWishlist("error from backend", firstArg())
                }
            }

            When("handle view toggle wishlist Original product") {
                similarSearchViewModel.onViewToggleWishlistOriginalProduct()
            }

            Then("assert remove wishlist Original product event is false") {
                val removeWishlistEventLiveData = similarSearchViewModel.getRemoveWishlistEventLiveData().value

                removeWishlistEventLiveData?.getContentIfNotHandled().shouldBe(
                        false,
                        "Remove wishlist event should be false"
                )
            }

            Then("assert Original product is wishlisted stays true, and update wishlist Original product event is null") {
                val similarSearchOriginalProduct = similarSearchViewModel.getOriginalProductLiveData().value

                similarSearchOriginalProduct?.isWishlisted.shouldBe(
                        true,
                        "Original Product is wishlisted should be true"
                )

                val updateWishlistOriginalProductEventLiveData = similarSearchViewModel.getUpdateWishlistOriginalProductEventLiveData().value

                updateWishlistOriginalProductEventLiveData?.getContentIfNotHandled().shouldBe(
                        null,
                        "Update wishlist selected product event should be null"
                )
            }
        }
    }
})