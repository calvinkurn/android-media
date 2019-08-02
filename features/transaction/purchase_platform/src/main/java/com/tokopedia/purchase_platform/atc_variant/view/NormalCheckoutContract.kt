package com.tokopedia.purchase_platform.atc_variant.view

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.common.payment.model.PaymentPassData
import com.tokopedia.purchase_platform.express_checkout.view.variant.viewmodel.FragmentViewModel
import com.tokopedia.purchase_platform.express_checkout.domain.model.atc.AtcResponseModel
import java.util.*

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

    }

    interface Presenter : CustomerPresenter<View> {

        fun loadData(fragmentViewModel: FragmentViewModel)

    }

}