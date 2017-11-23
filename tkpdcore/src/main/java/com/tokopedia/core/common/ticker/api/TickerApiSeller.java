package com.tokopedia.core.common.ticker.api;

import com.tokopedia.core.common.ticker.model.Ticker;
import com.tokopedia.core.network.constants.TkpdBaseURL;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
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
