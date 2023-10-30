package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sellerhomecommon.domain.model.GetRewardDetailByIdResponse

private const val QUERY = """
    query GetRewardDetailById(${'$'}rewardID: Int64!, ${'$'}source: String!) {
      getRewardDetailByID(
        Input: {
          rewardID: ${'$'}rewardID,
          source: ${'$'}source
        }
      ) {
        result {
          rewardID
          rewardTitle
          rewardSubtitle
          benefitList {
            benefitName
            benefitValue
          }
          isUnlimited
          rewardEstimationPrice
          rewardImage
          metadata
        }
        error {
          message
          code
        }
      }
    }
"""

@GqlQuery("GetRewardDetailById", QUERY)
class GetRewardDetailByIdUseCase(
    gqlRepository: GraphqlRepository
) : GraphqlUseCase<GetRewardDetailByIdResponse>(gqlRepository) {

    init {
        setTypeClass(GetRewardDetailByIdResponse::class.java)
        setGraphqlQuery(GetRewardDetailById())
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST).build())
    }

    suspend fun execute(rewardId: Long): GetRewardDetailByIdResponse {
        val params = generateParams(rewardId)
        setRequestParams(params)

        val response = executeOnBackground()

        if (response.data.error.message.isBlank()) {
            return response
        } else {
            throw MessageErrorException(response.data.error.message)
        }
    }

    companion object {
        private const val REWARD_ID_KEY = "rewardID"
        private const val SOURCE_KEY = "source"
        private const val ANDROID_SOURCE = "android"

        private fun generateParams(rewardId: Long): Map<String, Any> {
            return mapOf(
                REWARD_ID_KEY to rewardId,
                SOURCE_KEY to ANDROID_SOURCE
            )
        }
    }
}
