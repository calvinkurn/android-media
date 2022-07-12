package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sellerhomecommon.domain.mapper.UnificationMapper
import com.tokopedia.sellerhomecommon.domain.model.GetUnificationDataResponse
import com.tokopedia.sellerhomecommon.presentation.model.UnificationDataUiModel
import com.tokopedia.usecase.RequestParams

/**
 * Created by @ilhamsuaib on 08/07/22.
 */

@GqlQuery("GetUnificationDataGqlQuery", GetUnificationDataUseCase.QUERY)
class GetUnificationDataUseCase(
    gqlRepository: GraphqlRepository,
    unificationMapper: UnificationMapper,
    dispatchers: CoroutineDispatchers
) : CloudAndCacheGraphqlUseCase<GetUnificationDataResponse, List<UnificationDataUiModel>>(
    gqlRepository, unificationMapper, dispatchers, GetMilestoneDataGqlQuery()
) {

    override val classType: Class<GetUnificationDataResponse>
        get() = GetUnificationDataResponse::class.java

    override suspend fun executeOnBackground(): List<UnificationDataUiModel> {
        val gqlRequest = GraphqlRequest(
            graphqlQuery,
            classType,
            params.parameters
        )
        val gqlResponse = graphqlRepository.response(listOf(gqlRequest), cacheStrategy)

        val errors = gqlResponse.getError(classType)
        if (errors.isNullOrEmpty()) {
            val data = gqlResponse.getData<GetUnificationDataResponse>()
            val isFromCache = cacheStrategy.type == CacheType.CACHE_ONLY
            return mapper.mapRemoteDataToUiData(data, isFromCache)
        } else {
            throw MessageErrorException(errors.firstOrNull()?.message.orEmpty())
        }
    }

    companion object {
        internal const val QUERY = """
            query fetchNavigationTabWidgetData(${'$'}dataKeys: [dataKey!]!) {
              fetchNavigationTabWidgetData(dataKeys: ${'$'}dataKeys) {
                data {
                  dataKey
                  tabs {
                    title
                    isNew
                    content {
                      widget_type
                      datakey
                      configuration
                      metricsParam
                    }
                  }
                  error
                  showWidget
                }
              }
            }
        """

        fun createParams(dataKeys: List<String>): RequestParams {
            return RequestParams.create()
        }
    }
}