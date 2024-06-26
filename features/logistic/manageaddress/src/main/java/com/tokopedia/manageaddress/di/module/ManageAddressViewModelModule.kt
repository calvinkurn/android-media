package com.tokopedia.manageaddress.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.manageaddress.ui.manageaddress.ManageAddressViewModel
import com.tokopedia.manageaddress.ui.manageaddress.fromfriend.FromFriendViewModel
import com.tokopedia.manageaddress.ui.shareaddress.bottomsheets.ShareAddressConfirmationViewModel
import com.tokopedia.manageaddress.ui.shareaddress.bottomsheets.ShareAddressViewModel
import com.tokopedia.manageaddress.ui.shoplocation.ShopLocationViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ManageAddressViewModelModule {

    @ActivityScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @ActivityScope
    @Binds
    @IntoMap
    @ViewModelKey(ManageAddressViewModel::class)
    internal abstract fun providesManageAddressViewModel(viewModel: ManageAddressViewModel): ViewModel

    @ActivityScope
    @Binds
    @IntoMap
    @ViewModelKey(ShopLocationViewModel::class)
    internal abstract fun providesShopLocationViewModel(viewModel: ShopLocationViewModel): ViewModel

    @ActivityScope
    @Binds
    @IntoMap
    @ViewModelKey(FromFriendViewModel::class)
    internal abstract fun providesFromFriendViewModel(viewModel: FromFriendViewModel): ViewModel

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
