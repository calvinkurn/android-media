package com.tokopedia.payment.setting.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.payment.setting.authenticate.view.viewmodel.AuthenticateCCViewModel
import com.tokopedia.payment.setting.detail.view.viewmodel.DetailCreditCardViewModel
import com.tokopedia.payment.setting.list.view.viewmodel.SettingsListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @SettingPaymentScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SettingsListViewModel::class)
    internal abstract fun settingsListViewModel(viewModel: SettingsListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AuthenticateCCViewModel::class)
    internal abstract fun authenticateViewModel(CCViewModel: AuthenticateCCViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetailCreditCardViewModel::class)
    internal abstract fun detailCreditCardViewModel(CCViewModel: DetailCreditCardViewModel): ViewModel

}
