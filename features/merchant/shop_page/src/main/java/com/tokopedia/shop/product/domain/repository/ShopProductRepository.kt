package com.tokopedia.shop.product.domain.repository

import com.tokopedia.abstraction.common.data.model.response.PagingList
import com.tokopedia.shop.product.data.source.cloud.model.ShopProduct
import com.tokopedia.shop.product.domain.model.ShopProductRequestModel
import com.tokopedia.shop.product.data.source.cloud.model.ShopProductCampaign
import rx.Observable

/**
 * Created by sebastianuskh on 3/8/17.
 */
interface ShopProductRepository {
    fun getShopProductList(shopProductRequestModel: ShopProductRequestModel): Observable<PagingList<ShopProduct?>>?
    fun getProductCampaigns(ids: String): Observable<List<ShopProductCampaign?>?>
}