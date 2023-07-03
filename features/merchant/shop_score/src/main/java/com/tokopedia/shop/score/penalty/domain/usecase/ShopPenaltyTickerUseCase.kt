package com.tokopedia.shop.score.penalty.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.shop.score.penalty.domain.response.GetTargetedTicker
import com.tokopedia.shop.score.penalty.domain.response.ShopPenaltyTickerResponse
import com.tokopedia.shop.score.penalty.presentation.fragment.ShopPenaltyPageType
import timber.log.Timber
import javax.inject.Inject

private const val QUERY =
    """
        query ShopPenaltyTickerQuery(${'$'}page: String!, ${'$'}target: [GetTargetedTickerRequestTarget]!) {
           GetTargetedTicker(input:{Page: ${'$'}page, Target: ${'$'}target}) {
              List {
                  Title
                  Content
                  Action {
                    Label
                    Type
                    WebURL
                  }
                }
           }
        }
    """

@GqlQuery("ShopPenaltyTickerQuery", QUERY)
class ShopPenaltyTickerUseCase @Inject constructor(
    gqlRepository: GraphqlRepository
): GraphqlUseCase<ShopPenaltyTickerResponse>(gqlRepository) {

    init {
        setTypeClass(ShopPenaltyTickerResponse::class.java)
        setGraphqlQuery(ShopPenaltyTickerQuery())
    }

    suspend fun execute(@ShopPenaltyPageType pageType: String): List<GetTargetedTicker.TickerResponse> {
        return if (pageType == ShopPenaltyPageType.ONGOING) {
            setRequestParams(createParams())
            try {
                executeOnBackground().getTargetedTicker?.tickers.orEmpty()
            } catch (ex: Exception) {
                Timber.e(ex)
                listOf()
            }
        } else {
            listOf()
        }
    }

    companion object {

        private const val SHOP_SCORE_PENALTY_KEY = "seller.shop-score-penalty"
        private const val PAGE_KEY = "page"
        private const val TARGET_KEY = "target"

        fun createParams(): Map<String, Any> {
            return mapOf(
                PAGE_KEY to SHOP_SCORE_PENALTY_KEY,
                TARGET_KEY to emptyList<String>()
            )
        }
    }

}
