package com.tokopedia.topads.edit.view.adapter.keyword.viewmodel

import com.tokopedia.topads.common.data.response.ResponseKeywordSuggestion
import com.tokopedia.topads.edit.view.adapter.keyword.KeywordListAdapterTypeFactory

class KeywordItemViewModel(var data: ResponseKeywordSuggestion.Result.TopAdsGetKeywordSuggestion.Data) : KeywordViewModel() {

    var isChecked : Boolean = false
    
    override fun type(typesFactory: KeywordListAdapterTypeFactory): Int {
        return typesFactory.type(this)
    }
}