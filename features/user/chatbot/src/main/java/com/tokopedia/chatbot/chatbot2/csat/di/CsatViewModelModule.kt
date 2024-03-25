package com.tokopedia.chatbot.chatbot2.csat.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.chatbot.chatbot2.csat.view.CsatViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class CsatViewModelModule {
    @Binds
    @ActivityScope
    internal abstract fun bindViewModelFactory(
        viewModelFactory: ViewModelFactory
    ): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(CsatViewModel::class)
    internal abstract fun bindCsatViewModel(
        viewModel: CsatViewModel
    ): ViewModel
}
