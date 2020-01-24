package com.tokopedia.purchase_platform.features.cart.view.presenter

import androidx.fragment.app.FragmentActivity
import com.tokopedia.abstraction.R
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.promocheckout.common.domain.CheckPromoStackingCodeUseCase
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.promocheckout.common.domain.model.clearpromo.ClearCacheAutoApplyStackResponse
import com.tokopedia.promocheckout.common.domain.model.clearpromo.SuccessData
import com.tokopedia.promocheckout.common.view.model.PromoStackingData
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
import io.mockk.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Observable
import rx.Subscriber
import rx.subscriptions.CompositeSubscription
import java.lang.IllegalStateException

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

    Feature("clear promo test") {

        val cartListPresenter by memoized {
            CartListPresenter(
                    getCartListSimplifiedUseCase, deleteCartListUseCase, updateCartUseCase,
                    checkPromoStackingCodeUseCase, compositeSubscription, addWishListUseCase,
                    removeWishListUseCase, updateAndReloadCartUseCase, userSessionInterface,
                    clearCacheAutoApplyStackUseCase, getRecentViewUseCase, getWishlistUseCase,
                    getRecommendationUseCase, addToCartUseCase, getInsuranceCartUseCase,
                    removeInsuranceProductUsecase, updateInsuranceProductDataUsecase,
                    seamlessLoginUsecase, TestSchedulers
            )
        }
        val emptyCartListData = CartListData()

        Scenario("clear promo success") {

            val view: ICartListView = mockk(relaxed = true)

            Given("success response") {
                val response = ClearCacheAutoApplyStackResponse().apply {
                    successData = SuccessData().apply {
                        success = true
                    }
                }
                every { clearCacheAutoApplyStackUseCase.setParams(any(), any()) } just runs
                every { clearCacheAutoApplyStackUseCase.createObservable(any()) } returns Observable.just(response)
            }

            Given("attach view") {
                cartListPresenter.attachView(view)
            }

            When("process clear promo") {
                cartListPresenter.processCancelAutoApplyPromoStack(1, arrayListOf("code"), false)
            }

            Then("should render success") {
                verify {
                    view.hideProgressLoading()
                    view.onSuccessClearPromoStack(any())
                }
            }
        }

        Scenario("clear promo error") {

            val view: ICartListView = mockk(relaxed = true)

            Given("error response") {
                val response = ClearCacheAutoApplyStackResponse().apply {
                    successData = SuccessData().apply {
                        success = false
                    }
                }
                every { clearCacheAutoApplyStackUseCase.setParams(any(), any()) } just runs
                every { clearCacheAutoApplyStackUseCase.createObservable(any()) } returns Observable.just(response)
            }

            Given("attach view") {
                cartListPresenter.attachView(view)
            }

            When("process clear promo") {
                cartListPresenter.processCancelAutoApplyPromoStack(1, arrayListOf("code"), false)
            }

            Then("should render error") {
                verify {
                    view.hideProgressLoading()
                    view.onFailedClearPromoStack(any())
                }
            }
        }

        Scenario("clear promo error with exception") {

            val view: ICartListView = mockk(relaxed = true)

            Given("error response") {
                every { clearCacheAutoApplyStackUseCase.setParams(any(), any()) } just runs
                every { clearCacheAutoApplyStackUseCase.createObservable(any()) } returns Observable.error(IllegalStateException())
            }

            Given("attach view") {
                cartListPresenter.attachView(view)
            }

            When("process clear promo") {
                cartListPresenter.processCancelAutoApplyPromoStack(1, arrayListOf("code"), false)
            }

            Then("should render error") {
                verify {
                    view.hideProgressLoading()
                    view.onFailedClearPromoStack(any())
                }
            }
        }

        Scenario("clear promo fire and forget success") {

            val view: ICartListView = mockk(relaxed = true)

            Given("success response") {
                val response = ClearCacheAutoApplyStackResponse().apply {
                    successData = SuccessData().apply {
                        success = true
                    }
                }
                every { clearCacheAutoApplyStackUseCase.setParams(any(), any()) } just runs
                every { clearCacheAutoApplyStackUseCase.createObservable(any()) } returns Observable.just(response)
            }

            Given("attach view") {
                cartListPresenter.attachView(view)
            }

            When("process clear promo") {
                cartListPresenter.processCancelAutoApplyPromoStack(1, arrayListOf("code"), true)
            }

            Then("should render success") {
                verify {
                    view.hideProgressLoading()
                    view.onSuccessClearPromoStack(any())
                }
            }
        }

        Scenario("clear promo fire and forget error") {

            val view: ICartListView = mockk(relaxed = true)

            Given("error response") {
                val response = ClearCacheAutoApplyStackResponse().apply {
                    successData = SuccessData().apply {
                        success = false
                    }
                }
                every { clearCacheAutoApplyStackUseCase.setParams(any(), any()) } just runs
                every { clearCacheAutoApplyStackUseCase.createObservable(any()) } returns Observable.just(response)
            }

            Given("attach view") {
                cartListPresenter.attachView(view)
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

            val view: ICartListView = mockk(relaxed = true)

            Given("error response") {
                every { clearCacheAutoApplyStackUseCase.setParams(any(), any()) } just runs
                every { clearCacheAutoApplyStackUseCase.createObservable(any()) } returns Observable.error(IllegalStateException())
            }

            Given("attach view") {
                cartListPresenter.attachView(view)
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

            val view: ICartListView = mockk(relaxed = true)

            Given("success response") {
                val response = ClearCacheAutoApplyStackResponse().apply {
                    successData = SuccessData().apply {
                        success = true
                    }
                }
                every { clearCacheAutoApplyStackUseCase.setParams(any(), any()) } just runs
                every { clearCacheAutoApplyStackUseCase.createObservable(any()) } returns Observable.just(response)
            }

            Given("attach view") {
                cartListPresenter.attachView(view)
            }

            When("process clear promo") {
                cartListPresenter.processCancelAutoApplyPromoStackAfterClash(PromoStackingData(), arrayListOf("code"), arrayListOf(), "")
            }

            Then("should render success") {
                verify {
                    view.hideProgressLoading()
                    view.onSuccessClearPromoStackAfterClash()
                }
            }
        }

        Scenario("clear promo after clash error") {

            val view: ICartListView = mockk(relaxed = true)

            Given("success response") {
                val response = ClearCacheAutoApplyStackResponse().apply {
                    successData = SuccessData().apply {
                        success = false
                    }
                }
                every { clearCacheAutoApplyStackUseCase.setParams(any(), any()) } just runs
                every { clearCacheAutoApplyStackUseCase.createObservable(any()) } returns Observable.just(response)
            }

            Given("attach view") {
                cartListPresenter.attachView(view)
            }

            When("process clear promo") {
                cartListPresenter.processCancelAutoApplyPromoStackAfterClash(PromoStackingData(), arrayListOf("code"), arrayListOf(), "")
            }

            Then("should render error") {
                verify {
                    view.hideProgressLoading()
                    view.onFailedClearPromoStack(any())
                }
            }
        }

        Scenario("clear promo after clash error with exception") {

            val view: ICartListView = mockk(relaxed = true)

            Given("success response") {
                every { clearCacheAutoApplyStackUseCase.setParams(any(), any()) } just runs
                every { clearCacheAutoApplyStackUseCase.createObservable(any()) } returns Observable.error(IllegalStateException())
            }

            Given("attach view") {
                cartListPresenter.attachView(view)
            }

            When("process clear promo") {
                cartListPresenter.processCancelAutoApplyPromoStackAfterClash(PromoStackingData(), arrayListOf("code"), arrayListOf(), "")
            }

            Then("should render error") {
                verify {
                    view.hideProgressLoading()
                    view.onFailedClearPromoStack(any())
                }
            }
        }

    }

})