package com.tokopedia.review.feature.inbox.buyerreview.data.mapper

import com.tokopedia.review.feature.inbox.buyerreview.data.pojo.FavoriteCheckResult
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.CheckShopFavoriteDomain
import com.tokopedia.review.feature.inbox.buyerreview.network.ErrorMessageException
import retrofit2.Response
import rx.functions.Func1

/**
 * @author by nisie on 9/26/17.
 */
class ShopFavoritedMapper : Func1<Response<FavoriteCheckResult>, CheckShopFavoriteDomain> {
    override fun call(response: Response<FavoriteCheckResult>): CheckShopFavoriteDomain {
        return if (response.isSuccessful) {
            if (response.body()!!.shopIds != null) {
                mappingToDomain(!response.body()!!.shopIds!!.isEmpty())
            } else {
                throw ErrorMessageException("")
            }
        } else {
            throw RuntimeException(response.code().toString())
        }
    }

    private fun mappingToDomain(isFavorited: Boolean): CheckShopFavoriteDomain {
        return CheckShopFavoriteDomain(isFavorited)
    }
}