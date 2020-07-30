package com.tokopedia.logisticaddaddress.domain.usecase

import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.logisticaddaddress.data.query.AutoCompleteQuery
import com.tokopedia.logisticaddaddress.domain.executor.SchedulerProvider
import com.tokopedia.logisticaddaddress.domain.mapper.AutoCompleteMapper
import com.tokopedia.logisticaddaddress.domain.model.autocomplete.AutocompleteResponse
import com.tokopedia.logisticdata.data.autocomplete.SuggestedPlace
import com.tokopedia.network.exception.MessageErrorException
import rx.Observable
import javax.inject.Inject

class AutoCompleteUseCase
@Inject constructor(
        private val gql: GraphqlUseCase,
        private val scheduler: SchedulerProvider,
        private val mapper: AutoCompleteMapper) {

    fun execute(query: String): Observable<List<SuggestedPlace>> {
        val param = mapOf("param" to query)
        val gqlRequest = GraphqlRequest(AutoCompleteQuery.keroAutoCompleteGeocode, AutocompleteResponse::class.java, param)
        gql.clearRequest()
        gql.addRequest(gqlRequest)
        return gql.getExecuteObservable(null)
                .map { gqlResponse ->
                    val response: AutocompleteResponse? =
                            gqlResponse.getData(AutocompleteResponse::class.java)
                    response
                            ?: throw MessageErrorException(
                                    gqlResponse.getError(AutocompleteResponse::class.java)[0].message)
                }
                .map { mapper.mapAutoComplete(it) }
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
    }

    fun unsubscribe() {
        gql.unsubscribe()
    }

}