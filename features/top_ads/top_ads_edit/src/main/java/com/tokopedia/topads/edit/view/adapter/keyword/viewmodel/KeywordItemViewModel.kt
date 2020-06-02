package com.tokopedia.topads.edit.view.adapter.keyword.viewmodel

import com.tokopedia.topads.common.data.response.KeywordSuggestionResponse
import com.tokopedia.topads.edit.view.adapter.keyword.KeywordListAdapterTypeFactory

class KeywordItemViewModel(var data: KeywordSuggestionResponse.Result.TopAdsGetKeywordSuggestionV3.DataItem.KeywordDataItem) : KeywordViewModel() {

    var isChecked: Boolean = false

    override fun type(typesFactory: KeywordListAdapterTypeFactory): Int {
        return typesFactory.type(this)
    }
}