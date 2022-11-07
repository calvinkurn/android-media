package com.tokopedia.review.feature.inbox.buyerreview.data.factory

import com.tokopedia.review.feature.inbox.buyerreview.data.mapper.ShopFavoritedMapper
import com.tokopedia.review.feature.inbox.buyerreview.data.source.CloudCheckShopFavoriteDataSource
import com.tokopedia.review.feature.inbox.buyerreview.network.tome.TomeService
import javax.inject.Inject

/**
 * @author by nisie on 8/14/17.
 */
class ReputationFactory @Inject constructor(
    private val tomeService: TomeService,
    private val shopFavoritedMapper: ShopFavoritedMapper
) {
    fun createCloudCheckShopFavoriteDataSource(): CloudCheckShopFavoriteDataSource {
        return CloudCheckShopFavoriteDataSource(tomeService, shopFavoritedMapper)
    }
}