package com.tokopedia.core.network.apiservices.galadriel;

import android.content.Context;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.product.model.productdetail.promowidget.PromoWidgetResponse;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by alifa on 9/13/17.
 */

@Deprecated
public interface GaladrielApi {

    String VALUE_PDP_WIDGET = "pdp_widget";

    String VALUE_TARGET_GUEST = "guest";
    String VALUE_TARGET_LOGIN_USER = "login_user";
    String VALUE_TARGET_MERCHANT = "merchant";
    String VALUE_TARGET_GOLD_MERCHANT = "gold_merchant";

    String VALUE_DEVICE = "android";

    String VALUE_LANG = "id";


    String PLACEHOLDER = "placeholder";
    String TARGET_TYPE = "target_type";
    String DEVICE_TYPE = "device_type";
    String LANG = "lang";
    String USER_ID = "user_id";
    String SHOP_TYPE = "shop_type";

    @GET(TkpdBaseURL.Galadriel.PATH_PROMO_WIDGET)
    Observable<Response<PromoWidgetResponse>> getPromoWidget(
            @Query(PLACEHOLDER) String placeholder, @Query(TARGET_TYPE) String targetType, @Query(DEVICE_TYPE) String deviceType,
            @Query(LANG) String lang, @Query(USER_ID) String uerId, @Query(SHOP_TYPE) String shopType);
}
