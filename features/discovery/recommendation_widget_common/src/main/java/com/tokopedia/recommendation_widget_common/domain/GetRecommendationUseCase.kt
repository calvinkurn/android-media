package com.tokopedia.recommendation_widget_common.domain

import android.content.Context

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
                    entity?.productRecommendationWidget?.data?.get(0)
                }
                .map(RecommendationEntityMapper())
    }

    fun getRecomParams(pageNumber: Int,
                       xSource: String,
                       pageName: String): RequestParams {
        val params = RequestParams.create()
        params.putInt(USER_ID, userSession.userId.toInt())
        params.putInt(PAGE_NUMBER, pageNumber)

        if(xSource.isEmpty()) {
            params.putString(X_SOURCE, DEFAULT_VALUE_X_SOURCE)
        } else {
            params.putString(X_SOURCE, xSource)
        }

        if(pageName.isEmpty()) {
            params.putString(PAGE_NAME, DEFAULT_PAGE_NAME)
        } else {
            params.putString(PAGE_NAME, pageName)
        }

        params.putString(X_DEVICE, DEFAULT_VALUE_X_DEVICE)
        return params
    }

    companion object {
        val USER_ID = "userID"
        val X_SOURCE = "xSource"
        val PAGE_NUMBER = "pageNumber"
        val X_DEVICE = "xDevice"
        val PAGE_NAME = "pageName"
        val DEFAULT_VALUE_X_SOURCE = "recom_widget"
        val DEFAULT_VALUE_X_DEVICE = "android"
        val DEFAULT_PAGE_NAME = ""
    }
}
