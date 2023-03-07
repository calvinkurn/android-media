package com.tokopedia.tokofood.feature.search.container.presentation.listener

import android.view.View

interface InitialStateViewUpdateListener {
    fun showInitialStateView()
    fun setKeywordSearchBarView(keyword: String)
    fun hideKeyboard()
}