package com.tokopedia.topchat.chatsearch.view.adapter

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.topchat.chatsearch.data.SearchResult

interface ChatSearchTypeFactory: AdapterTypeFactory {
    fun type(searchResult: SearchResult): Int
}