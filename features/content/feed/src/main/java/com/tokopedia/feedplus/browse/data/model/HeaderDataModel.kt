package com.tokopedia.feedplus.browse.data.model

import com.tokopedia.content.common.model.FeedXHeaderBrowse

class HeaderDataModel(
    val title: String,
    val isShowSearchBar: Boolean,
    val searchBarPlaceholder: String
) {
    companion object {
        fun create(data: FeedXHeaderBrowse): HeaderDataModel {
            return HeaderDataModel(data.title, data.searchBar.isActive, data.searchBar.placeholder)
        }

        val DEFAULT = HeaderDataModel("", false, "")
    }
}
