package com.tokopedia.topads.view.adapter.keyword.viewmodel

import com.tokopedia.topads.data.response.ResponseKeywordSuggestion
import com.tokopedia.topads.view.adapter.keyword.KeywordListAdapterTypeFactory

/**
 * Author errysuprayogi on 12,November,2019
 */
class KeywordItemViewModel(var data: ResponseKeywordSuggestion.TopAdsGetKeywordSuggestion.Data) : KeywordViewModel() {

    var isChecked : Boolean = false
    
    override fun type(typesFactory: KeywordListAdapterTypeFactory): Int {
        return typesFactory.type(this)
    }
}