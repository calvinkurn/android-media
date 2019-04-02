package com.tokopedia.gm.subscribe.membership.domain

import android.content.Context
import android.text.TextUtils
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.gm.subscribe.R
import com.tokopedia.gm.subscribe.membership.data.model.GetMembershipData
import com.tokopedia.gm.subscribe.membership.data.model.ResponseGetSubscription
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable

class GetGmSubscribeMembershipUsecase(private val context: Context, private val graphqlUseCase: GraphqlUseCase) : UseCase<GetMembershipData>() {
    override fun createObservable(requestParams: RequestParams): Observable<GetMembershipData> {
        val query = GraphqlHelper.loadRawString(context.resources, R.raw.gql_gm_subscribe_get_membership)
        val graphqlRequest = GraphqlRequest(query, ResponseGetSubscription::class.java, requestParams.parameters, false)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(RequestParams.EMPTY).flatMap { graphqlResponse ->
            val data = graphqlResponse.getData<ResponseGetSubscription>(ResponseGetSubscription::class.java)
            val graphqlErrorList = graphqlResponse.getError(ResponseGetSubscription::class.java)
            if (graphqlErrorList != null && graphqlErrorList.size > 0) {
                val graphqlError = graphqlErrorList[0]
                val errorMessage = graphqlError.message
                if (TextUtils.isEmpty(errorMessage)) {
                    Observable.just(data.goldGetSubscription)
                } else {
                    Observable.error(MessageErrorException(errorMessage))
                }
            } else {
                Observable.just(data.goldGetSubscription)
            }
        }
    }
}