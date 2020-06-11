package com.tokopedia.topchat.chatsearch.view.adapter

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.topchat.chatsearch.data.RecentSearch
import com.tokopedia.topchat.chatsearch.data.SearchResult
import com.tokopedia.topchat.chatsearch.view.uimodel.ContactLoadMoreUiModel

interface ChatSearchTypeFactory : AdapterTypeFactory {
    fun type(searchResult: SearchResult): Int
    fun type(recentSearch: RecentSearch): Int
    fun type(contactLoadMoreUiModel: ContactLoadMoreUiModel): Int
}