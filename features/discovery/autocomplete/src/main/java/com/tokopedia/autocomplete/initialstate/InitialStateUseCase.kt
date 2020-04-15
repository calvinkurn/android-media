package com.tokopedia.autocomplete.initialstate

import android.text.TextUtils
import com.tokopedia.authentication.AuthHelper
import com.tokopedia.autocomplete.util.UrlParamHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import rx.functions.Func1
import java.util.HashMap
import com.tokopedia.discovery.common.constants.SearchConstant.GQL

class InitialStateUseCase(
        private val graphqlRequest: GraphqlRequest,
        private val graphqlUseCase: GraphqlUseCase,
        private val initialStateDataModelMapper: Func1<GraphqlResponse, List<InitialStateData>>
) : UseCase<List<InitialStateData>>() {

    companion object {
        private const val KEY_DEVICE = "device"
        private const val KEY_SOURCE = "source"
        private const val KEY_UNIQUE_ID = "unique_id"
        private const val KEY_COUNT = "count"
        private const val KEY_USER_ID = "user_id"
        private const val DEFAULT_DEVICE = "android"
        private const val DEFAULT_SOURCE = "searchbar"
        private const val DEFAULT_COUNT = "5"
        private const val DEVICE_ID = "device_id"

        fun getParams(searchParameter: Map<String, Any>, registrationId: String, userId: String): RequestParams {
            val params = RequestParams.create()

            params.putAll(searchParameter)

            params.putString(KEY_DEVICE, DEFAULT_DEVICE)
            params.putString(KEY_SOURCE, DEFAULT_SOURCE)
            params.putString(KEY_COUNT, DEFAULT_COUNT)
            var uniqueId = AuthHelper.getMD5Hash(registrationId)
            if (!TextUtils.isEmpty(userId)) {
                uniqueId = AuthHelper.getMD5Hash(userId)
                params.putString(KEY_USER_ID, userId)
            }
            params.putString(KEY_UNIQUE_ID, uniqueId)
            params.putString(DEVICE_ID, registrationId)

            return params
        }
    }

    override fun createObservable(requestParams: RequestParams): Observable<List<InitialStateData>> {
        val variables = createParametersForQuery(requestParams.parameters)
        graphqlRequest.variables = variables
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase
                .createObservable(RequestParams.EMPTY)
                .map(initialStateDataModelMapper)
    }

    private fun createParametersForQuery(parameters: Map<String, Any>): Map<String, Any> {
        val variables: MutableMap<String, Any> = HashMap()
        variables[GQL.KEY_PARAMS] = UrlParamHelper.generateUrlParamString(parameters)
        return variables
    }
}