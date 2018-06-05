package com.tokopedia.core.network.apiservices.transaction.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @author Angga.Prasetiyo on 08/12/2015.
 */
public interface TXCartActApi {

    @FormUrlEncoded
    @POST(TkpdBaseURL.Transaction.PATH_ADD_TO_CART)
    Observable<Response<TkpdResponse>> addToCart(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Transaction.PATH_CANCEL_CART)
    Observable<Response<TkpdResponse>> cancelCart(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Transaction.PATH_EDIT_ADDRESS)
    Observable<Response<TkpdResponse>> editAddress(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Transaction.PATH_EDIT_INSURANCE)
    Observable<Response<TkpdResponse>> editInsurance(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Transaction.PATH_EDIT_PRODUCT)
    Observable<Response<TkpdResponse>> editProduct(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Transaction.PATH_EDIT_CART)
    Observable<Response<TkpdResponse>> editCart(@FieldMap Map<String, String> params);

}
