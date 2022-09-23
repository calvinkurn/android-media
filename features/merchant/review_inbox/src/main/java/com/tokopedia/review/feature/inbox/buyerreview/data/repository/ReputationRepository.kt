package com.tokopedia.review.feature.inbox.buyerreview.data.repository

import com.tokopedia.review.feature.inbox.buyerreview.data.factory.ReputationFactory
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.CheckShopFavoriteDomain
import com.tokopedia.usecase.RequestParams
import rx.Observable
import javax.inject.Inject

/**
 * @author by nisie on 8/14/17.
 */
class ReputationRepository @Inject constructor(
    private val reputationFactory: ReputationFactory
) {

    fun checkIsShopFavorited(requestParams: RequestParams): Observable<CheckShopFavoriteDomain> {
        return reputationFactory
            .createCloudCheckShopFavoriteDataSource()
            .checkShopIsFavorited(requestParams)
    }
}