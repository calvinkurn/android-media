package com.tokopedia.tkpdreactnative.react.fingerprint.data;

import com.tokopedia.tkpdreactnative.react.fingerprint.data.model.ResponseRegisterFingerprint;
import com.tokopedia.tkpdreactnative.react.fingerprint.utils.FingerprintConstantRegister;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by zulfikarrahman on 3/23/18.
 */

public interface FingerprintApi {

    @FormUrlEncoded
    @POST(FingerprintConstantRegister.V2_FINGERPRINT_PUBLICKEY_SAVE)
    Observable<Response<ResponseRegisterFingerprint>> saveFingerPrint(@FieldMap Map<String, Object> params);
}
