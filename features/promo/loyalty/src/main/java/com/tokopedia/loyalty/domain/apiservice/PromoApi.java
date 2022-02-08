package com.tokopedia.loyalty.domain.apiservice;

import com.tokopedia.loyalty.domain.entity.response.promo.PromoDataNew;
import com.tokopedia.loyalty.domain.entity.response.promo.PromoMenu;
import com.tokopedia.network.constant.TkpdBaseURL;

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
    Observable<Response<PromoMenu>> getMenuIndexList(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.Promo.PATH_PROMO_LIST)
    Observable<Response<PromoDataNew>> getPromoList(@QueryMap Map<String, String> params);

}
