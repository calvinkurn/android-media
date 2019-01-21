package com.tokopedia.core.network.apiservices.transaction.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Angga.Prasetiyo on 08/12/2015.
 */
public interface TXCartApi {
    @FormUrlEncoded
    @POST(TkpdBaseURL.Transaction.PATH_CALCULATE_CART)
    Observable<Response<TkpdResponse>> calculateCart(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Transaction.PATH_CALCULATE_CREDIT_CARD)
    Observable<Response<TkpdResponse>> calculateCCCharge(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Transaction.PATH_CART_SEARCH_ADDRESS)
    Observable<Response<TkpdResponse>> cartSearchAddress(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Transaction.PATH_GET_ADD_TO_CART_FORM)
    Observable<Response<TkpdResponse>> getAddToCartForm(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Transaction.PATH_CANCEL_CART_FORM)
    Observable<Response<TkpdResponse>> getCancelCartForm(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Transaction.PATH_GET_EDIT_ADDRESS_SHIPPING_FORM)
    Observable<Response<TkpdResponse>> getEditAddressShippingForm(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Transaction.PATH_GET_EDIT_PRODUCT_FORM)
    Observable<Response<TkpdResponse>> getEditProductForm(@FieldMap Map<String, String> params);
}
