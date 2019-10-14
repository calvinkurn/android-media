package com.tokopedia.search.result.presentation.viewmodel

import android.arch.lifecycle.ViewModelProvider
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.discovery.common.coroutines.ProductionDispatcherProvider
import com.tokopedia.search.di.scope.SearchScope
import dagger.Module
import dagger.Provides
import javax.inject.Named

@SearchScope
@Module
internal class RedirectionViewModelFactoryModule {

    @SearchScope
    @Provides
    @Named(SearchConstant.REDIRECTION_VIEW_MODEL_FACTORY)
    fun provideRedirectionViewModelFactory(): ViewModelProvider.Factory {
        return RedirectionViewModelFactory(ProductionDispatcherProvider())
    }
}