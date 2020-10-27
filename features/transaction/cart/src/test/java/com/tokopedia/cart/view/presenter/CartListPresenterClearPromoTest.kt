package com.tokopedia.cart.view.presenter

import com.tokopedia.cart.view.ICartListView
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.promocheckout.common.view.model.clearpromo.ClearPromoUiModel
import com.tokopedia.purchase_platform.common.exception.CartResponseErrorException
import io.mockk.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Observable

object CartListPresenterClearPromoTest : Spek({

    val clearCacheAutoApplyStackUseCase: ClearCacheAutoApplyStackUseCase = mockk()
    val view: ICartListView = mockk(relaxed = true)

    Feature("clear promo action") {

        val cartListPresenter by memoized {
            PresenterProvider.provideCartListPresenter()
        }

        beforeEachTest {
            cartListPresenter.attachView(view)
        }

        Scenario("success clear promo") {

            val clearPromoModel = ClearPromoUiModel()

            Given("clear promo data") {
                every { clearCacheAutoApplyStackUseCase.setParams(any(), any()) } just Runs
                every { clearCacheAutoApplyStackUseCase.createObservable(any()) } returns Observable.just(clearPromoModel)
            }

            When("process clear promo") {
                cartListPresenter.doClearRedPromosBeforeGoToCheckout(ArrayList())
            }

            Then("should navigate to checkout page") {
                verify {
                    view.hideProgressLoading()
                    view.onSuccessClearRedPromosThenGoToCheckout()
                }
            }

        }

        Scenario("failed validate use promo") {

            val exception = CartResponseErrorException("error message")

            Given("clear promo data") {
                every { clearCacheAutoApplyStackUseCase.setParams(any(), any()) } just Runs
                every { clearCacheAutoApplyStackUseCase.createObservable(any()) } returns Observable.error(exception)
            }

            When("process clear promo") {
                cartListPresenter.doClearRedPromosBeforeGoToCheckout(ArrayList())
            }

            Then("should navigate to checkout page") {
                verify {
                    view.hideProgressLoading()
                    view.onSuccessClearRedPromosThenGoToCheckout()
                }
            }
        }

    }

})