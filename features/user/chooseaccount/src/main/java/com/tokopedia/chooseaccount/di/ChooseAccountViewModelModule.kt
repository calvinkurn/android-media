package com.tokopedia.chooseaccount.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.chooseaccount.view.ocl.OclChooseAccountViewModel
import com.tokopedia.chooseaccount.viewmodel.ChooseAccountFingerprintViewModel
import com.tokopedia.chooseaccount.viewmodel.ChooseAccountViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by Ade Fulki on 2019-11-18.
 * ade.hadian@tokopedia.com
 */

@Module
abstract class ChooseAccountViewModelModule{

    @Binds
    @ActivityScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ChooseAccountViewModel::class)
    internal abstract fun chooseAccountViewModel(viewModel: ChooseAccountViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChooseAccountFingerprintViewModel::class)
    internal abstract fun chooseAccountFingerprintViewModel(viewModel: ChooseAccountFingerprintViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(OclChooseAccountViewModel::class)
    internal abstract fun chooseAccountOclViewModel(viewModel: OclChooseAccountViewModel): ViewModel
}
