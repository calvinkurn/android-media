package com.tokopedia.search.result.shop.presentation.viewmodel

import android.arch.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.discovery.common.Mapper
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope
import com.tokopedia.search.result.domain.model.SearchShopModel
import com.tokopedia.search.result.domain.usecase.SearchUseCase
import com.tokopedia.search.result.shop.domain.SearchShopCoroutineUseCaseModule
import com.tokopedia.search.result.presentation.mapper.ShopViewModelMapperModule
import com.tokopedia.search.result.presentation.model.ShopHeaderViewModel
import com.tokopedia.search.result.presentation.model.ShopViewModel
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import javax.inject.Named

@SearchScope
@Module(includes = [
    SearchShopCoroutineUseCaseModule::class,
    ShopViewModelMapperModule::class
])
class SearchShopViewModelFactoryModule(
        private val searchParameter: Map<String, Any> = mapOf()
) {

    @SearchScope
    @Provides
    @Named(SearchConstant.SearchShop.SEARCH_SHOP_VIEW_MODEL_FACTORY)
    fun provideSearchShopViewModelFactory(
            @Named(SearchConstant.SearchShop.SEARCH_SHOP_FIRST_PAGE_USE_CASE)
            searchShopFirstPageUseCase: SearchUseCase<SearchShopModel>,
            @Named(SearchConstant.SearchShop.SEARCH_SHOP_LOAD_MORE_USE_CASE)
            searchShopLoadMoreUseCase: SearchUseCase<SearchShopModel>,
            shopHeaderViewModelMapper: Mapper<SearchShopModel, ShopHeaderViewModel>,
            shopViewModelMapper: Mapper<SearchShopModel, ShopViewModel>,
            userSession: UserSessionInterface,
            localCacheHandler: LocalCacheHandler
    ): ViewModelProvider.Factory {
        return SearchShopViewModelFactory(
                Dispatchers.Main,
                searchParameter,
                searchShopFirstPageUseCase,
                searchShopLoadMoreUseCase,
                shopHeaderViewModelMapper,
                shopViewModelMapper,
                userSession,
                localCacheHandler
        )
    }
}
