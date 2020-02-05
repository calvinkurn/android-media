package com.tokopedia.digital.newcart.presentation.contract

import com.tokopedia.common_digital.cart.view.model.cart.FintechProduct

interface DigitalCartMyBillsContract {
    interface View : DigitalBaseContract.View {
        fun isSubscriptionChecked(): Boolean

        fun isEgoldChecked(): Boolean

        fun renderMyBillsSusbcriptionView(headerTitle: String?, description: String?, checked: Boolean, isSubcribed: Boolean)

        fun renderMyBillsEgoldView(data: FintechProduct?)

        fun updateCheckoutButtonText(buttonTitle: String?)

        fun showMyBillsSubscriptionView()

        fun hideMyBillsSubscriptionView()

        fun renderMyBillsDescriptionView(title : String?)

        fun updateToolbarTitle(headerTitle: String?)

        fun renderEgoldMoreInfo(title: String?, tooltip: String?, linkUrl: String?)
    }

    interface Presenter : DigitalBaseContract.Presenter<View> {
        fun onMyBillsViewCreated()

        fun onSubcriptionCheckedListener(checked: Boolean)

        fun onEgoldCheckedListener(checked: Boolean)

        fun onEgoldMoreInfoClicked()
    }
}