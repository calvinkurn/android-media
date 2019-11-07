package com.tokopedia.shop.common.domain.interactor

import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.common.constant.GQLQueryNamedConstant
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase

import javax.inject.Inject

import rx.Observable
import javax.inject.Named

class GetShopInfoUseCaseRx @Inject constructor(
        val gqlUseCase: GraphqlUseCase,
        @Named(GQLQueryNamedConstant.SHOP_INFO)
        val gqlQuery: String
) : UseCase<ShopInfo>() {

    companion object{
        private const val PARAM_SHOP_IDS = "shopIds"
        private const val PARAM_SHOP_FIELDS = "fields"

        private val DEFAULT_SHOP_FIELDS = listOf("core", "favorite", "assets", "shipment",
                "last_active", "location", "terms", "allow_manage",
                "is_owner", "other-goldos", "status","is_open","closed_info","create_info")

        @JvmStatic
        @JvmOverloads
        fun createParams(shopIds: List<Int>, fields: List<String> = DEFAULT_SHOP_FIELDS): RequestParams
                = RequestParams.create().apply {
            putObject(PARAM_SHOP_IDS, shopIds)
            putObject(PARAM_SHOP_FIELDS, fields)
        }
    }

    var params: RequestParams = RequestParams.EMPTY

    val graphqlRequest by lazy {
        GraphqlRequest(gqlQuery, ShopInfo.Response::class.java, params.parameters)
    }

    override fun createObservable(requestParams: RequestParams): Observable<ShopInfo> {
        gqlUseCase.clearRequest()
        gqlUseCase.addRequest(graphqlRequest)
        return gqlUseCase.createObservable(params).map {
            val response = it.getData<ShopInfo.Response>(ShopInfo.Response::class.java)
            val error: List<GraphqlError> = it.getError(GraphqlError::class.java) ?: mutableListOf()
            if(response == null){
                throw RuntimeException()
            }else if (error.isNotEmpty() && error.first().message.isNotEmpty()) {
                throw MessageErrorException(error.firstOrNull()?.message)
            }
            response.result.data.first()
        }
    }
}
