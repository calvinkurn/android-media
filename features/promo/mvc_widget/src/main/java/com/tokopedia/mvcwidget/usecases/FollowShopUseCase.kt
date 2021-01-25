package com.tokopedia.mvcwidget.usecases

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.mvcwidget.FOLLOW_SHOP
import com.tokopedia.mvcwidget.FollowShop
import com.tokopedia.mvcwidget.FollowShopResponse
import com.tokopedia.mvcwidget.GqlUseCaseWrapper
import kotlinx.coroutines.delay
import javax.inject.Inject

@GqlQuery("FollowShopQuery", FOLLOW_SHOP)
class FollowShopUseCase @Inject constructor(val gqlWrapper: GqlUseCaseWrapper) {

    suspend fun getResponse(map: HashMap<String, Any>): FollowShopResponse? {
        return getFakeResponseSuccess()
//        return gqlWrapper.getResponse(FollowShopResponse::class.java, FollowShopQuery.GQL_QUERY, map)
    }

    fun getQueryParams(shopId: String): HashMap<String, Any> {
        val variables = HashMap<String, Any>()
        val map = mutableMapOf<String, Any>()
        map[FollowShopParams.SHOP_ID] = shopId
        variables[FollowShopParams.INPUT] = shopId
        return variables
    }

    suspend fun getFakeResponseSuccess(): FollowShopResponse {
        delay(1000)
        return FollowShopResponse(FollowShop(true))
    }

    suspend fun getFakeResponseFail(): FollowShopResponse {
        delay(1000)
        return FollowShopResponse(FollowShop(false))
    }
}

object FollowShopParams {
    const val SHOP_ID = "shopId"
    const val INPUT = "input"
}