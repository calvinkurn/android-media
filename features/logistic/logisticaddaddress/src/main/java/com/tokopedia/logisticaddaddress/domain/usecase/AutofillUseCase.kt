package com.tokopedia.logisticaddaddress.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.domain.model.autofill.AutofillResponse
import com.tokopedia.usecase.RequestParams
import rx.Observable
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-05-17.
 */
class AutofillUseCase @Inject constructor(@ApplicationContext val context: Context) : GraphqlUseCase() {
    var queryString: String = ""

    companion object {
        val PARAM_LATLNG = "#latlng"
    }

    fun setParams(latlng: String) {
        queryString = GraphqlHelper.loadRawString(context.resources, R.raw.autofill)
        queryString = queryString.replace(PARAM_LATLNG, latlng, false)
    }

    override fun createObservable(params: RequestParams?): Observable<GraphqlResponse> {
        val graphqlRequest = GraphqlRequest(queryString, AutofillResponse::class.java)
        clearRequest()
        addRequest(graphqlRequest)

        return super.createObservable(params)
    }
}