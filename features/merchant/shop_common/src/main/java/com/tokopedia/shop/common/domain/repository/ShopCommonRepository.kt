package com.tokopedia.shop.common.domain.repository

import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo
import rx.Observable

/**
 * Created by sebastianuskh on 3/8/17.
 */
interface ShopCommonRepository {
    fun getShopInfo(shopId: String?, userId: String?, deviceId: String?): Observable<ShopInfo>
    fun getShopInfoByDomain(shopDomain: String?, userId: String?, deviceId: String?): Observable<ShopInfo>
}