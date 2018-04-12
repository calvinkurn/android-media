package com.tokopedia.shop.common.data.source;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.shop.common.constant.ShopCommonParamApiConstant;
import com.tokopedia.shop.common.data.source.cloud.ShopCommonCloudDataSource;
import com.tokopedia.shop.common.data.source.cloud.model.ShopFavourite;
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author sebastianuskh on 3/8/17.
 */
public class ShopCommonDataSource {
    private ShopCommonCloudDataSource shopInfoCloudDataSource;

    @Inject
    public ShopCommonDataSource(ShopCommonCloudDataSource shopInfoCloudDataSource) {
        this.shopInfoCloudDataSource = shopInfoCloudDataSource;
    }

    public Observable<ShopInfo> getShopInfo(String shopId) {
        return shopInfoCloudDataSource.getShopInfo(shopId).flatMap(new Func1<Response<DataResponse<ShopInfo>>, Observable<ShopInfo>>() {
            @Override
            public Observable<ShopInfo> call(Response<DataResponse<ShopInfo>> dataResponseResponse) {
                return Observable.just(dataResponseResponse.body().getData());
            }
        });
    }

    public Observable<ShopInfo> getShopInfoByDomain(String shopDomain) {
        return shopInfoCloudDataSource.getShopInfoByDomain(shopDomain).flatMap(new Func1<Response<DataResponse<ShopInfo>>, Observable<ShopInfo>>() {
            @Override
            public Observable<ShopInfo> call(Response<DataResponse<ShopInfo>> dataResponseResponse) {
                return Observable.just(dataResponseResponse.body().getData());
            }
        });
    }

    public Observable<Boolean> toggleFavouriteShop(String shopId) {
        return shopInfoCloudDataSource.toggleFavouriteShop(shopId).flatMap(new Func1<Response<DataResponse<ShopFavourite>>, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(Response<DataResponse<ShopFavourite>> dataResponseResponse) {
                return Observable.just(dataResponseResponse.body().getData().getIsSuccess().equalsIgnoreCase(ShopCommonParamApiConstant.VALUE_TRUE_FAVOURITE));
            }
        });
    }
}
