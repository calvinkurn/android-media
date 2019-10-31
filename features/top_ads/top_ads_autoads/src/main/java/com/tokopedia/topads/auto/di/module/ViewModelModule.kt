package com.tokopedia.topads.auto.di.module

import androidx.lifecycle.ViewModel
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.topads.auto.di.AutoAdsScope
import com.tokopedia.topads.auto.view.viewmodel.AutoAdsWidgetViewModel
import com.tokopedia.topads.auto.view.viewmodel.DailyBudgetViewModel
import com.tokopedia.topads.auto.view.viewmodel.TopAdsInfoViewModel
import dagger.Binds
import dagger.Module
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

    @Binds
    @IntoMap
    @ViewModelKey(TopAdsInfoViewModel::class)
    internal abstract fun provideAdsShopInfoViewModel(viewModel: TopAdsInfoViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AutoAdsWidgetViewModel::class)
    internal abstract fun provideAutpAdsViewModel(viewModel: AutoAdsWidgetViewModel): ViewModel

}