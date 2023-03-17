package com.tokopedia.topads.common.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.topads.common.constant.TopAdsCommonConstant.SHOPID
import com.tokopedia.topads.common.constant.TopAdsCommonConstant.SOURCE
import com.tokopedia.topads.common.data.response.AutoAdsResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

const val QUERY_TOP_ADS_GET_AUTO_ADS = """
        query topAdsGetAutoAdsV2(${'$'}shopID: String!, ${'$'}source: String!){
            topAdsGetAutoAdsV2(shopID: ${'$'}shopID, source: ${'$'}source){
                data {
                    shopID
                    status
                    statusDesc
                    dailyBudget
                    dailyUsage
                    type
                    typeDesc
                }
            }
        }
        """

@GqlQuery("GetAutoAds", QUERY_TOP_ADS_GET_AUTO_ADS)
class TopAdsGetAutoAdsUseCase @Inject constructor(
    val userSession: UserSessionInterface,
    private val graphqlUseCase: MultiRequestGraphqlUseCase
) : UseCase<AutoAdsResponse.TopAdsGetAutoAds>() {

    var params: RequestParams = RequestParams.create()

    override suspend fun executeOnBackground(): AutoAdsResponse.TopAdsGetAutoAds {
        params.putString(SHOPID,userSession.shopId)
        params.putString(SOURCE,"android.see_ads_performance")
        val gqlRequest = GraphqlRequest(
            GetAutoAds(),
            AutoAdsResponse::class.java,
            params.parameters
        )
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(gqlRequest)

        val gqlResponse = graphqlUseCase.executeOnBackground()
        val gqlErrors = gqlResponse.getError(AutoAdsResponse::class.java) ?: listOf()
        if (gqlErrors.isNullOrEmpty()) {
            val data =
                gqlResponse.getData<AutoAdsResponse>(AutoAdsResponse::class.java)
            val topAdsGetAutoAds: AutoAdsResponse.TopAdsGetAutoAds? = data?.topAdsGetAutoAds
            if (topAdsGetAutoAds != null) {
                return topAdsGetAutoAds
            } else {
                throw RuntimeException()
            }
        } else {
            throw MessageErrorException(gqlErrors.firstOrNull()?.message.orEmpty())
        }
    }
}

