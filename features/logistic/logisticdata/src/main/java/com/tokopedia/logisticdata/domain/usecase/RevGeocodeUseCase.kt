package com.tokopedia.logisticdata.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.logisticdata.R
import com.tokopedia.logisticdata.data.entity.response.AutoFillResponse
import com.tokopedia.network.exception.MessageErrorException
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

class RevGeocodeUseCase @Inject constructor(
        @ApplicationContext val context: Context,
        val gql: GraphqlUseCase) {

    fun execute(latlng: String): Observable<AutoFillResponse> {
        val param: Map<String, Any> = mapOf(
                PARAM_LATLNG to latlng,
                PARAM_ERR to true
        )
        val gqlQuery = GraphqlHelper.loadRawString(context.resources, R.raw.autofill)
        val gqlRequest = GraphqlRequest(gqlQuery, AutoFillResponse::class.java, param)

        gql.clearRequest()
        gql.addRequest(gqlRequest)
        return gql.getExecuteObservable(null)
                .map { gqlResponse ->
                    val response: AutoFillResponse? =
                            gqlResponse.getData(AutoFillResponse::class.java)
                    response ?: throw MessageErrorException(
                            gqlResponse.getError(AutoFillResponse::class.java)[0].message
                    )
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun unsubscribe() {
        gql.unsubscribe()
    }

    fun clearCache() {
        gql.clearCache()
    }

    companion object {
        val PARAM_LATLNG = "latlng"
        val PARAM_ERR = "err"
    }
}