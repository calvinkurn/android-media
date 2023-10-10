package com.tokopedia.universal_sharing.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.universal_sharing.view.UniversalSharingPostPurchaseViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class UniversalSharingViewModelModule {

    @Binds
    @ActivityScope
    internal abstract fun bindViewModelFactory(
        viewModelFactory: ViewModelFactory
    ): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(UniversalSharingPostPurchaseViewModel::class)
    internal abstract fun bindUniversalSharingPostPurchaseViewModel(
        viewModel: UniversalSharingPostPurchaseViewModel
    ): ViewModel
}
