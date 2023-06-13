package com.tokopedia.product.manage.feature.suspend.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.product.manage.feature.suspend.view.viewmodel.SuspendReasonViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class SuspendReasonViewModelModule {

    @SuspendReasonScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SuspendReasonViewModel::class)
    internal abstract fun suspendReasonViewModel(viewModel: SuspendReasonViewModel): ViewModel
}
