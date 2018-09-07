package com.tokopedia.shop.etalase.data.source.cloud;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.shop.common.data.source.cloud.api.ShopWSApi;
import com.tokopedia.shop.etalase.data.source.cloud.model.EtalaseModel;
import com.tokopedia.shop.etalase.data.source.cloud.model.PagingListOther;
import com.tokopedia.shop.etalase.domain.model.ShopEtalaseRequestModel;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by normansyahputa on 2/28/18.
 */

public class ShopEtalaseCloudDataSource {
    private final ShopWSApi shopWS4Api;

    @Inject
    public ShopEtalaseCloudDataSource(ShopWSApi shopWS4Api) {
        this.shopWS4Api = shopWS4Api;
    }

    public Observable<Response<DataResponse<PagingListOther<EtalaseModel>>>> getShopEtalaseList(
            ShopEtalaseRequestModel shopProductRequestModel) {
        return shopWS4Api.getShopEtalase(shopProductRequestModel.getHashMap());
    }
}
