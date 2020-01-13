package com.tokopedia.search.result.shop.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.discovery.common.DispatcherProvider
import com.tokopedia.discovery.common.Mapper
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.search.result.presentation.presenter.localcache.SearchLocalCacheHandler
import com.tokopedia.search.result.shop.domain.model.SearchShopModel
import com.tokopedia.search.result.shop.presentation.model.ShopCpmViewModel
import com.tokopedia.search.result.shop.presentation.model.ShopTotalCountViewModel
import com.tokopedia.search.result.shop.presentation.model.ShopViewModel
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user.session.UserSessionInterface

internal class SearchShopViewModelFactory(
        private val coroutineDispatcher: DispatcherProvider,
        private val searchParameter: Map<String, Any>,
        private val searchShopFirstPageUseCase: UseCase<SearchShopModel>,
        private val searchShopLoadMoreUseCase: UseCase<SearchShopModel>,
        private val getDynamicFilterUseCase: UseCase<DynamicFilterModel>,
        private val shopCpmViewModelMapper: Mapper<SearchShopModel, ShopCpmViewModel>,
        private val shopTotalCountViewModelMapper: Mapper<SearchShopModel, ShopTotalCountViewModel>,
        private val shopViewModelMapper: Mapper<SearchShopModel, ShopViewModel>,
        private val searchLocalCacheHandler: SearchLocalCacheHandler,
        private val userSession: UserSessionInterface,
        private val localCacheHandler: LocalCacheHandler
): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SearchShopViewModel::class.java)) {
            return createSearchShopViewModel() as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }

    private fun createSearchShopViewModel(): SearchShopViewModel {
        return SearchShopViewModel(
                coroutineDispatcher,
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