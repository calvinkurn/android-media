package com.tokopedia.loyalty.domain.apiservice;

import com.tokopedia.loyalty.domain.entity.response.promo.MenuPromoResponse;
import com.tokopedia.loyalty.domain.entity.response.promo.PromoResponse;
import com.tokopedia.network.constant.TkpdBaseURL;

import java.util.List;
import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @author anggaprasetiyo on 02/01/18.
 */

public interface PromoApi {

    @GET(TkpdBaseURL.Promo.PATH_MENU_INDEX)
    Observable<Response<List<MenuPromoResponse>>> getMenuIndexList(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.Promo.PATH_PROMO_LIST)
    Observable<Response<List<PromoResponse>>> getPromoList(@QueryMap Map<String, String> params);

}
