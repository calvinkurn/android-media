package com.tokopedia.sellerhomecommon.domain.usecase

import com.google.gson.Gson
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

        val response = Gson().fromJson(DUMMY, GetMilestoneDataResponse::class.java)

        val data = mapper.mapMilestoneResponseToUiModel(
            response.fetchMilestoneWidgetData?.data.orEmpty(),
            false
        )
        return data
        val gqlRequest = GraphqlRequest(
            QUERY, GetMilestoneDataResponse::class.java,
            params.parameters
        )
        val gqlResponse = gqlRepository.getReseponse(listOf(gqlRequest), cacheStrategy)

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
                      "title": "Yuk, jadikan tokomu pilihan pembeli 👍",
                      "subtitle": "Ikuti langkah-langkah mudah berikut agar pembeli senang berbelanja di tokomu!",
                      "backgroundColor": "",
                      "backgroundImageUrl": "",
                      "showNumber": false,
                      "progressBar": {
                        "description": "Kemajuan",
                        "percentage": 0,
                        "percentageFormatted": "0 dari 8 <b> (0 %) </b>",
                        "taskCompleted": 2,
                        "totalTask": 8
                      },
                      "mission": [
                        {
                          "imageUrl": "https://images.tokopedia.net/img/feature-onboarding/icons/Illustration@2x.png",
                          "title": "Tokomu butuh produk pertama, nih",
                          "subtitle": "Tambahkan produk pertama supaya tokomu bisa mulai berjualan",
                          "missionCompletionStatus": false,
                          "button": {
                            "title": "Tambah",
                            "urlType": 1,
                            "url": "https://seller-staging.tokopedia.com/add-product",
                            "applink": "sellerapp://gold-merchant-statistic-dashboard",
                            "buttonStatus": 1
                          }
                        },
                        {
                          "imageUrl": "https://images.tokopedia.net/img/feature-onboarding/icons/toko2x.png",
                          "title": "Pentingnya Verifikasi akun",
                          "subtitle": "Segera verifikasi akun tokomu agar pembeli semakin nyaman berbelanja di tokomu, ya.",
                          "missionCompletionStatus": false,
                          "button": {
                            "title": "Verifikasi",
                            "urlType": 1,
                            "url": "https://seller.tokopedia.com/edu/verifikasi-toko/",
                            "applink": "http://any.link",
                            "buttonStatus": 1
                          }
                        },
                        {
                          "imageUrl": "https://images.tokopedia.net/img/feature-onboarding/icons/toko2x.png",
                          "title": "Tambah menarik dengan Voucher Toko",
                          "subtitle": "Bikin voucher cashback atau gratis ongkir sebagai magnet untuk menarik banyak pembeli.",
                          "missionCompletionStatus": false,
                          "button": {
                            "title": "Buat Voucher",
                            "urlType": 1,
                            "url": "https://seller-staging.tokopedia.com/promo",
                            "applink": "sellerapp://create-voucher",
                            "buttonStatus": 1
                          }
                        },
                        {
                          "imageUrl": "https://images.tokopedia.net/img/feature-onboarding/icons/Illustration@2x.png",
                          "title": "Yuk, tambah lebih banyak produk",
                          "subtitle": "Maksimalkan pengalaman berbelanja pembeli di tokomu dengan tambah 4 produk lagi.",
                          "missionCompletionStatus": false,
                          "button": {
                            "title": "Tambah",
                            "urlType": 1,
                            "url": "https://seller-staging.tokopedia.com/add-product",
                            "applink": "http://any.link",
                            "buttonStatus": 0
                          }
                        },
                        {
                          "imageUrl": "https://images.tokopedia.net/img/feature-onboarding/icons/toko2x.png",
                          "title": "Promosikan tokomu di media sosial",
                          "subtitle": "Sebarkan kabar kalau tokomu sudah buka dan siap untuk berjualan.",
                          "missionCompletionStatus": false,
                          "button": {
                            "title": "Share",
                            "urlType": 3,
                            "url": "sharing",
                            "applink": "",
                            "buttonStatus": 2
                          }
                        },
                        {
                          "imageUrl": "https://images.tokopedia.net/img/feature-onboarding/icons/toko2x.png",
                          "title": "Upgrade ke Power Merchant",
                          "subtitle": "Akses fitur khusus agar tokomu jadi lebih tepercaya dan tingkatkan penjualan.",
                          "missionCompletionStatus": false,
                          "button": {
                            "title": "Upgrade",
                            "urlType": 1,
                            "url": "https://seller-staging.tokopedia.com/settings/power-merchant",
                            "applink": "http://any.link",
                            "buttonStatus": 1
                          }
                        },
                        {
                          "imageUrl": "https://images.tokopedia.net/img/feature-onboarding/palugada.png",
                          "title": "Cek Performa Toko",
                          "subtitle": "Pelajari Performa Toko untuk memberikan kualitas layanan terbaik ke pembelimu!",
                          "missionCompletionStatus": false,
                          "button": {
                            "title": "Cek Performa Toko",
                            "urlType": 1,
                            "url": "https://api-staging.tokopedia.com/shopquest/mission/palugada?mission_id=34",
                            "applink": "https://seller.tokopedia.com/shop-score-page",
                            "buttonStatus": 1
                          }
                        },
                        {
                          "imageUrl": "https://images.tokopedia.net/img/feature-onboarding/palugada.png",
                          "title": "Pelajari tentang HKI, yuk!",
                          "subtitle": "Hindari jualan produk palsu/melanggar Hak Kekayaan Intelektual agar produk tidak dihapus",
                          "missionCompletionStatus": false,
                          "button": {
                            "title": "Baca Sekarang",
                            "urlType": 1,
                            "url": "https://api-staging.tokopedia.com/shopquest/mission/palugada?mission_id=39",
                            "applink": "",
                            "buttonStatus": 1
                          }
                        }
                      ],
                      "finishMission": {
                        "imageUrl": "",
                        "title": "",
                        "subtitle": "",
                        "button": {
                          "title": "",
                          "urlType": 0,
                          "url": "",
                          "applink": "",
                          "buttonStatus": 0
                        }
                      },
                      "cta": {
                        "text": "",
                        "url": "",
                        "applink": ""
                      },
                      "error": false,
                      "errorMsg": "",
                      "showWidget": true
                    }
                  ]
                }
              }
        """.trimIndent()
    }
}