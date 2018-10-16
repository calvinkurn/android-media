package com.tokopedia.affiliatecommon.domain

import android.content.res.Resources
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.affiliatecommon.R
import com.tokopedia.affiliatecommon.data.pojo.productaffiliate.TopAdsPdpAffiliateResponse
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

class GetProductAffiliateGqlUseCase @Inject constructor(private val resources: Resources,
                                                        private val graphqlUseCase: GraphqlUseCase)
    : UseCase<TopAdsPdpAffiliateResponse.TopAdsPdpAffiliate?>() {

    override fun createObservable(requestParams: RequestParams)
            : Observable<TopAdsPdpAffiliateResponse.TopAdsPdpAffiliate?> {
        val queryProductAffiliate = GraphqlHelper.loadRawString(
                resources,
                R.raw.query_product_affiliate_data
        )
        val graphRequest = GraphqlRequest(
                queryProductAffiliate,
                TopAdsPdpAffiliateResponse::class.java,
                requestParams.parameters
        )
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphRequest)

        return graphqlUseCase
                .createObservable(RequestParams.EMPTY)
                .map { graphqlResponse ->
                    val response: TopAdsPdpAffiliateResponse?
                            = graphqlResponse.getData(TopAdsPdpAffiliateResponse::class.java)
                    if (response?.topAdsPDPAffiliate != null) {
                        response.topAdsPDPAffiliate
                    } else {
                        null
                    }
                }
    }

    companion object {
        const val USER_ID_PARAM = "userId"
        const val SHOP_ID_PARAM = "shopId"
        const val PRODUCT_ID_PARAM = "productId"
        const val INCLUDE_UI_PARAM = "includeUI"

        const val PARAMS_GUEST_USER_ID = "0"
        const val PARAMS_DEFAULT_SHOP_ID = "1"
        const val PARAMS_DEFAULT_PRODUCT_ID = "1"
        const val PARAMS_INCLUDE_UI = true

        fun createRequestParams(produtIds: List<Int>, shopId: Int, userId: Int)
                : RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putObject(PRODUCT_ID_PARAM, produtIds)
            requestParams.putInt(SHOP_ID_PARAM, shopId)
            requestParams.putInt(USER_ID_PARAM, userId)
            requestParams.putBoolean(INCLUDE_UI_PARAM, PARAMS_INCLUDE_UI)
            return requestParams
        }
    }
}
