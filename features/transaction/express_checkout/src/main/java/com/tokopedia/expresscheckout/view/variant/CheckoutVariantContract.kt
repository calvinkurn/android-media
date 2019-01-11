package com.tokopedia.expresscheckout.view.variant

import android.content.Context
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.expresscheckout.domain.model.atc.AtcResponseModel
import com.tokopedia.expresscheckout.view.variant.viewmodel.FragmentViewModel
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ProductData
import com.tokopedia.transaction.common.data.expresscheckout.AtcRequestParam

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

interface CheckoutVariantContract {

    interface View : CustomerView {
        fun showLoading()

        fun hideLoading()

        fun showLoadingDialog()

        fun hideLoadingDialog()

        fun showData(arrayList: ArrayList<Visitable<*>>)

        fun showBottomsheetError(title: String, message: String, action: String)

        fun showToasterError(message: String?)

        fun showGetListError(t: Throwable?)

        fun finishWithError(messages: String)

        fun setShippingError()

        fun updateShippingData(productData: ProductData)

        fun navigateToOcs()

        fun navigateToNcf()

        fun getActivityContext(): Context?
    }

    interface Presenter : CustomerPresenter<View> {

        fun loadExpressCheckoutData(atcRequestParam: AtcRequestParam)

        fun loadShippingRates(atcResponseModel: AtcResponseModel, itemPrice: Int, quantity: Int)

        fun checkout(fragmentViewModel: FragmentViewModel)

        fun setAtcResponseModel(atcResponseModel: AtcResponseModel)

        fun prepareViewModel(productData: ProductData)
    }

}