package com.tokopedia.topads.auto.view.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.topads.auto.di.AutoAdsDispatcherProvider
import com.tokopedia.topads.auto.view.viewmodel.AutoAdsWidgetViewModel
import javax.inject.Inject

/**
 * Author errysuprayogi on 16,May,2019
 */
class AutoAdsWidgetViewModelFactory @Inject constructor(
        private val dispatcher: AutoAdsDispatcherProvider,
        private val repository: GraphqlRepository,
        private val query: Map<String, String>
): ViewModelProvider.NewInstanceFactory() {


    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AutoAdsWidgetViewModel(dispatcher, repository, query) as T
    }
}