package com.tokopedia.payment.fingerprint.data;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.payment.fingerprint.data.model.DataResponseSavePublicKey;
import com.tokopedia.payment.fingerprint.data.model.ResponsePaymentFingerprint;
import com.tokopedia.payment.fingerprint.data.model.ResponseRegisterFingerprint;

import java.util.HashMap;

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

    public FingerprintDataSourceCloud(FingerprintApi fingerprintApi,
                                      AccountFingerprintApi accountFingerprintApi) {
        this.fingerprintApi = fingerprintApi;
        this.accountFingerprintApi = accountFingerprintApi;
    }

    public Observable<Boolean> saveFingerPrint(HashMap<String, String> params) {
        return fingerprintApi.saveFingerPrint(params).map(new Func1<Response<ResponseRegisterFingerprint>, Boolean>() {
            @Override
            public Boolean call(Response<ResponseRegisterFingerprint> dataResponseResponse) {
                if(dataResponseResponse.isSuccessful() && dataResponseResponse.body() != null){
                    ResponseRegisterFingerprint responseRegisterFingerprint = dataResponseResponse.body();
                    return responseRegisterFingerprint.isSuccess();
                }else{
                    return false;
                }
            }
        });
    }

    public Observable<ResponsePaymentFingerprint> paymentWithFingerPrint(HashMap<String, String> params) {
        return fingerprintApi.paymentWithFingerPrint(params).map(new Func1<Response<ResponsePaymentFingerprint>, ResponsePaymentFingerprint>() {
            @Override
            public ResponsePaymentFingerprint call(Response<ResponsePaymentFingerprint> dataResponseResponse) {
                if(dataResponseResponse.isSuccessful() && dataResponseResponse.body() != null){
                    return dataResponseResponse.body();
                }else{
                    return null;
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
