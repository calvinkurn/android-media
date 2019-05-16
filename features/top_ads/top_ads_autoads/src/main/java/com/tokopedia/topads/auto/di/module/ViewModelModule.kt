package com.tokopedia.topads.auto.di.module

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.topads.auto.data.network.datasource.AutoAdsNetworkDataSource
import com.tokopedia.topads.auto.data.repository.AutoTopAdsRepositoy
import com.tokopedia.topads.auto.di.AutoAdsScope
import com.tokopedia.topads.auto.view.fragment.budget.DailyBudgetViewModel
import com.tokopedia.topads.auto.view.fragment.budget.DailyBudgetViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

/**
 * Author errysuprayogi on 20,May,2019
 */
@Module
@AutoAdsScope
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(DailyBudgetViewModel::class)
    internal abstract fun provideDailyBudgetViewModel(viewModel: DailyBudgetViewModel): ViewModel

}