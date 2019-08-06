package com.tokopedia.search.result.presentation.viewmodel.shop

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.discovery.common.Mapper
import com.tokopedia.search.result.domain.model.SearchShopModel
import com.tokopedia.search.result.domain.usecase.SearchUseCase
import com.tokopedia.search.result.presentation.model.ShopHeaderViewModel
import com.tokopedia.search.result.presentation.model.ShopViewModel
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher

class SearchShopViewModelFactory(
        private val coroutineDispatcher: CoroutineDispatcher,
        private val searchParameter: Map<String, Any>,
        private val searchShopFirstPageUseCase: SearchUseCase<SearchShopModel>,
        private val searchShopLoadMoreUseCase: SearchUseCase<SearchShopModel>,
        private val shopHeaderViewModelMapper: Mapper<SearchShopModel, ShopHeaderViewModel>,
        private val shopViewModelMapper: Mapper<SearchShopModel, ShopViewModel>,
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
                shopHeaderViewModelMapper,
                shopViewModelMapper,
                userSession,
                localCacheHandler
        )
    }

//    private fun provideSearchShopFirstPageUseCase(): SearchUseCase<SearchShopModel> {
//        val graphqlUseCase = createGraphqlUseCase(R.raw.gql_search_shop_first_page)
//
//        return SearchShopFirstPageUseCase(graphqlUseCase)
//    }
//
//    private fun provideSearchShopLoadMoreUseCase(): SearchUseCase<SearchShopModel> {
//        val graphqlUseCase = createGraphqlUseCase(R.raw.gql_search_shop_load_more)
//
//        return SearchShopLoadMoreUseCase(graphqlUseCase)
//    }
//
//    private fun createGraphqlUseCase(@RawRes rawQueryRes: Int): GraphqlUseCase<SearchShopModel> {
//        val query = GraphqlHelper.loadRawString(context.resources, rawQueryRes)
//        val repository = GraphqlInteractor.getInstance().graphqlRepository
//
//        val useCase = GraphqlUseCase<SearchShopModel>(repository)
//        useCase.setTypeClass(SearchShopModel::class.java)
//        useCase.setGraphqlQuery(query)
//
//        return useCase
//    }
}