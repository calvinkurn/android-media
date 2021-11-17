package com.tokopedia.review.feature.inbox.buyerreview.network.tome

import com.tokopedia.review.feature.inbox.buyerreview.data.pojo.FavoriteCheckResult
import com.tokopedia.review.feature.inbox.buyerreview.network.ReputationBaseURL
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable

interface TomeApi {
    @GET(ReputationBaseURL.PATH_IS_FAVORITE_SHOP)
    fun checkIsShopFavorited(
        @Query(USER_ID) userId: String?,
        @Query(SHOP_ID) shopIds: String?
    ): Observable<Response<FavoriteCheckResult?>?>

    companion object {
        const val USER_ID: String = "user_id"
        const val SHOP_ID: String = "shop_id"
    }
}