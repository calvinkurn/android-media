package com.tokopedia.cart.view.presenter

import com.tokopedia.cart.view.ICartListView
import com.tokopedia.purchase_platform.common.exception.CartResponseErrorException
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.ValidateUsePromoRevampUseCase
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Observable

object CartListPresenterValidateUseTest : Spek({

    val validateUsePromoRevampUseCase: ValidateUsePromoRevampUseCase = mockk()
    val view: ICartListView = mockk(relaxed = true)

    Feature("validate use action") {

        val cartListPresenter by memoized {
            PresenterProvider.provideCartListPresenter()
        }

        beforeEachTest {
            cartListPresenter.attachView(view)
        }

        Scenario("success validate use") {

            val validateUseModel = ValidateUsePromoRevampUiModel().apply {
                promoUiModel = PromoUiModel()
            }

            Given("validate use promo data") {
                every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(validateUseModel)
            }

            When("process validate use promo data") {
                cartListPresenter.doValidateUse(ValidateUsePromoRequest())
            }

            Then("should update promo state") {
                verify {
                    view.updateListRedPromos(validateUseModel)
                    view.updatePromoCheckoutStickyButton(validateUseModel.promoUiModel)
                }
            }

        }

        Scenario("failed validate use promo") {

            val exception = CartResponseErrorException("error message")

            Given("validate use promo data") {
                every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.error(exception)
            }

            When("process validate use promo data") {
                cartListPresenter.doValidateUse(ValidateUsePromoRequest())
            }

            Then("should set promo button inactive") {
                verify {
                    view.showPromoCheckoutStickyButtonInactive()
                }
            }
        }

    }

})