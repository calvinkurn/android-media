package com.tokopedia.digital.newcart.presentation.contract

interface DigitalCartMyBillsContract {
    interface View : DigitalBaseContract.View {
        fun isSubscriptionChecked(): Boolean

        fun renderMyBillsView(headerTitle: String, description: String, checked: Boolean)

        fun updateCheckoutButtonText(buttonTitle: String)

        fun showMyBillsSubscriptionView()

        fun hideMyBillsSubscriptionView()

        fun renderMyBillsDescriptionView(title : String)

        fun updateToolbarTitle(headerTitle: String?)
    }

    interface Presenter : DigitalBaseContract.Presenter<View> {
        fun onMyBillsViewCreated()

        fun onSubcriptionCheckedListener(checked: Boolean)
    }
}