package com.tokopedia.pms.paymentlist.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.pms.bankaccount.viewmodel.ChangeBankAccountViewModel
import com.tokopedia.pms.clickbca.view.ChangeClickBcaViewModel
import com.tokopedia.pms.paymentlist.viewmodel.PaymentListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(PaymentListViewModel::class)
    internal abstract fun bindsPaymentListViewModel(viewModel: PaymentListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChangeClickBcaViewModel::class)
    internal abstract fun bindsChangeClickBcaViewModel(viewModel: ChangeClickBcaViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChangeBankAccountViewModel::class)
    internal abstract fun bindsChangeBankAccountViewModel(viewModel: ChangeBankAccountViewModel): ViewModel

}