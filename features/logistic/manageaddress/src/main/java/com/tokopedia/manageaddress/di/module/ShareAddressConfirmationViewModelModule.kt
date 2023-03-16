package com.tokopedia.manageaddress.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.manageaddress.ui.shareaddress.bottomsheets.ShareAddressConfirmationViewModel
import com.tokopedia.manageaddress.ui.shareaddress.bottomsheets.ShareAddressViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ShareAddressConfirmationViewModelModule {

    @ActivityScope
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @ActivityScope
    @Binds
    @IntoMap
    @ViewModelKey(ShareAddressConfirmationViewModel::class)
    abstract fun shareAddressConfirmationViewModel(viewModelInactivePhone: ShareAddressConfirmationViewModel): ViewModel

    @ActivityScope
    @Binds
    @IntoMap
    @ViewModelKey(ShareAddressViewModel::class)
    abstract fun shareAddressViewModel(viewModelShareAddress: ShareAddressViewModel): ViewModel
}
