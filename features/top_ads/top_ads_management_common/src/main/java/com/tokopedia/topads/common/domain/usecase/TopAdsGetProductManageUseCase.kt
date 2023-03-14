package com.tokopedia.topads.common.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.topads.common.domain.model.TopAdsGetProductManage
import com.tokopedia.topads.common.domain.model.TopAdsGetProductManageResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

const val QUERY_TOP_ADS_PRODUCT_MANAGE = """
            query topAdsGetProductManageV2(${'$'}productID :String!,${'$'}shopID:String!){
               	topAdsGetProductManageV2(type:1, shop_id:${'$'}shopID,item_id:${'$'}productID,source: "topads.see_ads_performance"){
            			data {
            			  ad_id
            			  ad_type
            			  is_enable_ad
            			  item_id
            			  item_image
            			  item_name
            			  shop_id
            			  manage_link
            			}
                }
            }
        """

@GqlQuery("GetProductManageQuery", QUERY_TOP_ADS_PRODUCT_MANAGE)
class TopAdsGetProductManageUseCase @Inject constructor(
    val userSession: UserSessionInterface,
    private val graphqlUseCase: MultiRequestGraphqlUseCase
) : UseCase<TopAdsGetProductManage>() {

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): TopAdsGetProductManage {
        val gqlRequest = GraphqlRequest(
            GetProductManageQuery(),
            TopAdsGetProductManageResponse::class.java,
            params.parameters
        )
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(gqlRequest)

        val gqlResponse = graphqlUseCase.executeOnBackground()
        val gqlErrors = gqlResponse.getError(TopAdsGetProductManageResponse::class.java)?: listOf()
        if (gqlErrors.isNullOrEmpty()) {
            val data =
                gqlResponse.getData<TopAdsGetProductManageResponse>(TopAdsGetProductManageResponse::class.java)
            val topAdsGetProductManage: TopAdsGetProductManage? = data?.topAdsGetProductManage
            if (topAdsGetProductManage != null) {
                return topAdsGetProductManage
            } else {
                throw RuntimeException()
            }
        } else {
            throw MessageErrorException(gqlErrors.firstOrNull()?.message.orEmpty())
        }
    }

    fun setParams(productId: String) {
        params.putString("productID", productId)
        params.putString("shopID", userSession.shopId)
    }
}
