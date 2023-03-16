package com.tokopedia.topads.common.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.topads.common.constant.TopAdsCommonConstant.SHOPID
import com.tokopedia.topads.common.constant.TopAdsCommonConstant.SOURCE
import com.tokopedia.topads.common.domain.model.AdStatusResponse
import com.tokopedia.topads.common.domain.model.TopAdsGetShopInfo
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

const val QUERY_TOP_ADS_GET_SHOP_INFO = """
    query topAdsGetShopInfoV1(${'$'}shopID: String!, ${'$'}source: String!) {
        topAdsGetShopInfoV1_1(shop_id: ${'$'}shopID, source: ${'$'}source) {
            data {
                category
                category_desc
            }
        }
    }

"""


@GqlQuery("GetShopInfo", QUERY_TOP_ADS_GET_SHOP_INFO)
class TopAdsGetShopInfoV1UseCase @Inject constructor(
    val userSession: UserSessionInterface,
    private val graphqlUseCase: MultiRequestGraphqlUseCase
) : UseCase<TopAdsGetShopInfo>() {

    var params: RequestParams = RequestParams.create()

    override suspend fun executeOnBackground(): TopAdsGetShopInfo {
        params.putString(SHOPID, userSession.shopId)
        params.putString(SOURCE,"topads.see_ads_performance")
        val gqlRequest = GraphqlRequest(
            GetShopInfo(),
            AdStatusResponse::class.java,
            params.parameters
        )
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(gqlRequest)

        val gqlResponse = graphqlUseCase.executeOnBackground()
        val gqlErrors = gqlResponse.getError(AdStatusResponse::class.java)?: listOf()
        if (gqlErrors.isNullOrEmpty()) {
            val data =
                gqlResponse.getData<AdStatusResponse>(AdStatusResponse::class.java)
            val topAdsGetShopInfo: TopAdsGetShopInfo? = data?.topAdsGetShopInfo
            if (topAdsGetShopInfo != null) {
                return topAdsGetShopInfo
            } else {
                throw RuntimeException()
            }
        } else {
            throw MessageErrorException(gqlErrors.firstOrNull()?.message.orEmpty())
        }
    }
}
