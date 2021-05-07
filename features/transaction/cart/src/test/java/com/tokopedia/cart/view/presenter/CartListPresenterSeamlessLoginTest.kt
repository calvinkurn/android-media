package com.tokopedia.cart.view.presenter

import com.tokopedia.atc_common.domain.usecase.AddToCartExternalUseCase
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.atc_common.domain.usecase.UpdateCartCounterUseCase
import com.tokopedia.cart.domain.usecase.*
import com.tokopedia.cart.view.CartListPresenter
import com.tokopedia.cart.view.ICartListView
import com.tokopedia.cart.view.subscriber.CartSeamlessLoginSubscriber
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.ValidateUsePromoRevampUseCase
import com.tokopedia.purchase_platform.common.schedulers.TestSchedulers
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.seamless_login_common.domain.usecase.SeamlessLoginUsecase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.GetWishlistUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verifyOrder
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.subscriptions.CompositeSubscription

object CartListPresenterSeamlessLoginTest : Spek({

    val getCartListSimplifiedUseCase: GetCartListSimplifiedUseCase = mockk()
    val deleteCartUseCase: DeleteCartUseCase = mockk()
    val undoDeleteCartUseCase: UndoDeleteCartUseCase = mockk()
    val addCartToWishlistUseCase: AddCartToWishlistUseCase = mockk()
    val updateCartUseCase: UpdateCartUseCase = mockk()
    val updateCartAndValidateUseUseCase: UpdateCartAndValidateUseUseCase = mockk()
    val validateUsePromoRevampUseCase: ValidateUsePromoRevampUseCase = mockk()
    val compositeSubscription = CompositeSubscription()
    val addWishListUseCase: AddWishListUseCase = mockk()
    val removeWishListUseCase: RemoveWishListUseCase = mockk()
    val updateAndReloadCartUseCase: UpdateAndReloadCartUseCase = mockk()
    val userSessionInterface: UserSessionInterface = mockk()
    val clearCacheAutoApplyStackUseCase: ClearCacheAutoApplyStackUseCase = mockk()
    val getRecentViewUseCase: GetRecommendationUseCase = mockk()
    val getWishlistUseCase: GetWishlistUseCase = mockk()
    val getRecommendationUseCase: GetRecommendationUseCase = mockk()
    val addToCartUseCase: AddToCartUseCase = mockk()
    val addToCartExternalUseCase: AddToCartExternalUseCase = mockk()
    val seamlessLoginUsecase: SeamlessLoginUsecase = mockk()
    val updateCartCounterUseCase: UpdateCartCounterUseCase = mockk()
    val setCartlistCheckboxStateUseCase: SetCartlistCheckboxStateUseCase = mockk()
    val followShopUseCase: FollowShopUseCase = mockk()
    val view: ICartListView = mockk(relaxed = true)

    Feature("seamless login") {

        val cartListPresenter by memoized {
            CartListPresenter(
                    getCartListSimplifiedUseCase, deleteCartUseCase, undoDeleteCartUseCase,
                    updateCartUseCase, compositeSubscription, addWishListUseCase,
                    addCartToWishlistUseCase, removeWishListUseCase, updateAndReloadCartUseCase,
                    userSessionInterface, clearCacheAutoApplyStackUseCase, getRecentViewUseCase,
                    getWishlistUseCase, getRecommendationUseCase, addToCartUseCase,
                    addToCartExternalUseCase, seamlessLoginUsecase, updateCartCounterUseCase,
                    updateCartAndValidateUseUseCase, validateUsePromoRevampUseCase, setCartlistCheckboxStateUseCase,
                    followShopUseCase, TestSchedulers
            )
        }

        beforeEachTest {
            cartListPresenter.attachView(view)
        }

        Scenario("success redirect to lite") {

            val url = "http://"

            Given("redirect to lite data") {
                val slot = slot<CartSeamlessLoginSubscriber>()
                every {
                    seamlessLoginUsecase.generateSeamlessUrl(url, capture(slot))
                } answers {
                    val captured = slot.captured
                    captured.onUrlGenerated(url)
                }
                val adsId = "123"
                every { view.getAdsId() } returns adsId
            }

            When("process redirect to lite") {
                cartListPresenter.redirectToLite(url)
            }

            Then("should navigate to lite") {
                verifyOrder {
                    view.showProgressLoading()
                    view.getAdsId()
                    view.hideProgressLoading()
                    view.goToLite(url)
                }
            }
        }

        Scenario("failed redirect to lite") {

            val url = "http://"
            val errorMessage = "error"

            Given("redirect to lite data") {
                val slot = slot<CartSeamlessLoginSubscriber>()
                every {
                    seamlessLoginUsecase.generateSeamlessUrl(url, capture(slot))
                } answers {
                    val captured = slot.captured
                    captured.onError(errorMessage)
                }
                val adsId = "123"
                every { view.getAdsId() } returns adsId
            }

            When("process redirect to lite") {
                cartListPresenter.redirectToLite(url)
            }

            Then("should navigate show error") {
                verifyOrder {
                    view.showProgressLoading()
                    view.getAdsId()
                    view.hideProgressLoading()
                    view.showToastMessageRed(errorMessage)
                }
            }
        }

        Scenario("failed redirect to lite with error param") {

            val url = "http://"
            val errorMessage = "error"

            Given("redirect to lite data") {
                val slot = slot<CartSeamlessLoginSubscriber>()
                every {
                    seamlessLoginUsecase.generateSeamlessUrl(url, capture(slot))
                } answers {
                    val captured = slot.captured
                    captured.onError(errorMessage)
                }
                val adsId = null
                every { view.getAdsId() } returns adsId
            }

            When("process redirect to lite") {
                cartListPresenter.redirectToLite(url)
            }

            Then("should navigate show error") {
                verifyOrder {
                    view.showProgressLoading()
                    view.getAdsId()
                    view.hideProgressLoading()
                    view.showToastMessageRed()
                }
            }
        }

    }

})