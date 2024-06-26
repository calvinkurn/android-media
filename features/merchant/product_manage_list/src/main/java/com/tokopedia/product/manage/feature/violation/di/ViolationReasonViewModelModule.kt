package com.tokopedia.product.manage.feature.violation.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.product.manage.feature.violation.view.viewmodel.ViolationReasonViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViolationReasonViewModelModule {

    @ViolationReasonScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ViolationReasonViewModel::class)
    internal abstract fun violationReasonViewModel(viewModel: ViolationReasonViewModel): ViewModel
}