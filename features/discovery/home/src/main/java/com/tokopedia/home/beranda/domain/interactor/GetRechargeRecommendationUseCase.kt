package com.tokopedia.home.beranda.domain.interactor

import com.google.gson.Gson
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.home.beranda.domain.model.recharge_recommendation.RechargeRecommendation
import com.tokopedia.network.exception.MessageErrorException
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

        const val NULL_RESPONSE = "null response"
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
                  title: Title
                  backgroundColor: BackgroundColor
                  buttonText: ButtonText
                }
              }
            }
        """.trimIndent()
    }
    //endregion

    private val dummyResponse by lazy {
        "{\n" +
                "            \"rechargeRecommendation\": {\n" +
                "            \"UUID\": \"recharge_watf_6757678\",\n" +
                "            \"recommendations\": [\n" +
                "            {\n" +
                "                \"contentID\": \"2\",\n" +
                "                \"mainText\": \"Hi, Resa!\",\n" +
                "                \"subText\": \"Tagihan Listrik PLN sebesar Rp 269.939 jatuh tempo pada hari ini\",\n" +
                "                \"applink\": \"tokopedia://digital/form?category_id=3&client_number=547300824920&is_from_widget=true&operator_id=18&product_id=291\",\n" +
                "                \"link\": \"https://pulsa.tokopedia.com?action=init_data&client_number=547300824920&instant_checkout=false&operator_id=18&product_id=291\",\n" +
                "                \"iconURL\": \"https://ecs7.tokopedia.net/img/cache/100-square/attachment/2019/11/15/21181130/21181130_d593aa6d-5f53-4674-b1da-5988041541f4.png\",\n" +
                "                \"title\": \"Yuk, segera bayar tagihanmu!\",\n" +
                "                \"backgroundColor\": \"#ff8b00\",\n" +
                "                \"buttonText\": \"Bayar Sekarang\"\n" +
                "            }\n" +
                "            ]\n" +
                "        }\n" +
                "        }"
    }

    override suspend fun executeOnBackground(): RechargeRecommendation {
        graphqlUseCase.clearCache()
        graphqlUseCase.setGraphqlQuery(query)
        graphqlUseCase.setRequestParams(params.parameters)
        //val response = graphqlUseCase.executeOnBackground().response

        //if (response != null) return response
       // else throw (MessageErrorException(NULL_RESPONSE))
        return Gson().fromJson(dummyResponse, RechargeRecommendation.Response::class.java).response!!
    }

    fun setParams(type: Int = DEFAULT_TYPE) {
        params.parameters.clear()
        params.putInt(PARAM_TYPE, type)
    }
}