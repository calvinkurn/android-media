package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey.*
import com.tokopedia.seamless_login.domain.usecase.SeamlessLoginUsecase
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.ProductListSectionContract
import com.tokopedia.search.result.presentation.presenter.localcache.SearchLocalCacheHandler
import com.tokopedia.usecase.UseCase
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.spekframework.spek2.dsl.TestBody
import org.spekframework.spek2.style.gherkin.FeatureBody

@Suppress("UNUSED_VARIABLE")
internal fun FeatureBody.createTestInstance() {
    val productListView by memoized {
        mockk<ProductListSectionContract.View>(relaxed = true)
    }

    val getDynamicFilterUseCase by memoized {
        mockk<UseCase<DynamicFilterModel>>(relaxed = true)
    }

    val searchLocalCacheHandler by memoized {
        mockk<SearchLocalCacheHandler>(relaxed = true)
    }

    val searchProductFirstPageUseCase by memoized {
        mockk<UseCase<SearchProductModel>>(relaxed = true)
    }

    val searchProductLoadMoreUseCase by memoized {
        mockk<UseCase<SearchProductModel>>(relaxed = true)
    }

    val recommendationUseCase by memoized {
        mockk<GetRecommendationUseCase>(relaxed = true)
    }

    val seamlessLoginUseCase by memoized {
        mockk<SeamlessLoginUsecase>(relaxed = true)
    }

    val userSession by memoized {
        mockk<UserSessionInterface>(relaxed = true)
    }

    val remoteConfig by memoized {
        mockk<RemoteConfig>().also {
            every { it.getBoolean(ENABLE_GLOBAL_NAV_WIDGET, true) } answers { secondArg() }
            every { it.getBoolean(APP_CHANGE_PARAMETER_ROW, false) } answers { secondArg() }
            every { it.getBoolean(ENABLE_BOTTOM_SHEET_FILTER, true) } answers { secondArg() }
        }
    }

    val advertisingLocalCache by memoized {
        mockk<LocalCacheHandler>(relaxed = true)
    }
}

internal fun TestBody.createProductListPresenter(): ProductListPresenter {
    val productListView by memoized<ProductListSectionContract.View>()
    val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()
    val searchLocalCacheHandler by memoized<SearchLocalCacheHandler>()
    val searchProductFirstPageUseCase by memoized<UseCase<SearchProductModel>>()
    val searchProductLoadMoreUseCase by memoized<UseCase<SearchProductModel>>()
    val recommendationUseCase by memoized<GetRecommendationUseCase>()
    val seamlessLoginUseCase by memoized<SeamlessLoginUsecase>()
    val userSession by memoized<UserSessionInterface>()
    val remoteConfig by memoized<RemoteConfig>()
    val advertisingLocalCache by memoized<LocalCacheHandler>()

    val presenter = ProductListPresenter(
            searchProductFirstPageUseCase,
            searchProductLoadMoreUseCase,
            recommendationUseCase,
            seamlessLoginUseCase,
            userSession,
            advertisingLocalCache,
            getDynamicFilterUseCase,
            searchLocalCacheHandler,
            remoteConfig
    ).also {
        it.attachView(productListView)
    }

    verify {
        productListView.abTestRemoteConfig
    }

    return presenter
}