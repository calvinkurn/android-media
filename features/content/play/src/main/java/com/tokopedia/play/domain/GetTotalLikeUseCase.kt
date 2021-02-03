package com.tokopedia.play.domain

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.play.data.TotalLike
import com.tokopedia.play.data.TotalLikeContent
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * Created by mzennis on 2019-12-03.
 */

class GetTotalLikeUseCase @Inject constructor(private val gqlUseCase: GraphqlRepository) : UseCase<TotalLike>() {

    var params: HashMap<String, Any> = HashMap()

    override suspend fun executeOnBackground(): TotalLike {
        val gqlRequest = GraphqlRequest(query, TotalLikeContent.Response::class.java, params)
        val gqlResponse = gqlUseCase.getReseponse(listOf(gqlRequest), GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())
        val response = gqlResponse.getData<TotalLikeContent.Response>(TotalLikeContent.Response::class.java)

        return try {
            val metric = response.totalLikeContent.data.first().channel.metrics
            TotalLike(
                    totalLike = metric.value.toIntOrZero(),
                    totalLikeFormatted = metric.fmt
            )
        } catch (e: Throwable) {
            TotalLike(0, "0")
        }
    }

    companion object {

        private const val CHANNEL_ID = "channelId"
        private val query = getQuery()

        private fun getQuery() : String =  """
             query GetTotalLikes(${'$'}channelId: String!){
              broadcasterReportSummariesBulk(channelIDs: [${'$'}channelId]) {
                reportData {
                  channel {
                    metrics {
                      totalLike
                      totalLikeFmt
                    }
                  }
                }
              }
            }
            """

        fun createParam(channelId: String): HashMap<String, Any> {
            return hashMapOf(
                    CHANNEL_ID to channelId
            )
        }
    }
}
