package com.tokopedia.search.result.shop.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.discovery.common.Mapper
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.search.result.shop.domain.model.SearchShopModel
import com.tokopedia.search.result.shop.presentation.model.ShopCpmDataView
import com.tokopedia.search.result.shop.presentation.model.ShopDataView
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy as daggerLazy

internal class SearchShopViewModelFactory(
        private val coroutineDispatcher: CoroutineDispatchers,
        private val searchParameter: Map<String, Any>,
        private val searchShopFirstPageUseCase: daggerLazy<UseCase<SearchShopModel>>,
        private val searchShopLoadMoreUseCase: daggerLazy<UseCase<SearchShopModel>>,
        private val getDynamicFilterUseCase: daggerLazy<UseCase<DynamicFilterModel>>,
        private val getShopCountUseCase: daggerLazy<UseCase<Int>>,
        private val shopCpmDataViewMapper: daggerLazy<Mapper<SearchShopModel, ShopCpmDataView>>,
        private val shopDataViewMapper: daggerLazy<Mapper<SearchShopModel, ShopDataView>>,
        private val userSession: daggerLazy<UserSessionInterface>
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
                getShopCountUseCase,
                shopCpmDataViewMapper,
                shopDataViewMapper,
                userSession
        )
    }
}