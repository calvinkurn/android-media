package com.tokopedia.home_account.privacy_account.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.home_account.privacy_account.viewmodel.PrivacyAccountViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module abstract class LinkAccountViewModelModule {

    @Binds
    @ActivityScope
    internal abstract fun bindViewModelFactory(
            viewModelFactory: ViewModelFactory
    ): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(PrivacyAccountViewModel::class)
    internal abstract fun bindLinkAccountViewModel(
            viewModel: PrivacyAccountViewModel
    ): ViewModel

}