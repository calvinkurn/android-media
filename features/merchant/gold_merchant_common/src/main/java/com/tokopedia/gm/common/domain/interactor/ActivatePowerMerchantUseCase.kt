package com.tokopedia.gm.common.domain.interactor

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.gm.common.constant.GMParamConstant.RAW_ACTIVATION
import com.tokopedia.gm.common.data.source.cloud.model.GoldActivationSubscription
import com.tokopedia.gm.common.data.source.cloud.model.GoldDeactivationSubscription
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject
import javax.inject.Named

class ActivatePowerMerchantUseCase @Inject constructor(private val graphqlUseCase: GraphqlUseCase,
                                                       @Named(RAW_ACTIVATION) private val rawQuery: String)
    : UseCase<Boolean>() {

    override fun createObservable(requestParams: RequestParams?): Observable<Boolean> {

        val graphqlRequest = GraphqlRequest(rawQuery, GoldDeactivationSubscription::class.java)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)

        return graphqlUseCase.createObservable(RequestParams.EMPTY).map {
            val data: GoldActivationSubscription? = it.getData(GoldActivationSubscription::class.java)
            val error: List<GraphqlError> = it.getError(GraphqlError::class.java) ?: listOf()

            if (data == null) {
                throw RuntimeException()
            } else if (error.isNotEmpty() && error.first().message.isNotEmpty()) {
                throw MessageErrorException(error.first().message)
            }

            true

        }
    }
}