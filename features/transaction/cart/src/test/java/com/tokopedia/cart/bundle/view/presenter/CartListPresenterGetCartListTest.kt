package com.tokopedia.cart.bundle.view.presenter

import com.google.gson.Gson
import com.tokopedia.atc_common.domain.usecase.AddToCartExternalUseCase
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.atc_common.domain.usecase.UpdateCartCounterUseCase
import com.tokopedia.cart.bundle.data.model.response.shopgroupsimplified.CartData
import com.tokopedia.cart.bundle.domain.usecase.*
import com.tokopedia.cart.bundle.view.CartListPresenter
import com.tokopedia.cart.bundle.view.ICartListView
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UndoDeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.OldClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.OldValidateUsePromoRevampUseCase
import com.tokopedia.purchase_platform.common.schedulers.TestSchedulers
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.seamless_login_common.domain.usecase.SeamlessLoginUsecase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.GetWishlistUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import io.mockk.*
import io.mockk.every
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Observable
import rx.subscriptions.CompositeSubscription

object CartListPresenterGetCartListTest : Spek({

    val getCartRevampV3UseCase: GetCartRevampV3UseCase = mockk()
    val deleteCartUseCase: DeleteCartUseCase = mockk()
    val undoDeleteCartUseCase: UndoDeleteCartUseCase = mockk()
    val addCartToWishlistUseCase: AddCartToWishlistUseCase = mockk()
    val updateCartUseCase: UpdateCartUseCase = mockk()
    val updateCartAndValidateUseUseCase: UpdateCartAndValidateUseUseCase = mockk()
    val validateUsePromoRevampUseCase: OldValidateUsePromoRevampUseCase = mockk()
    val compositeSubscription = CompositeSubscription()
    val addWishListUseCase: AddWishListUseCase = mockk()
    val removeWishListUseCase: RemoveWishListUseCase = mockk()
    val updateAndReloadCartUseCase: UpdateAndReloadCartUseCase = mockk()
    val userSessionInterface: UserSessionInterface = mockk()
    val clearCacheAutoApplyStackUseCase: OldClearCacheAutoApplyStackUseCase = mockk()
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

    Feature("get cart list") {

        val cartListPresenter by memoized {
            CartListPresenter(
                    getCartRevampV3UseCase, deleteCartUseCase, undoDeleteCartUseCase,
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

        Scenario("initial load success") {

            val cartData = CartData()

            Given("empty response") {
                coEvery { getCartRevampV3UseCase.setParams(any(), any()) } just Runs
                coEvery { getCartRevampV3UseCase.execute(any(), any()) } answers {
                    firstArg<(CartData) -> Unit>().invoke(cartData)
                }
            }

            Given("update cart counter") {
                every { updateCartCounterUseCase.createObservable(any()) } answers { Observable.just(1) }
            }

            When("process initial get cart data") {
                cartListPresenter.processInitialGetCartData("", true, false)
            }

            Then("should render then finish loading") {
                verify {
                    view.renderLoadGetCartDataFinish()
                }
            }

            Then("should render success") {
                verify {
                    view.renderInitialGetCartListDataSuccess(cartData)
                }
            }

            Then("should try to stop performance trace") {
                verify {
                    view.stopCartPerformanceTrace()
                }
            }

        }

        Scenario("refresh load success") {

            val cartData = CartData()

            Given("empty response") {
                coEvery { getCartRevampV3UseCase.setParams(any(), any()) } just Runs
                coEvery { getCartRevampV3UseCase.execute(any(), any()) } answers {
                    firstArg<(CartData) -> Unit>().invoke(cartData)
                }
            }

            Given("update cart counter") {
                every { updateCartCounterUseCase.createObservable(any()) } answers { Observable.just(1) }
            }

            When("process initial get cart data") {
                cartListPresenter.processInitialGetCartData("", false, false)
            }

            Then("should render then finish loading") {
                verify {
                    view.renderLoadGetCartDataFinish()
                }
            }

            Then("should render success") {
                verify {
                    view.renderInitialGetCartListDataSuccess(cartData)
                }
            }

            Then("should try to stop performance trace") {
                verify {
                    view.stopCartPerformanceTrace()
                }
            }
        }

        Scenario("initial load failed") {

            val exception = ResponseErrorException("Terjadi kesalahan pada server. Ulangi beberapa saat lagi")

            val recommendationWidgetStringData = """
                {
                    "recommendationItemList":
                    [
                    ]
                }
            """.trimIndent()

            val response = mutableListOf<RecommendationWidget>().apply {
                val recommendationWidget = Gson().fromJson(recommendationWidgetStringData, RecommendationWidget::class.java)
                add(recommendationWidget)
            }

            Given("throw error") {
                coEvery { getCartRevampV3UseCase.setParams(any(), any()) } just Runs
                coEvery { getCartRevampV3UseCase.execute(any(), any()) } answers {
                    secondArg<(Throwable) -> Unit>().invoke(exception)
                }
                every { getRecentViewUseCase.createObservable(any()) } answers { Observable.just(response) }
            }

            Given("update cart counter") {
                every { updateCartCounterUseCase.createObservable(any()) } answers { Observable.just(1) }
            }

            When("process initial get cart data") {
                cartListPresenter.processInitialGetCartData("", true, true)
            }

            Then("should render error") {
                verify {
                    view.renderErrorInitialGetCartListData(exception)
                }
            }

            Then("should try to stop performance trace") {
                verify {
                    view.stopCartPerformanceTrace()
                }
            }

        }

        Scenario("refresh load failed") {

            val exception = ResponseErrorException("Terjadi kesalahan pada server. Ulangi beberapa saat lagi")

            val recommendationWidgetStringData = """
                {
                    "recommendationItemList":
                    [
                    ]
                }
            """.trimIndent()

            val response = mutableListOf<RecommendationWidget>().apply {
                val recommendationWidget = Gson().fromJson(recommendationWidgetStringData, RecommendationWidget::class.java)
                add(recommendationWidget)
            }
            Given("throw error") {
                coEvery { getCartRevampV3UseCase.setParams(any(), any()) } just Runs
                coEvery { getCartRevampV3UseCase.execute(any(), any()) } answers {
                    secondArg<(Throwable) -> Unit>().invoke(exception)
                }
                every { getRecentViewUseCase.createObservable(any()) } answers { Observable.just(response) }
            }

            Given("update cart counter") {
                every { updateCartCounterUseCase.createObservable(any()) } answers { Observable.just(1) }
            }

            When("process initial get cart data") {
                cartListPresenter.processInitialGetCartData("", false, true)
            }

            Then("should render error") {
                verify {
                    view.renderErrorInitialGetCartListData(exception)
                }
            }

            Then("should try to stop performance trace") {
                verify {
                    view.stopCartPerformanceTrace()
                }
            }
        }

    }

})
