package com.tokopedia.sellerhomecommon.domain.usecase

import com.google.gson.Gson
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.sellerhomecommon.domain.mapper.PostMapper
import com.tokopedia.sellerhomecommon.domain.model.DataKeyModel
import com.tokopedia.sellerhomecommon.domain.model.DynamicParameterModel
import com.tokopedia.sellerhomecommon.domain.model.GetPostDataResponse
import com.tokopedia.sellerhomecommon.presentation.model.PostListDataUiModel
import com.tokopedia.usecase.RequestParams

/**
 * Created By @ilhamsuaib on 21/05/20
 */

class GetPostDataUseCase(
        gqlRepository: GraphqlRepository,
        postMapper: PostMapper,
        dispatchers: CoroutineDispatchers
) : CloudAndCacheGraphqlUseCase<GetPostDataResponse, List<PostListDataUiModel>>(
        gqlRepository, postMapper, dispatchers, GetPostDataResponse::class.java, QUERY, false) {

    override suspend fun executeOnBackground(requestParams: RequestParams, includeCache: Boolean) {
        super.executeOnBackground(requestParams, includeCache).also { isFirstLoad = false }
    }

    override suspend fun executeOnBackground(): List<PostListDataUiModel> {
        val gqlRequest = GraphqlRequest(QUERY, GetPostDataResponse::class.java, params.parameters)
        val gqlResponse: GraphqlResponse = graphqlRepository.getReseponse(listOf(gqlRequest), cacheStrategy)

        val errors: List<GraphqlError>? = gqlResponse.getError(GetPostDataResponse::class.java)
        if (errors.isNullOrEmpty()) {
            //val data = gqlResponse.getData<GetPostDataResponse>()
            val dummy: GetPostDataResponse = Gson().fromJson(DUMMY, GetPostDataResponse::class.java).copy()
            return mapper.mapRemoteDataToUiData(dummy, cacheStrategy.type == CacheType.CACHE_ONLY)
        } else {
            throw MessageErrorException(errors.joinToString(", ") { it.message })
        }
    }

    companion object {
        private const val DATA_KEYS = "dataKeys"
        private const val DEFAULT_POST_LIMIT = 3

        fun getRequestParams(
                dataKey: List<Pair<String, String>>,
                dynamicParameter: DynamicParameterModel,
                limit: Int = DEFAULT_POST_LIMIT
        ): RequestParams = RequestParams.create().apply {
            val dataKeys = dataKey.map {
                DataKeyModel(
                        key = it.first,
                        jsonParams = dynamicParameter.copy(limit = limit, postFilter = it.second).toJsonString()
                )
            }
            putObject(DATA_KEYS, dataKeys)
        }

        private val QUERY = """
            query getPostWidgetData(${'$'}dataKeys: [dataKey!]!) {
              fetchPostWidgetData(dataKeys: ${'$'}dataKeys) {
                data {
                  datakey
                  list {
                    title
                    url
                    applink
                    subtitle
                    featuredMediaURL
                    stateMediaURL
                    stateText
                  }
                  cta{
                    text
                    applink
                  }
                  error
                  errorMsg
                  showWidget
                  emphasizeType
                }
              }
            }
        """.trimIndent()

        private val DUMMY = """
            {
                "fetchPostWidgetData": {
                  "data": [
                    {
                      "datakey": "dummyEmphasize",
                      "list": [
                        {
                          "title": "Gunakan Topads untuk meraih lebih banyak pengunjung yuk!",
                          "url": "https://tokopedia.com",
                          "applink": "tokopedia://webview?url=https://tokopedia.com",
                          "subtitle": "Rekomendasi Fitur",
                          "featuredMediaURL": "https://images.tokopedia.net/img/broadcast_chat@3x.png",
                          "stateMediaURL": "https://images.tokopedia.net/img/warningrecom.png",
                          "stateText": "Pengunjung di tokomu terus menurun"
                        },
                        {
                          "title": "Baca artikel tips nama produk agar mudah dicari disini!",
                          "url": "https://tokopedia.com",
                          "applink": "tokopedia://webview?url=https://tokopedia.com",
                          "subtitle": "Rekomendasi Artikel",
                          "featuredMediaURL": "https://images.tokopedia.net/img/artikel@3x.png",
                          "stateMediaURL": "https://images.tokopedia.net/img/warningrecom.png",
                          "stateText": "Produkmu sedikit dicari minggu ini"
                        },
                        {
                          "title": "Tonton sharing session `Tips Memulai Berjualan` bersama Top Seller",
                          "url": "https://tokopedia.com",
                          "applink": "tokopedia://webview?url=https://tokopedia.com",
                          "subtitle": "Rekomendasi Event",
                          "featuredMediaURL": "https://images.tokopedia.net/img/event@3x.png",
                          "stateMediaURL": "https://images.tokopedia.net/img/opportunity.png",
                          "stateText": "Baru memulai berjualan?"
                        }
                      ],
                      "cta": {
                        "text": "",
                        "applink": ""
                      },
                      "error": false,
                      "errorMsg": "",
                      "showWidget": true,
                      "emphasizeType": 1
                    },
                    {
                      "datakey": "sellerInfo",
                      "list": [
                        {
                          "title": "Gunakan Topads untuk meraih lebih banyak pengunjung yuk!",
                          "url": "https://tokopedia.com",
                          "applink": "tokopedia://webview?url=https://tokopedia.com",
                          "subtitle": "Rekomendasi Fitur",
                          "featuredMediaURL": "https://images.tokopedia.net/img/broadcast_chat@3x.png",
                          "stateMediaURL": "https://images.tokopedia.net/img/warningrecom.png",
                          "stateText": "Pengunjung di tokomu terus menurun"
                        },
                        {
                          "title": "Baca artikel tips nama produk agar mudah dicari disini!",
                          "url": "https://tokopedia.com",
                          "applink": "tokopedia://webview?url=https://tokopedia.com",
                          "subtitle": "Rekomendasi Artikel",
                          "featuredMediaURL": "https://images.tokopedia.net/img/artikel@3x.png",
                          "stateMediaURL": "https://images.tokopedia.net/img/warningrecom.png",
                          "stateText": "Produkmu sedikit dicari minggu ini"
                        },
                        {
                          "title": "Tonton sharing session `Tips Memulai Berjualan` bersama Top Seller",
                          "url": "https://tokopedia.com",
                          "applink": "tokopedia://webview?url=https://tokopedia.com",
                          "subtitle": "Rekomendasi Event",
                          "featuredMediaURL": "https://images.tokopedia.net/img/event@3x.png",
                          "stateMediaURL": "https://images.tokopedia.net/img/opportunity.png",
                          "stateText": "Baru memulai berjualan?"
                        }
                      ],
                      "cta": {
                        "text": "",
                        "applink": ""
                      },
                      "error": false,
                      "errorMsg": "",
                      "showWidget": true,
                      "emphasizeType": 0
                    }
                  ]
                }
              }
        """.trimIndent()
    }
}