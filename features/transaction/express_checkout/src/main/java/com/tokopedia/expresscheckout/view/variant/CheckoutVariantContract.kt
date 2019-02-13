package com.tokopedia.expresscheckout.view.variant

import android.content.Context
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.expresscheckout.domain.model.atc.AtcResponseModel
import com.tokopedia.expresscheckout.view.variant.viewmodel.FragmentViewModel
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ProductData
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ServiceData
import com.tokopedia.payment.model.PaymentPassData
import com.tokopedia.shipping_recommendation.domain.ShippingParam
import com.tokopedia.shipping_recommendation.domain.shipping.ShippingCourierViewModel
import com.tokopedia.transaction.common.sharedata.AddToCartRequest
import com.tokopedia.transaction.common.sharedata.AddToCartResult
import com.tokopedia.transactiondata.entity.request.CheckoutRequest
import com.tokopedia.transactiondata.entity.shared.checkout.CheckoutData
import com.tokopedia.transactiondata.entity.shared.expresscheckout.AtcRequestParam
import com.tokopedia.usecase.RequestParams
import rx.Observable

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

        fun updateShippingData(productData: ProductData, serviceData: ServiceData, shippingCourierViewModels: MutableList<ShippingCourierViewModel>?)

        fun navigateAtcToOcs()

        fun navigateAtcToNcf()

        fun navigateCheckoutToOcs()

        fun navigateCheckoutToPayment(paymentPassData: PaymentPassData)

        fun navigateCheckoutToThankYouPage(appLink: String)

        fun getAddToCartObservable(addToCartRequest: AddToCartRequest): Observable<AddToCartResult>

        fun getCheckoutObservable(checkoutRequest: CheckoutRequest): Observable<CheckoutData>

        fun getEditAddressObservable(requestParams: RequestParams): Observable<String>

        fun getActivityContext(): Context?
    }

    interface Presenter : CustomerPresenter<View> {

        fun loadExpressCheckoutData(atcRequestParam: AtcRequestParam)

        fun loadShippingRates(price: Long, quantity: Int, selectedServiceId: Int, selectedSpId: Int)

        fun checkoutExpress(fragmentViewModel: FragmentViewModel)

        fun checkoutOneClickShipment(fragmentViewModel: FragmentViewModel)

        fun updateAddress(fragmentViewModel: FragmentViewModel, latitude: String, longitude: String)

        fun setAtcResponseModel(atcResponseModel: AtcResponseModel)

        fun prepareViewModel(productData: ProductData?)

        fun getShippingParam(quantity: Int, price: Long): ShippingParam

        fun hitOldCheckout(fragmentViewModel: FragmentViewModel)
    }

}