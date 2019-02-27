package com.tokopedia.normalcheckout.view

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

interface NormalCheckoutContract {

    interface View : CustomerView {

        fun onNeedToValidateButtonBuyVisibility()

        fun updateFragmentViewModel(atcResponseModel: AtcResponseModel)

        fun showData(viewModels: ArrayList<Visitable<*>>)

        fun showBottomSheetError(title: String, message: String, action: String, enableRetry: Boolean)

        fun showErrorNotAvailable(message: String)

        fun showToasterError(message: String?)

        fun navigateCheckoutToPayment(paymentPassData: PaymentPassData)

        fun navigateCheckoutToThankYouPage(appLink: String)

        fun getAddToCartObservable(addToCartRequest: AddToCartRequest): Observable<AddToCartResult>

    }

    interface Presenter : CustomerPresenter<View> {

        fun loadData(fragmentViewModel: FragmentViewModel)

    }

}