package com.tokopedia.shop.product.data.repository

import com.tokopedia.abstraction.common.data.model.response.PagingList
import com.tokopedia.shop.product.data.source.cloud.ShopProductCloudDataSource
import com.tokopedia.shop.product.data.source.cloud.model.ShopProduct
import com.tokopedia.shop.product.data.source.cloud.model.ShopProductCampaign
import com.tokopedia.shop.product.domain.model.ShopProductRequestModel
import com.tokopedia.shop.product.domain.repository.ShopProductRepository
import rx.Observable
import rx.functions.Func1
import javax.inject.Inject

/**
 * Created by nathan on 2/15/18.
 */
class ShopProductRepositoryImpl @Inject constructor(private val shopProductCloudDataSource: ShopProductCloudDataSource) : ShopProductRepository {
    override fun getShopProductList(shopProductRequestModel: ShopProductRequestModel): Observable<PagingList<ShopProduct?>>? {
        return shopProductCloudDataSource.getShopProductList(shopProductRequestModel)
            ?.flatMap(Func1 { dataResponseResponse -> Observable.just(dataResponseResponse?.body()?.data) })
    }

    override fun getProductCampaigns(ids: String): Observable<List<ShopProductCampaign?>?> {
        return shopProductCloudDataSource.getProductCampaigns(ids)
    }
}
