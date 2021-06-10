package com.tokopedia.shop.common.data.source.cloud.api

import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.shop.common.constant.ShopCommonParamApiConstant
import com.tokopedia.shop.common.constant.ShopCommonUrl
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable

/**
 * Created by Hendry on 4/20/2017.
 */
interface ShopCommonApi {
    @GET(ShopCommonUrl.SHOP_INFO_PATH)
    fun getShopInfo(@Query(ShopCommonParamApiConstant.SHOP_ID) shopId: String?,
                    @Query(ShopCommonParamApiConstant.USER_ID) userId: String?,
                    @Query(ShopCommonParamApiConstant.OS_TYPE) osType: String?,
                    @Query(ShopCommonParamApiConstant.DEVICE_ID) deviceId: String?): Observable<Response<DataResponse<ShopInfo?>?>?>?

    @GET(ShopCommonUrl.SHOP_INFO_PATH)
    fun getShopInfoByDomain(@Query(ShopCommonParamApiConstant.SHOP_DOMAIN) shopDomain: String?,
                            @Query(ShopCommonParamApiConstant.USER_ID) userId: String?,
                            @Query(ShopCommonParamApiConstant.OS_TYPE) osType: String?,
                            @Query(ShopCommonParamApiConstant.DEVICE_ID) deviceId: String?): Observable<Response<DataResponse<ShopInfo?>?>?>?
}