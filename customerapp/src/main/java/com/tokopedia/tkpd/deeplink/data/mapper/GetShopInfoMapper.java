package com.tokopedia.tkpd.deeplink.data.mapper;

import com.tokopedia.core.network.exception.ServerErrorException;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.seller.common.data.response.DataResponse;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
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
        if (dataResponseResponse.isSuccessful()) {
            if(dataResponseResponse.body() != null && dataResponseResponse.body().getData() != null) {
                return dataResponseResponse.body().getData();
            } else {
                return null;
            }
        } else {
            throw new RuntimeException("Server Error");
        }
    }
}
