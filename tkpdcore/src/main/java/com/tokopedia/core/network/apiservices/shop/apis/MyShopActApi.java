package com.tokopedia.core.network.apiservices.shop.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.shop.model.openShopSubmitData.OpenShopSubmitData;
import com.tokopedia.core.shop.model.openShopValidationData.OpenShopValidationData;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author Angga.Prasetiyo on 07/12/2015.
 */
public interface MyShopActApi {

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_CHECK_DOMAIN)
    Observable<Response<TkpdResponse>> checkDomain(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_CHECK_SHOP_NAME)
    Observable<Response<TkpdResponse>> checkShopName(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_OPEN_SHOP_SUBMIT)
    Observable<Response<TkpdResponse>> openShopSubmit(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_OPEN_SHOP_VALIDATION)
    Observable<Response<TkpdResponse>> openShopValidation(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_OPEN_SHOP_VALIDATION)
    Observable<OpenShopValidationData> openShopValidationNew(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_OPEN_SHOP_SUBMIT)
    Observable<OpenShopSubmitData> openShopSubmitNew(@FieldMap Map<String, String> params);

}
