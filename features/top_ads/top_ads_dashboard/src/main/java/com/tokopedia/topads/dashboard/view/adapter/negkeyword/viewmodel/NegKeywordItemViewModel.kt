package com.tokopedia.topads.dashboard.view.adapter.negkeyword.viewmodel

import com.tokopedia.topads.dashboard.data.model.KeywordsResponse
import com.tokopedia.topads.dashboard.view.adapter.negkeyword.NegKeywordAdapterTypeFactory

class NegKeywordItemViewModel(val result: KeywordsResponse.GetTopadsDashboardKeywords.DataItem): NegKeywordViewModel() {
    var isChecked: Boolean = false
    override fun type(typesFactory: NegKeywordAdapterTypeFactory): Int {
        return typesFactory.type(this)
    }

}