package com.tokopedia.recommendation_widget_common.domain

import android.text.TextUtils
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.recommendation_widget_common.data.SingleProductRecommendationEntity
import com.tokopedia.recommendation_widget_common.data.mapper.SingleProductRecommendationMapper
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import com.tokopedia.user.session.UserSessionInterface
import rx.Observable
import javax.inject.Inject

/**
 * Created by Lukas on 05/09/19
 */

open class GetSingleRecommendationUseCase @Inject
constructor(
        private val recomRawString: String,
        private val graphqlUseCase: GraphqlUseCase,
        private val userSession: UserSessionInterface) : UseCase<List<RecommendationItem>>() {

    override fun createObservable(requestParams: RequestParams): Observable<List<RecommendationItem>> {
        val graphqlRequest = GraphqlRequest(recomRawString, SingleProductRecommendationEntity::class.java, requestParams.parameters)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(RequestParams.EMPTY)
                .map {
                    val entity = it.getData<SingleProductRecommendationEntity>(SingleProductRecommendationEntity::class.java)
                    mapToRecommendationItem(entity.productRecommendationWidget?.data)
                }
    }

    fun getRecomParams(pageNumber: Int,
                       productIds: List<String>,
                       queryParam: String = ""): RequestParams {
        val params = RequestParams.create()
        val productIdsString = TextUtils.join(",", productIds)

        if (userSession.isLoggedIn) {
            params.putInt(USER_ID, userSession.userId.toInt())
        } else {
            params.putInt(USER_ID, 0)
        }
        params.putInt(PAGE_NUMBER, pageNumber)
        params.putString(PRODUCT_IDS, productIdsString)
        params.putString(QUERY_PARAM, queryParam)
        params.putString(X_DEVICE, DEFAULT_VALUE_X_DEVICE)
        return params
    }

    private fun mapToRecommendationItem(data: SingleProductRecommendationEntity.RecommendationData?): List<RecommendationItem>{
        if(data == null) return listOf()
        return SingleProductRecommendationMapper.convertIntoRecommendationList(
                data.recommendation,
                data.title,
                data.pageName,
                data.layoutType
        )
    }

    companion object {
        val USER_ID = "userID"
        val PAGE_NUMBER = "pageNumber"
        val X_DEVICE = "xDevice"
        val QUERY_PARAM = "queryParam"
        val DEFAULT_VALUE_X_DEVICE = "android"
        val PRODUCT_IDS = "productIDs"
    }
}
