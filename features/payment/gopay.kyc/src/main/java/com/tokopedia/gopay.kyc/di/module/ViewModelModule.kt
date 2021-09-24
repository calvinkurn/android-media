package com.tokopedia.gopay.kyc.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.gopay.kyc.viewmodel.GoPayKycImageUploadViewModel
import com.tokopedia.gopay.kyc.viewmodel.GoPayKycViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(GoPayKycViewModel::class)
    internal abstract fun bindsKycCameraViewModel(viewModel: GoPayKycViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(GoPayKycImageUploadViewModel::class)
    internal abstract fun bindsKycImageUploadViewModel(viewModel: GoPayKycImageUploadViewModel): ViewModel

}