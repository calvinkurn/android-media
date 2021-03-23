package com.tokopedia.gamification.pdp.domain.usecase

import android.content.Context
import com.tokopedia.gamification.pdp.domain.GAMI_PRODUCT_RECOM_WIDGET_QUERY
import com.tokopedia.gamification.pdp.domain.Mapper
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

@GqlQuery("GamiProductRecomWidgetQuery", GAMI_PRODUCT_RECOM_WIDGET_QUERY)
class GamingRecommendationProductUseCase @Inject constructor(context: Context,
                                                             graphqlUseCase: GraphqlUseCase,
                                                             val userSession: UserSessionInterface,
                                                             val mapper: Mapper)
    : GetRecommendationUseCase(context,GamiProductRecomWidgetQuery.GQL_QUERY, graphqlUseCase, userSession) {
    var useEmptyShopId = false

    fun getRequestParams(pageNumber: Int, shopId: String, pageName: String): RequestParams {
        val rp = RequestParams()
        rp.putString(PAGE_NAME, pageName)
        rp.putInt(PAGE_NUMBER, pageNumber)
        rp.putBoolean(OFFICIAL_STORE, false)
        rp.putString(SHOP_ID, if (useEmptyShopId) "" else shopId)
        rp.putString(X_SOURCE, DEFAULT_VALUE_X_SOURCE)
        if (userSession.isLoggedIn) {
            rp.putInt(USER_ID, userSession.userId.toInt())
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