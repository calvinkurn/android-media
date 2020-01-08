package com.tokopedia.purchase_platform.features.cart.view.presenter

import androidx.fragment.app.FragmentActivity
import com.tokopedia.abstraction.R
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.promocheckout.common.domain.CheckPromoStackingCodeUseCase
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.promocheckout.common.domain.mapper.CheckPromoStackingCodeMapper
import com.tokopedia.purchase_platform.common.domain.schedulers.TestSchedulers
import com.tokopedia.purchase_platform.common.domain.usecase.GetInsuranceCartUseCase
import com.tokopedia.purchase_platform.common.domain.usecase.RemoveInsuranceProductUsecase
import com.tokopedia.purchase_platform.common.domain.usecase.UpdateInsuranceProductDataUsecase
import com.tokopedia.purchase_platform.features.cart.data.model.response.recentview.GqlRecentViewResponse
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.CartListData
import com.tokopedia.purchase_platform.features.cart.domain.usecase.*
import com.tokopedia.purchase_platform.features.cart.view.CartListPresenter
import com.tokopedia.purchase_platform.features.cart.view.ICartListView
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.seamless_login.domain.usecase.SeamlessLoginUsecase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.GetWishlistUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Observable
import rx.Subscriber
import rx.subscriptions.CompositeSubscription

/**
 * Created by Irfan Khoirul on 2020-01-07.
 */

class CartListPresenterGetCartListTest : Spek({

    val getCartListSimplifiedUseCase: GetCartListSimplifiedUseCase = mockk()
    val deleteCartListUseCase: DeleteCartUseCase = mockk()
    val updateCartUseCase: UpdateCartUseCase = mockk()
    val checkPromoStackingCodeUseCase: CheckPromoStackingCodeUseCase = mockk()
    val checkPromoStackingCodeMapper: CheckPromoStackingCodeMapper = mockk()
    val compositeSubscription = CompositeSubscription()
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
    val seamlessLoginUsecase: SeamlessLoginUsecase = mockk()

    Feature("get cart list") {

        val cartListPresenter by memoized {
            CartListPresenter(
                    getCartListSimplifiedUseCase, deleteCartListUseCase, updateCartUseCase,
                    checkPromoStackingCodeUseCase, checkPromoStackingCodeMapper, compositeSubscription,
                    addWishListUseCase, removeWishListUseCase, updateAndReloadCartUseCase,
                    userSessionInterface, clearCacheAutoApplyStackUseCase, getRecentViewUseCase,
                    getWishlistUseCase, getRecommendationUseCase, addToCartUseCase, getInsuranceCartUseCase,
                    removeInsuranceProductUsecase, updateInsuranceProductDataUsecase, seamlessLoginUsecase, TestSchedulers
            )
        }
        val emptyCartListData = CartListData()

        Scenario("initial load success") {

            val view: ICartListView = mockk(relaxed = true)

            Given("empty response") {
                every { getCartListSimplifiedUseCase.createObservable(any()) } returns Observable.just(emptyCartListData)
            }

            Given("attach view") {
                cartListPresenter.attachView(view)
            }

            When("process initial get cart data") {
                cartListPresenter.processInitialGetCartData("", true, false)
            }

            Then("should render success") {
                verify {
                    view.renderInitialGetCartListDataSuccess(emptyCartListData)
                }
            }

            Then("should render then finish loading") {
                verify {
                    view.renderLoadGetCartData()
                    view.renderLoadGetCartDataFinish()
                }
            }
        }

        Scenario("refresh load success") {

            val view: ICartListView = mockk(relaxed = true)

            Given("empty response") {
                every { getCartListSimplifiedUseCase.createObservable(any()) } returns Observable.just(emptyCartListData)
            }

            Given("attach view") {
                cartListPresenter.attachView(view)
            }

            When("process initial get cart data") {
                cartListPresenter.processInitialGetCartData("", false, false)
            }

            Then("should render success") {
                verify {
                    view.renderInitialGetCartListDataSuccess(emptyCartListData)
                }
            }

            Then("should show then hide progress loading") {
                verifyOrder {
                    view.showProgressLoading()
                    view.hideProgressLoading()
                }
            }
        }

        Scenario("initial load failed") {

            val view: ICartListView = mockk(relaxed = true)
            val context: FragmentActivity = mockk()
            val errorMessage = "Terjadi kesalahan pada server. Ulangi beberapa saat lagi"

            Given("throw error") {
                every { getCartListSimplifiedUseCase.createObservable(any()) } returns Observable.error(ResponseErrorException("testing"))
                every { getRecentViewUseCase.createObservable(any(), any()) } answers {
                    Observable.just(GraphqlResponse(
                            mapOf(GqlRecentViewResponse::class.java to GqlRecentViewResponse()), emptyMap(), false))
                            .subscribe(secondArg() as Subscriber<GraphqlResponse>)
                }
            }

            Given("attach view") {
                every { view.getActivityObject() } returns context
                every { context.getString(R.string.default_request_error_internal_server) } returns errorMessage
                cartListPresenter.attachView(view)
            }

            When("process initial get cart data") {
                cartListPresenter.processInitialGetCartData("", true, true)
            }

            Then("should render error") {
                verify {
                    view.renderErrorInitialGetCartListData(errorMessage)
                }
            }
        }

        Scenario("refresh load failed") {

            val view: ICartListView = mockk(relaxed = true)
            val context: FragmentActivity = mockk()
            val errorMessage = "Terjadi kesalahan pada server. Ulangi beberapa saat lagi"

            Given("throw error") {
                every { getCartListSimplifiedUseCase.createObservable(any()) } returns Observable.error(ResponseErrorException("testing"))
                every { getRecentViewUseCase.createObservable(any(), any()) } answers {
                    Observable.just(GraphqlResponse(
                            mapOf(GqlRecentViewResponse::class.java to GqlRecentViewResponse()), emptyMap(), false))
                            .subscribe(secondArg() as Subscriber<GraphqlResponse>)
                }
            }

            Given("attach view") {
                every { view.getActivityObject() } returns context
                every { context.getString(R.string.default_request_error_internal_server) } returns errorMessage
                cartListPresenter.attachView(view)
            }

            When("process initial get cart data") {
                cartListPresenter.processInitialGetCartData("", false, true)
            }

            Then("should render error") {
                verify {
                    view.renderErrorInitialGetCartListData(errorMessage)
                }
            }
        }

    }

})