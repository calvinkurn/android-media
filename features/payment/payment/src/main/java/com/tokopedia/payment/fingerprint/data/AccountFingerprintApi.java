package com.tokopedia.payment.fingerprint.data;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.payment.fingerprint.data.model.DataResponseSavePublicKey;
import com.tokopedia.payment.fingerprint.util.FingerprintConstant;

import java.util.HashMap;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by zulfikarrahman on 3/27/18.
 */

public interface AccountFingerprintApi {

    @FormUrlEncoded
    @POST(FingerprintConstant.OTP_FINGERPRINT_ADD)
    Observable<Response<DataResponse<DataResponseSavePublicKey>>> savePublicKey(@FieldMap HashMap<String, String> params);
}
