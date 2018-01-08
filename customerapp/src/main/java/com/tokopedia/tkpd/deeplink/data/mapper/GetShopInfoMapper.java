package com.tokopedia.tkpd.deeplink.data.mapper;

import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.seller.common.data.response.DataResponse;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by okasurya on 1/5/18.
 */

public class GetShopInfoMapper implements Func1<Response<DataResponse<ShopModel>>, ShopModel> {
    @Inject
    public GetShopInfoMapper() {

    }

    @Override
    public ShopModel call(Response<DataResponse<ShopModel>> dataResponseResponse) {
        return null;
    }
}
