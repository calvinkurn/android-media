package com.tokopedia.recommendation_widget_common.domain

import android.content.Context
import android.text.TextUtils

import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.data.RecomendationEntity
import com.tokopedia.recommendation_widget_common.data.mapper.RecommendationEntityMapper
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import com.tokopedia.user.session.UserSessionInterface

import javax.inject.Inject

import rx.Observable

/**
 * Created by devara fikry on 16/04/19.
 * Credit errysuprayogi
 */

open class GetRecommendationUseCase @Inject
constructor(
            private val recomRawString: String,
            private val graphqlUseCase: GraphqlUseCase,
            private val userSession: UserSessionInterface) : UseCase<List<RecommendationWidget>>() {

    override fun createObservable(requestParams: RequestParams): Observable<List<RecommendationWidget>> {
        val graphqlRequest = GraphqlRequest(recomRawString, RecomendationEntity::class.java, requestParams.parameters)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(RequestParams.EMPTY)
                .map<List<RecomendationEntity.RecomendationData>> { graphqlResponse ->
                    val entity = graphqlResponse.getData<RecomendationEntity>(RecomendationEntity::class.java)
                    entity?.productRecommendationWidget?.data
                }
                .map(RecommendationEntityMapper())
    }

    fun getRecomParams(pageNumber: Int,
                       xSource: String,
                       pageName: String,
                       ref: String,
                       productIds: List<String>): RequestParams {
        val params = RequestParams.create()
        val productIdsString = TextUtils.join(",", productIds)

        if (userSession.isLoggedIn) {
            params.putInt(USER_ID, userSession.userId.toInt())
        } else {
            params.putInt(USER_ID, 0)
        }
        params.putInt(PAGE_NUMBER, pageNumber)
        params.putString(PRODUCT_IDS, productIdsString)

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
        params.putString(REF, ref)
        return params
    }

    companion object {
        val USER_ID = "userID"
        val X_SOURCE = "xSource"
        val PAGE_NUMBER = "pageNumber"
        val X_DEVICE = "xDevice"
        val PAGE_NAME = "pageName"
        val REF = "ref"
        val DEFAULT_VALUE_X_SOURCE = "recom_widget"
        val DEFAULT_VALUE_X_DEVICE = "android"
        val DEFAULT_PAGE_NAME = ""
        val PRODUCT_IDS = "productIDs"
    }
}
