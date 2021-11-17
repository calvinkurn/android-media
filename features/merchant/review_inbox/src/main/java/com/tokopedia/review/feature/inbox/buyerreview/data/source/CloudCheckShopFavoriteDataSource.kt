package com.tokopedia.review.feature.inbox.buyerreview.data.source

import com.tokopedia.review.feature.inbox.buyerreview.data.mapper.ShopFavoritedMapper
import com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inboxdetail.CheckShopFavoritedUseCase
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.CheckShopFavoriteDomain
import com.tokopedia.review.feature.inbox.buyerreview.network.tome.TomeService
import com.tokopedia.usecase.RequestParams
import rx.Observable

/**
 * @author by nisie on 9/26/17.
 */
class CloudCheckShopFavoriteDataSource(
    private val tomeService: TomeService,
    private val shopFavoritedMapper: ShopFavoritedMapper
) {
    fun checkShopIsFavorited(requestParams: RequestParams): Observable<CheckShopFavoriteDomain> {
        return tomeService.api
            .checkIsShopFavorited(
                requestParams.getString(CheckShopFavoritedUseCase.PARAM_USER_ID, ""),
                requestParams.getString(CheckShopFavoritedUseCase.PARAM_SHOP_ID, "")
            )
            .map(shopFavoritedMapper)
    }
}