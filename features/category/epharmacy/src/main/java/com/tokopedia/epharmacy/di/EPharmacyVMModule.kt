package com.tokopedia.epharmacy.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.basemvvm.viewmodel.ViewModelKey
import com.tokopedia.epharmacy.viewmodel.EPharmacyChatLoadingViewModel
import com.tokopedia.epharmacy.viewmodel.EPharmacyCheckoutViewModel
import com.tokopedia.epharmacy.viewmodel.EPharmacyOrderDetailViewModel
import com.tokopedia.epharmacy.viewmodel.EPharmacyPrescriptionAttachmentViewModel
import com.tokopedia.epharmacy.viewmodel.EPharmacyReminderBsViewModel
import com.tokopedia.epharmacy.viewmodel.MiniConsultationMasterBsViewModel
import com.tokopedia.epharmacy.viewmodel.UploadPrescriptionViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class EPharmacyVMModule {

    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(EPharmacyCheckoutViewModel::class)
    internal abstract fun ePharmacyCheckoutViewModel(viewModel: EPharmacyCheckoutViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UploadPrescriptionViewModel::class)
    internal abstract fun uploadPrescriptionViewModel(viewModel: UploadPrescriptionViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EPharmacyPrescriptionAttachmentViewModel::class)
    internal abstract fun ePharmacyPrescriptionAttachmentViewModel(viewModel: EPharmacyPrescriptionAttachmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MiniConsultationMasterBsViewModel::class)
    internal abstract fun miniConsultationMasterBsViewModel(viewModel: MiniConsultationMasterBsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EPharmacyReminderBsViewModel::class)
    internal abstract fun ePharmacyReminderBsViewModel(viewModel: EPharmacyReminderBsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EPharmacyChatLoadingViewModel::class)
    internal abstract fun ePharmacyLoadingViewModel(viewModel: EPharmacyChatLoadingViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EPharmacyOrderDetailViewModel::class)
    internal abstract fun ePharmacyOrderDetailViewModel(viewModel: EPharmacyOrderDetailViewModel): ViewModel
}
