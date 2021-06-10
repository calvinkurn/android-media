package com.tokopedia.search.result.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers

internal class SearchViewModelFactory(
        private val coroutineDispatcher: CoroutineDispatchers
): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
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