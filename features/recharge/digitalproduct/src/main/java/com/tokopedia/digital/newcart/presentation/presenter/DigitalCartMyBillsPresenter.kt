package com.tokopedia.digital.newcart.presentation.presenter

import com.tokopedia.common_digital.common.RechargeAnalytics
import com.tokopedia.digital.common.analytic.DigitalAnalytics
import com.tokopedia.digital.newcart.constant.DigitalCartCrossSellingType
import com.tokopedia.digital.newcart.data.entity.requestbody.checkout.FintechProductCheckout
import com.tokopedia.digital.newcart.data.entity.requestbody.checkout.RequestBodyCheckout
import com.tokopedia.digital.newcart.domain.interactor.ICartDigitalInteractor
import com.tokopedia.digital.newcart.domain.usecase.DigitalCheckoutUseCase
import com.tokopedia.digital.newcart.presentation.contract.DigitalCartMyBillsContract
import com.tokopedia.digital.newcart.presentation.model.cart.CartDigitalInfoData
import com.tokopedia.digital.newcart.presentation.model.checkout.CheckoutDataParameter
import com.tokopedia.digital.newcart.presentation.usecase.DigitalAddToCartUseCase
import com.tokopedia.digital.newcart.presentation.usecase.DigitalGetCartUseCase
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class DigitalCartMyBillsPresenter @Inject constructor(digitalAddToCartUseCase: DigitalAddToCartUseCase?,
                                                      digitalGetCartUseCase: DigitalGetCartUseCase?,
                                                      digitalAnalytics: DigitalAnalytics?,
                                                      rechargeAnalytics: RechargeAnalytics?,
                                                      cartDigitalInteractor: ICartDigitalInteractor?,
                                                      val userSession: UserSessionInterface?,
                                                      digitalCheckoutUseCase: DigitalCheckoutUseCase?) :
        DigitalBaseCartPresenter<DigitalCartMyBillsContract.View>(digitalAddToCartUseCase,
                digitalGetCartUseCase,
                digitalAnalytics,
                rechargeAnalytics,
                cartDigitalInteractor,
                userSession,
                digitalCheckoutUseCase), DigitalCartMyBillsContract.Presenter {

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
        view.renderMyBillsEgoldView(view.cartInfoData.attributes?.fintechProduct?.getOrNull(0))
    }

    override fun renderBaseCart(cartDigitalInfoData: CartDigitalInfoData?) {
        super.renderBaseCart(cartDigitalInfoData)

        // Update total price based on fintech amount if already shown and checked
        view.updateTotalPriceWithFintechAmount()
    }

    override fun onEgoldCheckedListener(checked: Boolean) {
        updateTotalPriceWithFintechAmount(checked)
    }

    override fun getRequestBodyCheckout(parameter: CheckoutDataParameter): RequestBodyCheckout {
        val bodyCheckout = super.getRequestBodyCheckout(parameter)
        if (view.cartInfoData.crossSellingType == DigitalCartCrossSellingType.MYBILLS) {
            bodyCheckout.attributes?.let { it.subscribe = view.isSubscriptionChecked() }
        }
        if (view.isEgoldChecked()) {
            view.cartInfoData.attributes?.fintechProduct?.getOrNull(0)?.run {
                bodyCheckout.attributes?.apply {
                    var title = info?.let { it.title ?: "" }
                    fintechProduct = listOf(FintechProductCheckout(
                            transactionType = transactionType,
                            tierId = tierId,
                            userId = identifier?.userId?.toLongOrNull(),
                            fintechAmount = fintechAmount,
                            fintechPartnerAmount = fintechPartnerAmount,
                            productName = title
                    ))
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

    override fun updateTotalPriceWithFintechAmount(checked: Boolean) {
        view.cartInfoData.attributes?.pricePlain?.let { pricePlain ->
            var totalPrice = pricePlain
            if (checked) {
                val egoldPrice = view.cartInfoData.attributes?.fintechProduct?.getOrNull(0)?.fintechAmount
                        ?: 0
                totalPrice += egoldPrice
            }
            view.renderCheckoutView(totalPrice)
        }
    }
}
