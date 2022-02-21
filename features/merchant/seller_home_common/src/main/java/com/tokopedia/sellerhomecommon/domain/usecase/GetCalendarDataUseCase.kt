package com.tokopedia.sellerhomecommon.domain.usecase

import com.google.gson.Gson
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sellerhomecommon.domain.gqlquery.GqlGetCalendarData
import com.tokopedia.sellerhomecommon.domain.mapper.CalendarMapper
import com.tokopedia.sellerhomecommon.domain.model.DataKeyModel
import com.tokopedia.sellerhomecommon.domain.model.GetCalendarDataResponse
import com.tokopedia.sellerhomecommon.presentation.model.CalendarDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.CalendarFilterDataKeyUiModel
import com.tokopedia.usecase.RequestParams

/**
 * Created by @ilhamsuaib on 07/02/22.
 */

class GetCalendarDataUseCase(
    private val gqlRepository: GraphqlRepository,
    private val calendarMapper: CalendarMapper,
    dispatchers: CoroutineDispatchers,
) : CloudAndCacheGraphqlUseCase<GetCalendarDataResponse, List<CalendarDataUiModel>>(
    gqlRepository,
    calendarMapper,
    dispatchers,
    GetCalendarDataResponse::class.java,
    GqlGetCalendarData.GQL_QUERY,
    false
) {

    companion object {
        private const val DATA_KEYS = "dataKeys"
        private const val KEY_START_DATE = "start_date"
        private const val KEY_END_DATE = "end_date"

        fun createParams(dataKeys: List<CalendarFilterDataKeyUiModel>): RequestParams {
            return RequestParams.create().apply {
                val mDataKeys = dataKeys.map {
                    val map = mapOf(
                        KEY_START_DATE to it.startDate,
                        KEY_END_DATE to it.endDate,
                    )
                    return@map DataKeyModel(
                        key = it.dataKey,
                        jsonParams = Gson().toJson(map)
                    )
                }
                putObject(DATA_KEYS, mDataKeys)
            }
        }
    }

    override suspend fun executeOnBackground(requestParams: RequestParams, includeCache: Boolean) {
        super.executeOnBackground(requestParams, includeCache).also { isFirstLoad = false }
    }

    override suspend fun executeOnBackground(): List<CalendarDataUiModel> {
        val typeClass = GetCalendarDataResponse::class.java
        val gqlRequest = GraphqlRequest(GqlGetCalendarData, typeClass, params.parameters)
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
}