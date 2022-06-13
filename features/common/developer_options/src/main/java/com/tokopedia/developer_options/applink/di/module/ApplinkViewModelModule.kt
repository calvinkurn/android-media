package com.tokopedia.developer_options.applink.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.developer_options.applink.di.scope.AppLinkScope
import com.tokopedia.developer_options.applink.presentation.viewmodel.AppLinkViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AppLinkViewModelModule {
    @AppLinkScope
    @Binds
    abstract fun bindDeeplinkViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(AppLinkViewModel::class)
    abstract fun bindDeepLinkViewModel(appLinkViewModel: AppLinkViewModel): ViewModel
}