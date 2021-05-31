package com.tokopedia.loginphone.chooseaccount.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.loginphone.chooseaccount.viewmodel.ChooseAccountViewModel
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
    @ChooseAccountScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ChooseAccountViewModel::class)
    internal abstract fun registerInitialViewModel(viewModel: ChooseAccountViewModel): ViewModel
}