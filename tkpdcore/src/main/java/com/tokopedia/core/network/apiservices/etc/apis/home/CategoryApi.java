package com.tokopedia.core.network.apiservices.etc.apis.home;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.entity.home.Brands;
import com.tokopedia.core.network.entity.home.Slide;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by m.normansyah on 2/24/16.
 * https://wiki.tokopedia.net/Slides/Banner_API_Documentation
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public interface CategoryApi {
    String MOJITO = TkpdBaseURL.MOJITO_DOMAIN;

    //[DEFAULT] values
    String size = "50";
    String number = "1";
    String ANDROID_DEVICE = "8";
    String TARGET_SLIDES = "65536";
    String state = "1";
    String expired = "0";
    String newExpired = "1";
    String TARGET_BANNER = "65535";
    String FILTER_ANDROID_DEVICE = "android";

    String TARGET_UNKNOWN = "0";
    String TARGET_ALL = "65535";
    String TARGET_ANONYMOUS = "1";
    String TARGET_LOGGED_IN = "2";
    String TARGET_MERCHANT = "4";
    String TARGET_GOLD_MERCHAT = "8";

    String TARGET_SLIDE_TYPE = "digital_items";

    // query param
    String API_V1_BANNERS = "/api/v1/banners";
    String API_V1_SLIDES = "/api/v1/slides";
    String API_V1_BRANDS = "/os/api/v1/brands/list";
    String API_V1_ANNOUNCEMENT_TICKER = "/api/v1/tickers";
    String DEVICE = "device";
    String PAGE_SIZE = "page[size]";
    String FILTER_STATE = "filter[state]";
    String FILTER_EXPIRED = "filter[expired]";
    String PAGE_NUMBER = "page[number]";
    String FILTER_DEVICE = "filter[device]";
    String FILTER_TARGET = "filter[target]";
    String FILTER_STATE1 = "filter[state]";
    String FILTER_SLIDE_TYPE = "filter[slide_type]";

    String HEADER_USER_ID = "Tkpd-UserId";

    @GET(API_V1_SLIDES)
    Observable<Response<String>> getBanners(@Header(HEADER_USER_ID) String user_id,
                                            @QueryMap Map<String, Object> params);

    @GET(API_V1_SLIDES)
    Observable<Response<Slide>> getSlides(
            @Header(HEADER_USER_ID) String user_id,
            @Query(PAGE_SIZE) String size,
            @Query(PAGE_NUMBER) String number,
            @Query(FILTER_DEVICE) String device,
            @Query(FILTER_STATE) String state,
            @Query(FILTER_EXPIRED) String expired
    );

    @GET(API_V1_BRANDS)
    Observable<Response<Brands>> getBrands(
            @Header(HEADER_USER_ID) String user_id,
            @Query(DEVICE) String device
    );
}
