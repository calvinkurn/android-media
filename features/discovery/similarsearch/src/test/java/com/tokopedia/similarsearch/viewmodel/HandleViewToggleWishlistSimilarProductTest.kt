package com.tokopedia.similarsearch.viewmodel

import com.tokopedia.discovery.common.model.WishlistTrackingModel
import com.tokopedia.similarsearch.*
import com.tokopedia.similarsearch.testinstance.getSimilarProductModelCommon
import com.tokopedia.similarsearch.testinstance.getSimilarSearchQuery
import com.tokopedia.similarsearch.getsimilarproducts.GetSimilarProductsUseCase
import com.tokopedia.similarsearch.testutils.InstantTaskExecutorRuleSpek
import com.tokopedia.similarsearch.testutils.shouldBe
import com.tokopedia.similarsearch.testutils.stubExecute
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import io.mockk.every
import io.mockk.verify
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

internal class HandleViewToggleWishlistSimilarProductTest: Spek({
    InstantTaskExecutorRuleSpek(this)

    Feature("Handle View Toggle Wishlist for Similar Product") {
        createTestInstance()

        Scenario("Handle View Toggle Wishlist Similar Product for non-login user") {
            val similarProductModel = getSimilarProductModelCommon()
            val productToWishlist = similarProductModel.getSimilarProductList()[2]
            val isUserLoggedIn = false
            val userSession by memoized<UserSessionInterface>()

            lateinit var similarSearchViewModel: SimilarSearchViewModel

            Given("similar search view model") {
                similarSearchViewModel = createSimilarSearchViewModel()
            }

            Given("user is not logged in") {
                every { userSession.isLoggedIn }.returns(isUserLoggedIn)
            }

            When("handle view toggle wishlist similar product") {
                similarSearchViewModel.onViewToggleWishlistSimilarProduct(productToWishlist.id, productToWishlist.isWishlisted)
            }

            Then("assert tracking toggle wishlist event has correct WishlistTrackingModel") {
                val trackingWishlistEventLiveData = similarSearchViewModel.getTrackingWishlistEventLiveData().value
                val expectedWishlistTrackingModel = WishlistTrackingModel(
                        isAddWishlist = true,
                        productId = productToWishlist.id,
                        isTopAds = false,
                        keyword = getSimilarSearchQuery(),
                        isUserLoggedIn = isUserLoggedIn
                )

                trackingWishlistEventLiveData?.getContentIfNotHandled() shouldBe expectedWishlistTrackingModel
            }

            Then("should post event go to login page") {
                val routeToLoginEvent = similarSearchViewModel.getRouteToLoginPageEventLiveData().value

                routeToLoginEvent?.getContentIfNotHandled().shouldBe(true,
                        "Route to login page should be true")
            }
        }

        Scenario("Handle View Toggle Wishlist Similar Product for logged in user and product is not wishlisted") {
            val userId = "123456"
            val similarProductModel = getSimilarProductModelCommon()
            val productToWishlist = similarProductModel.getSimilarProductList()[2]
            val getSimilarProductsUseCase by memoized<GetSimilarProductsUseCase>()
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

            Given("view already created and has similar search data") {
                getSimilarProductsUseCase.stubExecute().returns(similarProductModel)
                similarSearchViewModel.onViewCreated()
            }

            When("handle view toggle wishlist similar product") {
                similarSearchViewModel.onViewToggleWishlistSimilarProduct(productToWishlist.id, productToWishlist.isWishlisted)
            }

            Then("verify add wishlist API is called with product id equals to similar product id") {
                verify(exactly = 1) { addWishListUseCase.createObservable(productToWishlist.id, userId, any()) }
            }
        }

        Scenario("Handle View Toggle Wishlist Similar Product for logged in user and product is wishlisted") {
            val userId = "123456"
            val similarProductModel = getSimilarProductModelCommon()
            val productToUnWishlist = similarProductModel.getSimilarProductList()[1]
            val getSimilarProductsUseCase by memoized<GetSimilarProductsUseCase>()
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

            Given("view already created and has similar search data") {
                getSimilarProductsUseCase.stubExecute().returns(similarProductModel)
                similarSearchViewModel.onViewCreated()
            }

            When("handle view toggle wishlist similar product") {
                similarSearchViewModel.onViewToggleWishlistSimilarProduct(productToUnWishlist.id, productToUnWishlist.isWishlisted)
            }

            Then("verify remove wishlist API is called with product id equals to similar product id") {
                verify(exactly = 1) { removeWishListUseCase.createObservable(productToUnWishlist.id, userId, any()) }
            }
        }

        Scenario("Handle View Toggle Wishlist with product not in similar search data") {
            val userId = "123456"
            val randomProductIdToWishlist = "123456"
            val similarProductModel = getSimilarProductModelCommon()
            val getSimilarProductsUseCase by memoized<GetSimilarProductsUseCase>()
            val addWishListUseCase by memoized<AddWishListUseCase>()
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

            Given("view already created and has similar search data") {
                getSimilarProductsUseCase.stubExecute().returns(similarProductModel)
                similarSearchViewModel.onViewCreated()
            }

            When("handle view toggle wishlist similar product") {
                similarSearchViewModel.onViewToggleWishlistSimilarProduct(randomProductIdToWishlist, false)
            }

            Then("verify not call any API") {
                verify(exactly = 0) { addWishListUseCase.createObservable(randomProductIdToWishlist, userId, any()) }
                verify(exactly = 0) { removeWishListUseCase.createObservable(randomProductIdToWishlist, userId, any()) }
            }
        }
    }

    Feature("Add Wishlist Selected Product") {
        createTestInstance()

        Scenario("Add Wishlist Similar Product Success") {
            val userId = "123456"
            val similarProductModel = getSimilarProductModelCommon()
            val productToWishlist = similarProductModel.getSimilarProductList()[2]
            val getSimilarProductsUseCase by memoized<GetSimilarProductsUseCase>()
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

            Given("view already created and has similar search data") {
                getSimilarProductsUseCase.stubExecute().returns(similarProductModel)
                similarSearchViewModel.onViewCreated()
            }

            Given("add wishlist API will be successful") {
                every {
                    addWishListUseCase.createObservable(productToWishlist.id, userId, any())
                }.answers {
                    thirdArg<WishListActionListener>().onSuccessAddWishlist(firstArg())
                }
            }

            When("handle view toggle wishlist similar product") {
                similarSearchViewModel.onViewToggleWishlistSimilarProduct(productToWishlist.id, productToWishlist.isWishlisted)
            }

            Then("assert similar search view model list is updated with chosen similar product isWishlisted is true") {
                val similarSearchLiveData = similarSearchViewModel.getSimilarSearchLiveData().value
                val similarSearchViewModelList = similarSearchLiveData?.data ?: listOf()

                similarSearchViewModelList.shouldHaveSimilarProductWithExpectedWishlistStatus(productToWishlist.id, true)
            }

            Then("assert update wishlist similar product event has Product with isWishlisted = true") {
                val updateWishlistSimilarProductEventLiveData = similarSearchViewModel.getUpdateWishlistSimilarProductEventLiveData().value

                updateWishlistSimilarProductEventLiveData?.getContentIfNotHandled()?.isWishlisted.shouldBe(
                        true,
                        "Update wishlist similar product event should have isWishlisted = true"
                )
            }

            Then("assert add wishlist event is true") {
                val addWishlistEventLiveData = similarSearchViewModel.getAddWishlistEventLiveData().value

                addWishlistEventLiveData?.getContentIfNotHandled().shouldBe(
                        true,
                        "Add wishlist event should be true"
                )
            }

            Then("assert tracking toggle wishlist event has correct WishlistTrackingModel") {
                val trackingWishlistEventLiveData = similarSearchViewModel.getTrackingWishlistEventLiveData().value
                val expectedWishlistTrackingModel = WishlistTrackingModel(
                        isAddWishlist = true,
                        productId = productToWishlist.id,
                        isTopAds = false,
                        keyword = getSimilarSearchQuery(),
                        isUserLoggedIn = true
                )

                trackingWishlistEventLiveData?.getContentIfNotHandled() shouldBe expectedWishlistTrackingModel
            }
        }

        Scenario("Add Wishlist Similar Product Failed") {
            val userId = "123456"
            val similarProductModel = getSimilarProductModelCommon()
            val productToWishlist = similarProductModel.getSimilarProductList()[0]
            val getSimilarProductsUseCase by memoized<GetSimilarProductsUseCase>()
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

            Given("view already created and has similar search data") {
                getSimilarProductsUseCase.stubExecute().returns(similarProductModel)
                similarSearchViewModel.onViewCreated()
            }

            Given("add wishlist API will fail") {
                every {
                    addWishListUseCase.createObservable(productToWishlist.id, userId, any())
                }.answers {
                    thirdArg<WishListActionListener>().onErrorAddWishList("error from backend", firstArg())
                }
            }

            When("handle view toggle wishlist similar product") {
                similarSearchViewModel.onViewToggleWishlistSimilarProduct(productToWishlist.id, productToWishlist.isWishlisted)
            }

            Then("assert similar search view model list is updated with chosen similar product isWishlisted is false") {
                val similarSearchLiveData = similarSearchViewModel.getSimilarSearchLiveData().value
                val similarSearchViewModelList = similarSearchLiveData?.data ?: listOf()

                similarSearchViewModelList.shouldHaveSimilarProductWithExpectedWishlistStatus(productToWishlist.id, false)
            }

            Then("assert update wishlist similar product event is null") {
                val updateWishlistSimilarProductEventLiveData = similarSearchViewModel.getUpdateWishlistSimilarProductEventLiveData().value

                updateWishlistSimilarProductEventLiveData?.getContentIfNotHandled()?.isWishlisted.shouldBe(
                        null,
                        "Update wishlist similar product event is null"
                )
            }

            Then("assert add wishlist event is false") {
                val addWishlistEventLiveData = similarSearchViewModel.getAddWishlistEventLiveData().value

                addWishlistEventLiveData?.getContentIfNotHandled().shouldBe(
                        false,
                        "Add wishlist event should be false"
                )
            }

            Then("assert tracking toggle wishlist event is null") {
                val trackingWishlistEventLiveData = similarSearchViewModel.getTrackingWishlistEventLiveData().value

                trackingWishlistEventLiveData?.getContentIfNotHandled() shouldBe null
            }
        }
    }

    Feature("Remove Wishlist Similar Product") {
        createTestInstance()

        Scenario("Remove Wishlist Similar Product Success") {
            val userId = "123456"
            val similarProductModel = getSimilarProductModelCommon()
            val productToUnWishlist = similarProductModel.getSimilarProductList()[1]
            val getSimilarProductsUseCase by memoized<GetSimilarProductsUseCase>()
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

            Given("view already created and has similar search data") {
                getSimilarProductsUseCase.stubExecute().returns(similarProductModel)
                similarSearchViewModel.onViewCreated()
            }

            Given("remove wishlist API will be successful") {
                every {
                    removeWishListUseCase.createObservable(productToUnWishlist.id, userId, any())
                }.answers {
                    thirdArg<WishListActionListener>().onSuccessRemoveWishlist(firstArg())
                }
            }

            When("handle view toggle wishlist similar product") {
                similarSearchViewModel.onViewToggleWishlistSimilarProduct(productToUnWishlist.id, productToUnWishlist.isWishlisted)
            }

            Then("assert similar search view model list is updated with chosen similar product isWishlisted is false") {
                val similarSearchLiveData = similarSearchViewModel.getSimilarSearchLiveData().value
                val similarSearchViewModelList = similarSearchLiveData?.data ?: listOf()

                similarSearchViewModelList.shouldHaveSimilarProductWithExpectedWishlistStatus(productToUnWishlist.id, false)
            }

            Then("assert update wishlist similar product event has Product with isWishlisted = false") {
                val updateWishlistSimilarProductEventLiveData = similarSearchViewModel.getUpdateWishlistSimilarProductEventLiveData().value

                updateWishlistSimilarProductEventLiveData?.getContentIfNotHandled()?.isWishlisted.shouldBe(
                        false,
                        "Update wishlist similar product event should have isWishlisted = false"
                )
            }

            Then("assert remove wishlist event is true") {
                val removeWishlistEventLiveData = similarSearchViewModel.getRemoveWishlistEventLiveData().value

                removeWishlistEventLiveData?.getContentIfNotHandled().shouldBe(
                        true,
                        "Remove wishlist event should be true"
                )
            }

            Then("assert tracking toggle wishlist event has correct WishlistTrackingModel") {
                val trackingWishlistEventLiveData = similarSearchViewModel.getTrackingWishlistEventLiveData().value
                val expectedWishlistTrackingModel = WishlistTrackingModel(
                        isAddWishlist = false,
                        productId = productToUnWishlist.id,
                        isTopAds = false,
                        keyword = getSimilarSearchQuery(),
                        isUserLoggedIn = true
                )

                trackingWishlistEventLiveData?.getContentIfNotHandled() shouldBe expectedWishlistTrackingModel
            }
        }

        Scenario("Remove Wishlist Similar Product Failed") {
            val userId = "123456"
            val similarProductModel = getSimilarProductModelCommon()
            val productToUnWishlist = similarProductModel.getSimilarProductList()[1]
            val getSimilarProductsUseCase by memoized<GetSimilarProductsUseCase>()
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

            Given("view already created and has similar search data") {
                getSimilarProductsUseCase.stubExecute().returns(similarProductModel)
                similarSearchViewModel.onViewCreated()
            }

            Given("remove wishlist API will fail") {
                every {
                    removeWishListUseCase.createObservable(productToUnWishlist.id, userId, any())
                }.answers {
                    thirdArg<WishListActionListener>().onErrorRemoveWishlist("error from backend", firstArg())
                }
            }

            When("handle view toggle wishlist similar product") {
                similarSearchViewModel.onViewToggleWishlistSimilarProduct(productToUnWishlist.id, productToUnWishlist.isWishlisted)
            }

            Then("assert similar search view model list is updated with chosen similar product isWishlisted is true") {
                val similarSearchLiveData = similarSearchViewModel.getSimilarSearchLiveData().value
                val similarSearchViewModelList = similarSearchLiveData?.data ?: listOf()

                similarSearchViewModelList.shouldHaveSimilarProductWithExpectedWishlistStatus(productToUnWishlist.id, true)
            }

            Then("assert update wishlist similar product event is null") {
                val updateWishlistSimilarProductEventLiveData = similarSearchViewModel.getUpdateWishlistSimilarProductEventLiveData().value

                updateWishlistSimilarProductEventLiveData?.getContentIfNotHandled()?.isWishlisted.shouldBe(
                        null,
                        "Update wishlist similar product event is null"
                )
            }

            Then("assert remove wishlist event is false") {
                val removeWishlistEventLiveData = similarSearchViewModel.getRemoveWishlistEventLiveData().value

                removeWishlistEventLiveData?.getContentIfNotHandled().shouldBe(
                        false,
                        "Remove wishlist event should be false"
                )
            }

            Then("assert tracking toggle wishlist event is null") {
                val trackingWishlistEventLiveData = similarSearchViewModel.getTrackingWishlistEventLiveData().value

                trackingWishlistEventLiveData?.getContentIfNotHandled() shouldBe null
            }
        }
    }
})