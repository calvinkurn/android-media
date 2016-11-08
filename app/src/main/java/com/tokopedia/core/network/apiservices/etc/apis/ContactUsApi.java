package com.tokopedia.core.network.apiservices.etc.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * ContactUsApi
 * Created by Angga.Prasetiyo on 07/12/2015.
 */
public interface ContactUsApi {

    @GET(TkpdBaseURL.Etc.PATH_GET_FORM_MODEL_CONTACT_US)
    Observable<Response<TkpdResponse>> getFormModel(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.Etc.PATH_GET_TREE_TICKET_CATEGORY)
    Observable<Response<TkpdResponse>> getTreeTicketCategory(@QueryMap Map<String, String> params);
}
