package com.tokopedia.sellerhomecommon.domain.usecase

import com.google.gson.Gson
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sellerhomecommon.domain.mapper.CalendarMapper
import com.tokopedia.sellerhomecommon.domain.model.CalendarQueryParam
import com.tokopedia.sellerhomecommon.domain.model.DataKeyModel
import com.tokopedia.sellerhomecommon.domain.model.GetCalendarDataResponse
import com.tokopedia.sellerhomecommon.presentation.model.CalendarDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.CalendarFilterDataKeyUiModel
import com.tokopedia.usecase.RequestParams

/**
 * Created by @ilhamsuaib on 07/02/22.
 */

@GqlQuery("GetCalendarDataGqlQuery", GetCalendarDataUseCase.QUERY)
class GetCalendarDataUseCase(
    private val gqlRepository: GraphqlRepository,
    private val calendarMapper: CalendarMapper,
    dispatchers: CoroutineDispatchers,
) : CloudAndCacheGraphqlUseCase<GetCalendarDataResponse, List<CalendarDataUiModel>>(
    gqlRepository,
    calendarMapper,
    dispatchers,
    GetCalendarDataGqlQuery()
) {

    override val classType: Class<GetCalendarDataResponse>
        get() = GetCalendarDataResponse::class.java

    override suspend fun executeOnBackground(requestParams: RequestParams, includeCache: Boolean) {
        super.executeOnBackground(requestParams, includeCache).also { isFirstLoad = false }
    }

    override suspend fun executeOnBackground(): List<CalendarDataUiModel> {
        val typeClass = GetCalendarDataResponse::class.java
        val gqlRequest = GraphqlRequest(graphqlQuery, typeClass, params.parameters)
        val gqlResponse = gqlRepository.response(listOf(gqlRequest), cacheStrategy)

        val gqlErrors = gqlResponse.getError(typeClass)
        if (gqlErrors.isNullOrEmpty()) {
            val responseData = gqlResponse.getData<GetCalendarDataResponse>(typeClass)
            val isFromCache = cacheStrategy.type == CacheType.CACHE_ONLY
            return calendarMapper.mapRemoteDataToUiData(responseData, isFromCache)
        } else {
            throw MessageErrorException(gqlErrors.firstOrNull()?.message.orEmpty())
        }
    }

    companion object {
        internal const val QUERY = """
            query fetchCalendarWidgetData(${'$'}dataKeys: [dataKey!]!) {
              fetchCalendarWidgetData(dataKeys: ${'$'}dataKeys) {
                data{
                  dataKey
                  events {
                    eventName
                    description
                    label
                    startDate
                    endDate
                    url
                    applink
                  }
                  error
                  errorMsg
                  showWidget
                }
              }
            }
        """
        private const val DATA_KEYS = "dataKeys"

        fun createParams(dataKeys: List<CalendarFilterDataKeyUiModel>): RequestParams {
            return RequestParams.create().apply {
                val mDataKeys = dataKeys.map {
                    val dateRange = CalendarQueryParam(
                        startDate = it.getDateRange().startDate,
                        endDate = it.getDateRange().endDate
                    )
                    return@map DataKeyModel(
                        key = it.dataKey,
                        jsonParams = Gson().toJson(dateRange)
                    )
                }
                putObject(DATA_KEYS, mDataKeys)
            }
        }
    }
}