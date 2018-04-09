package com.tokopedia.payment.fingerprint.data;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.payment.fingerprint.data.model.DataResponseSavePublicKey;
import com.tokopedia.payment.fingerprint.data.model.ResponsePaymentFingerprint;
import com.tokopedia.payment.fingerprint.data.model.ResponseRegisterFingerprint;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 3/23/18.
 */

public class FingerprintDataSourceCloud {

    public static final int SUCCESS_VALUE = 1;
    public static final String TRANSACTION_ID = "transaction_id";
    private FingerprintApi fingerprintApi;
    private AccountFingerprintApi accountFingerprintApi;

    @Inject
    public FingerprintDataSourceCloud(FingerprintApi fingerprintApi,
                                      AccountFingerprintApi accountFingerprintApi) {
        this.fingerprintApi = fingerprintApi;
        this.accountFingerprintApi = accountFingerprintApi;
    }

    public Observable<Boolean> saveFingerPrint(HashMap<String, String> params) {
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

    public Observable<ResponsePaymentFingerprint> paymentWithFingerPrint(HashMap<String, String> params) {
        return fingerprintApi.paymentWithFingerPrint(params).map(new Func1<Response<ResponsePaymentFingerprint>, ResponsePaymentFingerprint>() {
            @Override
            public ResponsePaymentFingerprint call(Response<ResponsePaymentFingerprint> dataResponseResponse) {
                if (dataResponseResponse.isSuccessful() && dataResponseResponse.body() != null) {
                    return dataResponseResponse.body();
                } else {
                    return null;
                }
            }
        });
    }

    public Observable<Boolean> savePublicKey(HashMap<String, String> params) {
        return accountFingerprintApi.savePublicKey(params).map(new Func1<Response<DataResponse<DataResponseSavePublicKey>>, Boolean>() {
            @Override
            public Boolean call(Response<DataResponse<DataResponseSavePublicKey>> dataResponseResponse) {
                if (dataResponseResponse.isSuccessful() && dataResponseResponse.body() != null) {
                    DataResponseSavePublicKey dataResponseSavePublicKey = dataResponseResponse.body().getData();
                    return dataResponseSavePublicKey.getSuccess() == SUCCESS_VALUE;
                } else {
                    return false;
                }
            }
        });
    }

    public Observable<HashMap<String, String>> getDataPostOtp(String transactionId) {
        return fingerprintApi.getPostDataOtp(generateMapPostData(transactionId))
                .map(new Func1<Response<DataResponse<JSONObject>>, HashMap<String, String>>() {
                    @Override
                    public HashMap<String, String> call(Response<DataResponse<JSONObject>> dataResponseResponse) {
                        if (dataResponseResponse.isSuccessful() && dataResponseResponse.body() != null
                                && dataResponseResponse.body().getData() != null) {
                            try {
                                return jsonToMap(dataResponseResponse.body().getData());
                            } catch (JSONException e) {
                                return null;
                            }
                        } else {
                            return null;
                        }
                    }
                });
    }

    public static HashMap<String, String> jsonToMap(JSONObject json) throws JSONException {
        HashMap<String, String> retMap = new HashMap<String, String>();

        if (json != JSONObject.NULL) {
            retMap = toMap(json);
        }
        return retMap;
    }

    public static HashMap<String, String> toMap(JSONObject object) throws JSONException {
        HashMap<String, String> map = new HashMap<String, String>();

        Iterator<String> keysItr = object.keys();
        while (keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);
            if(value instanceof String){
                map.put(key, (String)value);
            }
        }
        return map;
    }

    private HashMap<String, String> generateMapPostData(String transactionId) {
        HashMap<String, String> maps = new HashMap<>();
        maps.put(TRANSACTION_ID, transactionId);
        return maps;
    }
}
