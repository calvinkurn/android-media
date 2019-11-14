package com.tokopedia.similarsearch

import com.tokopedia.similarsearch.testinstance.similarSearchSelectedProductNotWishlisted
import com.tokopedia.similarsearch.testinstance.similarSearchSelectedProductWishlisted
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
            val addWishlistUseCase by memoized<AddWishListUseCase>()
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
                verify(exactly = 1) { addWishlistUseCase.createObservable(similarSearchSelectedProductNotWishlisted.id, userId, any()) }
            }
        }

        Scenario("Handle View Toggle Wishlist Selected Product for logged in user and product is wishlisted") {
            val userId = "123456"
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
            val addWishlistUseCase by memoized<AddWishListUseCase>()
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
                    addWishlistUseCase.createObservable(similarSearchSelectedProductNotWishlisted.id, userId, any())
                }.answers {
                    thirdArg<WishListActionListener>().onSuccessAddWishlist(firstArg())
                }
            }

            When("handle view toggle wishlist selected product") {
                similarSearchViewModel.onViewToggleWishlistSelectedProduct()
            }

            Then("assert add wishlist selected product event is true") {
                val addWishlistSelectedProductEvent = similarSearchViewModel.getAddWishlistSelectedProductEventLiveData().value

                addWishlistSelectedProductEvent?.getContentIfNotHandled().shouldBe(
                        true,
                        "Add wishlist selected product event should be true"
                )
            }

            Then("assert selected product is wishlisted is true") {
                val similarSearchSelectedProduct = similarSearchViewModel.similarSearchSelectedProduct

                similarSearchSelectedProduct.isWishlisted.shouldBe(
                        true,
                        "Selected Product is wishlisted should be true"
                )
            }
        }

        Scenario("Add Wishlist Selected Product Failed") {
            val userId = "123456"
            val addWishlistUseCase by memoized<AddWishListUseCase>()
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
                    addWishlistUseCase.createObservable(similarSearchSelectedProductNotWishlisted.id, userId, any())
                }.answers {
                    thirdArg<WishListActionListener>().onErrorAddWishList("error from backend", firstArg())
                }
            }

            When("handle view toggle wishlist selected product", timeout = 100000) {
                similarSearchViewModel.onViewToggleWishlistSelectedProduct()
            }

            Then("assert add wishlist selected product event is false", timeout = 100000) {
                val addWishlistSelectedProductEvent = similarSearchViewModel.getAddWishlistSelectedProductEventLiveData().value

                addWishlistSelectedProductEvent?.getContentIfNotHandled().shouldBe(
                        false,
                        "Add wishlist selected product event should be false"
                )
            }

//            Then("assert selected product is wishlisted stays false") {
//                val similarSearchSelectedProduct = similarSearchViewModel.similarSearchSelectedProduct
//
//                similarSearchSelectedProduct.isWishlisted.shouldBe(
//                        false,
//                        "Selected Product is wishlisted should be false"
//                )
//            }
        }
    }
})