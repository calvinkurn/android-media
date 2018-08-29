package com.tokopedia.nps.data.net;

import com.tokopedia.network.constant.TkpdBaseURL;

import java.util.HashMap;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by meta on 28/06/18.
 */
public interface NpsApi {

    @POST(TkpdBaseURL.ContactUs.PATH_FEEDBACK)
    @FormUrlEncoded
    Observable<Response<String>> postFeedback(
            @FieldMap() HashMap<String, String> params
    );
}
