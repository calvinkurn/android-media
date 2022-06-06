package com.tokopedia.epharmacy.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.basemvvm.viewmodel.ViewModelKey
import com.tokopedia.basemvvm.viewmodel.ViewModelProviderFactory
import com.tokopedia.epharmacy.viewmodel.EPharmacyViewModel
import com.tokopedia.epharmacy.viewmodel.UploadPrescriptionViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class EPharmacyVMModule {

    @EPharmacyScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelProviderFactory: ViewModelProviderFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @EPharmacyScope
    @ViewModelKey(EPharmacyViewModel::class)
    internal abstract fun ePharmacyViewModel(viewModel: EPharmacyViewModel): ViewModel

    @Binds
    @IntoMap
    @EPharmacyScope
    @ViewModelKey(UploadPrescriptionViewModel::class)
    internal abstract fun uploadPrescriptionViewModel(viewModel: UploadPrescriptionViewModel): ViewModel
}
