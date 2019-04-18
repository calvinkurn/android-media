package com.tokopedia.recommendation_widget_common.domain

import android.content.Context
import android.text.TextUtils

import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.data.RecomendationEntity
import com.tokopedia.recommendation_widget_common.data.mapper.RecommendationEntityMapper
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import com.tokopedia.user.session.UserSessionInterface

import javax.inject.Inject

import rx.Observable

/**
 * Created by devara fikry on 16/04/19.
 * Credit errysuprayogi
 */

class GetRecommendationUseCase @Inject
constructor(private val context: Context,
            private val graphqlUseCase: GraphqlUseCase,
            private val userSession: UserSessionInterface) : UseCase<RecommendationModel>() {

    override fun createObservable(requestParams: RequestParams): Observable<RecommendationModel> {
        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(context.resources,
                R.raw.query_recommendation_widget), RecomendationEntity::class.java, requestParams.parameters)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(RequestParams.EMPTY)
                .map<RecomendationEntity.RecomendationData> { graphqlResponse ->
                    val entity = graphqlResponse.getData<RecomendationEntity>(RecomendationEntity::class.java)
                    entity!!.productRecommendationWidget.data[0]
                }
                .map(RecommendationEntityMapper())
    }

    fun getRecomParams(pageNumber: Int,
                       xSource: String,
                       pageName: String): RequestParams {
        val params = RequestParams.create()
        params.putInt(USER_ID, Integer.parseInt(userSession.userId))
        params.putInt(PAGE_NUMBER, pageNumber)
        params.putString(X_SOURCE, if (TextUtils.isEmpty(xSource)) DEFAULT_VALUE_X_SOURCE else xSource)
        params.putString(X_DEVICE, if (TextUtils.isEmpty(pageName)) DEFAULT_VALUE_X_DEVICE else pageName)
        params.putString(PAGE_NAME, pageName)
        return params
    }

    companion object {

        private val USER_ID = "userID"
        private val X_SOURCE = "xSource"
        private val PAGE_NUMBER = "pageNumber"
        private val X_DEVICE = "xDevice"
        private val PAGE_NAME = "pageName"
        private val DEFAULT_VALUE_X_SOURCE = "recom_widget"
        private val DEFAULT_VALUE_X_DEVICE = "android"
        val DEFAULT_PAGE_NAME = ""
    }
}
