package com.tokopedia.core.network.apiservices.shop.apis;

import com.tokopedia.core.network.apiservices.shop.apis.model.openshopdistrict.OpenShopDistrictModel;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author Angga.Prasetiyo on 07/12/2015.
 */
public interface MyShopApi {

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_GET_OPEN_SHOP_FORM)
    Observable<Response<TkpdResponse>> getOpenShopForm(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_GET_OPEN_SHOP_FORM)
    Observable<Response<OpenShopDistrictModel>> fetchDistrictData(@FieldMap Map<String, String> params);
}
