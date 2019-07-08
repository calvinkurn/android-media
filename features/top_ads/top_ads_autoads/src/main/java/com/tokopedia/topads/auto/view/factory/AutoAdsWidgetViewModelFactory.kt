package com.tokopedia.topads.auto.view.factory

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.topads.auto.view.viewmodel.AutoAdsWidgetViewModel
import com.tokopedia.topads.auto.view.viewmodel.DailyBudgetViewModel
import com.tokopedia.topads.auto.view.viewmodel.TopAdsInfoViewModel
import kotlinx.coroutines.experimental.CoroutineDispatcher
import javax.inject.Inject

/**
 * Author errysuprayogi on 16,May,2019
 */
class AutoAdsWidgetViewModelFactory @Inject constructor(
        private val dispatcher: CoroutineDispatcher,
        private val repository: GraphqlRepository,
        private val query: Map<String, String>
): ViewModelProvider.NewInstanceFactory() {


    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AutoAdsWidgetViewModel(dispatcher, repository, query) as T
    }
}