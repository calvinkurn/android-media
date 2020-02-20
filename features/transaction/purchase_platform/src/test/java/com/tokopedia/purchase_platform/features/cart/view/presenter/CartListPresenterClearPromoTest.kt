package com.tokopedia.purchase_platform.features.cart.view.presenter

import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.atc_common.domain.usecase.UpdateCartCounterUseCase
import com.tokopedia.promocheckout.common.domain.CheckPromoStackingCodeUseCase
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.promocheckout.common.domain.model.clearpromo.ClearCacheAutoApplyStackResponse
import com.tokopedia.promocheckout.common.domain.model.clearpromo.SuccessData
import com.tokopedia.promocheckout.common.view.model.PromoStackingData
import com.tokopedia.purchase_platform.common.domain.schedulers.TestSchedulers
import com.tokopedia.purchase_platform.common.domain.usecase.GetInsuranceCartUseCase
import com.tokopedia.purchase_platform.common.domain.usecase.RemoveInsuranceProductUsecase
import com.tokopedia.purchase_platform.common.domain.usecase.UpdateInsuranceProductDataUsecase
import com.tokopedia.purchase_platform.features.cart.domain.usecase.*
import com.tokopedia.purchase_platform.features.cart.view.CartListPresenter
import com.tokopedia.purchase_platform.features.cart.view.ICartListView
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.seamless_login.domain.usecase.SeamlessLoginUsecase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.GetWishlistUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import io.mockk.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Observable
import rx.subscriptions.CompositeSubscription

/**
 * Created by Irfan Khoirul on 2020-01-17.
 */

object CartListPresenterClearPromoTest : Spek({

    val getCartListSimplifiedUseCase: GetCartListSimplifiedUseCase = mockk()
    val deleteCartListUseCase: DeleteCartUseCase = mockk()
    val updateCartUseCase: UpdateCartUseCase = mockk()
    val checkPromoStackingCodeUseCase: CheckPromoStackingCodeUseCase = mockk()
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
    val updateCartCounterUseCase: UpdateCartCounterUseCase = mockk()
    val view: ICartListView = mockk(relaxed = true)

    Feature("clear promo test") {

        val cartListPresenter by memoized {
            CartListPresenter(
                    getCartListSimplifiedUseCase, deleteCartListUseCase, updateCartUseCase,
                    checkPromoStackingCodeUseCase, compositeSubscription, addWishListUseCase,
                    removeWishListUseCase, updateAndReloadCartUseCase, userSessionInterface,
                    clearCacheAutoApplyStackUseCase, getRecentViewUseCase, getWishlistUseCase,
                    getRecommendationUseCase, addToCartUseCase, getInsuranceCartUseCase,
                    removeInsuranceProductUsecase, updateInsuranceProductDataUsecase,
                    seamlessLoginUsecase, updateCartCounterUseCase, TestSchedulers
            )
        }

        beforeEachTest {
            cartListPresenter.attachView(view)
        }

        Scenario("clear promo success") {

            val response = ClearCacheAutoApplyStackResponse().apply {
                successData = SuccessData().apply {
                    success = true
                }
            }
            val shopIndex = 1

            Given("success response") {
                every { clearCacheAutoApplyStackUseCase.setParams(any(), any()) } just runs
                every { clearCacheAutoApplyStackUseCase.createObservable(any()) } returns Observable.just(response)
            }

            When("process clear promo") {
                cartListPresenter.processCancelAutoApplyPromoStack(shopIndex, arrayListOf("code"), false)
            }

            Then("should render success") {
                verify {
                    view.onSuccessClearPromoStack(shopIndex)
                }
            }
        }

        Scenario("clear promo error") {

            val response = ClearCacheAutoApplyStackResponse().apply {
                successData = SuccessData().apply {
                    success = false
                }
            }
            val ignoreApiResponse = false

            Given("error response") {
                every { clearCacheAutoApplyStackUseCase.setParams(any(), any()) } just runs
                every { clearCacheAutoApplyStackUseCase.createObservable(any()) } returns Observable.just(response)
            }

            When("process clear promo") {
                cartListPresenter.processCancelAutoApplyPromoStack(1, arrayListOf("code"), ignoreApiResponse)
            }

            Then("should render error") {
                verify {
                    view.onFailedClearPromoStack(ignoreApiResponse)
                }
            }
        }

        Scenario("clear promo error with exception") {

            val ignoreApiResponse = false

            Given("error response") {
                every { clearCacheAutoApplyStackUseCase.setParams(any(), any()) } just runs
                every { clearCacheAutoApplyStackUseCase.createObservable(any()) } returns Observable.error(IllegalStateException())
            }

            When("process clear promo") {
                cartListPresenter.processCancelAutoApplyPromoStack(1, arrayListOf("code"), ignoreApiResponse)
            }

            Then("should render error") {
                verify {
                    view.onFailedClearPromoStack(ignoreApiResponse)
                }
            }
        }

        Scenario("clear promo fire and forget success") {

            val response = ClearCacheAutoApplyStackResponse().apply {
                successData = SuccessData().apply {
                    success = true
                }
            }
            val shopIndex = 1

            Given("success response") {
                every { clearCacheAutoApplyStackUseCase.setParams(any(), any()) } just runs
                every { clearCacheAutoApplyStackUseCase.createObservable(any()) } returns Observable.just(response)
            }

            When("process clear promo") {
                cartListPresenter.processCancelAutoApplyPromoStack(shopIndex, arrayListOf("code"), true)
            }

            Then("should render success") {
                verify {
                    view.onSuccessClearPromoStack(shopIndex)
                }
            }
        }

        Scenario("clear promo fire and forget error") {

            val response = ClearCacheAutoApplyStackResponse().apply {
                successData = SuccessData().apply {
                    success = false
                }
            }

            Given("error response") {
                every { clearCacheAutoApplyStackUseCase.setParams(any(), any()) } just runs
                every { clearCacheAutoApplyStackUseCase.createObservable(any()) } returns Observable.just(response)
            }

            When("process clear promo") {
                cartListPresenter.processCancelAutoApplyPromoStack(1, arrayListOf("code"), true)
            }

            Then("should just hide progress loading") {
                verify {
                    view.hideProgressLoading()
                }
            }
        }

        Scenario("clear promo fire and forget error with exception") {

            Given("error response") {
                every { clearCacheAutoApplyStackUseCase.setParams(any(), any()) } just runs
                every { clearCacheAutoApplyStackUseCase.createObservable(any()) } returns Observable.error(IllegalStateException())
            }

            When("process clear promo") {
                cartListPresenter.processCancelAutoApplyPromoStack(1, arrayListOf("code"), true)
            }

            Then("should only hide progress loading") {
                verify {
                    view.hideProgressLoading()
                }
            }
        }

        Scenario("clear promo after clash success") {

            val response = ClearCacheAutoApplyStackResponse().apply {
                successData = SuccessData().apply {
                    success = true
                }
            }

            Given("success response") {
                every { clearCacheAutoApplyStackUseCase.setParams(any(), any()) } just runs
                every { clearCacheAutoApplyStackUseCase.createObservable(any()) } returns Observable.just(response)
            }

            When("process clear promo") {
                cartListPresenter.processCancelAutoApplyPromoStackAfterClash(PromoStackingData(), arrayListOf("code"), arrayListOf(), "")
            }

            Then("should render success") {
                verify {
                    view.onSuccessClearPromoStackAfterClash()
                }
            }
        }

        Scenario("clear promo after clash error") {

            val response = ClearCacheAutoApplyStackResponse().apply {
                successData = SuccessData().apply {
                    success = false
                }
            }

            Given("success response") {
                every { clearCacheAutoApplyStackUseCase.setParams(any(), any()) } just runs
                every { clearCacheAutoApplyStackUseCase.createObservable(any()) } returns Observable.just(response)
            }

            When("process clear promo") {
                cartListPresenter.processCancelAutoApplyPromoStackAfterClash(PromoStackingData(), arrayListOf("code"), arrayListOf(), "")
            }

            Then("should render error") {
                verify {
                    view.onFailedClearPromoStack(false)
                }
            }
        }

        Scenario("clear promo after clash error with exception") {

            Given("success response") {
                every { clearCacheAutoApplyStackUseCase.setParams(any(), any()) } just runs
                every { clearCacheAutoApplyStackUseCase.createObservable(any()) } returns Observable.error(IllegalStateException())
            }

            When("process clear promo") {
                cartListPresenter.processCancelAutoApplyPromoStackAfterClash(PromoStackingData(), arrayListOf("code"), arrayListOf(), "")
            }

            Then("should render error") {
                verify {
                    view.onFailedClearPromoStack(false)
                }
            }
        }

    }

})