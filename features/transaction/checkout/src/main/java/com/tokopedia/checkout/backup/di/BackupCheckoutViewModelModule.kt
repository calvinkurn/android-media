package com.tokopedia.checkout.backup.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.checkout.backup.view.BackupCheckoutVM
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class BackupCheckoutViewModelModule {

    @Binds
    @ActivityScope
    abstract fun bindViewModelFactory(viewModelFactor: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @ActivityScope
    @IntoMap
    @ViewModelKey(BackupCheckoutVM::class)
    abstract fun bindCheckoutViewModel(checkoutViewModel: BackupCheckoutVM): ViewModel
}
