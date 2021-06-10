package com.tokopedia.shop.common.data.source

import com.tokopedia.shop.common.data.source.cloud.ShopCommonCloudDataSource
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo
import rx.Observable
import javax.inject.Inject

/**
 * @author sebastianuskh on 3/8/17.
 */
class ShopCommonDataSource @Inject constructor(private val shopInfoCloudDataSource: ShopCommonCloudDataSource?) {
    fun getShopInfo(shopId: String?, userId: String?, deviceId: String?): Observable<ShopInfo>? {
        return shopInfoCloudDataSource?.getShopInfo(shopId, userId, deviceId)?.flatMap { dataResponseResponse -> Observable.just(dataResponseResponse?.body()?.data) }
    }

    fun getShopInfoByDomain(shopDomain: String?, userId: String?, deviceId: String?): Observable<ShopInfo>? {
        return shopInfoCloudDataSource?.getShopInfoByDomain(shopDomain, userId, deviceId)?.flatMap { dataResponseResponse -> Observable.just(dataResponseResponse?.body()?.data) }
    }
}