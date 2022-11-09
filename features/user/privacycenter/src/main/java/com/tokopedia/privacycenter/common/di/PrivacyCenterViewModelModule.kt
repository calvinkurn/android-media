package com.tokopedia.privacycenter.common.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.privacycenter.main.PrivacyCenterViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class PrivacyCenterViewModelModule {

    @Binds
    @ActivityScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(PrivacyCenterViewModel::class)
    internal abstract fun bindPrivacyCenterViewModel(viewModel: PrivacyCenterViewModel): ViewModel

//    @Binds
//    @IntoMap
//    @ActivityScope
//    @ViewModelKey(ConsentWithdrawalViewModel::class)
//    abstract fun provideConsentWithdrawalViewModel(viewModel: ConsentWithdrawalViewModel): ViewModel
}
