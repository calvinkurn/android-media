package com.tokopedia.gamification.pdp.domain.usecase

import com.tokopedia.gamification.pdp.data.GamingRecommendationParam
import com.tokopedia.gamification.pdp.domain.GAMI_PRODUCT_RECOM_WIDGET_QUERY
import com.tokopedia.gamification.pdp.domain.Mapper
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

@GqlQuery("GamiProductRecomWidgetQuery", GAMI_PRODUCT_RECOM_WIDGET_QUERY)
class GamingRecommendationProductUseCase @Inject constructor(graphqlUseCase: GraphqlUseCase,
                                                             val userSession: UserSessionInterface,
                                                             val mapper: Mapper)
    : GetRecommendationUseCase(GamiProductRecomWidgetQuery.GQL_QUERY, graphqlUseCase, userSession) {

    fun getRequestParams(params: GamingRecommendationParam, pageNumber: Int, shopId: Long, pageName: String): RequestParams {
        val rp = RequestParams()
        //todo Rahul ask which pageName to use in productRecommendationWidget api - from gamiCrack api or GamiRecomParams api
        rp.putString(PAGE_NAME, params.pageName)
//        rp.putString(PAGE_NAME, pageName)
        rp.putInt(PAGE_NUMBER, pageNumber)
        rp.putString(CATEGORY_IDS, params.categoryIDs)

        rp.putString(MV, params.mv)
        rp.putBoolean(OFFICIAL_STORE, params.os)
        rp.putBoolean(POWER_BADGE, params.powerBadge)
        rp.putBoolean(POWER_MERCHANT, params.powerMerchant)
        rp.putBoolean(HAS_DISCOUNT, params.hasDiscount)
        rp.putLong(SHOP_ID, shopId)
        rp.putInt(PRICE_MIN, params.priceMin)
        rp.putInt(PRICE_MAX, params.priceMax)
        rp.putString(REF, params.ref)
        rp.putString(QUERY_PARAM, params.queryParam)

        if (params.xSource.isEmpty()) {
            rp.putString(X_SOURCE, DEFAULT_VALUE_X_SOURCE)
        } else {
            rp.putString(X_SOURCE, params.xSource)
        }

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
        const val CATEGORY_IDS = "categoryIDs"
        const val MV = "mv"
        const val OFFICIAL_STORE = "os"
        const val POWER_BADGE = "powerBadge"
        const val POWER_MERCHANT = "powerMerchant"
        const val HAS_DISCOUNT = "hasDiscount"
        const val SHOP_ID = "shopId"
        const val PRICE_MIN = "priceMin"
        const val PRICE_MAX = "priceMax"
        const val REF = "ref"
        const val QUERY_PARAM = "queryParam"
        const val X_SOURCE = "xSource"
        const val USER_ID = "userID"
    }

}