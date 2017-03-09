package com.tokopedia.core.network.apiservices.rescenter.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by hangnadi on 3/9/17.
 */

public interface ResolutionApi {

    @GET(TkpdBaseURL.ResCenterV2.GET_RESOLUTION_DETAIL)
    Observable<Response<TkpdResponse>> getResCenterDetail(@Path("resolution_id") String resolutionID);

    @GET(TkpdBaseURL.ResCenterV2.GET_RESOLUTION_CONVERSATION)
    Observable<Response<TkpdResponse>> getResCenterConversation(@Path("resolution_id") String resolutionID);

    @GET(TkpdBaseURL.ResCenterV2.GET_RESOLUTION_CONVERSATION_MORE)
    Observable<Response<TkpdResponse>> getResCenterConversationMore(@Path("resolution_id") String resolutionID,
                                                              @Path("res_conv_id") String conversationID);

}
