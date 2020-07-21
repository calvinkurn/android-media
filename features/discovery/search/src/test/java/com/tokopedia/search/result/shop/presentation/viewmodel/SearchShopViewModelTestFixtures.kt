package com.tokopedia.search.result.shop.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.search.result.TestDispatcherProvider
import com.tokopedia.search.result.isExecuted
import com.tokopedia.search.result.presentation.presenter.localcache.SearchLocalCacheHandler
import com.tokopedia.search.result.shop.domain.model.SearchShopModel
import com.tokopedia.search.result.shop.presentation.mapper.ShopViewModelMapperModule
import com.tokopedia.search.result.shop.presentation.viewmodel.testinstance.dynamicFilterModel
import com.tokopedia.search.result.shop.presentation.viewmodel.testinstance.searchShopModel
import com.tokopedia.search.result.shop.presentation.viewmodel.testinstance.searchShopQuickFilterModel
import com.tokopedia.search.result.stubExecute
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule

internal open class SearchShopViewModelTestFixtures {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val shopViewModelMapperModule = ShopViewModelMapperModule()

    protected val searchShopFirstPageUseCase = mockk<UseCase<SearchShopModel>>(relaxed = true)
    protected val searchShopLoadMoreUseCase = mockk<UseCase<SearchShopModel>>(relaxed = true)
    protected val getDynamicFilterUseCase = mockk<UseCase<DynamicFilterModel>>(relaxed = true)
    protected val searchLocalCacheHandler = mockk<SearchLocalCacheHandler>(relaxed = true)
    protected val userSession = mockk<UserSessionInterface>(relaxed = true)
    protected val shopCpmViewModelMapper = shopViewModelMapperModule.provideShopCpmViewModelMapper()
    protected val shopTotalCountViewModelMapper = shopViewModelMapperModule.provideShopTotalCountViewModelMapper()
    protected val shopViewModelMapper = shopViewModelMapperModule.provideShopViewModelMapper()

    protected val searchShopParameterCommon = mapOf(
            SearchApiConst.Q to "samsung",
            SearchApiConst.OFFICIAL to true
    )

    protected lateinit var searchShopViewModel: SearchShopViewModel

    @Before
    open fun setUp() {
        searchShopViewModel = createSearchShopViewModel()
    }

    protected open fun createSearchShopViewModel(parameter: Map<String, Any> = searchShopParameterCommon): SearchShopViewModel {
        return SearchShopViewModel(
                TestDispatcherProvider(), parameter,
                searchShopFirstPageUseCase, searchShopLoadMoreUseCase, getDynamicFilterUseCase,
                shopCpmViewModelMapper, shopTotalCountViewModelMapper, shopViewModelMapper,
                searchLocalCacheHandler, userSession
        )
    }

    protected fun `Given search shop API call will be successful`() {
        searchShopFirstPageUseCase.stubExecute().returns(searchShopModel)
        getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
    }

    protected fun `Then verify search shop and dynamic filter API is called once`() {
        searchShopFirstPageUseCase.isExecuted()
        getDynamicFilterUseCase.isExecuted()
    }
}