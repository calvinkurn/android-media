package com.tokopedia.shop.common.data.repository

import com.tokopedia.shop.common.data.source.ShopCommonDataSource
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo
import com.tokopedia.shop.common.domain.repository.ShopCommonRepository
import rx.Observable

/**
 * @author hendry on 4/20/17.
 */
class ShopCommonRepositoryImpl(private val shopInfoDataSource: ShopCommonDataSource) : ShopCommonRepository {

    override fun getShopInfo(shopId: String?, userId: String?, deviceId: String?): Observable<ShopInfo> {
        return shopInfoDataSource.getShopInfo(shopId, userId, deviceId) ?: Observable.just(ShopInfo())
    }

    override fun getShopInfoByDomain(shopDomain: String?, userId: String?, deviceId: String?): Observable<ShopInfo> {
        return shopInfoDataSource.getShopInfoByDomain(shopDomain, userId, deviceId) ?: Observable.just(ShopInfo())
    }
}