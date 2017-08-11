package com.tokopedia.posapp.data.mapper;

import com.google.gson.Gson;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.posapp.domain.model.shop.ShopDomain;
import com.tokopedia.posapp.domain.model.shop.ShopInfoDomain;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by okasurya on 8/3/17.
 */

public class GetShopMapper implements Func1<Response<TkpdResponse>, ShopDomain> {

    public GetShopMapper() {

    }

    @Override
    public ShopDomain call(Response<TkpdResponse> response) {
        return mapResponse(response);
    }

    private ShopDomain mapResponse(Response<TkpdResponse> response) {
        if(response.body() != null && response.isSuccessful()) {
            ShopModel shopModel = response.body().convertDataObj(ShopModel.class);

            if(shopModel != null) {
                ShopDomain shopDomain = getShopFromResponse(shopModel);
                return shopDomain;
            }
        }

        return null;
    }

    private ShopDomain getShopFromResponse(ShopModel shopModel) {
        ShopDomain shopDomain = new ShopDomain();
        ShopInfoDomain shopInfoDomain = new ShopInfoDomain();
        shopInfoDomain.setShopName(shopModel.info.shopName);
        shopDomain.setShopInfo(shopInfoDomain);
        return shopDomain;
    }
}
