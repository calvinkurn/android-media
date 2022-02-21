package com.tokopedia.common.topupbills.view.model

class TopupBillsAutoCompleteHeaderDataView: TopupBillsAutoComplete {
    override fun getViewType(): TopupBillsAutoCompleteView {
        return TopupBillsAutoCompleteView.HEADER
    }
}