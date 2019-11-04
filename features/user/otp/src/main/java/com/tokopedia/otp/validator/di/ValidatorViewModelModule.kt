package com.tokopedia.otp.validator.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.otp.validator.viewmodel.ValidatorViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by Ade Fulki on 2019-10-20.
 * ade.hadian@tokopedia.com
 */

@ValidatorScope
@Module
abstract class ValidatorViewModelModule{

    @Binds
    @ValidatorScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ValidatorViewModel::class)
    internal abstract fun validatorViewModel(viewModel: ValidatorViewModel): ViewModel
}