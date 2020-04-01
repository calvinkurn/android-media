package com.tokopedia.search.result.shop.presentation.viewmodel

import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.search.result.TestDispatcherProvider
import com.tokopedia.search.result.presentation.presenter.localcache.SearchLocalCacheHandler
import com.tokopedia.search.result.shop.domain.model.SearchShopModel
import com.tokopedia.search.result.shop.presentation.mapper.ShopViewModelMapperModule
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.mockk
import org.spekframework.spek2.dsl.TestBody
import org.spekframework.spek2.style.gherkin.FeatureBody

@Suppress("UNUSED_VARIABLE")
internal fun FeatureBody.createTestInstance() {
    val searchShopFirstPageUseCase by memoized {
        mockk<UseCase<SearchShopModel>>(relaxed = true)
    }

    val searchShopLoadMoreUseCase by memoized {
        mockk<UseCase<SearchShopModel>>(relaxed = true)
    }

    val getDynamicFilterUseCase by memoized {
        mockk<UseCase<DynamicFilterModel>>(relaxed = true)
    }

    val searchLocalCacheHandler by memoized {
        mockk<SearchLocalCacheHandler>(relaxed = true)
    }

    val userSession by memoized {
        mockk<UserSessionInterface>(relaxed = true)
    }

    val localCacheHandler by memoized {
        mockk<LocalCacheHandler>(relaxed = true)
    }
}

internal fun TestBody.createSearchShopViewModel(parameter: Map<String, Any> =
                                               mapOf(
                                                       SearchApiConst.Q to "samsung",
                                                       SearchApiConst.OFFICIAL to true
                                               )
): SearchShopViewModel {
    val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
    val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()
    val searchShopLoadMoreUseCase by memoized<UseCase<SearchShopModel>>()
    val searchLocalCacheHandler by memoized<SearchLocalCacheHandler>()
    val userSession by memoized<UserSessionInterface>()
    val localCacheHandler by memoized<LocalCacheHandler>()


    val shopViewModelMapperModule = ShopViewModelMapperModule()

    val shopCpmViewModelMapper = shopViewModelMapperModule.provideShopCpmViewModelMapper()
    val shopTotalCountViewModelMapper = shopViewModelMapperModule.provideShopTotalCountViewModelMapper()
    val shopViewModelMapper = shopViewModelMapperModule.provideShopViewModelMapper()

    return SearchShopViewModel(
            TestDispatcherProvider(), parameter,
            searchShopFirstPageUseCase, searchShopLoadMoreUseCase, getDynamicFilterUseCase,
            shopCpmViewModelMapper, shopTotalCountViewModelMapper, shopViewModelMapper,
            searchLocalCacheHandler, userSession, localCacheHandler
    )
}