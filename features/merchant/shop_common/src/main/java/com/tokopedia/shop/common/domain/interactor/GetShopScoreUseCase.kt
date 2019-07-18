package com.tokopedia.shop.common.domain.interactor

import android.text.TextUtils
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.shop.common.constant.ShopCommonParamApiConstant
import com.tokopedia.shop.common.graphql.data.shopscore.ShopScore
import com.tokopedia.shop.common.graphql.data.shopscore.ShopScoreResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject
import javax.inject.Named

/**
 * @author by milhamj on 15/06/19.
 */
class GetShopScoreUseCase @Inject constructor(
        @Named(ShopCommonParamApiConstant.QUERY_SHOP_SCORE) private val query: String,
        private val graphqlUseCase: GraphqlUseCase)
    : UseCase<ShopScore>() {
    override fun createObservable(requestParams: RequestParams?): Observable<ShopScore> {
        val request =  GraphqlRequest(query, ShopScoreResponse::class.java, requestParams?.parameters)

        graphqlUseCase.addRequest(request)
        graphqlUseCase.clearRequest()
        return graphqlUseCase.createObservable(RequestParams.EMPTY).map {
            val response = it.getData<ShopScoreResponse>(ShopScoreResponse::class.java)
            if (!TextUtils.isEmpty(response.shopScore.error.message)) {
                throw MessageErrorException(response.shopScore.error.message)
            }
            response.shopScore
        }
    }

    companion object {
        private const val SHOP_ID = "shopId"

        fun createParams(shopId: String): RequestParams {
            return RequestParams.create().apply {
                putString(SHOP_ID, shopId)
            }
        }
    }
}