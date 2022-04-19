package com.tokopedia.otp.silentverification.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.otp.silentverification.view.viewmodel.SilentVerificationViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by Yoris on 18/10/21.
 */

@Module
abstract class SilentVerificationViewModelModule {

    @Binds
    @ActivityScope
    internal abstract fun bindViewModelFactory(
        viewModelFactory: ViewModelFactory
    ): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SilentVerificationViewModel::class)
    internal abstract fun bindSilentVerificationViewModel(
        viewModel: SilentVerificationViewModel
    ): ViewModel
}