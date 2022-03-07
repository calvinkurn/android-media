package com.tokopedia.common.topupbills.view.model


class TopupBillsAutoCompleteContactDataView(
    val name: String,
    val phoneNumber: String
): TopupBillsAutoComplete {

    override fun getViewType(): TopupBillsAutoCompleteView {
        return TopupBillsAutoCompleteView.CONTACT
    }
}