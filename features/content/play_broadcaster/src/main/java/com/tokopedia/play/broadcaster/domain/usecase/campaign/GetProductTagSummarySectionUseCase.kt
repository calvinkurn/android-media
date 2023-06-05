package com.tokopedia.play.broadcaster.domain.usecase.campaign

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.play.broadcaster.domain.model.campaign.GetProductTagSummarySectionResponse
import com.tokopedia.play_common.domain.usecase.RetryableGraphqlUseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on February 08, 2022
 */
@GqlQuery(GetProductTagSummarySectionUseCase.QUERY_NAME, GetProductTagSummarySectionUseCase.QUERY)
class GetProductTagSummarySectionUseCase @Inject constructor(
    gqlRepository: GraphqlRepository,
    private val dispatchers: CoroutineDispatchers,
) : RetryableGraphqlUseCase<GetProductTagSummarySectionResponse>(gqlRepository) {

    init {
        setGraphqlQuery(GetProductTagSummarySectionUseCaseQuery())
        setCacheStrategy(
            GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(GetProductTagSummarySectionResponse::class.java)
    }

    override suspend fun executeOnBackground() = withContext(dispatchers.io) {
        super.executeOnBackground()
    }

    companion object {
        private const val PARAM_CHANNEL_ID = "channelID"
        private const val PARAM_FETCH_COMMISSION = "fetchCommission"

        const val QUERY_NAME = "GetProductTagSummarySectionUseCaseQuery"
        const val QUERY = """
            query BroadcasterGetProductTagSection(
            ${"$${PARAM_CHANNEL_ID}"}: Int64!,
            ${"$${PARAM_FETCH_COMMISSION}"}: Boolean,
            ) {
                broadcasterGetProductTagSection(req: {
                    channelID: ${"$${PARAM_CHANNEL_ID}"},
                    fetchCommission: ${"$${PARAM_FETCH_COMMISSION}"}
                }) {
                    sections {
                        name
                        statusFmt
                        products {
                            productID
                            productName
                            hasCommission
                            commissionFmt
                            commission
                            extraCommission
                            imageURL
                            price
                            priceFmt
                            discount
                            originalPrice
                            originalPriceFmt
                            quantity
                            is_pinned: isPinned
                            is_pinnable: isPinnable
                            product_number: productNumber
                        }
                    }
                }
            }
        """

        fun createParams(
            channelID: String,
            fetchCommission: Boolean,
        ): Map<String, Any> {
            return mapOf(
                PARAM_CHANNEL_ID to channelID,
                PARAM_FETCH_COMMISSION to fetchCommission,
            )
        }
    }
}
