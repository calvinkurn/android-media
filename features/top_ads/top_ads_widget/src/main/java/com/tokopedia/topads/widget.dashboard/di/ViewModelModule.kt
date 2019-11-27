package com.tokopedia.topads.widget.dashboard.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.topads.widget.dashboard.viewmodel.DashboardWidgetViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Author errysuprayogi on 25,October,2019
 */
@Module
@DashboardWidgetScope
abstract class ViewModelModule {

    @DashboardWidgetScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @DashboardWidgetScope
    @Binds
    @IntoMap
    @ViewModelKey(DashboardWidgetViewModel::class)
    internal abstract fun provideTopAdsSheetViewModel(viewModel: DashboardWidgetViewModel): ViewModel
}