package com.tokopedia.shop.note.data.source.cloud;

import com.tokopedia.network.data.model.response.DataResponse;
import com.tokopedia.shop.common.data.source.cloud.api.ShopApi;
import com.tokopedia.shop.common.data.source.cloud.api.ShopCommonApi;
import com.tokopedia.shop.note.data.source.cloud.model.ShopNoteDetail;
import com.tokopedia.shop.note.data.source.cloud.model.ShopNoteList;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;

/**
 * @author hendry on 4/4/17.
 */

public class ShopNoteCloudDataSource {

    private ShopApi shopApi;

    @Inject
    public ShopNoteCloudDataSource(ShopApi shopApi) {
        this.shopApi = shopApi;
    }

    public Observable<Response<DataResponse<ShopNoteList>>> getShopNoteList(String shopId) {
        return shopApi.getShopNotes(shopId);
    }

    public Observable<Response<DataResponse<ShopNoteDetail>>> getShopNoteDetail(String shopNoteId){
        return shopApi.getShopNoteDetail(shopNoteId);
    }
}
