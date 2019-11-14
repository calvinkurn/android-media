package com.tokopedia.purchase_platform.features.cart.view

import android.app.Activity
import com.google.android.gms.common.Feature
import com.tokopedia.abstraction.R
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.promocheckout.common.domain.CheckPromoStackingCodeUseCase
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.promocheckout.common.domain.mapper.CheckPromoStackingCodeMapper
import com.tokopedia.purchase_platform.common.domain.usecase.GetInsuranceCartUseCase
import com.tokopedia.purchase_platform.common.domain.usecase.RemoveInsuranceProductUsecase
import com.tokopedia.purchase_platform.common.domain.usecase.UpdateInsuranceProductDataUsecase
import com.tokopedia.purchase_platform.common.utils.CartApiRequestParamGenerator
import com.tokopedia.purchase_platform.features.cart.data.model.response.recentview.GqlRecentViewResponse
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.CartListData
import com.tokopedia.purchase_platform.features.cart.domain.usecase.*
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.GetWishlistUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifyOrder
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Observable
import rx.Scheduler
import rx.Subscriber
import rx.android.plugins.RxAndroidPlugins
import rx.android.plugins.RxAndroidSchedulersHook
import rx.plugins.RxJavaHooks
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import javax.annotation.meta.When

class CartListPresenterTest : Spek({

    val getCartListSimplifiedUseCase: GetCartListSimplifiedUseCase = mockk()
    val deleteCartListUseCase: DeleteCartListUseCase = mockk()
    val updateCartUseCase: UpdateCartUseCase = mockk()
    val checkPromoStackingCodeUseCase: CheckPromoStackingCodeUseCase = mockk()
    val checkPromoStackingCodeMapper: CheckPromoStackingCodeMapper = mockk()
    val checkPromoCodeCartListUseCase: CheckPromoCodeCartListUseCase = mockk()
    val compositeSubscription = CompositeSubscription()
    val cartApiRequestParamGenerator: CartApiRequestParamGenerator = mockk()
    val addWishListUseCase: AddWishListUseCase = mockk()
    val removeWishListUseCase: RemoveWishListUseCase = mockk()
    val updateAndReloadCartUseCase: UpdateAndReloadCartUseCase = mockk()
    val userSessionInterface: UserSessionInterface = mockk()
    val clearCacheAutoApplyStackUseCase: ClearCacheAutoApplyStackUseCase = mockk()
    val getRecentViewUseCase: GetRecentViewUseCase = mockk()
    val getWishlistUseCase: GetWishlistUseCase = mockk()
    val getRecommendationUseCase: GetRecommendationUseCase = mockk()
    val addToCartUseCase: AddToCartUseCase = mockk()
    val getInsuranceCartUseCase: GetInsuranceCartUseCase = mockk()
    val removeInsuranceProductUsecase: RemoveInsuranceProductUsecase = mockk()
    val updateInsuranceProductDataUsecase: UpdateInsuranceProductDataUsecase = mockk()
    val view: ICartListView = mockk(relaxed = true)

    RxAndroidPlugins.getInstance().registerSchedulersHook(object : RxAndroidSchedulersHook() {
        override fun getMainThreadScheduler(): Scheduler {
            return Schedulers.trampoline()
        }
    })

    RxJavaHooks.setOnIOScheduler { Schedulers.trampoline() }

    Feature("get cart list") {

        val cartListPresenter by memoized {
            println("new")
            CartListPresenter(
                    getCartListSimplifiedUseCase, deleteCartListUseCase, updateCartUseCase,
                    checkPromoStackingCodeUseCase, checkPromoStackingCodeMapper, checkPromoCodeCartListUseCase,
                    compositeSubscription, cartApiRequestParamGenerator, addWishListUseCase,
                    removeWishListUseCase, updateAndReloadCartUseCase, userSessionInterface,
                    clearCacheAutoApplyStackUseCase, getRecentViewUseCase, getWishlistUseCase,
                    getRecommendationUseCase, addToCartUseCase, getInsuranceCartUseCase,
                    removeInsuranceProductUsecase, updateInsuranceProductDataUsecase
            )
        }
        val emptyCartListData = CartListData()

        Scenario("initial load") {

            Given("empty response") {
                println("given 2")
                every { getCartListSimplifiedUseCase.createObservable(any()) } returns Observable.just(emptyCartListData)
            }

            Given("attach view") {
                println("given 2-2")
                cartListPresenter.attachView(view)
            }

            When("process initial get cart data") {
                println("do 2")
                cartListPresenter.processInitialGetCartData("", true, false)
            }

            Then("check") {
                println("check 2")
                verifyOrder {
                    view.renderLoadGetCartData()
                    view.renderLoadGetCartDataFinish()
                    view.renderInitialGetCartListDataSuccess(emptyCartListData)
                    view.stopCartPerformanceTrace()
                }
                confirmVerified(view)
            }
        }

        Scenario("refresh load") {

            Given("empty response") {
                println("given 3")
                every { getCartListSimplifiedUseCase.createObservable(any()) } returns Observable.just(emptyCartListData)
            }

            Given("attach view") {
                println("given 3-2")
                cartListPresenter.attachView(view)
            }

            When("process initial get cart data") {
                println("do 3")
                cartListPresenter.processInitialGetCartData("", false, false)
            }

            Then("check") {
                println("check 3")
                verifyOrder {
                    view.showProgressLoading()
                    view.hideProgressLoading()
                    view.renderLoadGetCartDataFinish()
                    view.renderInitialGetCartListDataSuccess(emptyCartListData)
                    view.stopCartPerformanceTrace()
                }
                confirmVerified(view)
            }
        }

        Scenario("error load") {

            val context: Activity = mockk()
            val errorMessage = "Terjadi kesalahan pada server. Ulangi beberapa saat lagi"

            Given("throw error") {
                println("given 1")
                every { getCartListSimplifiedUseCase.createObservable(any()) } returns Observable.error(ResponseErrorException("testing"))
                every { getRecentViewUseCase.createObservable(any(), any()) } answers {
                    Observable.just(GraphqlResponse(
                            mapOf(GqlRecentViewResponse::class.java to GqlRecentViewResponse()), emptyMap(), false))
                            .subscribe(secondArg() as Subscriber<GraphqlResponse>)
                }
            }

            Given("attach view") {
                println("given 1-2")
                every { view.activity } returns context
                every { context.getString(R.string.default_request_error_internal_server) } returns errorMessage
                cartListPresenter.attachView(view)
            }

            When("process initial get cart data") {
                println("do 1")
                cartListPresenter.processInitialGetCartData("", true, true)
            }

            Then("check") {
                println("check 1")
                verifyOrder {
                    view.renderLoadGetCartData()
                    view.renderLoadGetCartDataFinish()
                    view.activity
                    view.renderErrorInitialGetCartListData(errorMessage)
                    view.stopCartPerformanceTrace()
                }
                confirmVerified(view)
            }
        }
    }
})