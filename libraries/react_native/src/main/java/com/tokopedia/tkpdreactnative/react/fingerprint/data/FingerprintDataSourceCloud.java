package com.tokopedia.tkpdreactnative.react.fingerprint.data;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.tkpdreactnative.react.fingerprint.data.model.DataResponseSavePublicKey;
import com.tokopedia.tkpdreactnative.react.fingerprint.data.model.ResponseRegisterFingerprint;

import java.util.HashMap;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 3/23/18.
 */

public class FingerprintDataSourceCloud {

    public static final int SUCCESS_VALUE = 1;
    private FingerprintApi fingerprintApi;
    private AccountFingerprintApi accountFingerprintApi;

    @Inject
    public FingerprintDataSourceCloud(FingerprintApi fingerprintApi,
                                      AccountFingerprintApi accountFingerprintApi) {
        this.fingerprintApi = fingerprintApi;
        this.accountFingerprintApi = accountFingerprintApi;
    }

    public Observable<Boolean> saveFingerPrint(HashMap<String, Object> params) {
        return fingerprintApi.saveFingerPrint(params).map(new Func1<Response<ResponseRegisterFingerprint>, Boolean>() {
            @Override
            public Boolean call(Response<ResponseRegisterFingerprint> dataResponseResponse) {
                if (dataResponseResponse.isSuccessful() && dataResponseResponse.body() != null) {
                    ResponseRegisterFingerprint responseRegisterFingerprint = dataResponseResponse.body();
                    return responseRegisterFingerprint.isSuccess();
                } else {
                    return false;
                }
            }
        });
    }

    public Observable<Boolean> savePublicKey(HashMap<String, String> params) {
        return accountFingerprintApi.savePublicKey(params).map(new Func1<Response<DataResponse<DataResponseSavePublicKey>>, Boolean>() {
            @Override
            public Boolean call(Response<DataResponse<DataResponseSavePublicKey>> dataResponseResponse) {
                if(dataResponseResponse.isSuccessful() && dataResponseResponse.body() != null){
                    DataResponseSavePublicKey dataResponseSavePublicKey = dataResponseResponse.body().getData();
                    return dataResponseSavePublicKey.getSuccess() == SUCCESS_VALUE;
                }else{
                    return false;
                }
            }
        });
    }
}
