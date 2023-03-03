package com.tokopedia.search.result.presentation.view.activity

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.search.di.module.CartLocalCacheHandlerModule
import com.tokopedia.search.di.module.RemoteConfigModule
import com.tokopedia.search.di.module.UserSessionModule
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.SearchParameterModule
import com.tokopedia.search.result.SearchViewModelModule
import com.tokopedia.search.result.mps.MPSFragment
import com.tokopedia.search.result.mps.MPSViewModelModule
import com.tokopedia.search.result.mps.bottomsheet.MPSShimmeringFragment
import com.tokopedia.search.result.presentation.view.fragment.ProductListFragment
import com.tokopedia.search.result.shop.presentation.fragment.ShopListFragment
import com.tokopedia.search.result.shop.presentation.viewmodel.SearchShopViewModelModule
import dagger.Component

@SearchScope
@Component(
    modules = [
        SearchParameterModule::class,
        SearchShopViewModelModule::class,
        SearchViewModelModule::class,
        MPSViewModelModule::class,
        UserSessionModule::class,
        RemoteConfigModule::class,
        CartLocalCacheHandlerModule::class,
    ],
    dependencies = [BaseAppComponent::class]
)
internal interface SearchComponent {

    fun inject(searchActivity: SearchActivity)

    fun inject(shopListFragment: ShopListFragment)

    fun inject(mpsFragment: MPSFragment)

    fun inject(mpsShimmeringFragment: MPSShimmeringFragment)
}
