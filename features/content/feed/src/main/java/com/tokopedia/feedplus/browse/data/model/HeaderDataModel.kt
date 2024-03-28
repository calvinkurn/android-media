package com.tokopedia.feedplus.browse.data.model

import com.tokopedia.content.common.model.FeedXHeaderBrowse

class HeaderDataModel(
    val title: String,
    val isShowSearchBar: Boolean,
    val searchBarPlaceholder: String,
    val applink: String
) {
    operator fun component1(): String = searchBarPlaceholder
    operator fun component2(): String = applink

    companion object {
        fun create(data: FeedXHeaderBrowse): HeaderDataModel {
            return HeaderDataModel(data.title, data.searchBar.isActive, data.searchBar.placeholder, data.applink)
        }

        val DEFAULT = HeaderDataModel("", false, "", "")
    }
}
