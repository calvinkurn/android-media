package com.tokopedia.sellerapp.activities.sellerhome;

import com.tokopedia.core.network.apiservices.shop.apis.ShopApi;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.shopinfo.models.shopnotes.GetShopNotes;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.mock.BehaviorDelegate;
import retrofit2.mock.MockRetrofit;
import rx.Observable;

/**
 * Created by normansyahputa on 9/10/16.
 */
@Singleton
public class MockShopApi implements ShopApi {

    private final BehaviorDelegate<ShopApi> shopApiBehaviorDelegate;

    @Inject
    public MockShopApi(MockRetrofit mockRetrofit){
        shopApiBehaviorDelegate = mockRetrofit.create(ShopApi.class);
    }

    @Override
    public Observable<Response<TkpdResponse>> getLikeReview(@FieldMap Map<String, String> params) {
        return null;
    }

    @Override
    public Observable<Response<TkpdResponse>> getWhoFave(@FieldMap Map<String, String> params) {
        return null;
    }

    @Override
    public Observable<Response<TkpdResponse>> getEtalase(@FieldMap Map<String, String> params) {
        return null;
    }

    @Override
    public Observable<Response<TkpdResponse>> getInfo(@FieldMap Map<String, String> params) {
        return null;
    }

    @Override
    public Observable<Response<TkpdResponse>> getLocation(@FieldMap Map<String, String> params) {
        return null;
    }

    @Override
    public Observable<Response<TkpdResponse>> getNotes(@FieldMap Map<String, String> params) {
        return null;
    }

    @Override
    public Observable<Response<TkpdResponse>> getProduct(@FieldMap Map<String, String> params) {
        return null;
    }

    @Override
    public Observable<Response<TkpdResponse>> getReview(@FieldMap Map<String, String> params) {
        return null;
    }

    public Observable<Response<TkpdResponse>> getTalk(@FieldMap Map<String, String> params) {
        return null;
    }

    @Override
    public Observable<GetShopNotes> getNotes2(@FieldMap Map<String, String> params) {
        return null;
    }
}
