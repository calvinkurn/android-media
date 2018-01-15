package com.tokopedia.tkpd.deeplink.data.source;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.seller.common.data.response.DataResponse;
import com.tokopedia.seller.shop.common.data.source.cloud.api.ShopApi;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by okasurya on 1/5/18.
 */

public class ShopInfoCloudSource implements ShopInfoSource  {
    private ShopApi shopApi;

    @Inject
    public ShopInfoCloudSource(ShopApi shopApi) {
        this.shopApi = shopApi;
    }

    @Override
    public Observable<Response<DataResponse<ShopModel>>> getShopInfo(RequestParams requestParams) {
        return shopApi.getShopInfo(requestParams.getParamsAllValueInString());
    }
}
