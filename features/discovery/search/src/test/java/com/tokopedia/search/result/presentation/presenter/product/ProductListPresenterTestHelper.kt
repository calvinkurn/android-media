package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.seamless_login.domain.usecase.SeamlessLoginUsecase
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.ProductListSectionContract
import com.tokopedia.search.result.presentation.presenter.localcache.SearchLocalCacheHandler
import com.tokopedia.usecase.UseCase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import io.mockk.mockk
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

    val productWishlistUrlUseCase by memoized {
        mockk<UseCase<Boolean>>(relaxed = true)
    }

    val recommendationUseCase by memoized {
        mockk<GetRecommendationUseCase>(relaxed = true)
    }

    val addWishlistActionUseCase by memoized {
        mockk<AddWishListUseCase>(relaxed = true)
    }

    val removeWishlistActionUseCase by memoized {
        mockk<RemoveWishListUseCase>(relaxed = true)
    }

    val seamlessLoginUseCase by memoized {
        mockk<SeamlessLoginUsecase>(relaxed = true)
    }

    val userSession by memoized {
        mockk<UserSessionInterface>(relaxed = true)
    }

    val remoteConfig by memoized {
        mockk<RemoteConfig>(relaxed = true)
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
    val productWishlistUrlUseCase by memoized<UseCase<Boolean>>()
    val recommendationUseCase by memoized<GetRecommendationUseCase>()
    val addWishlistActionUseCase by memoized<AddWishListUseCase>()
    val removeWishlistActionUseCase by memoized<RemoveWishListUseCase>()
    val seamlessLoginUseCase by memoized<SeamlessLoginUsecase>()
    val userSession by memoized<UserSessionInterface>()
    val remoteConfig by memoized<RemoteConfig>()
    val advertisingLocalCache by memoized<LocalCacheHandler>()

    return ProductListPresenter().also {
        it.attachView(productListView)
        it.getDynamicFilterUseCase = getDynamicFilterUseCase
        it.searchLocalCacheHandler = searchLocalCacheHandler
        it.searchProductFirstPageUseCase = searchProductFirstPageUseCase
        it.searchProductLoadMoreUseCase = searchProductLoadMoreUseCase
        it.productWishlistUrlUseCase = productWishlistUrlUseCase
        it.recommendationUseCase = recommendationUseCase
        it.addWishlistActionUseCase = addWishlistActionUseCase
        it.removeWishlistActionUseCase = removeWishlistActionUseCase
        it.seamlessLoginUsecase = seamlessLoginUseCase
        it.userSession = userSession
        it.remoteConfig = remoteConfig
        it.advertisingLocalCache = advertisingLocalCache
    }
}