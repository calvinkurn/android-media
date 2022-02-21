package com.tokopedia.review.feature.inbox.buyerreview.data.mapper

import com.tokopedia.review.feature.inbox.buyerreview.data.pojo.FavoriteCheckResult
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.CheckShopFavoriteDomain
import retrofit2.Response
import rx.functions.Func1
import javax.inject.Inject

/**
 * @author by nisie on 9/26/17.
 */
class ShopFavoritedMapper @Inject constructor() : Func1<Response<FavoriteCheckResult?>?, CheckShopFavoriteDomain> {

    override fun call(response: Response<FavoriteCheckResult?>?): CheckShopFavoriteDomain {
        return if (response?.isSuccessful == true) {
            mappingToDomain(response.body()?.shopIds?.isNullOrEmpty() == true)
        } else {
            throw RuntimeException(response?.code().toString())
        }
    }
    private fun mappingToDomain(isFavorited: Boolean): CheckShopFavoriteDomain {
        return CheckShopFavoriteDomain(isFavorited)
    }
}