package com.tokopedia.topads.view.adapter.keyword.viewmodel

import com.tokopedia.topads.data.response.KeywordDataItem
import com.tokopedia.topads.view.adapter.keyword.KeywordListAdapterTypeFactory

/**
 * Author errysuprayogi on 12,November,2019
 */
class KeywordItemViewModel(var data: KeywordDataItem) : KeywordViewModel() {

    var isChecked: Boolean = false

    override fun type(typesFactory: KeywordListAdapterTypeFactory): Int {
        return typesFactory.type(this)
    }
}