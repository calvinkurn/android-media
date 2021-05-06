package com.tokopedia.play.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.play.data.ReportSummaries
import javax.inject.Inject

/**
 * Created by jegul on 28/01/21
 */
class GetReportSummariesUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository
) : GraphqlUseCase<ReportSummaries>(graphqlRepository) {

    var params: HashMap<String, Any> = HashMap()

    override suspend fun executeOnBackground(): ReportSummaries {
        val gqlRequest = GraphqlRequest(query, ReportSummaries.Response::class.java, params)
        val gqlResponse = graphqlRepository.getReseponse(listOf(gqlRequest), GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())

        return gqlResponse.getData<ReportSummaries.Response>(ReportSummaries.Response::class.java).reportSummaries
    }

    companion object {

        private const val CHANNEL_ID = "channelId"
        private val query = getQuery()

        private fun getQuery() : String =  """
             query GetReportSummaries(${'$'}channelId: String!){
              broadcasterReportSummariesBulk(channelIDs: [${'$'}channelId]) {
                reportData {
                  channel {
                    metrics {
                      totalLike
                      totalLikeFmt
                      visitChannel
                      visitChannelFmt
                    }
                  }
                }
              }
            }
            """.trimIndent()

        fun createParam(channelId: String): HashMap<String, Any> {
            return hashMapOf(
                    CHANNEL_ID to channelId
            )
        }
    }
}