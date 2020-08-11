package com.tokopedia.topads.edit.view.adapter.keyword.viewmodel

import com.tokopedia.topads.edit.view.adapter.keyword.KeywordListAdapterTypeFactory

class KeywordGroupViewModel(var title: String = "") : KeywordViewModel() {

    override fun type(typesFactory: KeywordListAdapterTypeFactory): Int {
        return typesFactory.type(this)
    }
}