package com.tokopedia.kyc_centralized.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.kyc_centralized.view.viewmodel.KycUploadViewModel
import com.tokopedia.kyc_centralized.view.viewmodel.UserIdentificationViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class KycCentralizedViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(UserIdentificationViewModel::class)
    abstract fun userIdentificationViewModel(viewModel: UserIdentificationViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(KycUploadViewModel::class)
    abstract fun kycUploadViewModel(viewModel: KycUploadViewModel): ViewModel

    @Binds
    @UserIdentificationCommonScope
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
}