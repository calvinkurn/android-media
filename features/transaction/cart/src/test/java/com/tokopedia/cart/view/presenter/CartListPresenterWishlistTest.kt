package com.tokopedia.cart.view.presenter

import com.tokopedia.cart.view.ICartListView
import com.tokopedia.wishlist.common.data.source.cloud.model.Wishlist
import com.tokopedia.wishlist.common.response.GetWishlistResponse
import com.tokopedia.wishlist.common.response.WishlistDataResponse
import com.tokopedia.wishlist.common.usecase.GetWishlistUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Observable

/**
 * Created by Irfan Khoirul on 2020-01-29.
 */

object CartListPresenterWishlistTest : Spek({

    val getWishlistUseCase: GetWishlistUseCase = mockk()
    val view: ICartListView = mockk(relaxed = true)

    Feature("get wishlist test") {

        val cartListPresenter by memoized {
            PresenterProvider.provideCartListPresenter()
        }

        beforeEachTest {
            cartListPresenter.attachView(view)
        }

        Scenario("get wishlist success") {

            val response = GetWishlistResponse().apply {
                gqlWishList = WishlistDataResponse().apply {
                    wishlistDataList = mutableListOf<Wishlist>().apply {
                        add(Wishlist())
                    }
                }
            }

            Given("success response") {
                every { getWishlistUseCase.createObservable(any()) } returns Observable.just(response)
            }

            When("process get wishlist") {
                cartListPresenter.processGetWishlistData()
            }

            Then("should render wishlist") {
                verify {
                    view.renderWishlist(response.gqlWishList?.wishlistDataList, true)
                }
            }

            Then("should try to stop firebase performance tracker") {
                verify {
                    view.setHasTriedToLoadWishList()
                    view.stopAllCartPerformanceTrace()
                }
            }

        }

        Scenario("get wishlist empty") {

            val response = GetWishlistResponse().apply {
                gqlWishList = WishlistDataResponse().apply {
                    wishlistDataList = mutableListOf()
                }
            }

            Given("success response") {
                every { getWishlistUseCase.createObservable(any()) } returns Observable.just(response)
            }

            When("process get wishlist") {
                cartListPresenter.processGetWishlistData()
            }

            Then("should not render wishlist") {
                verify(inverse = true) {
                    view.renderWishlist(response.gqlWishList?.wishlistDataList, false)
                }
            }

            Then("should try to stop firebase performance tracker") {
                verify {
                    view.setHasTriedToLoadWishList()
                    view.stopAllCartPerformanceTrace()
                }
            }

        }

        Scenario("get wishlist error") {

            Given("error response") {
                every { getWishlistUseCase.createObservable(any()) } returns Observable.error(IllegalStateException())
            }

            When("process get wishlist") {
                cartListPresenter.processGetWishlistData()
            }

            Then("should try to stop firebase performance tracker") {
                verify {
                    view.setHasTriedToLoadWishList()
                    view.stopAllCartPerformanceTrace()
                }
            }

        }

    }

})