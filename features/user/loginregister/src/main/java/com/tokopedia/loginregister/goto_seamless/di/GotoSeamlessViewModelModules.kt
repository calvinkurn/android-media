package com.tokopedia.loginregister.goto_seamless.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.loginregister.goto_seamless.GotoSeamlessLoginViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class GotoSeamlessViewModelModules {
    @Binds
    @ActivityScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(GotoSeamlessLoginViewModel::class)
    internal abstract fun provideGotoSeamlessViewModel(viewModel: GotoSeamlessLoginViewModel): ViewModel
}