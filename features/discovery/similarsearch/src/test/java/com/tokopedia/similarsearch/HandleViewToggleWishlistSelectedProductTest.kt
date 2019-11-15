package com.tokopedia.similarsearch

import com.tokopedia.similarsearch.testinstance.getSimilarSearchSelectedProductNotWishlisted
import com.tokopedia.similarsearch.testinstance.getSimilarSearchSelectedProductWishlisted
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import io.mockk.every
import io.mockk.verify
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

internal class HandleViewToggleWishlistSelectedProductTest: Spek({

    InstantTaskExecutorRuleSpek(this)

    Feature("Handle View Toggle Wishlist for Selected Product") {
        createTestInstance()

        Scenario("Handle View Toggle Wishlist Selected Product for non-login user") {
            val userSession by memoized<UserSessionInterface>()

            lateinit var similarSearchViewModel: SimilarSearchViewModel

            Given("similar search view model") {
                similarSearchViewModel = createSimilarSearchViewModel()
            }

            Given("user is not logged in") {
                every { userSession.isLoggedIn }.returns(false)
            }

            When("handle view toggle wishlist selected product") {
                similarSearchViewModel.onViewToggleWishlistSelectedProduct()
            }

            Then("should post event go to login page") {
                val routeToLoginEvent = similarSearchViewModel.getRouteToLoginPageEventLiveData().value

                routeToLoginEvent?.getContentIfNotHandled().shouldBe(true,
                        "Route to login page should be true")
            }
        }

        Scenario("Handle View Toggle Wishlist Selected Product for logged in user and product is not wishlisted") {
            val userId = "123456"
            val similarSearchSelectedProductNotWishlisted = getSimilarSearchSelectedProductNotWishlisted()
            val addWishListUseCase by memoized<AddWishListUseCase>()
            val userSession by memoized<UserSessionInterface>()

            lateinit var similarSearchViewModel: SimilarSearchViewModel

            Given("similar search view model with not-wishlisted product") {
                similarSearchViewModel = createSimilarSearchViewModel(similarSearchSelectedProductNotWishlisted)
            }

            Given("user is logged in") {
                every { userSession.isLoggedIn }.returns(true)
                every { userSession.userId }.returns(userId)
            }

            When("handle view toggle wishlist selected product") {
                similarSearchViewModel.onViewToggleWishlistSelectedProduct()
            }

            Then("verify add wishlist API is called with product id equals to selected product id") {
                verify(exactly = 1) { addWishListUseCase.createObservable(similarSearchSelectedProductNotWishlisted.id, userId, any()) }
            }
        }

        Scenario("Handle View Toggle Wishlist Selected Product for logged in user and product is wishlisted") {
            val userId = "123456"
            val similarSearchSelectedProductWishlisted = getSimilarSearchSelectedProductWishlisted()
            val removeWishListUseCase by memoized<RemoveWishListUseCase>()
            val userSession by memoized<UserSessionInterface>()

            lateinit var similarSearchViewModel: SimilarSearchViewModel

            Given("similar search view model with wishlisted product") {
                similarSearchViewModel = createSimilarSearchViewModel(similarSearchSelectedProductWishlisted)
            }

            Given("user is logged in") {
                every { userSession.isLoggedIn }.returns(true)
                every { userSession.userId }.returns(userId)
            }

            When("handle view toggle wishlist selected product") {
                similarSearchViewModel.onViewToggleWishlistSelectedProduct()
            }

            Then("verify remove wishlist API is called with product id equals to selected product id") {
                verify(exactly = 1) { removeWishListUseCase.createObservable(similarSearchSelectedProductWishlisted.id, userId, any()) }
            }
        }
    }

    Feature("Add Wishlist Selected Product") {
        createTestInstance()

        Scenario("Add Wishlist Selected Product Success") {
            val userId = "123456"
            val similarSearchSelectedProductNotWishlisted = getSimilarSearchSelectedProductNotWishlisted()
            val addWishListUseCase by memoized<AddWishListUseCase>()
            val userSession by memoized<UserSessionInterface>()

            lateinit var similarSearchViewModel: SimilarSearchViewModel

            Given("similar search view model with not-wishlisted product") {
                similarSearchViewModel = createSimilarSearchViewModel(similarSearchSelectedProductNotWishlisted)
            }

            Given("user is logged in") {
                every { userSession.isLoggedIn }.returns(true)
                every { userSession.userId }.returns(userId)
            }

            Given("add wishlist API will be successful") {
                every {
                    addWishListUseCase.createObservable(similarSearchSelectedProductNotWishlisted.id, userId, any())
                }.answers {
                    thirdArg<WishListActionListener>().onSuccessAddWishlist(firstArg())
                }
            }

            When("handle view toggle wishlist selected product") {
                similarSearchViewModel.onViewToggleWishlistSelectedProduct()
            }

            Then("assert add wishlist event is true") {
                val addWishlistEventLiveData = similarSearchViewModel.getAddWishlistEventLiveData().value

                addWishlistEventLiveData?.getContentIfNotHandled().shouldBe(
                        true,
                        "Add wishlist event should be true"
                )
            }

            Then("assert selected product is wishlisted is true, and update wishlist selected product event is true") {
                val similarSearchSelectedProduct = similarSearchViewModel.similarSearchSelectedProduct

                similarSearchSelectedProduct.isWishlisted.shouldBe(
                        true,
                        "Selected Product is wishlisted should be true"
                )

                val updateWishlistSelectedProductEventLiveData = similarSearchViewModel.getUpdateWishlistSelectedProductEventLiveData().value

                updateWishlistSelectedProductEventLiveData?.getContentIfNotHandled().shouldBe(
                        true,
                        "Update wishlist selected product event should be true"
                )
            }
        }

        Scenario("Add Wishlist Selected Product Failed") {
            val userId = "123456"
            val similarSearchSelectedProductNotWishlisted = getSimilarSearchSelectedProductNotWishlisted()
            val addWishListUseCase by memoized<AddWishListUseCase>()
            val userSession by memoized<UserSessionInterface>()

            lateinit var similarSearchViewModel: SimilarSearchViewModel

            Given("similar search view model with not-wishlisted product") {
                similarSearchViewModel = createSimilarSearchViewModel(similarSearchSelectedProductNotWishlisted)
            }

            Given("user is logged in") {
                every { userSession.isLoggedIn }.returns(true)
                every { userSession.userId }.returns(userId)
            }

            Given("add wishlist API will fail") {
                every {
                    addWishListUseCase.createObservable(similarSearchSelectedProductNotWishlisted.id, userId, any())
                }.answers {
                    thirdArg<WishListActionListener>().onErrorAddWishList("error from backend", firstArg())
                }
            }

            When("handle view toggle wishlist selected product") {
                similarSearchViewModel.onViewToggleWishlistSelectedProduct()
            }

            Then("assert add wishlist event is false") {
                val addWishlistEventLiveData = similarSearchViewModel.getAddWishlistEventLiveData().value

                addWishlistEventLiveData?.getContentIfNotHandled().shouldBe(
                        false,
                        "Add wishlist event should be false"
                )
            }

            Then("assert selected product is wishlisted stays false, and update wishlist selected product event is null") {
                val similarSearchSelectedProduct = similarSearchViewModel.similarSearchSelectedProduct

                similarSearchSelectedProduct.isWishlisted.shouldBe(
                        false,
                        "Selected Product is wishlisted should be false"
                )

                val updateWishlistSelectedProductEventLiveData = similarSearchViewModel.getUpdateWishlistSelectedProductEventLiveData().value

                updateWishlistSelectedProductEventLiveData?.getContentIfNotHandled().shouldBe(
                        null,
                        "Update wishlist selected product event should be null"
                )
            }
        }
    }

    Feature("Remove Wishlist Selected Product") {
        createTestInstance()

        Scenario("Remove Wishlist Selected Product Success") {
            val userId = "123456"
            val similarSearchSelectedProductWishlisted = getSimilarSearchSelectedProductWishlisted()
            val removeWishListUseCase by memoized<RemoveWishListUseCase>()
            val userSession by memoized<UserSessionInterface>()

            lateinit var similarSearchViewModel: SimilarSearchViewModel

            Given("similar search view model with wishlisted product") {
                similarSearchViewModel = createSimilarSearchViewModel(similarSearchSelectedProductWishlisted)
            }

            Given("user is logged in") {
                every { userSession.isLoggedIn }.returns(true)
                every { userSession.userId }.returns(userId)
            }

            Given("remove wishlist API will be successful") {
                every {
                    removeWishListUseCase.createObservable(similarSearchSelectedProductWishlisted.id, userId, any())
                }.answers {
                    thirdArg<WishListActionListener>().onSuccessRemoveWishlist(firstArg())
                }
            }

            When("handle view toggle wishlist selected product") {
                similarSearchViewModel.onViewToggleWishlistSelectedProduct()
            }

            Then("assert remove wishlist event is true") {
                val removeWishlistEventLiveData = similarSearchViewModel.getRemoveWishlistEventLiveData().value

                removeWishlistEventLiveData?.getContentIfNotHandled().shouldBe(
                        true,
                        "Remove wishlist event should be true"
                )
            }

            Then("assert selected product is wishlisted is false, and update wishlist selected product event is false") {
                val similarSearchSelectedProduct = similarSearchViewModel.similarSearchSelectedProduct

                similarSearchSelectedProduct.isWishlisted.shouldBe(
                        false,
                        "Selected Product is wishlisted should be false"
                )

                val updateWishlistSelectedProductEventLiveData = similarSearchViewModel.getUpdateWishlistSelectedProductEventLiveData().value

                updateWishlistSelectedProductEventLiveData?.getContentIfNotHandled().shouldBe(
                        false,
                        "Update wishlist selected product event should be false"
                )
            }
        }

        Scenario("Remove Wishlist Selected Product Failed") {
            val userId = "123456"
            val similarSearchSelectedProductWishlisted = getSimilarSearchSelectedProductWishlisted()
            val removeWishListUseCase by memoized<RemoveWishListUseCase>()
            val userSession by memoized<UserSessionInterface>()

            lateinit var similarSearchViewModel: SimilarSearchViewModel

            Given("similar search view model with wishlisted product") {
                similarSearchViewModel = createSimilarSearchViewModel(similarSearchSelectedProductWishlisted)
            }

            Given("user is logged in") {
                every { userSession.isLoggedIn }.returns(true)
                every { userSession.userId }.returns(userId)
            }

            Given("remove wishlist API will fail") {
                every {
                    removeWishListUseCase.createObservable(similarSearchSelectedProductWishlisted.id, userId, any())
                }.answers {
                    thirdArg<WishListActionListener>().onErrorRemoveWishlist("error from backend", firstArg())
                }
            }

            When("handle view toggle wishlist selected product") {
                similarSearchViewModel.onViewToggleWishlistSelectedProduct()
            }

            Then("assert remove wishlist selected product event is false") {
                val removeWishlistEventLiveData = similarSearchViewModel.getRemoveWishlistEventLiveData().value

                removeWishlistEventLiveData?.getContentIfNotHandled().shouldBe(
                        false,
                        "Remove wishlist event should be false"
                )
            }

            Then("assert selected product is wishlisted stays true, and update wishlist selected product event is null") {
                val similarSearchSelectedProduct = similarSearchViewModel.similarSearchSelectedProduct

                similarSearchSelectedProduct.isWishlisted.shouldBe(
                        true,
                        "Selected Product is wishlisted should be true"
                )

                val updateWishlistSelectedProductEventLiveData = similarSearchViewModel.getUpdateWishlistSelectedProductEventLiveData().value

                updateWishlistSelectedProductEventLiveData?.getContentIfNotHandled().shouldBe(
                        null,
                        "Update wishlist selected product event should be null"
                )
            }
        }
    }
})