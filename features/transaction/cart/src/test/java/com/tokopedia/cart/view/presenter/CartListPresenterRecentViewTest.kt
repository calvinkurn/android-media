package com.tokopedia.cart.view.presenter

import com.google.gson.Gson
import com.tokopedia.atc_common.domain.usecase.AddToCartExternalUseCase
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.atc_common.domain.usecase.UpdateCartCounterUseCase
import com.tokopedia.cart.domain.usecase.*
import com.tokopedia.cart.view.CartListPresenter
import com.tokopedia.cart.view.ICartListView
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.ValidateUsePromoRevampUseCase
import com.tokopedia.purchase_platform.common.schedulers.TestSchedulers
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.seamless_login_common.domain.usecase.SeamlessLoginUsecase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.GetWishlistUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Observable
import rx.subscriptions.CompositeSubscription

/**
 * Created by Irfan Khoirul on 2020-01-29.
 */

object CartListPresenterRecentViewTest : Spek({

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

    Feature("get recent view test") {

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

        Scenario("get recent view success") {
            val recommendationWidgetStringData = """
                {
                    "recommendationItemList": 
                    [
                        {
                            "productId":0
                        }
                    ]
                }
            """.trimIndent()
            val response = mutableListOf<RecommendationWidget>().apply {
                val recommendationWidget = Gson().fromJson(recommendationWidgetStringData, RecommendationWidget::class.java)
                add(recommendationWidget)
            }

            Given("success response") {
                every { getRecentViewUseCase.createObservable(any()) } returns Observable.just(response)
            }

            Given("request params") {
                every { getRecentViewUseCase.getRecomParams(any(), any(), any(), any(), any()) } returns RequestParams.create()
            }

            When("process get recent view") {
                cartListPresenter.processGetRecentViewData(emptyList())
            }

            Then("should render recent view") {
                verify {
                    view.renderRecentView(response[0])
                }
            }

            Then("should try to stop firebase performance tracker") {
                verify {
                    view.setHasTriedToLoadRecentView()
                    view.stopAllCartPerformanceTrace()
                }
            }

        }

        Scenario("get recent view empty") {

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

            Given("success response") {
                every { getRecentViewUseCase.createObservable(any()) } returns Observable.just(response)
            }

            Given("request params") {
                every { getRecentViewUseCase.getRecomParams(any(), any(), any(), any(), any()) } returns RequestParams.create()
            }

            When("process get recent view") {
                cartListPresenter.processGetRecentViewData(emptyList())
            }

            Then("should not render recent view") {
                verify(inverse = true) {
                    view.renderRecentView(response[0])
                }
            }

            Then("should try to stop firebase performance tracker") {
                verify {
                    view.setHasTriedToLoadRecentView()
                    view.stopAllCartPerformanceTrace()
                }
            }

        }

        Scenario("get recent view error") {

            Given("error response") {
                every { getRecentViewUseCase.createObservable(any()) } returns Observable.error(IllegalStateException())
            }

            Given("request params") {
                every { getRecentViewUseCase.getRecomParams(any(), any(), any(), any(), any()) } returns RequestParams.create()
            }

            When("process get recent view") {
                cartListPresenter.processGetRecentViewData(emptyList())
            }

            Then("should try to stop firebase performance tracker") {
                verify {
                    view.setHasTriedToLoadRecentView()
                    view.stopAllCartPerformanceTrace()
                }
            }

        }

    }

})