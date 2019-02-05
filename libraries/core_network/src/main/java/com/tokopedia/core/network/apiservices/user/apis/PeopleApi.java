package com.tokopedia.core.network.apiservices.user.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @author Angga.Prasetiyo on 07/12/2015.
 */

@Deprecated
public interface PeopleApi {

    @GET(TkpdBaseURL.User.PATH_GET_ADDRESS)
    Observable<Response<TkpdResponse>> getAddress(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.User.PATH_GET_BANK_ACCOUNT)
    Observable<Response<TkpdResponse>> getBankAccount(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.User.PATH_GET_DEFAULT_BANK_ACCOUNT)
    Observable<Response<TkpdResponse>> getDefaultBankAccount(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.User.PATH_GET_FAVORITE_SHOP)
    Observable<Response<TkpdResponse>> getFavoriteShop(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.User.PATH_GET_NOTIFICATION)
    Observable<Response<TkpdResponse>> getNotification(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.User.PATH_GET_PEOPLE_INFO)
    Observable<Response<TkpdResponse>> getPeopleInfo(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.User.PATH_GET_PRIVACY)
    Observable<Response<TkpdResponse>> getPrivacy(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.User.PATH_GET_PROFILE)
    Observable<Response<TkpdResponse>> getProfile(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.User.PATH_GET_RANDOM_FAV_SHOP)
    Observable<Response<TkpdResponse>> getRandomFavShop(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.User.PATH_GET_SEARCH_BANK_ACCOUNT)
    Observable<Response<TkpdResponse>> searchBankAccount(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.User.PATH_GET_PEOPLE_INFO)
    Observable<Response<TkpdResponse>> getProfile2(@QueryMap Map<String, Object> params);
}
