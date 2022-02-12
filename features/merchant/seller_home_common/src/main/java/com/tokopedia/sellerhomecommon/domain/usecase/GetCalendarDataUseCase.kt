package com.tokopedia.sellerhomecommon.domain.usecase

import com.google.gson.Gson
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sellerhomecommon.domain.gqlquery.GqlGetCalendarData
import com.tokopedia.sellerhomecommon.domain.mapper.CalendarMapper
import com.tokopedia.sellerhomecommon.presentation.model.CalendarFilterDataKeyUiModel
import com.tokopedia.sellerhomecommon.domain.model.DataKeyModel
import com.tokopedia.sellerhomecommon.domain.model.GetCalendarDataResponse
import com.tokopedia.sellerhomecommon.presentation.model.CalendarDataUiModel
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

        fun createParams(dataKeys: List<CalendarFilterDataKeyUiModel>): RequestParams {
            return RequestParams.create().apply {
                val mDataKeys = dataKeys.map {
                    DataKeyModel(
                        key = it.dataKey,
                        jsonParams = """
                          {
                            start_date: ${it.startDate},
                            end_date: ${it.endDate}
                          }
                        """.trimIndent()
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
        return dummy()
        /*val typeClass = GetCalendarDataResponse::class.java
        val gqlRequest = GraphqlRequest(GqlGetCalendarData, typeClass, params.parameters)
        val gqlResponse = gqlRepository.response(listOf(gqlRequest), cacheStrategy)

        val gqlErrors = gqlResponse.getError(typeClass)
        if (gqlErrors.isNullOrEmpty()) {
            val responseData = gqlResponse.getData<GetCalendarDataResponse>(typeClass)
            val isFromCache = cacheStrategy.type == CacheType.CACHE_ONLY
            return calendarMapper.mapRemoteDataToUiData(responseData, isFromCache)
        } else {
            throw MessageErrorException(gqlErrors.firstOrNull()?.message.orEmpty())
        }*/
    }

    private fun dummy(): List<CalendarDataUiModel> {
        val response: GetCalendarDataResponse =
            Gson().fromJson(dummyResponse, GetCalendarDataResponse::class.java)
        return calendarMapper.mapRemoteDataToUiData(response, true)
    }

    private val dummyResponse = """
        {
          "fetchCalendarWidgetData": {
            "data": [
              {
                "dataKey": "sellerCalendarEvent",
                "events": [
                  {
                    "eventName": "Judul maksimal 2 baris, boleh pakai 1 emoji 🎉 seperti contoh text ini",
                    "description": "Untuk desktipsi event/campaign maksimal 2 baris, tidak ada emoji.",
                    "label": "campaign",
                    "startDate": "25-01-2022",
                    "endDate": "31-01-2022",
                    "url": "tokopedia.com",
                    "applink": "tokopedia://gold-merchant-statistic-dashboard"
                  },
                  {
                    "eventName": "Judul maksimal 2 baris, boleh pakai 1 emoji 🎉 seperti contoh text ini",
                    "description": "Untuk desktipsi event/campaign maksimal 2 baris, tidak ada emoji.",
                    "label": "campaign",
                    "startDate": "25-01-2022",
                    "endDate": "31-01-2022",
                    "url": "tokopedia.com",
                    "applink": "tokopedia://gold-merchant-statistic-dashboard"
                  },
                  {
                    "eventName": "Judul maksimal 2 baris, boleh pakai 1 emoji 🎉 seperti contoh text ini",
                    "description": "Untuk desktipsi event/campaign maksimal 2 baris, tidak ada emoji.",
                    "label": "campaign",
                    "startDate": "25-01-2022",
                    "endDate": "31-01-2022",
                    "url": "tokopedia.com",
                    "applink": "tokopedia://gold-merchant-statistic-dashboard"
                  },
                  {
                    "eventName": "Judul maksimal 2 baris, boleh pakai 1 emoji 🎉 seperti contoh text ini",
                    "description": "Untuk desktipsi event/campaign maksimal 2 baris, tidak ada emoji.",
                    "label": "campaign",
                    "startDate": "25-01-2022",
                    "endDate": "31-01-2022",
                    "url": "tokopedia.com",
                    "applink": "tokopedia://gold-merchant-statistic-dashboard"
                  },
                  {
                    "eventName": "Judul maksimal 2 baris, boleh pakai 1 emoji 🎉 seperti contoh text ini",
                    "description": "Untuk desktipsi event/campaign maksimal 2 baris, tidak ada emoji.",
                    "label": "campaign",
                    "startDate": "25-01-2022",
                    "endDate": "31-01-2022",
                    "url": "tokopedia.com",
                    "applink": "tokopedia://gold-merchant-statistic-dashboard"
                  },
                  {
                    "eventName": "Judul maksimal 2 baris, boleh pakai 1 emoji 🎉 seperti contoh text ini",
                    "description": "Untuk desktipsi event/campaign maksimal 2 baris, tidak ada emoji.",
                    "label": "campaign",
                    "startDate": "25-01-2022",
                    "endDate": "31-01-2022",
                    "url": "tokopedia.com",
                    "applink": "tokopedia://gold-merchant-statistic-dashboard"
                  },
                  {
                    "eventName": "Judul maksimal 2 baris, boleh pakai 1 emoji 🎉 seperti contoh text ini",
                    "description": "Untuk desktipsi event/campaign maksimal 2 baris, tidak ada emoji.",
                    "label": "campaign",
                    "startDate": "25-01-2022",
                    "endDate": "31-01-2022",
                    "url": "tokopedia.com",
                    "applink": "tokopedia://gold-merchant-statistic-dashboard"
                  },
                  {
                    "eventName": "Judul maksimal 2 baris, boleh pakai 1 emoji 🎉 seperti contoh text ini",
                    "description": "Untuk desktipsi event/campaign maksimal 2 baris, tidak ada emoji.",
                    "label": "campaign",
                    "startDate": "25-01-2022",
                    "endDate": "25-01-2022",
                    "url": "tokopedia.com",
                    "applink": "tokopedia://gold-merchant-statistic-dashboard"
                  },
                  {
                    "eventName": "Judul maksimal 2 baris, boleh pakai 1 emoji 🎉 seperti contoh text ini",
                    "description": "Untuk desktipsi event/campaign maksimal 2 baris, tidak ada emoji.",
                    "label": "campaign",
                    "startDate": "25-01-2022",
                    "endDate": "25-01-2022",
                    "url": "tokopedia.com",
                    "applink": "tokopedia://gold-merchant-statistic-dashboard"
                  }
                ],
                "error": false,
                "errorMsg": "",
                "showWidget": true
              }
            ]
          }
        }
    """.trimIndent()
}