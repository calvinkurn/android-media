package com.tokopedia.digital.common.domain.interactor

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.digital.R
import com.tokopedia.digital.common.data.entity.response.RechargeFavoritNumberResponseEntity
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import rx.Subscriber

/**
 * Created by resakemal on 04/07/19.
 */
class RechargePushEventRecommendationUseCase(private val graphqlUseCase: GraphqlUseCase, @ApplicationContext private val context: Context) {

    fun execute(requestParams: RequestParams, subscriber: Subscriber<GraphqlResponse>) {
        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(context.resources, R.raw.gql_recharge_push_event_recommendation),
                RechargeFavoritNumberResponseEntity::class.java, requestParams.parameters)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        graphqlUseCase.execute(requestParams, subscriber)
    }

    fun createRequestParams(categoryId: Int, actionType: String): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putInt(PARAM_CATEGORY_ID, categoryId)
        requestParams.putString(PARAM_ACTION_TYPE, actionType)
        return requestParams
    }

    companion object {
        const val PARAM_CATEGORY_ID = "categoryID"
        const val PARAM_ACTION_TYPE = "action"
    }
}

