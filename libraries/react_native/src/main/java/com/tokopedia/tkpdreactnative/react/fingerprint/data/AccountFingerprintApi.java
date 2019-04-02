package com.tokopedia.tkpdreactnative.react.fingerprint.data;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.tkpdreactnative.react.fingerprint.data.model.DataResponseSavePublicKey;
import com.tokopedia.tkpdreactnative.react.fingerprint.utils.FingerprintConstantRegister;

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
    @POST(FingerprintConstantRegister.OTP_FINGERPRINT_ADD)
    Observable<Response<DataResponse<DataResponseSavePublicKey>>> savePublicKey(@FieldMap HashMap<String, String> params);
}
