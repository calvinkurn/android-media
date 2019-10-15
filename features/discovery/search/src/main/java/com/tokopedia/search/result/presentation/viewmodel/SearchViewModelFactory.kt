package com.tokopedia.search.result.presentation.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.tokopedia.discovery.common.DispatcherProvider

internal class SearchViewModelFactory(
        private val coroutineDispatcher: DispatcherProvider
): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return createRedirectionViewModel() as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }

    private fun createRedirectionViewModel(): SearchViewModel {
        return SearchViewModel(coroutineDispatcher)
    }
}