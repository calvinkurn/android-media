package com.tokopedia.atc_common.domain.usecase

import com.tokopedia.atc_common.domain.analytics.AddToCartBaseAnalytics
import com.tokopedia.atc_common.domain.model.response.AtcMultiData
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

class AddToCartMultiLegacyUseCase @Inject constructor(private val graphqlUseCase: GraphqlUseCase): UseCase<AtcMultiData>() {

    private var query: String = ""
    private var params: MutableMap<String, Any> = mutableMapOf()
    private var userId: String = ""

    fun setup(query: String, params: MutableMap<String, Any>, userId: String) {
        this.query = query
        this.params = params
        this.userId = userId
    }

    override fun createObservable(requestParams: RequestParams): Observable<AtcMultiData> {
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(GraphqlRequest(query, AtcMultiData::class.java, params, false))
        return graphqlUseCase.createObservable(RequestParams.EMPTY).map {
            val result = it.getData<AtcMultiData>(AtcMultiData::class.java)
            if (result.atcMulti.buyAgainData.success == 1) {
                for (product in result.atcMulti.buyAgainData.listProducts) {
                    AddToCartBaseAnalytics.sendAppsFlyerTracking(product.productId.toString(), "", "",
                            product.quantity.toString(), "")
                    AddToCartBaseAnalytics.sendBranchIoTracking(product.productId.toString(), "", "",
                            product.quantity.toString(), "", "",
                            "", "", "",
                            "", "", userId)
                }
            }
            result
        }
    }
}