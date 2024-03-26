package com.tokopedia.accountprofile.settingprofile.addname.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.accountprofile.settingprofile.addname.viewmodel.AddNameViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AddNameViewModelModule {

    @Binds
    abstract fun bindViewModelFactroy(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(AddNameViewModel::class)
    abstract fun bindAddNameViewModel(addNameViewModel: AddNameViewModel): ViewModel
}
