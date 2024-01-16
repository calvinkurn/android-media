package com.tokopedia.targetedticker.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.targetedticker.ui.TargetedTickerViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by irpan on 09/10/23.
 */
@Module
abstract class TargetedTickerViewModelModule {
    @ActivityScope
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @ActivityScope
    @Binds
    @IntoMap
    @ViewModelKey(TargetedTickerViewModel::class)
    abstract fun provideTargetedTickerViewModel(viewModel: TargetedTickerViewModel): ViewModel
}

