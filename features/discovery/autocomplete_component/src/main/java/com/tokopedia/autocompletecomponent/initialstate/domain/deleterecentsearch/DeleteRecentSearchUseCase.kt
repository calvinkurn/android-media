package com.tokopedia.autocompletecomponent.initialstate.domain.deleterecentsearch

import android.text.TextUtils
import com.google.gson.annotations.SerializedName
import com.tokopedia.network.authentication.AuthHelper
import com.tokopedia.autocompletecomponent.initialstate.BaseItemInitialStateSearch
import com.tokopedia.autocompletecomponent.util.UrlParamHelper
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.NAVSOURCE
import com.tokopedia.discovery.common.constants.SearchConstant.GQL
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable

open class DeleteRecentSearchUseCase(
    protected val graphqlUseCase: GraphqlUseCase
) : UseCase<Boolean>() {

    override fun createObservable(requestParams: RequestParams): Observable<Boolean> {
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(createGraphqlRequest(requestParams))

        return graphqlUseCase
                .createObservable(RequestParams.EMPTY)
                .map(::getIsSuccess)
    }

    protected open fun createGraphqlRequest(requestParams: RequestParams): GraphqlRequest =
        GraphqlRequest(
            getGraphqlQuery(),
            DeleteRecentSearchResponse::class.java,
            createGraphqlRequestParams(requestParams)
        )

    @GqlQuery("DeleteRecentSearchQuery", DELETE_RECENT_SEARCH_GQL_QUERY)
    protected open fun getGraphqlQuery(): String =
        DeleteRecentSearchQuery.GQL_QUERY

    protected open fun createGraphqlRequestParams(
        requestParams: RequestParams
    ): Map<String, Any> =
        mapOf(GQL.KEY_PARAMS to UrlParamHelper.generateUrlParamString(requestParams.parameters))

    protected open fun getIsSuccess(graphqlResponse: GraphqlResponse): Boolean {
        val searchResponse = graphqlResponse
            .getData<DeleteRecentSearchResponse>(DeleteRecentSearchResponse::class.java)

        return searchResponse != null && searchResponse.data.isSuccess
    }

    companion object {

        const val DELETE_RECENT_SEARCH_GQL_QUERY = """
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

        fun getParams(
            registrationId: String,
            userId: String,
            item: BaseItemInitialStateSearch,
            navSource: String
        ): RequestParams {
            val params = getParams(registrationId, userId, DELETE_ALL_FALSE, navSource)

            params.putString(KEY_Q, item.title)
            params.putString(KEY_TYPE, item.type)
            params.putString(KEY_ITEM_ID, item.productId)

            return params
        }

        fun getParams(
            registrationId: String,
            userId: String,
            deleteAllValue: String = DELETE_ALL_TRUE,
            navSource: String
        ): RequestParams {
            val params = RequestParams.create()

            var uniqueId = AuthHelper.getMD5Hash(registrationId)
            if (!TextUtils.isEmpty(userId)) {
                uniqueId = AuthHelper.getMD5Hash(userId)
                params.putString(KEY_USER_ID, userId)
            }

            params.putString(KEY_UNIQUE_ID, uniqueId)
            params.putString(KEY_DELETE_ALL, deleteAllValue)
            params.putString(KEY_DEVICE, DEFAULT_DEVICE)
            params.putString(DEVICE_ID, registrationId)
            params.putString(KEY_SOURCE, DEFAULT_SOURCE)
            params.putString(NAVSOURCE, navSource)

            return params
        }
    }

    protected data class DeleteRecentSearchResponse(
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