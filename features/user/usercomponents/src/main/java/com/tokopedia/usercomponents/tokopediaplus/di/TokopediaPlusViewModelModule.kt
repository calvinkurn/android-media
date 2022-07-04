package com.tokopedia.usercomponents.tokopediaplus.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.usercomponents.tokopediaplus.ui.TokopediaPlusViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class TokopediaPlusViewModelModule {

    @ActivityScope
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @ActivityScope
    @Binds
    @IntoMap
    @ViewModelKey(TokopediaPlusViewModel::class)
    abstract fun provideTokopediaViewModel(viewModel: TokopediaPlusViewModel): ViewModel
}