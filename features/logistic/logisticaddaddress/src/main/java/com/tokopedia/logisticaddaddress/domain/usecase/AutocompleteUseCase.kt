package com.tokopedia.logisticaddaddress.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.domain.model.autocomplete.AutocompleteResponse
import com.tokopedia.usecase.RequestParams
import rx.Observable
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-05-20.
 */
class AutocompleteUseCase @Inject constructor(@ApplicationContext val context: Context) : GraphqlUseCase() {
    var queryString: String = ""

    companion object {
        val PARAM_INPUT = "#keystring"
    }

    fun setParams(input: String) {
        queryString = GraphqlHelper.loadRawString(context.resources, R.raw.autocomplete)
        queryString = queryString.replace(PARAM_INPUT, input, false)
    }

    override fun createObservable(params: RequestParams?): Observable<GraphqlResponse> {
        val graphqlRequest = GraphqlRequest(queryString, AutocompleteResponse::class.java)
        clearRequest()
        addRequest(graphqlRequest)

        return super.createObservable(params)
    }
}