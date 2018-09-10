package com.tokopedia.gm.subscribe.membership.domain

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.gm.subscribe.R
import com.tokopedia.gm.subscribe.membership.data.model.ResponseSetSubscription
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable

class SetGmSubscribeMembershipUsecase(private val context: Context, private val graphqlUseCase: GraphqlUseCase) : UseCase<String>() {
    override fun createObservable(requestParams: RequestParams): Observable<String> {
        val query = GraphqlHelper.loadRawString(context.resources, R.raw.gql_gm_subscribe_set_membership)
        val graphqlRequest = GraphqlRequest(query, ResponseSetSubscription::class.java, requestParams.parameters)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(RequestParams.EMPTY).map {
            graphqlResponse -> graphqlResponse.getData<ResponseSetSubscription>(ResponseSetSubscription::class.java).goldSetSubscription.data
        }
    }

    companion object {
        private const val PARAM_SUBSCRIPTION_TYPE = "subscription_type"
        @JvmStatic
        fun createRequestParams(subscriptionType: Int): RequestParams {
            return RequestParams.create().apply {
                putInt(PARAM_SUBSCRIPTION_TYPE, subscriptionType)
            }
        }
    }
}