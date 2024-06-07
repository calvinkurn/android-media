package com.tokopedia.feedplus.browse.data.model

import com.tokopedia.content.common.model.FeedXHeaderBrowse
import com.tokopedia.content.common.model.FeedXHeaderDetail

data class HeaderDetailModel(
    val title: String,
    val isShowSearchBar: Boolean,
    val searchBarPlaceholder: String,
    val applink: String
) {
    companion object {
        fun create(data: FeedXHeaderBrowse): HeaderDetailModel {
            return HeaderDetailModel(data.title, data.searchBar.isActive, data.searchBar.placeholder, data.searchBar.applink)
        }

        fun create(data: FeedXHeaderDetail): HeaderDetailModel {
            return HeaderDetailModel(data.title, data.searchBar.isActive, data.searchBar.placeholder, data.searchBar.applink)
        }

        val DEFAULT = HeaderDetailModel("", false, "", "")
    }
}
