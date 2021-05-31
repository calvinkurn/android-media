package com.tokopedia.digital.newcart.presentation.presenter

import com.tokopedia.common_digital.common.RechargeAnalytics
import com.tokopedia.digital.common.analytic.DigitalAnalytics
import com.tokopedia.digital.newcart.constant.DigitalCartCrossSellingType
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
        view.cartInfoData.attributes?.run {
            digitalAnalytics.eventClickSubscription(checked, categoryName, operatorName, userSession?.userId ?: "")
        }
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
            if (!headerTitle.isNullOrEmpty()) view.updateToolbarTitle(headerTitle)

            val description = if (isChecked) bodyContentAfter else bodyContentBefore
            val isSubscribed = view.digitalSubscriptionParams.isSubscribed
            view.renderMyBillsSusbcriptionView(bodyTitle, description, isChecked, isSubscribed)
        }
    }

    override fun getRequestBodyCheckout(parameter: CheckoutDataParameter): RequestBodyCheckout {
        val bodyCheckout = super.getRequestBodyCheckout(parameter)
        if (view.cartInfoData.crossSellingType == DigitalCartCrossSellingType.MYBILLS) {
            bodyCheckout.attributes?.let { it.subscribe = view.isSubscriptionChecked() }
        }
        return bodyCheckout
    }

    override fun renderCrossSellingCart(cartDigitalInfoData: CartDigitalInfoData?) {
        super.renderCrossSellingCart(cartDigitalInfoData)
        if (cartDigitalInfoData?.crossSellingType == DigitalCartCrossSellingType.MYBILLS
                || cartDigitalInfoData?.crossSellingType == DigitalCartCrossSellingType.SUBSCRIBED) {
            view.showMyBillsSubscriptionView()
        } else {
            view.hideMyBillsSubscriptionView()
        }
    }
}
