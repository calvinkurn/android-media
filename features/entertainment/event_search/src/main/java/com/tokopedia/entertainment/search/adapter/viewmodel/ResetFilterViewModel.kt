package com.tokopedia.entertainment.search.adapter.viewmodel

import com.tokopedia.entertainment.search.adapter.DetailEventItem
import com.tokopedia.entertainment.search.adapter.factory.DetailTypeFactory

class ResetFilterViewModel: DetailEventItem<DetailTypeFactory> {

    override fun type(typeFactory: DetailTypeFactory): Int {
        return typeFactory.type(this)
    }
}