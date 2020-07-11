package com.tokopedia.cart.view.presenter

import com.tokopedia.atc_common.domain.model.response.atcexternal.AddToCartExternalModel
import com.tokopedia.atc_common.domain.usecase.AddToCartExternalUseCase
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.atc_common.domain.usecase.UpdateCartCounterUseCase
import com.tokopedia.cart.domain.usecase.*
import com.tokopedia.cart.view.CartListPresenter
import com.tokopedia.cart.view.ICartListView
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.feature.insurance.usecase.GetInsuranceCartUseCase
import com.tokopedia.purchase_platform.common.feature.insurance.usecase.RemoveInsuranceProductUsecase
import com.tokopedia.purchase_platform.common.feature.insurance.usecase.UpdateInsuranceProductDataUsecase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.ValidateUsePromoRevampUseCase
import com.tokopedia.purchase_platform.common.schedulers.TestSchedulers
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
import rx.subscriptions.CompositeSubscription

/**
 * Created by Irfan Khoirul on 2020-01-07.
 */

object CartListPresenterAddToCartExternalTest : Spek({

    val getCartListSimplifiedUseCase: GetCartListSimplifiedUseCase = mockk()
    val deleteCartListUseCase: DeleteCartUseCase = mockk()
    val updateCartUseCase: UpdateCartUseCase = mockk()
    val updateCartAndValidateUseUseCase: UpdateCartAndValidateUseUseCase = mockk()
    val validateUsePromoRevampUseCase: ValidateUsePromoRevampUseCase = mockk()
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
    val addToCartExternalUseCase: AddToCartExternalUseCase = mockk()
    val getInsuranceCartUseCase: GetInsuranceCartUseCase = mockk()
    val removeInsuranceProductUsecase: RemoveInsuranceProductUsecase = mockk()
    val updateInsuranceProductDataUsecase: UpdateInsuranceProductDataUsecase = mockk()
    val seamlessLoginUsecase: SeamlessLoginUsecase = mockk()
    val updateCartCounterUseCase: UpdateCartCounterUseCase = mockk()
    val view: ICartListView = mockk(relaxed = true)

    Feature("add to cart") {

        val cartListPresenter by memoized {
            CartListPresenter(
                    getCartListSimplifiedUseCase, deleteCartListUseCase, updateCartUseCase,
                    compositeSubscription, addWishListUseCase, removeWishListUseCase,
                    updateAndReloadCartUseCase, userSessionInterface, clearCacheAutoApplyStackUseCase,
                    getRecentViewUseCase, getWishlistUseCase, getRecommendationUseCase,
                    addToCartUseCase, addToCartExternalUseCase, getInsuranceCartUseCase,
                    removeInsuranceProductUsecase, updateInsuranceProductDataUsecase, seamlessLoginUsecase,
                    updateCartCounterUseCase, updateCartAndValidateUseUseCase, validateUsePromoRevampUseCase,
                    TestSchedulers
            )
        }

        beforeEachTest {
            cartListPresenter.attachView(view)
        }

        Scenario("success add to cart") {

            val addToCartExternalModel = AddToCartExternalModel().apply {
                success = 1
                message = arrayListOf<String>().apply {
                    add("Success message")
                }
            }

            Given("add to cart data") {
                every { addToCartExternalUseCase.createObservable(any()) } returns Observable.just(addToCartExternalModel)
            }

            When("process to add to cart") {
                cartListPresenter.processAddToCartExternal(1)
            }

            Then("should render success") {
                verifyOrder {
                    view.hideProgressLoading()
                    view.showToastMessageGreen(addToCartExternalModel.message[0])
                    view.refreshCart()
                }
            }
        }

        Scenario("failed add to cart") {

            val errorMessage = "Error message"
            val exception = MessageErrorException(errorMessage)

            Given("add to cart data") {
                every { addToCartExternalUseCase.createObservable(any()) } returns Observable.error(exception)
            }

            When("process to update cart data") {
                cartListPresenter.processAddToCartExternal(1)
            }

            Then("should show error") {
                verifyOrder {
                    view.hideProgressLoading()
                    view.showToastMessageRed(exception)
                    view.refreshCart()
                }
            }
        }

    }

})