package com.tokopedia.posapp.shop.data;

import com.tokopedia.posapp.base.data.pojo.PosSimpleResponse;
import com.tokopedia.posapp.shop.data.pojo.ShopResponse;
import com.tokopedia.posapp.shop.domain.model.ShopDomain;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by okasurya on 8/3/17.
 */

public class GetShopMapper implements Func1<Response<PosSimpleResponse<ShopResponse>>, ShopDomain> {

    @Inject
    public GetShopMapper() {

    }

    @Override
    public ShopDomain call(Response<PosSimpleResponse<ShopResponse>> response) {
        if(response.body() != null && response.isSuccessful()) {
            return getShopFromResponse(response.body().getData().getData());
        }

        return null;
    }

    private ShopDomain getShopFromResponse(ShopResponse shopModel) {
        ShopDomain shopDomain = new ShopDomain();
        shopDomain.setId(shopModel.getShopId());
        shopDomain.setAvatar(shopModel.getShopAvatar());
        shopDomain.setCover(shopModel.getShopCover());
        shopDomain.setDescription(shopModel.getShopDescription());
        shopDomain.setDomain(shopModel.getShopDomain());
        shopDomain.setName(shopModel.getShopDomain());
        shopDomain.setTagline(shopDomain.getTagline());
        return shopDomain;
    }
}
