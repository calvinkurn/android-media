package com.tokopedia.topads.dashboard.view.adapter.negkeyword.viewmodel

import com.tokopedia.topads.dashboard.data.model.KeywordsResponse
import com.tokopedia.topads.dashboard.view.adapter.negkeyword.NegKeywordAdapterTypeFactory

class NegKeywordItemModel(val result: KeywordsResponse.GetTopadsDashboardKeywords.DataItem) : NegKeywordModel() {
    var isChecked: Boolean = false
    override fun type(typesFactory: NegKeywordAdapterTypeFactory): Int {
        return typesFactory.type(this)
    }

}