package com.tokopedia.developer_options.deeplink.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.developer_options.deeplink.di.scope.DeepLinkScope
import com.tokopedia.developer_options.deeplink.presentation.viewmodel.DeepLinkViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class DeepLinkViewModelModule {
    @DeepLinkScope
    @Binds
    abstract fun bindDeeplinkViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(DeepLinkViewModel::class)
    abstract fun bindDeepLinkViewModel(deepLinkViewModel: DeepLinkViewModel): ViewModel
}