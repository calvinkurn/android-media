package com.tokopedia.entertainment.search.adapter.viewmodel

import com.tokopedia.entertainment.search.adapter.SearchEventItem
import com.tokopedia.entertainment.search.adapter.factory.SearchTypeFactory

class FirstTimeModel(val isFirstTime:Boolean = true) : SearchEventItem<SearchTypeFactory>{

    override fun type(typeFactory: SearchTypeFactory): Int {
        return typeFactory.type(this)
    }
}