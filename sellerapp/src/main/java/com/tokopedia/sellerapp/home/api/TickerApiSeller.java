package com.tokopedia.sellerapp.home.api;

import com.tokopedia.tkpd.home.model.Ticker;
import com.tokopedia.tkpd.network.constants.TkpdBaseURL;

import retrofit.Response;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by zulfikarrahman on 11/7/16.
 */

public interface TickerApiSeller {

    String size = "50";
    String HEADER_USER_ID = "Tkpd-UserId";
    String PAGE_SIZE = "page[size]";
    String FILTER_DEVICE = "filter[device]";
    String FILTER_SELLERAPP_ANDROID_DEVICE = "sellerapp-android";

    @GET(TkpdBaseURL.Home.PATH_API_V1_ANNOUNCEMENT_TICKER)
    Observable<Response<Ticker>> getTickers(
            @Header(HEADER_USER_ID) String user_id,
            @Query(PAGE_SIZE) String size,
            @Query(FILTER_DEVICE) String device
    );
}
