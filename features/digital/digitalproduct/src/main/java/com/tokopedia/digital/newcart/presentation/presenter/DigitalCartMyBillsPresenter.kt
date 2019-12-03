package com.tokopedia.digital.newcart.presentation.presenter

import com.tokopedia.applink.RouteManager
import com.tokopedia.common_digital.cart.data.entity.requestbody.checkout.RequestBodyCheckout
import com.tokopedia.common_digital.cart.domain.usecase.DigitalAddToCartUseCase
import com.tokopedia.common_digital.cart.domain.usecase.DigitalInstantCheckoutUseCase
import com.tokopedia.common_digital.cart.view.model.cart.CartDigitalInfoData
import com.tokopedia.common_digital.cart.view.model.checkout.CheckoutDataParameter
import com.tokopedia.common_digital.common.RechargeAnalytics
import com.tokopedia.digital.common.analytic.DigitalAnalytics
import com.tokopedia.digital.common.router.DigitalModuleRouter
import com.tokopedia.common_digital.cart.constant.DigitalCartCrossSellingType
import com.tokopedia.common_digital.cart.data.entity.requestbody.checkout.FintechProductCheckout
import com.tokopedia.common_digital.cart.view.model.cart.FintechProduct
import com.tokopedia.digital.newcart.domain.interactor.ICartDigitalInteractor
import com.tokopedia.digital.newcart.domain.usecase.DigitalCheckoutUseCase
import com.tokopedia.digital.newcart.presentation.contract.DigitalCartMyBillsContract
import com.tokopedia.user.session.UserSession
import javax.inject.Inject

class DigitalCartMyBillsPresenter @Inject constructor(digitalAddToCartUseCase: DigitalAddToCartUseCase?,
                                                      digitalAnalytics: DigitalAnalytics?,
                                                      rechargeAnalytics: RechargeAnalytics?,
                                                      digitalModuleRouter: DigitalModuleRouter?,
                                                      cartDigitalInteractor: ICartDigitalInteractor?,
                                                      val userSession: UserSession?,
                                                      digitalCheckoutUseCase: DigitalCheckoutUseCase?,
                                                      digitalInstantCheckoutUseCase: DigitalInstantCheckoutUseCase?) :
        DigitalBaseCartPresenter<DigitalCartMyBillsContract.View>(digitalAddToCartUseCase,
                digitalAnalytics,
                rechargeAnalytics,
                digitalModuleRouter,
                cartDigitalInteractor,
                userSession,
                digitalCheckoutUseCase,
                digitalInstantCheckoutUseCase), DigitalCartMyBillsContract.Presenter {

    override fun onSubcriptionCheckedListener(checked: Boolean) {
        view.cartInfoData.crossSellingConfig?.run {
            view.renderMyBillsDescriptionView(if (checked) bodyContentAfter else bodyContentBefore)
        }
    }

    override fun onMyBillsViewCreated() {
        view.setCheckoutParameter(buildCheckoutData(view.cartInfoData, userSession?.accessToken))
        renderBaseCart(view.cartInfoData)
        renderPostPaidPopUp(view.cartInfoData)
        view.renderCategoryInfo(view.cartInfoData.attributes!!.categoryName)
        view.cartInfoData.crossSellingConfig?.run {
            view.updateCheckoutButtonText(checkoutButtonText)
            view.updateToolbarTitle(headerTitle)

            val description = if (isChecked) bodyContentAfter else bodyContentBefore
            val isSubscribed = view.digitalSubscriptionParams.isSubscribed
            view.renderMyBillsSusbcriptionView(bodyTitle, description, isChecked, isSubscribed)
        }
        view.cartInfoData.attributes?.fintechProduct?.getOrNull(0)?.run {
            view.renderMyBillsEgoldView(info?.title, info?.subtitle, checkBoxDisabled)
        }
    }

    override fun onEgoldCheckedListener(checked: Boolean) {
        view.cartInfoData.attributes?.pricePlain?.let { pricePlain ->
            var totalPrice = pricePlain
            if (checked) {
                val egoldPrice = view.cartInfoData.attributes?.fintechProduct?.getOrNull(0)?.fintechAmount ?: 0
                totalPrice += egoldPrice
            }
            view.renderCheckoutView(totalPrice)
        }
    }

    override fun getRequestBodyCheckout(parameter: CheckoutDataParameter): RequestBodyCheckout {
        val bodyCheckout = super.getRequestBodyCheckout(parameter)
        if (view.cartInfoData.crossSellingType == DigitalCartCrossSellingType.MYBILLS) {
            bodyCheckout.attributes!!.subscribe = view.isSubscriptionChecked()
            if (view.isEgoldChecked()) {
                view.cartInfoData.attributes?.fintechProduct?.getOrNull(0)?.run {
                    bodyCheckout.attributes?.apply {
                        fintechProduct = listOf(FintechProductCheckout(
                                transactionType = transactionType,
                                tierId = tierId,
                                userId = identifier?.userId?.toLongOrNull(),
                                fintechAmount = fintechAmount,
                                fintechPartnerAmount = fintechPartnerAmount
                        ))
                    }
                }
            }
        }
        return bodyCheckout
    }

    override fun renderCrossSellingCart(cartDigitalInfoData: CartDigitalInfoData?) {
        super.renderCrossSellingCart(cartDigitalInfoData)
        if (cartDigitalInfoData?.crossSellingType == DigitalCartCrossSellingType.MYBILLS) {
            view.showMyBillsSubscriptionView()
        } else {
            view.hideMyBillsSubscriptionView()
        }
    }

    override fun onEgoldMoreInfoClicked() {
        view.cartInfoData.attributes?.fintechProduct?.getOrNull(0)?.info?.run {
            view.renderEgoldMoreInfo(title, tooltipText, urlLink)
        }
    }
}
