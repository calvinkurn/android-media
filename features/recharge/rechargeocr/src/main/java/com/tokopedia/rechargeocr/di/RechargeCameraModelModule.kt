package com.tokopedia.rechargeocr.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.rechargeocr.viewmodel.RechargeUploadImageViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class RechargeCameraModelModule {

    @RechargeCameraScope
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(RechargeUploadImageViewModel::class)
    abstract fun rechargeUploadImageViewModel(customViewModel: RechargeUploadImageViewModel): ViewModel

}