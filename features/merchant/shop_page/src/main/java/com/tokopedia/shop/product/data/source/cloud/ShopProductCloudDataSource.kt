package com.tokopedia.shop.product.data.source.cloud

import com.tokopedia.abstraction.common.data.model.response.PagingList
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.shop.common.constant.ShopCommonUrl
import com.tokopedia.shop.common.constant.ShopUrl
import com.tokopedia.shop.common.data.source.cloud.api.ShopApi
import com.tokopedia.shop.product.data.source.cloud.api.ShopOfficialStoreApi
import com.tokopedia.shop.product.data.source.cloud.model.ShopProduct
import com.tokopedia.shop.product.data.source.cloud.model.ShopProductCampaign
import com.tokopedia.shop.product.domain.model.ShopProductRequestModel
import retrofit2.Response
import rx.Observable
import rx.functions.Func1
import javax.inject.Inject

/**
 * Created by nathan on 2/15/18.
 */
class ShopProductCloudDataSource @Inject constructor(private val shopApi: ShopApi, private val shopOfficalStoreApi: ShopOfficialStoreApi) {
    fun getShopProductList(shopProductRequestModel: ShopProductRequestModel): Observable<Response<DataResponse<PagingList<ShopProduct?>?>?>?>? {
        val baseUrl: String
        baseUrl = if (shopProductRequestModel.useTome()) {
            ShopCommonUrl.BASE_URL
        } else {
            ShopUrl.BASE_ACE_URL
        }
        return shopApi.getShopProductList(baseUrl + ShopUrl.SHOP_PRODUCT_PATH, shopProductRequestModel.hashMap.toMap())
    }

    fun getProductCampaigns(idList: String?): Observable<List<ShopProductCampaign?>?> {
        return shopOfficalStoreApi.getProductCampaigns(idList)!!.map(Func1 { response -> response?.body()?.data })
    }
}