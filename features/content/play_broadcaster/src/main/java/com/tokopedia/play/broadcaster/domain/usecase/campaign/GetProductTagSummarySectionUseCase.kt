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

        const val QUERY_NAME = "GetProductTagSummarySectionUseCaseQuery"
        const val QUERY = """
            query GetProductTagSummarySection(${"$${PARAM_CHANNEL_ID}"}: Int,) {
                broadcasterGetProductTagSection(req: {
                    channelID: ${"$${PARAM_CHANNEL_ID}"}
                }) {
                    section {
                        name,
                        statusFmt,
                        products {
                            productID,
                            productName,
                            imageURL,
                            price,
                            priceFmt,
                            discount,
                            originalPrice,
                            originalPriceFmt,
                            quantity
                        }
                    }
                }
            }
        """

        fun createparams(
            channelID: Int,
        ): Map<Any, Any> {
            return mapOf(
                PARAM_CHANNEL_ID to channelID
            )
        }
    }
}