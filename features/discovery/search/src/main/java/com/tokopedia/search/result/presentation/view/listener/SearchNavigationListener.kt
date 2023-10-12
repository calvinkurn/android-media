package com.tokopedia.search.result.presentation.view.listener

import com.tokopedia.discovery.common.model.SearchParameter

interface SearchNavigationListener {
    fun updateSearchParameter(searchParameter: SearchParameter?)
    fun updateSearchBarNotification()
}
