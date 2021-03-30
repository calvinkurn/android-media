package com.tokopedia.topads.auto.di.module

import androidx.lifecycle.ViewModel
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.topads.auto.view.viewmodel.DailyBudgetViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Author errysuprayogi on 20,May,2019
 */
@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(DailyBudgetViewModel::class)
    internal abstract fun provideDailyBudgetViewModel(viewModel: DailyBudgetViewModel): ViewModel

}