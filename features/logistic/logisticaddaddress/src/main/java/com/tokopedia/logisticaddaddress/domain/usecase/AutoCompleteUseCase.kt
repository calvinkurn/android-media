package com.tokopedia.logisticaddaddress.domain.usecase

import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.logisticCommon.data.response.AutoCompleteResponse
import com.tokopedia.logisticCommon.domain.model.Place
import com.tokopedia.logisticaddaddress.data.query.AutoCompleteQuery
import com.tokopedia.logisticaddaddress.domain.executor.SchedulerProvider
import com.tokopedia.logisticaddaddress.domain.mapper.AutoCompleteMapper
import com.tokopedia.network.exception.MessageErrorException
import rx.Observable
import javax.inject.Inject

class AutoCompleteUseCase
@Inject constructor(
        private val gql: GraphqlUseCase,
        private val scheduler: SchedulerProvider,
        private val mapper: AutoCompleteMapper) {

    fun execute(query: String): Observable<Place> {
        val param = mapOf("param" to query)
        val gqlRequest = GraphqlRequest(AutoCompleteQuery.keroAutoCompleteGeocode, AutoCompleteResponse::class.java, param)
        gql.clearRequest()
        gql.addRequest(gqlRequest)
        return gql.getExecuteObservable(null)
                .map { gqlResponse ->
                    val response: AutoCompleteResponse? =
                            gqlResponse.getData(AutoCompleteResponse::class.java)
                    response
                            ?: throw MessageErrorException(
                                    gqlResponse.getError(AutoCompleteResponse::class.java)[0].message)
                }
                .map { mapper.mapAutoComplete(it) }
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
    }

    fun unsubscribe() {
        gql.unsubscribe()
    }

}