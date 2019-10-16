package com.tokopedia.digital.newcart.presentation.presenter

import com.tokopedia.common_digital.cart.data.entity.requestbody.atc.Field
import com.tokopedia.common_digital.cart.data.entity.requestbody.atc.RequestBodyAtcDigital
import com.tokopedia.common_digital.cart.data.entity.requestbody.checkout.RequestBodyCheckout
import com.tokopedia.common_digital.cart.domain.usecase.DigitalAddToCartUseCase
import com.tokopedia.common_digital.cart.domain.usecase.DigitalInstantCheckoutUseCase
import com.tokopedia.common_digital.cart.view.model.cart.CartDigitalInfoData
import com.tokopedia.common_digital.cart.view.model.checkout.CheckoutDataParameter
import com.tokopedia.common_digital.common.RechargeAnalytics
import com.tokopedia.digital.common.analytic.DigitalAnalytics
import com.tokopedia.common_digital.common.usecase.RechargePushEventRecommendationUseCase
import com.tokopedia.digital.common.router.DigitalModuleRouter
import com.tokopedia.digital.newcart.constants.DigitalCartCrossSellingType
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

        if (checked) {
            view.renderMyBillsDescriptionView(view.cartInfoData.crossSellingConfig!!.bodyContentAfter)
        } else {
            view.renderMyBillsDescriptionView(view.cartInfoData.crossSellingConfig!!.bodyContentBefore)
        }
    }

    override fun onMyBillsViewCreated() {
        view.setCheckoutParameter(buildCheckoutData(view.cartInfoData, userSession?.accessToken))
        renderBaseCart(view.cartInfoData)
        renderPostPaidPopUp(view.cartInfoData)
        view.renderCategoryInfo(view.cartInfoData.attributes!!.categoryName)
        if (view.cartInfoData.crossSellingConfig != null) {
            view.updateCheckoutButtonText(view.cartInfoData.crossSellingConfig!!.checkoutButtonText)
            view.updateToolbarTitle(view.cartInfoData.crossSellingConfig!!.headerTitle)
        }

        val description = if (view.cartInfoData.crossSellingConfig!!.isChecked) {
            view.cartInfoData!!.crossSellingConfig!!.bodyContentAfter
        } else {
            view.cartInfoData!!.crossSellingConfig!!.bodyContentBefore
        }

        view.renderMyBillsView(
                view.cartInfoData.crossSellingConfig!!.bodyTitle,
                description,
                view.cartInfoData.crossSellingConfig!!.isChecked
        )
    }

    override fun getRequestBodyCheckout(parameter: CheckoutDataParameter): RequestBodyCheckout {
        val bodyCheckout = super.getRequestBodyCheckout(parameter)
        if (view.cartInfoData.crossSellingType == DigitalCartCrossSellingType.MYBILLS) {
            bodyCheckout.attributes!!.subscribe = view.isSubscriptionChecked()
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

    override fun getRequestBodyAtcDigital(): RequestBodyAtcDigital {
        val requestBody = super.getRequestBodyAtcDigital()
        val fieldList = requestBody.attributes?.fields?.toMutableList()
        fieldList?.run {
            view.getShowSubscribePopUp()?.run {
                val field = Field()
                field.name = "show_subscribe_pop_up"
                field.value = this
                add(field)
            }
            view.getAutoSubscribe()?.run {
                val field = Field()
                field.name = "auto_subscribe"
                field.value = this
                add(field)
            }
            requestBody.attributes?.fields = fieldList
        }
        return requestBody
    }
}
