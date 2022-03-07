package com.tokopedia.common.topupbills.view.model

class TopupBillsAutoCompleteEmptyDataView: TopupBillsAutoComplete {
    override fun getViewType(): TopupBillsAutoCompleteView {
        return TopupBillsAutoCompleteView.EMPTY_STATE
    }
}