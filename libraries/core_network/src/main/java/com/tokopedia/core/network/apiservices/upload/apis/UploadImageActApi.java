package com.tokopedia.core.network.apiservices.upload.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author hangnadi on 2/22/16.
 */
public interface UploadImageActApi {

    @FormUrlEncoded
    @POST(TkpdBaseURL.Upload.PATH_ADD_PRODUCT_PICTURE)
    Observable<Response<TkpdResponse>> addProductPicture(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Upload.PATH_CREATE_RESOLUTION_PICTURE)
    Observable<Response<TkpdResponse>> createResolutionPicture(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Upload.PATH_OPEN_SHOP_PICTURE)
    Observable<Response<TkpdResponse>> openShopPicture(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Upload.PATH_TICKET_PICTURE)
    Observable<Response<TkpdResponse>> ticketPicture(@FieldMap Map<String, String> params);
}
