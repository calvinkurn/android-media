package com.tokopedia.search.result.product.similarsearch

import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.discovery.common.utils.SimilarSearchCoachMarkLocalCache
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RollenceKey
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import dagger.Lazy
import javax.inject.Inject
import javax.inject.Named

@SearchScope
class SimilarSearchOnBoardingPresenterDelegate @Inject constructor(
    private val similarSearchLocalCache: SimilarSearchCoachMarkLocalCache,
    @Named(SearchConstant.AB_TEST_REMOTE_CONFIG)
    private val abTestRemoteConfig: Lazy<RemoteConfig>,
    private val view: SimilarSearchOnBoardingView,
) {

    private val isEnabledRollence by lazy(LazyThreadSafetyMode.NONE, ::getIsEnabledRollence)

    private fun getIsEnabledRollence() = try {
        abTestRemoteConfig.get()
            .getString(
                RollenceKey.SEARCH_SIMILAR_SEARCH_COACHMARK,
                ""
            ) == RollenceKey.SEARCH_SIMILAR_SEARCH_COACHMARK_VARIANT
    } catch (ignored: Throwable) {
        false
    }

    fun checkShouldDisplaySimilarSearchThreeDotsCoachmark(
        item: ProductItemDataView,
        adapterPosition: Int,
    ) {
        if (isEnabledRollence && item.isWishlistButtonEnabled) {
            if (similarSearchLocalCache.shouldShowThreeDotsCoachmark()) {
                view.showSimilarSearchThreeDotsCoachmark(item, adapterPosition)
            }
        }
    }
}
