package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.sellerhomecommon.domain.mapper.MilestoneMapper
import com.tokopedia.sellerhomecommon.domain.model.DataKeyModel
import com.tokopedia.sellerhomecommon.domain.model.GetMilestoneDataResponse
import com.tokopedia.sellerhomecommon.presentation.model.MilestoneDataUiModel
import com.tokopedia.usecase.RequestParams

class GetMilestoneDataUseCase(
    private val gqlRepository: GraphqlRepository,
    private val mapper: MilestoneMapper
) : BaseGqlUseCase<List<MilestoneDataUiModel>>() {

    override suspend fun executeOnBackground(): List<MilestoneDataUiModel> {
        val gqlRequest = GraphqlRequest(
            QUERY, GetMilestoneDataResponse::class.java,
            params.parameters
        )
        val gqlResponse = gqlRepository.getReseponse(listOf(gqlRequest), cacheStrategy)

        /*val response = Gson().fromJson(DUMMY, GetMilestoneDataResponse::class.java)

        val data = mapper.mapMilestoneResponseToUiModel(
            response.fetchMilestoneWidgetData?.data.orEmpty(),
            false
        )
        return data*/
        val gqlErrors = gqlResponse.getError(GetMilestoneDataResponse::class.java)
        if (gqlErrors.isNullOrEmpty()) {
            val response: GetMilestoneDataResponse? = gqlResponse.getData<GetMilestoneDataResponse>(
                GetMilestoneDataResponse::class.java
            )
            response?.let {
                val isFromCache = cacheStrategy.type == CacheType.CACHE_ONLY
                return mapper.mapMilestoneResponseToUiModel(
                    it.fetchMilestoneWidgetData?.data.orEmpty(),
                    isFromCache
                )
            }
            throw NullPointerException("milestone widget data can not be null")
        } else {
            throw RuntimeException(gqlErrors.firstOrNull()?.message.orEmpty())
        }
    }

    companion object {
        private const val DATA_KEYS = "dataKeys"

        private val QUERY = """
            query fetchMilestoneWidgetData(${'$'}dataKeys: [dataKey!]!) {
              fetchMilestoneWidgetData(dataKeys: ${'$'}dataKeys) {
                data {
                  dataKey
                  title
                  subtitle
                  backgroundColor
                  backgroundImageUrl
                  showNumber
                  progressBar {
                    description
                    percentage
                    percentageFormatted
                    taskCompleted
                    totalTask
                  }
                  mission {
                    imageUrl
                    title
                    subtitle
                    missionCompletionStatus
                    button {
                      title
                      urlType
                      url
                      applink
                      buttonStatus
                    }
                  }
                  finishMission {
                    imageUrl
                    title
                    subtitle
                    button {
                      title
                      urlType
                      url
                      applink
                      buttonStatus
                    }
                  }
                  cta {
                    text
                    url
                    applink
                  }
                  error
                  errorMsg
                  showWidget
                }
              }
            }
        """.trimIndent()

        fun createParams(dataKeys: List<String>): RequestParams = RequestParams.create().apply {
            val mDataKeys = dataKeys.map {
                DataKeyModel(
                    key = it,
                    jsonParams = "{}"
                )
            }
            putObject(DATA_KEYS, mDataKeys)
        }

        private val DUMMY = """
            {
              "fetchMilestoneWidgetData": {
                "data": [
                  {
                    "dataKey": "shopQuest",
                    "showWidget": true,
                    "title": "Misi berjualan tokopedia",
                    "subtitle": "Sebelum mulai berjualan, selesaikan misi berikut agar tokomu jadi lebih tepercaya!",
                    "backgroundColor": "#000000",
                    "backgroundImageUrl": "https://ecs7.tokopedia.net/seller-dashboard/sample.png",
                    "showNumber": false,
                    "progressBar": {
                      "description": "Kemajuan",
                      "percentage": 100,
                      "percentageFormatted": "0 dari 4 (0%)",
                      "taskCompleted": 1,
                      "totalTask": 1
                    },
                    "mission": [
                      {
                        "imageUrl": "https://ecs7.tokopedia.net/seller-dashboard/sample.png",
                        "title": "Tambah lebih banyak produk",
                        "subtitle": "Maksimalkan pengalaman belanja di tokomu dengan tambah 4 produk lagi, ya!",
                        "missionCompletionStatus": false,
                        "button": {
                          "title": "Tambah Produk",
                          "urlType": 1,
                          "url": "www.tokopedia.com",
                          "applink": "www.tokopedia.com",
                          "buttonStatus": 1
                        }
                      },
                      {
                        "imageUrl": "https://ecs7.tokopedia.net/seller-dashboard/sample.png",
                        "title": "Tambah lebih banyak produk",
                        "subtitle": "Maksimalkan pengalaman belanja di tokomu dengan tambah 4 produk lagi, ya!",
                        "missionCompletionStatus": false,
                        "button": {
                          "title": "Tambah Produk",
                          "urlType": 1,
                          "url": "www.tokopedia.com",
                          "applink": "www.tokopedia.com",
                          "buttonStatus": 1
                        }
                      },
                      {
                        "imageUrl": "https://ecs7.tokopedia.net/seller-dashboard/sample.png",
                        "title": "Tambah lebih banyak produk",
                        "subtitle": "Maksimalkan pengalaman belanja di tokomu dengan tambah 4 produk lagi, ya!",
                        "missionCompletionStatus": false,
                        "button": {
                          "title": "Tambah Produk",
                          "urlType": 1,
                          "url": "www.tokopedia.com",
                          "applink": "www.tokopedia.com",
                          "buttonStatus": 1
                        }
                      },
                      {
                        "imageUrl": "https://ecs7.tokopedia.net/seller-dashboard/sample.png",
                        "title": "Tambah lebih banyak produk",
                        "subtitle": "Maksimalkan pengalaman belanja di tokomu dengan tambah 4 produk lagi, ya!",
                        "missionCompletionStatus": false,
                        "button": {
                          "title": "Tambah Produk",
                          "urlType": 1,
                          "url": "www.tokopedia.com",
                          "applink": "www.tokopedia.com",
                          "buttonStatus": 1
                        }
                      }
                    ],
                    "finishMission": {},
                    "cta": {
                      "text": "Selengkapnya",
                      "url": "www.tokopedia.com",
                      "applink": "www.tokopedia.com"
                    },
                    "error": false,
                    "errorMsg": ""
                  }
                ]
              }
            }
        """.trimIndent()
    }
}