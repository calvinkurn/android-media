package com.tokopedia.digital.newcart.presentation.presenter

import com.tokopedia.common_digital.cart.data.entity.requestbody.checkout.RequestBodyCheckout
import com.tokopedia.common_digital.cart.domain.usecase.DigitalAddToCartUseCase
import com.tokopedia.common_digital.cart.domain.usecase.DigitalInstantCheckoutUseCase
import com.tokopedia.common_digital.cart.view.model.checkout.CheckoutDataParameter
import com.tokopedia.digital.R
import com.tokopedia.digital.cart.data.cache.DigitalPostPaidLocalCache
import com.tokopedia.digital.cart.domain.interactor.ICartDigitalInteractor
import com.tokopedia.digital.cart.domain.usecase.DigitalCheckoutUseCase
import com.tokopedia.digital.common.router.DigitalModuleRouter
import com.tokopedia.digital.common.util.DigitalAnalytics
import com.tokopedia.digital.newcart.presentation.contract.DigitalCartMyBillsContract
import com.tokopedia.user.session.UserSession
import javax.inject.Inject

class DigitalCartMyBillsPresenter @Inject constructor(digitalAddToCartUseCase: DigitalAddToCartUseCase?,
                                                      digitalAnalytics: DigitalAnalytics?,
                                                      digitalModuleRouter: DigitalModuleRouter?,
                                                      cartDigitalInteractor: ICartDigitalInteractor?,
                                                      val userSession: UserSession?,
                                                      digitalCheckoutUseCase: DigitalCheckoutUseCase?,
                                                      digitalInstantCheckoutUseCase: DigitalInstantCheckoutUseCase?,
                                                      digitalPostPaidLocalCache: DigitalPostPaidLocalCache?) :
        DigitalBaseCartPresenter<DigitalCartMyBillsContract.View>(digitalAddToCartUseCase,
                digitalAnalytics,
                digitalModuleRouter,
                cartDigitalInteractor,
                userSession,
                digitalCheckoutUseCase,
                digitalInstantCheckoutUseCase,
                digitalPostPaidLocalCache), DigitalCartMyBillsContract.Presenter {

    override fun onMyBillsViewCreated() {
        view.setCheckoutParameter(buildCheckoutData(view.cartInfoData, userSession?.accessToken))
        renderBaseCart(view.cartInfoData)
        view.renderCategoryInfo(view.cartInfoData.attributes.categoryName)
        if (view.cartInfoData.crossSellingConfig != null) {
            view.updateCheckoutButtonText(view.cartInfoData.crossSellingConfig.checkoutButtonText)
        }
        view.renderMyBillsView(
                view.cartInfoData.crossSellingConfig.headerTitle,
                view.cartInfoData.crossSellingConfig.bodyContentBefore,
                view.cartInfoData.crossSellingConfig.isChecked
                )
    }

    override fun getRequestBodyCheckout(parameter: CheckoutDataParameter) : RequestBodyCheckout {
        val bodyCheckout = super.getRequestBodyCheckout(parameter)
        bodyCheckout.attributes.setSubscribe(view.isSubscriptionChecked())
        return bodyCheckout
    }
}
