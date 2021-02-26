package com.tokopedia.recommendation_widget_common.domain

import android.content.Context
import android.text.TextUtils
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.recommendation_widget_common.data.RecommendationEntity
import com.tokopedia.recommendation_widget_common.data.SingleProductRecommendationEntity
import com.tokopedia.recommendation_widget_common.ext.toQueryParam
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
        private val context: Context,
        private val recomRawString: String,
        private val graphqlUseCase: GraphqlUseCase,
        private val userSession: UserSessionInterface) : UseCase<RecommendationEntity.RecommendationData>() {

    override fun createObservable(requestParams: RequestParams): Observable<RecommendationEntity.RecommendationData> {
        val graphqlRequest = GraphqlRequest(recomRawString, SingleProductRecommendationEntity::class.java, requestParams.parameters)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(RequestParams.EMPTY)
                .map {
                    val entity = it.getData<SingleProductRecommendationEntity>(SingleProductRecommendationEntity::class.java)
                    entity.productRecommendationWidget.data
                }
    }

    fun getRecomParams(pageNumber: Int,
                       productIds: List<String>,
                       queryParam: String = ""): RequestParams {
        val params = RequestParams.create()
        val productIdsString = TextUtils.join(",", productIds)
        val newQueryParam = ChooseAddressUtils.getLocalizingAddressData(context)?.toQueryParam(queryParam) ?: queryParam
        if (userSession.isLoggedIn) {
            params.putInt(USER_ID, userSession.userId.toInt())
        } else {
            params.putInt(USER_ID, 0)
        }
        params.putInt(PAGE_NUMBER, pageNumber)
        params.putString(PRODUCT_IDS, productIdsString)
        params.putString(QUERY_PARAM, newQueryParam)
        params.putString(X_DEVICE, DEFAULT_VALUE_X_DEVICE)
        return params
    }

    companion object {
        const val USER_ID = "userID"
        const val PAGE_NUMBER = "pageNumber"
        const val X_DEVICE = "xDevice"
        const val QUERY_PARAM = "queryParam"
        const val DEFAULT_VALUE_X_DEVICE = "android"
        const val PRODUCT_IDS = "productIDs"
    }
}
