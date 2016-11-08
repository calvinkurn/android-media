package com.tokopedia.core.network.apiservices.user.apis;

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
public interface InboxTalkApi {

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_GET_INBOX_DETAIL_TALK)
    Observable<Response<TkpdResponse>> getDetail(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_GET_INBOX_TALK)
    Observable<Response<TkpdResponse>> getInbox(@FieldMap Map<String, String> params);
}
