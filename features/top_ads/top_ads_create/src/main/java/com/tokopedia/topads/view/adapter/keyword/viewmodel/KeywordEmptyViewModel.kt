package com.tokopedia.topads.view.adapter.keyword.viewmodel

import com.tokopedia.topads.view.adapter.keyword.KeywordListAdapterTypeFactory

/**
 * Author errysuprayogi on 12,November,2019
 */
class KeywordEmptyViewModel(var title: String = "") : KeywordViewModel() {

    override fun type(typesFactory: KeywordListAdapterTypeFactory): Int {
        return typesFactory.type(this)
    }
}