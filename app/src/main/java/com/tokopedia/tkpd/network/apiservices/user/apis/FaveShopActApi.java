package com.tokopedia.tkpd.network.apiservices.user.apis;

import com.tokopedia.tkpd.home.model.network.FavoriteSendData;
import com.tokopedia.tkpd.network.constants.TkpdBaseURL;
import com.tokopedia.tkpd.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author Angga.Prasetiyo on 02/12/2015.
 */
public interface FaveShopActApi {

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_FAVE_SHOP)
    Observable<Response<TkpdResponse>> faveShop(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_FAVE_SHOP)
    Observable<FavoriteSendData> faveShop2(@FieldMap Map<String, String> params);
}
