package com.tokopedia.logisticaddaddress.domain.usecase

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.domain.model.autocomplete_geocode.AutocompleteGeocodeResponse
import com.tokopedia.usecase.RequestParams
import rx.Observable
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-05-14.
 */
class AutocompleteGeocodeUseCase @Inject constructor(@ApplicationContext val context: Context) : GraphqlUseCase() {
    var queryString: String = ""

    companion object {
        val PARAM_LAT = "#lat"
        val PARAM_LONG = "#long"
    }

    fun setParams(lat: Double?, long: Double?) {
        queryString = GraphqlHelper.loadRawString(context.resources, R.raw.autocomplete_geocode)
        queryString = queryString.replace(PARAM_LAT, lat.toString())
        queryString = queryString.replace(PARAM_LONG, long.toString())
    }

    override fun createObservable(params: RequestParams?): Observable<GraphqlResponse> {
        val graphqlRequest = GraphqlRequest(queryString, AutocompleteGeocodeResponse::class.java)
        clearRequest()
        addRequest(graphqlRequest)

        return super.createObservable(params)
    }
}