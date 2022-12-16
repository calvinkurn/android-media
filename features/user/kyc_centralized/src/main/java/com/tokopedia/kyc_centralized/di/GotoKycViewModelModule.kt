package com.tokopedia.kyc_centralized.di

import androidx.lifecycle.ViewModel
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.kyc_centralized.ui.gotoKyc.main.GotoKycMainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class GotoKycViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(GotoKycMainViewModel::class)
    abstract fun gotoKycMainViewModel(viewModel: GotoKycMainViewModel): ViewModel

}
