package com.tokopedia.topads.common.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.topads.common.data.model.ticker.TopAdsTickerV2
import com.tokopedia.topads.common.data.model.ticker.TopAdsTickerResponse
import com.tokopedia.topads.common.domain.usecase.TopAdsTicker.ANDROID
import com.tokopedia.topads.common.domain.usecase.TopAdsTicker.PARAM_SHOP_ID
import com.tokopedia.topads.common.domain.usecase.TopAdsTicker.PARAM_SOURCE
import com.tokopedia.topads.common.domain.usecase.TopAdsTicker.PARAM_TARGET_PAGE
import com.tokopedia.topads.common.domain.usecase.TopAdsTicker.TICKER_SOURCE_DASHBOARD
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Get Recipe GQL Docs:
 * https://tokopedia.atlassian.net/wiki/spaces/TokoNow/pages/1973782733/GQL+Get+Recipe
 */
class TopAdsTickerUseCase @Inject constructor(gqlRepository: GraphqlRepository, private val userSession: UserSessionInterface) {

    companion object {
        private const val DEFAULT_RECIPE_ID = "0"
    }

    private val graphql by lazy { GraphqlUseCase<TopAdsTickerV2>(gqlRepository) }

    /**
     * @param recipeId id of the recipe
     * @param slug slug obtained from recipe url as identifier
     * @param warehouseId warehouseId obtained from address data
     */
    suspend fun execute(
    ): TopAdsTickerResponse {
        graphql.apply {
            setGraphqlQuery(TopAdsTicker)
            setTypeClass(TopAdsTickerV2::class.java)

            setRequestParams(RequestParams.create().apply {
                putString(PARAM_SHOP_ID, userSession.shopId)
                putString(PARAM_SOURCE, ANDROID)
                putString(PARAM_TARGET_PAGE, TICKER_SOURCE_DASHBOARD)
            }.parameters)

            val getTicker = executeOnBackground()
            return getTicker.topAdsTicker
        }
    }
}
