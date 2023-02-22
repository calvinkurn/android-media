package com.tokopedia.kyc_centralized.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.kyc_centralized.ui.gotoKyc.main.GotoKycMainViewModel
import com.tokopedia.kyc_centralized.ui.gotoKyc.transparent.GotoKycTransparentViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class GotoKycViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(GotoKycMainViewModel::class)
    abstract fun gotoKycMainViewModel(viewModel: GotoKycMainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(GotoKycTransparentViewModel::class)
    abstract fun gotoKycTransparentViewModel(viewModel: GotoKycTransparentViewModel): ViewModel

    @Binds
    @ActivityScope
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

}
