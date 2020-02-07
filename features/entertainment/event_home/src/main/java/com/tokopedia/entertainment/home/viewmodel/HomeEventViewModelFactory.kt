package com.tokopedia.entertainment.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

/**
 * Author errysuprayogi on 06,February,2020
 */

class HomeEventViewModelFactory @Inject constructor(
        private val dispatcher: CoroutineDispatcher,
        private val repository: GraphqlRepository) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeEventViewModel(dispatcher, repository) as T
    }

}