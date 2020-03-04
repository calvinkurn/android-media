package com.tokopedia.sellerhomedrawer.domain.usecase

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.sellerhomedrawer.data.GoldGetPmOsStatus
import com.tokopedia.sellerhomedrawer.data.constant.SellerHomeParamConstant
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject
import javax.inject.Named

class GetShopStatusUseCase @Inject constructor(private val graphqlUseCase: GraphqlUseCase,
                                               @Named(SellerHomeParamConstant.RAW_GM_STATUS) private val rawQuery: String) : UseCase<GoldGetPmOsStatus>() {
    companion object {
        fun createRequestParams(shopId: String): RequestParams {
            return RequestParams.create().apply {
                putInt(SellerHomeParamConstant.SHOP_ID, shopId.toIntOrNull() ?: 0)
            }
        }
    }

    override fun createObservable(requestParams: RequestParams): Observable<GoldGetPmOsStatus> {
        val graphqlRequest = GraphqlRequest(rawQuery, GoldGetPmOsStatus::class.java, requestParams.parameters)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)

        return graphqlUseCase.createObservable(requestParams).map {
            val data: GoldGetPmOsStatus? = it.getData(GoldGetPmOsStatus::class.java)
            val error: List<GraphqlError> = it.getError(GraphqlError::class.java) ?: listOf()

            if (data == null) {
                throw RuntimeException()
            } else if (error.isNotEmpty() && error.first().message.isNotEmpty()) {
                throw MessageErrorException(error.first().message)
            } else if (data.result.header.errorCode != "" && data.result.header.message.isNotEmpty()) {
                throw MessageErrorException(data.result.header.message.first())
            }

            data
        }
    }

}