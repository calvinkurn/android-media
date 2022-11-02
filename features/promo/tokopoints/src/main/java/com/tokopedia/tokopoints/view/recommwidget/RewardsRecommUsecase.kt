package com.tokopedia.tokopoints.view.recommwidget

import android.content.Context
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

@GqlQuery("RewardsProductRecomWidgetQuery", REWARDS_PRODUCT_RECOM_WIDGET_QUERY)
class RewardsRecommUsecase @Inject constructor(context: Context,
                                                             graphqlUseCase: GraphqlUseCase,
                                                             val userSession: UserSessionInterface,
                                                             val mapper: DataMapper)
    : GetRecommendationUseCase(context,RewardsProductRecomWidgetQuery.GQL_QUERY, graphqlUseCase, userSession) {

    suspend fun getRequestParams(pageNumber: Int , pageName: String): RequestParams {
        val rp = RequestParams()
        rp.putString(PAGE_NAME, pageName)
        rp.putInt(PAGE_NUMBER, pageNumber)
        rp.putBoolean(OFFICIAL_STORE, false)
        rp.putString(SHOP_ID, "")
        rp.putString(X_SOURCE, "rewards")
        if (userSession.isLoggedIn) {
            rp.putInt(USER_ID, userSession.userId.toIntOrZero())
        } else {
            rp.putInt(USER_ID, 0)
        }
        return rp
    }

    companion object PARAMS {
        const val PAGE_NAME = "pageName"
        const val PAGE_NUMBER = "pageNumber"
        const val OFFICIAL_STORE = "os"
        const val SHOP_ID = "shopIDs"
        const val X_SOURCE = "xSource"
        const val USER_ID = "userID"
    }

}