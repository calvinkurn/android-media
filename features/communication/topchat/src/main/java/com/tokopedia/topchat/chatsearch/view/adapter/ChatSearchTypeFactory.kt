package com.tokopedia.topchat.chatsearch.view.adapter

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.topchat.chatsearch.data.RecentSearch
import com.tokopedia.topchat.chatsearch.view.uimodel.BigDividerUiModel
import com.tokopedia.topchat.chatsearch.view.uimodel.ChatReplyUiModel
import com.tokopedia.topchat.chatsearch.view.uimodel.SearchResultUiModel
import com.tokopedia.topchat.chatsearch.view.uimodel.SearchListHeaderUiModel

interface ChatSearchTypeFactory : AdapterTypeFactory {
    fun type(searchResultUiModel: SearchResultUiModel): Int
    fun type(recentSearch: RecentSearch): Int
    fun type(searchListHeaderUiModel: SearchListHeaderUiModel): Int
    fun type(chatReplyUiModel: ChatReplyUiModel): Int
    fun type(bigDividerUiModel: BigDividerUiModel): Int
}