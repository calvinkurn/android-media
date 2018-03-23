package com.tokopedia.payment.fingerprint.data;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;

import java.util.HashMap;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 3/23/18.
 */

public class FingerprintDataSourceCloud {

    private FingerprintApi fingerprintApi;

    public FingerprintDataSourceCloud(FingerprintApi fingerprintApi) {
        this.fingerprintApi = fingerprintApi;
    }

    public Observable<Boolean> saveFingerPrint(HashMap<String, String> params) {
        return fingerprintApi.saveFingerPrint(params).map(new Func1<Response<DataResponse<Boolean>>, Boolean>() {
            @Override
            public Boolean call(Response<DataResponse<Boolean>> dataResponseResponse) {
                return null;
            }
        });
    }
}
