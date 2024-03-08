package com.tokopedia.common.topupbills.view.model

class TopupBillsAutoCompleteContactModel(
    val name: String,
    val phoneNumber: String,
    val token: String = "",
    val isFavoriteNumber: Boolean = true
) : TopupBillsAutoComplete {

    override fun getViewType(): TopupBillsAutoCompleteView {
        return TopupBillsAutoCompleteView.CONTACT
    }
}
