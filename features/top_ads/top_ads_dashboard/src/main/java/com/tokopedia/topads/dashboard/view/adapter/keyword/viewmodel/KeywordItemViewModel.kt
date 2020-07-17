package com.tokopedia.topads.dashboard.view.adapter.keyword.viewmodel

import com.tokopedia.topads.dashboard.data.model.KeywordsResponse
import com.tokopedia.topads.dashboard.view.adapter.keyword.KeywordAdapterTypeFactory

class KeywordItemViewModel(val result: KeywordsResponse.GetTopadsDashboardKeywords.DataItem): KeywordViewModel() {
    var isChecked = false

    override fun type(typesFactory: KeywordAdapterTypeFactory): Int {
        return typesFactory.type(this)
    }
}