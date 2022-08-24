package com.tokopedia.common.topupbills.view.model

class TopupBillsAutoCompleteHeaderModel: TopupBillsAutoComplete {
    override fun getViewType(): TopupBillsAutoCompleteView {
        return TopupBillsAutoCompleteView.HEADER
    }
}