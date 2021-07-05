package com.tokopedia.topads.dashboard.view.adapter.keyword.viewmodel

import com.tokopedia.topads.dashboard.data.model.KeywordsResponse
import com.tokopedia.topads.dashboard.view.adapter.keyword.KeywordAdapterTypeFactory

class KeywordItemModel(val result: KeywordsResponse.GetTopadsDashboardKeywords.DataItem) : KeywordModel() {
    var isChecked = false
    var isChanged = false
    var changedValue = false

    override fun type(typesFactory: KeywordAdapterTypeFactory): Int {
        return typesFactory.type(this)
    }
}