package com.tokopedia.tokofood.feature.search.container.presentation.listener

interface InitialStateViewUpdateListener {
    fun showInitialStateView()
    fun setKeywordSearchBarView(keyword: String)
    fun hideKeyboard()
}
