package com.tokopedia.search.result.presentation.view.listener

import com.tokopedia.discovery.common.model.SearchParameter

interface SearchNavigationListener {
    fun removeSearchPageLoading()
    fun updateSearchParameter(searchParameter: SearchParameter?)
}
