package com.tokopedia.expresscheckout.view.variant

import android.content.Context
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.expresscheckout.domain.model.atc.AtcResponseModel
import com.tokopedia.expresscheckout.view.variant.mapper.ViewModelMapper
import com.tokopedia.expresscheckout.view.variant.viewmodel.FragmentViewModel
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ProductData
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ServiceData
import com.tokopedia.shipping_recommendation.domain.ShippingParam
import com.tokopedia.shipping_recommendation.domain.shipping.ShippingCourierViewModel
import com.tokopedia.transaction.common.data.expresscheckout.AtcRequestParam
import com.tokopedia.transaction.common.sharedata.AddToCartRequest
import com.tokopedia.transaction.common.sharedata.AddToCartResult
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

        fun updateFragmentViewModel(atcResponseModel: AtcResponseModel)

        fun showData(viewModels: ArrayList<Visitable<*>>)

        fun showBottomsheetError(title: String, message: String, action: String)

        fun showToasterError(message: String?)

        fun showGetListError(t: Throwable?)

        fun finishWithError(messages: String)

        fun setShippingError()

        fun updateShippingData(productData: ProductData, serviceData: ServiceData, shippingCourierViewModels: MutableList<ShippingCourierViewModel>?)

        fun navigateAtcToOcs()

        fun navigateAtcToNcf()

        fun navigateCheckoutToOcs()

        fun getAddToCartObservable(addToCartRequest: AddToCartRequest): Observable<AddToCartResult>

        fun getActivityContext(): Context?
    }

    interface Presenter : CustomerPresenter<View> {

        fun loadExpressCheckoutData(atcRequestParam: AtcRequestParam)

        fun loadShippingRates(price: Int, quantity: Int, selectedServiceId: Int, isReloadData: Boolean)

        fun checkout(fragmentViewModel: FragmentViewModel)

        fun setAtcResponseModel(atcResponseModel: AtcResponseModel)

        fun prepareViewModel(productData: ProductData)

        fun getShippingParam(quantity: Int, price: Int): ShippingParam
    }

}