package com.tokopedia.editshipping.di.shopeditaddress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.editshipping.ui.shopeditaddress.ShopEditAddressViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ShopEditAddressViewModelModule {

    @ActivityScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @ActivityScope
    @Binds
    @IntoMap
    @ViewModelKey(ShopEditAddressViewModel::class)
    internal abstract fun provideShopEditAddressViewModel(viewModel: ShopEditAddressViewModel): ViewModel
}
