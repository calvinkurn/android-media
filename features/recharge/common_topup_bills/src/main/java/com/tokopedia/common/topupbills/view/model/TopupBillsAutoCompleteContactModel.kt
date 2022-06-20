package com.tokopedia.common.topupbills.view.model


class TopupBillsAutoCompleteContactModel(
    val name: String,
    val phoneNumber: String,
    val token: String = "",
): TopupBillsAutoComplete {

    override fun getViewType(): TopupBillsAutoCompleteView {
        return TopupBillsAutoCompleteView.CONTACT
    }
}