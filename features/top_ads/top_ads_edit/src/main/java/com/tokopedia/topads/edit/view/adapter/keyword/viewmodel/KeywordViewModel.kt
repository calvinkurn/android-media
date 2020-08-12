package com.tokopedia.topads.edit.view.adapter.keyword.viewmodel

import com.tokopedia.topads.edit.view.adapter.keyword.KeywordListAdapterTypeFactory

abstract class KeywordViewModel {
    abstract fun type(typesFactory: KeywordListAdapterTypeFactory): Int
}



