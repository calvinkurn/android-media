package com.tokopedia.updateinactivephone.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.updateinactivephone.di.UpdateInactivePhoneScope
import com.tokopedia.updateinactivephone.viewmodel.ChangeInactiveFormRequestViewModel
import com.tokopedia.updateinactivephone.viewmodel.ChangeInactivePhoneViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class UpdateInactivePhoneViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(ChangeInactiveFormRequestViewModel::class)
    abstract fun provideInactiveFormRequestViewModel(viewModel: ChangeInactiveFormRequestViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChangeInactivePhoneViewModel::class)
    abstract fun provideChangeInactivePhoneViewModel(viewModel: ChangeInactivePhoneViewModel): ViewModel

    @Binds
    @UpdateInactivePhoneScope
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
}