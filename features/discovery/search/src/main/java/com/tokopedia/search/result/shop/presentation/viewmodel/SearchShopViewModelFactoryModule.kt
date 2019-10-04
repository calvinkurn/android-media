package com.tokopedia.search.result.shop.presentation.viewmodel

import android.arch.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.discovery.common.Mapper
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.search.di.module.GCMLocalCacheHandlerModule
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.common.EmptySearchCreator
import com.tokopedia.search.result.common.EmptySearchCreatorModule
import com.tokopedia.search.result.domain.usecase.SearchUseCase
import com.tokopedia.search.result.domain.usecase.getdynamicfilter.GetDynamicFilterCoroutineUseCaseModule
import com.tokopedia.search.result.shop.domain.model.SearchShopModel
import com.tokopedia.search.result.shop.domain.usecase.SearchShopCoroutineUseCaseModule
import com.tokopedia.search.result.shop.presentation.mapper.ShopViewModelMapperModule
import com.tokopedia.search.result.shop.presentation.model.ShopHeaderViewModel
import com.tokopedia.search.result.shop.presentation.model.ShopViewModel
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import javax.inject.Named

@SearchScope
@Module(includes = [
    SearchShopCoroutineUseCaseModule::class,
    ShopViewModelMapperModule::class,
    EmptySearchCreatorModule::class,
    GetDynamicFilterCoroutineUseCaseModule::class,
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
            searchShopFirstPageUseCase: SearchUseCase<SearchShopModel>,
            @Named(SearchConstant.SearchShop.SEARCH_SHOP_LOAD_MORE_REPOSITORY)
            searchShopLoadMoreUseCase: SearchUseCase<SearchShopModel>,
            @Named(SearchConstant.DynamicFilter.GET_DYNAMIC_FILTER_USE_CASE)
            getDynamicFilterUseCase: SearchUseCase<DynamicFilterModel>,
            shopHeaderViewModelMapper: Mapper<SearchShopModel, ShopHeaderViewModel>,
            shopViewModelMapper: Mapper<SearchShopModel, ShopViewModel>,
            emptySearchCreator: EmptySearchCreator,
            userSession: UserSessionInterface,
            @Named(SearchConstant.GCM.GCM_LOCAL_CACHE)
            localCacheHandler: LocalCacheHandler
    ): ViewModelProvider.Factory {
        return SearchShopViewModelFactory(
                Dispatchers.Main,
                searchParameter,
                searchShopFirstPageUseCase,
                searchShopLoadMoreUseCase,
                getDynamicFilterUseCase,
                shopHeaderViewModelMapper,
                shopViewModelMapper,
                emptySearchCreator,
                userSession,
                localCacheHandler
        )
    }
}
