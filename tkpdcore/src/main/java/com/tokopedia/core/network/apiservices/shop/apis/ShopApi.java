package com.tokopedia.core.network.apiservices.shop.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.shopinfo.models.shopnotes.GetShopNotes;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @author Angga.Prasetiyo on 07/12/2015.
 */
public interface ShopApi {

    @GET(TkpdBaseURL.Shop.PATH_GET_LIKE_DISLIKE_REVIEW)
    Observable<Response<TkpdResponse>> getLikeReview(@QueryMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_GET_PEOPLE_FAV_MY_SHOP)
    Observable<Response<TkpdResponse>> getWhoFave(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_GET_SHOP_ETALASE)
    Observable<Response<TkpdResponse>> getEtalase(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_GET_SHOP_INFO)
    Observable<Response<TkpdResponse>> getInfo(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_GET_SHOP_LOCATION)
    Observable<Response<TkpdResponse>> getLocation(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_GET_SHOP_NOTES)
    Observable<Response<TkpdResponse>> getNotes(@FieldMap Map<String, String> params);

    @GET(TkpdBaseURL.Shop.PATH_GET_SHOP_PRODUCT)
    Observable<Response<TkpdResponse>> getProduct(@QueryMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_GET_SHOP_REVIEW)
    Observable<Response<TkpdResponse>> getReview(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_GET_SHOP_NOTES)
    Observable<GetShopNotes> getNotes2(@FieldMap Map<String, String> params);

}
