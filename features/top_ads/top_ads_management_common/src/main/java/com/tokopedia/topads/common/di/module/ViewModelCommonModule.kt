package com.tokopedia.topads.common.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.topads.common.view.AutoAdsWidgetViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by Pika on 18/9/20.
 */


@Module
abstract class ViewModelCommonModule {

    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(AutoAdsWidgetViewModel::class)
    internal abstract fun provideAutoAdsWidgetViewModel(viewModel: AutoAdsWidgetViewModel): ViewModel
}


