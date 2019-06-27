package com.tokopedia.core.network.apiservices.etc.apis.home;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.entity.home.TopAdsHome;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by m.normansyah on 27/11/2015.
 * migrate retrofit 2 by Angga.Prasetiyo
 */

@Deprecated
public interface FavoriteApi {

    @GET(TkpdBaseURL.TopAds.PATH_DISPLAY_SHOP)
    Observable<Response<TopAdsHome>> getTopAdsApi(
            @Header("Tkpd-UserId") String contentMD5,// 1
            @Header("Tkpd-SessionId") String tkpdSessionId,// 2
            @Header("X-Device") String xDevice, // 3
            @Query("item") String item,// 4
            @Query("src") String src, // 5
            @Query("page") String page// 6
    );

}
