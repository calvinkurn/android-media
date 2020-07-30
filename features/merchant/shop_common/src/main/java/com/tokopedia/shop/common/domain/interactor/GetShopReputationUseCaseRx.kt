package com.tokopedia.shop.common.domain.interactor

import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.common.constant.GQLQueryNamedConstant
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopBadge
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase

import javax.inject.Inject

import rx.Observable
import javax.inject.Named

class GetShopReputationUseCaseRx @Inject constructor(
        val gqlUseCase: GraphqlUseCase,
        @Named(GQLQueryNamedConstant.SHOP_REPUTATION)
        val gqlQuery: String
) : UseCase<ShopBadge>() {

    companion object{
        private const val PARAM_SHOP_IDS = "shopIds"

        @JvmStatic
        fun createParams(shopIds: List<Int>): RequestParams
                = RequestParams.create().apply {
            putObject(PARAM_SHOP_IDS, shopIds)
        }
    }

    var params: RequestParams = RequestParams.EMPTY

    val graphqlRequest by lazy {
        GraphqlRequest(gqlQuery, ShopBadge.Response::class.java, params.parameters)
    }

    override fun createObservable(requestParams: RequestParams): Observable<ShopBadge> {
        gqlUseCase.clearRequest()
        gqlUseCase.addRequest(graphqlRequest)
        return gqlUseCase.createObservable(params).map {
            val response = it.getData<ShopBadge.Response>(ShopBadge.Response::class.java)
            val error: List<GraphqlError> = it.getError(GraphqlError::class.java) ?: mutableListOf()
            if(response == null){
                throw RuntimeException()
            }else if (error.isNotEmpty() && error.first().message.isNotEmpty()) {
                throw MessageErrorException(error.firstOrNull()?.message)
            }
            response.result.first()
        }
    }
}
