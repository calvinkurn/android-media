package com.tokopedia.search.result.shop.presentation.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.discovery.common.Mapper
import com.tokopedia.discovery.common.data.DynamicFilterModel
import com.tokopedia.search.result.common.EmptySearchCreator
import com.tokopedia.search.result.domain.usecase.SearchUseCase
import com.tokopedia.search.result.shop.domain.model.SearchShopModel
import com.tokopedia.search.result.shop.presentation.model.ShopHeaderViewModel
import com.tokopedia.search.result.shop.presentation.model.ShopViewModel
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher

class SearchShopViewModelFactory(
        private val coroutineDispatcher: CoroutineDispatcher,
        private val searchParameter: Map<String, Any>,
        private val searchShopFirstPageUseCase: SearchUseCase<SearchShopModel>,
        private val searchShopLoadMoreUseCase: SearchUseCase<SearchShopModel>,
        private val getDynamicFilterUseCase: SearchUseCase<DynamicFilterModel>,
        private val shopHeaderViewModelMapper: Mapper<SearchShopModel, ShopHeaderViewModel>,
        private val shopViewModelMapper: Mapper<SearchShopModel, ShopViewModel>,
        private val emptySearchCreator: EmptySearchCreator,
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
                shopHeaderViewModelMapper,
                shopViewModelMapper,
                emptySearchCreator,
                userSession,
                localCacheHandler
        )
    }
}