package com.tokopedia.autocomplete.initialstate.recentsearch

import android.text.TextUtils
import com.google.gson.annotations.SerializedName
import com.tokopedia.authentication.AuthHelper
import com.tokopedia.autocomplete.initialstate.BaseItemInitialStateSearch
import com.tokopedia.autocomplete.util.UrlParamHelper
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import rx.functions.Func1
import java.util.*

class DeleteRecentSearchUseCase(
        private val graphqlUseCase: GraphqlUseCase
) : UseCase<Boolean>() {

    private val graphqlRequest = GraphqlRequest(GQL_QUERY, DeleteRecentSearchResponse::class.java)

    override fun createObservable(requestParams: RequestParams): Observable<Boolean> {
        val variables = createParametersForQuery(requestParams.parameters)
        graphqlRequest.variables = variables
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase
                .createObservable(RequestParams.EMPTY)
                .map(mapIsSuccess())
    }

    private fun createParametersForQuery(parameters: Map<String, Any>): Map<String, Any> {
        val variables: MutableMap<String, Any> = HashMap()
        variables[SearchConstant.GQL.KEY_PARAMS] = UrlParamHelper.generateUrlParamString(parameters)
        return variables
    }

    private fun mapIsSuccess(): Func1<GraphqlResponse, Boolean>? {
        return Func1 { graphqlResponse: GraphqlResponse ->
            val searchResponse = graphqlResponse.getData<DeleteRecentSearchResponse>(DeleteRecentSearchResponse::class.java)
            (searchResponse != null && searchResponse.data.isSuccess)
        }
    }

    companion object {

        private const val GQL_QUERY = """
            mutation universe_delete_recent_search(
                ${'$'}params: String!
            ) {
                universe_delete_recent_search(param: ${'$'}params) {
                    status
                }
            }
        """

        private const val KEY_UNIQUE_ID = "unique_id"
        private const val KEY_USER_ID = "user_id"
        private const val DEVICE_ID = "device_id"
        private const val KEY_Q = "q"
        private const val KEY_DELETE_ALL = "clear_all"
        private const val KEY_TYPE = "type"
        private const val KEY_ITEM_ID = "id"
        private const val KEY_DEVICE = "device"
        private const val KEY_SOURCE = "source"

        private const val DEFAULT_DEVICE = "android"
        private const val DEFAULT_SOURCE = "searchbar"
        private const val DELETE_ALL_TRUE = "true"
        private const val DELETE_ALL_FALSE = "false"

        fun getParams(registrationId: String, userId: String, item: BaseItemInitialStateSearch): RequestParams {
            val params = RequestParams.create()

            params.putString(KEY_Q, item.title)
            params.putString(KEY_TYPE, item.type)
            params.putString(KEY_ITEM_ID, item.productId)

            var uniqueId = AuthHelper.getMD5Hash(registrationId)
            if (!TextUtils.isEmpty(userId)) {
                uniqueId = AuthHelper.getMD5Hash(userId)
                params.putString(KEY_USER_ID, userId)
            }

            params.putString(KEY_UNIQUE_ID, uniqueId)
            params.putString(KEY_DELETE_ALL, DELETE_ALL_FALSE)
            params.putString(KEY_DEVICE, DEFAULT_DEVICE)
            params.putString(DEVICE_ID, registrationId)
            params.putString(KEY_SOURCE, DEFAULT_SOURCE)

            return params
        }

        fun getParams(registrationId: String, userId: String): RequestParams {
            val params = RequestParams.create()

            var uniqueId = AuthHelper.getMD5Hash(registrationId)
            if (!TextUtils.isEmpty(userId)) {
                uniqueId = AuthHelper.getMD5Hash(userId)
                params.putString(KEY_USER_ID, userId)
            }

            params.putString(KEY_UNIQUE_ID, uniqueId)
            params.putString(KEY_DELETE_ALL, DELETE_ALL_TRUE)
            params.putString(KEY_DEVICE, DEFAULT_DEVICE)
            params.putString(DEVICE_ID, registrationId)
            params.putString(KEY_SOURCE, DEFAULT_SOURCE)

            return params
        }
    }

    private data class DeleteRecentSearchResponse(
            @SerializedName("universe_delete_recent_search")
            val data: DeleteRecentSearchUniverse = DeleteRecentSearchUniverse()
    ) {
        data class DeleteRecentSearchUniverse(
                @SerializedName("status")
                val status: String = ""
        ) {
            val isSuccess: Boolean
                get() = status.equals("success", true)
        }
    }
}