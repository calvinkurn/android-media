package com.tokopedia.gm.common.domain.interactor

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.gm.common.constant.GMParamApiContant
import com.tokopedia.gm.common.constant.GMParamApiContant.INCLUDE_OS
import com.tokopedia.gm.common.constant.GMParamConstant.RAW_GM_STATUS
import com.tokopedia.gm.common.data.source.cloud.model.GoldGetPmOsStatus
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject
import javax.inject.Named

/**
 * @author by milhamj on 12/06/19.
 */
class GetShopStatusUseCase @Inject constructor(private val graphqlUseCase: GraphqlUseCase,
                                               @Named(RAW_GM_STATUS) private val rawQuery: String) : UseCase<GoldGetPmOsStatus>() {
    companion object {
        fun createRequestParams(shopId: String, includeOS: Boolean = true): RequestParams {
            return RequestParams.create().apply {
                putInt(GMParamApiContant.SHOP_ID, shopId.toIntOrNull() ?: 0)
                putBoolean(INCLUDE_OS, includeOS)
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