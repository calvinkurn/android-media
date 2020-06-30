package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.seamless_login.domain.usecase.SeamlessLoginUsecase
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.ProductListSectionContract
import com.tokopedia.search.result.presentation.presenter.localcache.SearchLocalCacheHandler
import com.tokopedia.usecase.UseCase
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before

internal open class ProductListPresenterTestFixtures {

    protected val productListView = mockk<ProductListSectionContract.View>(relaxed = true)
    protected val searchProductFirstPageUseCase = mockk<UseCase<SearchProductModel>>(relaxed = true)
    protected val searchProductLoadMoreUseCase = mockk<UseCase<SearchProductModel>>(relaxed = true)
    protected val getDynamicFilterUseCase = mockk<UseCase<DynamicFilterModel>>(relaxed = true)
    protected val searchLocalCacheHandler = mockk<SearchLocalCacheHandler>(relaxed = true)
    protected val recommendationUseCase = mockk<GetRecommendationUseCase>(relaxed = true)
    protected val seamlessLoginUseCase = mockk<SeamlessLoginUsecase>(relaxed = true)
    protected val userSession = mockk<UserSessionInterface>(relaxed = true)
    protected val remoteConfig = mockk<RemoteConfig>().also {
        every { it.getBoolean(RemoteConfigKey.ENABLE_GLOBAL_NAV_WIDGET, true) } answers { secondArg() }
        every { it.getBoolean(RemoteConfigKey.APP_CHANGE_PARAMETER_ROW, false) } answers { secondArg() }
        every { it.getBoolean(RemoteConfigKey.ENABLE_BOTTOM_SHEET_FILTER, true) } answers { secondArg() }
        every { it.getBoolean(RemoteConfigKey.ENABLE_TRACKING_VIEW_PORT, true) } answers { secondArg() }
    }
    protected val advertisingLocalCache = mockk<LocalCacheHandler>(relaxed = true)
    protected lateinit var productListPresenter: ProductListPresenter

    @Before
    open fun setUp() {
        productListPresenter = ProductListPresenter(
                searchProductFirstPageUseCase,
                searchProductLoadMoreUseCase,
                recommendationUseCase,
                seamlessLoginUseCase,
                userSession,
                advertisingLocalCache,
                getDynamicFilterUseCase,
                searchLocalCacheHandler,
                remoteConfig
        )
        productListPresenter.attachView(productListView)

        verify {
            productListView.abTestRemoteConfig
        }
    }
}