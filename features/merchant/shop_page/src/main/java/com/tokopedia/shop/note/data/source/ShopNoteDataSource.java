package com.tokopedia.shop.note.data.source;

import com.tokopedia.network.data.model.response.DataResponse;
import com.tokopedia.shop.note.data.source.cloud.ShopNoteCloudDataSource;
import com.tokopedia.shop.note.data.source.cloud.model.ShopNoteDetail;
import com.tokopedia.shop.note.data.source.cloud.model.ShopNoteList;
import com.tokopedia.shop.note.data.source.cloud.model.ShopNote;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author sebastianuskh on 3/8/17.
 */
public class ShopNoteDataSource {
    private ShopNoteCloudDataSource shopNoteCloudDataSource;

    @Inject
    public ShopNoteDataSource(ShopNoteCloudDataSource shopNoteCloudDataSource) {
        this.shopNoteCloudDataSource = shopNoteCloudDataSource;
    }

    public Observable<List<ShopNote>> getShopNoteList(String shopId) {
        return shopNoteCloudDataSource.getShopNoteList(shopId).flatMap(new Func1<Response<DataResponse<ShopNoteList>>, Observable<List<ShopNote>>>() {
            @Override
            public Observable<List<ShopNote>> call(Response<DataResponse<ShopNoteList>> dataResponse) {
                return Observable.just(dataResponse.body().getData().getNotes());
            }
        });
    }

    public Observable<ShopNoteDetail> getShopNoteDetail(String shopNoteDetail){
        return shopNoteCloudDataSource.getShopNoteDetail(shopNoteDetail).flatMap(new Func1<Response<DataResponse<ShopNoteDetail>>, Observable<ShopNoteDetail>>() {
            @Override
            public Observable<ShopNoteDetail> call(Response<DataResponse<ShopNoteDetail>> dataResponseResponse) {
                return Observable.just(dataResponseResponse.body().getData());
            }
        });
    }
}
