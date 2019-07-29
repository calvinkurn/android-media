package com.tokopedia.logisticaddaddress.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.domain.mapper.AutofillMapper
import com.tokopedia.logisticaddaddress.domain.model.autofill.AutofillResponse
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.autofill.AutofillResponseUiModel
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-05-17.
 */
class AutofillUseCase @Inject constructor(
        @ApplicationContext val context: Context,
        val gql: GraphqlUseCase,
        val mapper: AutofillMapper) {

    fun execute(latlng: String): Observable<AutofillResponseUiModel> {
        val param: Map<String, Any> = mapOf(
                PARAM_LATLNG to latlng,
                PARAM_ERR to true
        )
        val gqlQuery = GraphqlHelper.loadRawString(context.resources, R.raw.autofill)
        val gqlRequest = GraphqlRequest(gqlQuery, AutofillResponse::class.java, param)

        gql.clearRequest()
        gql.addRequest(gqlRequest)
        return gql.getExecuteObservable(null)
                .map { gqlResponse ->
                    val response: AutofillResponse? =
                            gqlResponse.getData(AutofillResponse::class.java)
                    response ?: throw MessageErrorException(
                            gqlResponse.getError(AutofillResponse::class.java)[0].message
                    )
                }
                .map { mapper.map(it) }
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