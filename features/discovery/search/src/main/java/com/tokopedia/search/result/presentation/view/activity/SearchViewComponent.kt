package com.tokopedia.search.result.presentation.view.activity

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.search.di.module.CartLocalCacheHandlerModule
import com.tokopedia.search.di.module.RemoteConfigModule
import com.tokopedia.search.di.module.UserSessionModule
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.presentation.viewmodel.SearchViewModelFactoryModule
import com.tokopedia.search.result.shop.presentation.viewmodel.SearchShopViewModelFactoryModule
import dagger.Component

@SearchScope
@Component(
    modules = [
        SearchShopViewModelFactoryModule::class,
        SearchViewModelFactoryModule::class,
        UserSessionModule::class,
        RemoteConfigModule::class,
        CartLocalCacheHandlerModule::class,
    ],
    dependencies = [BaseAppComponent::class]
)
interface SearchViewComponent {

    fun inject(searchActivity: SearchActivity?)
}