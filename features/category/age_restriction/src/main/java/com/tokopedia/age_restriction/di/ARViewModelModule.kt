package com.tokopedia.age_restriction.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.age_restriction.viewmodel.ARHomeViewModel
import com.tokopedia.age_restriction.viewmodel.VerifyDOBViewModel
import com.tokopedia.basemvvm.viewmodel.ViewModelKey
import com.tokopedia.basemvvm.viewmodel.ViewModelProviderFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ARViewModelModule {

    @ARScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelProviderFactory: ViewModelProviderFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ARScope
    @ViewModelKey(VerifyDOBViewModel::class)
    internal abstract fun verifyDOBViewModel(viewModel: VerifyDOBViewModel): ViewModel

    @Binds
    @IntoMap
    @ARScope
    @ViewModelKey(ARHomeViewModel::class)
    internal abstract fun arHomeViewModel(viewModel: ARHomeViewModel): ViewModel

}