package com.tokopedia.affiliatecommon.domain

import android.content.res.Resources
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
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
                        if (graphqlResponse.getError(TopAdsPdpAffiliateResponse::class.java).isEmpty().not()) {
                            throw MessageErrorException(graphqlResponse.getError(TopAdsPdpAffiliateResponse::class.java)[0].message)
                        }
                        null
                    }
                }
    }

    companion object {
        const val SHOP_ID_PARAM = "shopId"
        const val PRODUCT_ID_PARAM = "productId"
        const val INCLUDE_UI_PARAM = "includeUI"

        const val PARAMS_INCLUDE_UI = true

        fun createRequestParams(produtIds: List<Int>, shopId: Int)
                : RequestParams {
            val requestParams = createRequestParams(produtIds)
            requestParams.putInt(SHOP_ID_PARAM, shopId)
            return requestParams
        }

        fun createRequestParams(produtIds: List<Int>): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putObject(PRODUCT_ID_PARAM, produtIds)
            requestParams.putBoolean(INCLUDE_UI_PARAM, PARAMS_INCLUDE_UI)
            return requestParams
        }
    }
}
