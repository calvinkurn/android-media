package com.tokopedia.recentview.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.recentview.view.presenter.RecentViewViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author by yoasfs on 2019-09-18
 */

@Module
abstract class ViewModelModule {

    @RecentViewScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(RecentViewViewModel::class)
    internal abstract fun RecentViewViewModel(viewModel: RecentViewViewModel): ViewModel


}