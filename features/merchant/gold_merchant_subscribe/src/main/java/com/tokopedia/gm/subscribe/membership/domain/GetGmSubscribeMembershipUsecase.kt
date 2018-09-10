package com.tokopedia.gm.subscribe.membership.domain

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.gm.subscribe.R
import com.tokopedia.gm.subscribe.membership.data.model.MembershipData
import com.tokopedia.gm.subscribe.membership.data.model.ResponseGetSubscription
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable

class GetGmSubscribeMembershipUsecase(private val context: Context, private val graphqlUseCase: GraphqlUseCase) : UseCase<MembershipData>() {
    override fun createObservable(requestParams: RequestParams): Observable<MembershipData> {
        val query = GraphqlHelper.loadRawString(context.resources, R.raw.gql_gm_subscribe_get_membership)
        val graphqlRequest = GraphqlRequest(query, ResponseGetSubscription::class.java, requestParams.parameters)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(RequestParams.EMPTY).map {
            graphqlResponse -> graphqlResponse.getData<ResponseGetSubscription>(ResponseGetSubscription::class.java).goldGetSubscription.data
        }
    }
}