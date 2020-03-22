package com.tokopedia.purchase_platform.features.express_checkout.view.variant

import android.content.Context
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.purchase_platform.features.express_checkout.view.variant.uimodel.FragmentUiModel
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ProductData
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ServiceData
import com.tokopedia.common.payment.model.PaymentPassData
import com.tokopedia.logisticcart.shipping.model.ShippingParam
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel
import com.tokopedia.purchase_platform.features.express_checkout.domain.model.atc.AtcResponseModel
import com.tokopedia.purchase_platform.common.data.model.request.atc.AtcRequestParam

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

interface CheckoutVariantContract {

    interface View : CustomerView {
        fun showLoading()

        fun hideLoading()

        fun showLoadingDialog()

        fun hideLoadingDialog()

        fun onNeedToValidateButtonBuyVisibility()

        fun updateFragmentViewModel(atcResponseModel: AtcResponseModel)

        fun showData(viewModels: ArrayList<Visitable<*>>)

        fun showBottomSheetError(title: String, message: String, action: String, enableRetry: Boolean)

        fun showErrorCourier(message: String)

        fun showErrorNotAvailable(message: String)

        fun showErrorPayment(message: String)

        fun showErrorAPI(retryAction: String)

        fun showErrorPinpoint()

        fun showToasterError(message: String?)

        fun showDurationOptions()

        fun showDurationOptions(latitude: String, longitude: String)

        fun finishWithError(messages: String)

        fun setShippingDurationError(message: String)

        fun setShippingCourierError(message: String)

        fun updateShippingData(productData: ProductData, serviceData: ServiceData, shippingCourierUiModels: MutableList<ShippingCourierUiModel>?)

        fun navigateAtcToOcs()

        fun navigateAtcToNcf()

        fun navigateCheckoutToOcs()

        fun navigateCheckoutToPayment(paymentPassData: PaymentPassData)

        fun navigateCheckoutToThankYouPage(appLink: String)

        fun generateFingerprintPublicKey()

        fun getActivityContext(): Context?
    }

    interface Presenter : CustomerPresenter<View> {

        fun loadExpressCheckoutData(atcRequestParam: AtcRequestParam)

        fun loadShippingRates(price: Long, quantity: Int, selectedServiceId: Int, selectedSpId: Int)

        fun checkoutExpress(fragmentUiModel: FragmentUiModel,
                            trackerAttribution:String?,
                            trackerListName:String?)

        fun checkoutOneClickShipment(fragmentUiModel: FragmentUiModel,
                                     trackerAttribution: String?, trackerListName: String?)

        fun updateAddress(fragmentUiModel: FragmentUiModel, latitude: String, longitude: String)

        fun setAtcResponseModel(atcResponseModel: AtcResponseModel)

        fun prepareViewModel(productData: ProductData?)

        fun getShippingParam(quantity: Int, price: Long): ShippingParam

        fun hitOldCheckout(fragmentUiModel: FragmentUiModel)
    }

}