package com.tokopedia.core.network.apiservices.user.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @author Angga.Prasetiyo on 07/12/2015.
 */

@Deprecated
public interface InvoiceApi {

    @GET(TkpdBaseURL.User.PATH_RENDER_INVOICE)
    Observable<Response<String>> render(@QueryMap Map<String, String> params);
}
