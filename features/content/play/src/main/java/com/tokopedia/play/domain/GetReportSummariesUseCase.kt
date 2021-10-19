package com.tokopedia.play.domain

import com.tokopedia.gql_query_annotation.GqlQuery
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
@GqlQuery(GetReportSummariesUseCase.QUERY_NAME, GetReportSummariesUseCase.QUERY)
class GetReportSummariesUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository
) : GraphqlUseCase<ReportSummaries>(graphqlRepository) {

    var params: HashMap<String, Any> = HashMap()

    override suspend fun executeOnBackground(): ReportSummaries {
        val gqlRequest = GraphqlRequest(GetReportSummariesUseCaseQuery.GQL_QUERY, ReportSummaries.Response::class.java, params)
        val gqlResponse = graphqlRepository.response(listOf(gqlRequest), GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())

        return gqlResponse.getData<ReportSummaries.Response>(ReportSummaries.Response::class.java).reportSummaries
    }

    companion object {

        private const val CHANNEL_ID = "channelId"
        const val QUERY_NAME = "GetReportSummariesUseCaseQuery"
        const val QUERY = """
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
        """

        fun createParam(channelId: String): HashMap<String, Any> {
            return hashMapOf(
                    CHANNEL_ID to channelId
            )
        }
    }
}