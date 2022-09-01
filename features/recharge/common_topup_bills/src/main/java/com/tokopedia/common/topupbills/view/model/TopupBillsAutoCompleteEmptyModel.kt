package com.tokopedia.common.topupbills.view.model

class TopupBillsAutoCompleteEmptyModel: TopupBillsAutoComplete {
    override fun getViewType(): TopupBillsAutoCompleteView {
        return TopupBillsAutoCompleteView.EMPTY_STATE
    }
}