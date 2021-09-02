package com.tokopedia.common.topupbills.view.model

interface TopupBillsAutoComplete {
    fun getViewType(): TopupBillsAutoCompleteView
}

enum class TopupBillsAutoCompleteView(val type: Int) {
    EMPTY_STATE(0),
    CONTACT(1),
    HEADER(2)
}