package com.tokopedia.autocomplete.initialstate

import android.text.TextUtils
import com.tokopedia.authentication.AuthHelper
import com.tokopedia.autocomplete.initialstate.data.InitialStateGqlResponse
import com.tokopedia.autocomplete.util.UrlParamHelper
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant.GQL
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable

class InitialStateUseCase(
        private val graphqlUseCase: GraphqlUseCase
) : UseCase<List<InitialStateData>>() {

    companion object {
        internal const val GQL_QUERY = """
            query universe_initial_state(${'$'}params: String!){
              universe_initial_state(param: ${'$'}params) {
                data{
                  id
                  header
                  label_action
                  feature_id
                  items{
                    id
                    template
                    image_url
                    applink
                    url
                    title
                    subtitle
                    icon_title
                    icon_subtitle
                    label
                    label_type
                    shortcut_image
                    type
                    discount_percentage
                    original_price
                  }
                }
              }
            }
        """

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

            //need to get user_warehouseId from chooseAddress later
            params.putString(SearchApiConst.USER_WAREHOUSE_ID, SearchApiConst.HARDCODED_WAREHOUSE_ID_PLEASE_DELETE)

            return params
        }
    }

    @GqlQuery("InitialStateUseCaseQuery", GQL_QUERY)
    override fun createObservable(requestParams: RequestParams): Observable<List<InitialStateData>> {
        val params = UrlParamHelper.generateUrlParamString(requestParams.parameters)
        val graphqlRequest = GraphqlRequest(
                InitialStateUseCaseQuery.GQL_QUERY,
                InitialStateGqlResponse::class.java,
                mapOf(GQL.KEY_PARAMS to params)
        )

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequests(listOf(graphqlRequest))

        return graphqlUseCase
                .createObservable(RequestParams.EMPTY)
                .map {
                    it.getData<InitialStateGqlResponse>(InitialStateGqlResponse::class.java)?.initialStateUniverse?.data ?: listOf()
                }
    }
}