package com.tokopedia.topads.auto.view.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.topads.auto.di.AutoAdsDispatcherProvider
import com.tokopedia.topads.auto.view.viewmodel.DailyBudgetViewModel
import com.tokopedia.topads.common.domain.usecase.TopAdsGetDepositUseCase
import kotlinx.coroutines.CoroutineDispatcher
import com.tokopedia.topads.common.domain.interactor.BidInfoUseCase
import javax.inject.Inject

/**
 * Author errysuprayogi on 16,May,2019
 */
class DailyBudgetViewModelFactory @Inject constructor(
        private val context: Context,
        private val dispatcher: AutoAdsDispatcherProvider,
        private val repository: GraphqlRepository,
        private val query: Map<String, String>,
        private val bidInfoUseCase: BidInfoUseCase
        private val topAdsGetShopDepositUseCase: TopAdsGetDepositUseCase
        ): ViewModelProvider.NewInstanceFactory() {


    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DailyBudgetViewModel(context, dispatcher, repository, query, bidInfoUseCase ,topAdsGetShopDepositUseCase) as T
    }
}