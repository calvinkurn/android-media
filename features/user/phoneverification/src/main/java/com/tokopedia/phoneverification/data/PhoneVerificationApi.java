package com.tokopedia.phoneverification.data;

import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response;
import com.tokopedia.network.utils.TKPDMapParam;
import com.tokopedia.phoneverification.PhoneVerificationConst;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author by alvinatin on 12/10/18.
 */

public interface PhoneVerificationApi {
    @FormUrlEncoded
    @POST(PhoneVerificationConst.CHANGE_PHONE_NUMBER)
    Observable<Response<TokopediaWsV4Response>> changePhoneNumber(@FieldMap Map<String, String> parameters);

    @FormUrlEncoded
    @POST(PhoneVerificationConst.VERIFY_PHONE_NUMBER)
    Observable<Response<TokopediaWsV4Response>> verifyPhoneNumber(@FieldMap HashMap<String, Object> param);

}
