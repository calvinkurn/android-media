package com.tokopedia.search.result.shop.presentation.viewmodel

import android.arch.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.discovery.common.Mapper
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.discovery.common.coroutines.ProductionDispatcherProvider
import com.tokopedia.discovery.common.coroutines.Repository
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.search.di.module.GCMLocalCacheHandlerModule
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.data.repository.dynamicfilter.DynamicFilterCoroutineRepositoryModule
import com.tokopedia.search.result.presentation.presenter.localcache.SearchLocalCacheHandler
import com.tokopedia.search.result.presentation.presenter.localcache.SearchLocalCacheHandlerModule
import com.tokopedia.search.result.shop.domain.model.SearchShopModel
import com.tokopedia.search.result.shop.presentation.mapper.ShopViewModelMapperModule
import com.tokopedia.search.result.shop.presentation.model.ShopHeaderViewModel
import com.tokopedia.search.result.shop.presentation.model.ShopViewModel
import com.tokopedia.search.result.shop.repository.SearchShopRepositoryModule
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import javax.inject.Named

@SearchScope
@Module(includes = [
    SearchShopRepositoryModule::class,
    ShopViewModelMapperModule::class,
    DynamicFilterCoroutineRepositoryModule::class,
    SearchLocalCacheHandlerModule::class,
    GCMLocalCacheHandlerModule::class
])
class SearchShopViewModelFactoryModule(
        private val searchParameter: Map<String, Any> = mapOf()
) {

    @SearchScope
    @Provides
    @Named(SearchConstant.SearchShop.SEARCH_SHOP_VIEW_MODEL_FACTORY)
    fun provideSearchShopViewModelFactory(
            @Named(SearchConstant.SearchShop.SEARCH_SHOP_FIRST_PAGE_REPOSITORY)
            searchShopFirstPageRepository: Repository<SearchShopModel>,
            @Named(SearchConstant.SearchShop.SEARCH_SHOP_LOAD_MORE_REPOSITORY)
            searchShopLoadMoreRepository: Repository<SearchShopModel>,
            @Named(SearchConstant.DynamicFilter.DYNAMIC_FILTER_REPOSITORY)
            dynamicFilterRepository: Repository<DynamicFilterModel>,
            shopHeaderViewModelMapper: Mapper<SearchShopModel, ShopHeaderViewModel>,
            shopViewModelMapper: Mapper<SearchShopModel, ShopViewModel>,
            searchLocalCacheHandler: SearchLocalCacheHandler,
            userSession: UserSessionInterface,
            @Named(SearchConstant.GCM.GCM_LOCAL_CACHE)
            localCacheHandler: LocalCacheHandler
    ): ViewModelProvider.Factory {
        return SearchShopViewModelFactory(
                ProductionDispatcherProvider(),
                searchParameter,
                searchShopFirstPageRepository,
                searchShopLoadMoreRepository,
                dynamicFilterRepository,
                shopHeaderViewModelMapper,
                shopViewModelMapper,
                searchLocalCacheHandler,
                userSession,
                localCacheHandler
        )
    }
}
