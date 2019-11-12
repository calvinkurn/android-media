package com.tokopedia.similarsearch

import com.tokopedia.similarsearch.testinstance.similarSearchSelectedProduct
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
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

        Scenario("Handle View Toggle Wishlist Selected Product for logged in user") {
            val userId = "123456"
            val addWishlistUseCase by memoized<AddWishListUseCase>()
            val userSession by memoized<UserSessionInterface>()

            lateinit var similarSearchViewModel: SimilarSearchViewModel

            Given("similar search view model") {
                similarSearchViewModel = createSimilarSearchViewModel()
            }

            Given("user is logged in") {
                every { userSession.isLoggedIn }.returns(true)
                every { userSession.userId }.returns(userId)
            }

            When("handle view toggle wishlist selected product") {
                similarSearchViewModel.onViewToggleWishlistSelectedProduct()
            }

            Then("verify add wishlist API is called with product id equals to selected product id") {
                verify(exactly = 1) { addWishlistUseCase.createObservable(similarSearchSelectedProduct.id, userId, any()) }
            }
        }
    }
})