package com.tokopedia.digital.newcart.presentation.contract

interface DigitalCartMyBillsContract{
    interface View : DigitalBaseContract.View{
        fun isSubscriptionChecked() : Boolean

        fun renderMyBillsView(headerTitle: String, description: String, checked: Boolean)

        fun updateCheckoutButtonText(buttonTitle: String)
    }

    interface Presenter :  DigitalBaseContract.Presenter<View>{
        fun onMyBillsViewCreated()
    }
}