package com.tokopedia.search.result.shop.presentation.viewmodel

import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.discovery.common.Mapper
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.discovery.common.coroutines.ProductionDispatcherProvider
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.search.di.module.GCMLocalCacheHandlerModule
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.domain.usecase.getdynamicfilter.GetDynamicFilterCoroutineUseCaseModule
import com.tokopedia.search.result.presentation.presenter.localcache.SearchLocalCacheHandler
import com.tokopedia.search.result.presentation.presenter.localcache.SearchLocalCacheHandlerModule
import com.tokopedia.search.result.shop.domain.model.SearchShopModel
import com.tokopedia.search.result.shop.domain.usecase.SearchShopUseCaseModule
import com.tokopedia.search.result.shop.presentation.mapper.ShopViewModelMapperModule
import com.tokopedia.search.result.shop.presentation.model.ShopCpmViewModel
import com.tokopedia.search.result.shop.presentation.model.ShopTotalCountViewModel
import com.tokopedia.search.result.shop.presentation.model.ShopViewModel
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import javax.inject.Named

@SearchScope
@Module(includes = [
    SearchShopUseCaseModule::class,
    GetDynamicFilterCoroutineUseCaseModule::class,
    ShopViewModelMapperModule::class,
    SearchLocalCacheHandlerModule::class,
    GCMLocalCacheHandlerModule::class
])
internal class SearchShopViewModelFactoryModule(
        private val searchParameter: Map<String, Any> = mapOf()
) {

    @SearchScope
    @Provides
    @Named(SearchConstant.SearchShop.SEARCH_SHOP_VIEW_MODEL_FACTORY)
    fun provideSearchShopViewModelFactory(
            @Named(SearchConstant.SearchShop.SEARCH_SHOP_FIRST_PAGE_USE_CASE)
            searchShopFirstPageUseCase: UseCase<SearchShopModel>,
            @Named(SearchConstant.SearchShop.SEARCH_SHOP_LOAD_MORE_USE_CASE)
            searchShopLoadMoreUseCase: UseCase<SearchShopModel>,
            @Named(SearchConstant.DynamicFilter.GET_DYNAMIC_FILTER_SHOP_USE_CASE)
            getDynamicFilterUseCase: UseCase<DynamicFilterModel>,
            shopCpmViewModelMapper: Mapper<SearchShopModel, ShopCpmViewModel>,
            shopTotalCountViewModelMapper: Mapper<SearchShopModel, ShopTotalCountViewModel>,
            shopViewModelMapper: Mapper<SearchShopModel, ShopViewModel>,
            searchLocalCacheHandler: SearchLocalCacheHandler,
            userSession: UserSessionInterface,
            @Named(SearchConstant.GCM.GCM_LOCAL_CACHE)
            localCacheHandler: LocalCacheHandler
    ): ViewModelProvider.Factory {
        return SearchShopViewModelFactory(
                ProductionDispatcherProvider(),
                searchParameter,
                searchShopFirstPageUseCase,
                searchShopLoadMoreUseCase,
                getDynamicFilterUseCase,
                shopCpmViewModelMapper,
                shopTotalCountViewModelMapper,
                shopViewModelMapper,
                searchLocalCacheHandler,
                userSession,
                localCacheHandler
        )
    }
}
