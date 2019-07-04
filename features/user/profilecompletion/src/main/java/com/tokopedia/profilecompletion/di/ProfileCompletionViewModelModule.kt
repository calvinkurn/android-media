package com.tokopedia.profilecompletion.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.profilecompletion.addemail.viewmodel.AddEmailViewModel
import com.tokopedia.profilecompletion.addphone.viewmodel.AddPhoneViewModel
import com.tokopedia.profilecompletion.changegender.viewmodel.ChangeGenderViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
@ProfileCompletionScope
abstract class ProfileCompletionViewModelModule {
    @Binds
    @ProfileCompletionScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ChangeGenderViewModel::class)
    internal abstract fun changeGenderViewModel(viewModel: ChangeGenderViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddEmailViewModel::class)
    internal abstract fun addEmailViewModel(viewModel: AddEmailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddPhoneViewModel::class)
    internal abstract fun AddPhoneViewModel(viewModel: AddPhoneViewModel): ViewModel
}