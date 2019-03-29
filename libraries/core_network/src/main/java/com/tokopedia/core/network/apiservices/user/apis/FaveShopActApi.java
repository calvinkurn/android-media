package com.tokopedia.core.network.apiservices.user.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.entity.home.FavoriteSendData;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author Angga.Prasetiyo on 02/12/2015.
 */

@Deprecated
public interface FaveShopActApi {

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_FAVE_SHOP)
    Observable<Response<TkpdResponse>> faveShop(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_FAVE_SHOP)
    Observable<FavoriteSendData> faveShop2(@FieldMap Map<String, String> params);
}
