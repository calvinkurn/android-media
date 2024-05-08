package com.tokopedia.accountprofile.changephonenumber.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.accountprofile.changephonenumber.features.ChangePhoneNumberViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ChangePhoneNumberViewModelModule {

    @ActivityScope
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @ActivityScope
    @Binds
    @IntoMap
    @ViewModelKey(ChangePhoneNumberViewModel::class)
    abstract fun changePhoneNumberViewModel(changePhoneNumberViewModel: ChangePhoneNumberViewModel): ViewModel

}
