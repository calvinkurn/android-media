package com.tokopedia.home.beranda.domain.interactor

import com.google.gson.Gson
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.home.beranda.domain.model.recharge_recommendation.RechargeRecommendation
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * @author by resakemal on 2020-03-09
 */

class GetRechargeRecommendationUseCase @Inject constructor(
        private val graphqlUseCase: GraphqlUseCase<RechargeRecommendation.Response>)
    : UseCase<RechargeRecommendation>() {
    init {
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        graphqlUseCase.setTypeClass(RechargeRecommendation.Response::class.java)
    }
    companion object {
        const val PARAM_TYPE = "type"
        const val DEFAULT_TYPE = 1
    }

    private var params: RequestParams = RequestParams.create()

    //region query
    private val query by lazy {
        val type = "\$type"

        """
            query rechargeRecommendation($type: Int!) {
              rechargeRecommendation(recommendationType: $type) {
                #LIST OF RECOMMENDATION
                UUID # recharge_watf_userID, used as recommendation hash_key
                recommendations: Recommendations {
                  contentID: ContentID
                  mainText: MainText
                  subText: SubText
                  applink: AppLink
                  link: Link
                  iconURL: IconURL
                }
              }
            }
        """.trimIndent()
    }

    private val dummyQuery by lazy {
        """
            {
                "rechargeRecommendation": {
                  "UUID": "recharge_watf_1234567",
                  "recommendations": [
                    {
                      "contentID": "70_0812345678",
                      "mainText": "Tagihan Pulsa anda: 0812345678 sudah due",
                      "subText": "Rp 100.000",
                      "applink": "tokopedia://digital/form?category_id=1&client_number=0812345678&operator_id=12&product_id=70&is_from_widget=true",
                      "link": "https://pulsa.tokopedia.com/?action=init_data&amp;client_number=0812345678&amp;instant_checkout=false&amp;operator_id=12&amp;product_id=70&amp;slug=pulsa",
                      "iconURL": "https://ecs7.tokopedia.net/img/attachment/2019/10/22/21181130/21181130_31fffa3a-b61f-4b67-b183-785aef289a5b.png"
                    }
                  ]
                }
            }
        """.trimIndent()
    }
    //endregion

    override suspend fun executeOnBackground(): RechargeRecommendation {
        graphqlUseCase.clearCache()
        graphqlUseCase.setGraphqlQuery(query)
        graphqlUseCase.setRequestParams(params.parameters)
//        return graphqlUseCase.executeOnBackground().response
        return Gson().fromJson(dummyQuery, RechargeRecommendation.Response::class.java).response
    }

    fun setParams(type: Int = DEFAULT_TYPE) {
        params.parameters.clear()
        params.putInt(PARAM_TYPE, type)
    }
}