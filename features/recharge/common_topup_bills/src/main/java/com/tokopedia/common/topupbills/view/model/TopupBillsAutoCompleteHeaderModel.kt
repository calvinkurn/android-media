package com.tokopedia.common.topupbills.view.model

class TopupBillsAutoCompleteHeaderModel(
    val text: String
): TopupBillsAutoComplete {
    override fun getViewType(): TopupBillsAutoCompleteView {
        return TopupBillsAutoCompleteView.HEADER
    }
}
