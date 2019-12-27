package com.tokopedia.core.network.apiservices.upload.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.GeneratedHost;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author Angga.Prasetiyo on 10/12/2015.
 *         Added by m.normansyah on 22/12/2015, for manual purpose
 */
@Deprecated
public interface GeneratedHostActApi {

    @FormUrlEncoded
    @POST(TkpdBaseURL.Upload.PATH_GENERATE_HOST)
    Observable<GeneratedHost> generateHost(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Upload.PATH_GENERATE_HOST_V2)
    Observable<Response<TkpdResponse>> generateHost3(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Upload.PATH_GENERATE_HOST)
    Observable<Response<TkpdResponse>> generateHost4(@FieldMap Map<String, String> params);
}
