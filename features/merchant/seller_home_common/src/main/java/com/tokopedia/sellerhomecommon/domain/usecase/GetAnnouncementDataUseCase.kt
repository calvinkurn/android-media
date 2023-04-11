package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sellerhomecommon.domain.mapper.AnnouncementMapper
import com.tokopedia.sellerhomecommon.domain.model.DataKeyModel
import com.tokopedia.sellerhomecommon.domain.model.GetAnnouncementDataResponse
import com.tokopedia.sellerhomecommon.presentation.model.AnnouncementDataUiModel
import com.tokopedia.usecase.RequestParams

/**
 * Created By @ilhamsuaib on 09/11/20
 */

@GqlQuery("GetAnnouncementDataGqlQuery", GetAnnouncementDataUseCase.QUERY)
class GetAnnouncementDataUseCase(
    gqlRepository: GraphqlRepository,
    mapper: AnnouncementMapper,
    dispatchers: CoroutineDispatchers
) : CloudAndCacheGraphqlUseCase<GetAnnouncementDataResponse, List<AnnouncementDataUiModel>>(
    gqlRepository, mapper, dispatchers, GetAnnouncementDataGqlQuery()
) {

    override val classType: Class<GetAnnouncementDataResponse>
        get() = GetAnnouncementDataResponse::class.java

    override suspend fun executeOnBackground(requestParams: RequestParams, includeCache: Boolean) {
        super.executeOnBackground(requestParams, includeCache).also { isFirstLoad = false }
    }

    override suspend fun executeOnBackground(): List<AnnouncementDataUiModel> {
        val gqlRequest = GraphqlRequest(graphqlQuery, classType, params.parameters)
        val gqlResponse = graphqlRepository.response(listOf(gqlRequest), cacheStrategy)

        val errors = gqlResponse.getError(classType)
        if (errors.isNullOrEmpty()) {
            val response = gqlResponse.getData<GetAnnouncementDataResponse>()
            val isFromCache = cacheStrategy.type == CacheType.CACHE_ONLY
            return mapper.mapRemoteDataToUiData(response, isFromCache)
        } else {
            throw MessageErrorException(errors.joinToString(", ") { it.message })
        }
    }

    companion object {
        internal const val QUERY = """
            query fetchAnnouncementWidgetData(${'$'}dataKeys: [dataKey!]!) {
              fetchAnnouncementWidgetData(dataKeys:${'$'}dataKeys) {
                data {
                  dataKey
                  errorMsg
                  title
                  showWidget
                  subtitle
                  imageUrl
                  button {
                    applink
                  }
                  widgetDataSign
                }
              }
            }
        """
        private const val DATA_KEYS = "dataKeys"

        fun createRequestParams(
            dataKey: List<String>
        ): RequestParams = RequestParams.create().apply {
            val dataKeys = dataKey.map {
                DataKeyModel(key = it)
            }
            putObject(DATA_KEYS, dataKeys)
        }
    }
}