package com.tokopedia.common.network.data.source.cache;

import com.tokopedia.common.network.data.model.RestRequest;
import com.tokopedia.common.network.data.model.RestResponseIntermediate;
import com.tokopedia.common.network.data.source.RestDataStore;
import com.tokopedia.common.network.util.CommonUtil;
import com.tokopedia.common.network.util.FingerprintManager;
import com.tokopedia.common.network.util.RestCacheManager;
import com.tokopedia.common.network.util.NetworkClient;
import com.tokopedia.common.network.util.RestConstant;

import javax.inject.Inject;

import rx.Observable;

import static com.tokopedia.common.network.util.RestConstant.RES_CODE_CACHE;

/**
 * Retrieve the response from cache only
 */
public class RestCacheDataStore implements RestDataStore {

    private RestCacheManager mCacheManager;
    private FingerprintManager mFingerprintManager;

    @Inject
    public RestCacheDataStore() {
        this.mCacheManager = new RestCacheManager();
        this.mFingerprintManager = NetworkClient.getFingerPrintManager();
    }

    @Override
    public Observable<RestResponseIntermediate> getResponse(RestRequest request) {
        RestResponseIntermediate returnResponse;
        try {
            String rawJson = mCacheManager.get(mFingerprintManager.generateFingerPrint(request.toString(), request.getCacheStrategy().isSessionIncluded()));

            if (rawJson == null || rawJson.isEmpty()) {
                return Observable.just(null);
            }

            returnResponse = new RestResponseIntermediate(CommonUtil.fromJson(rawJson, request.getTypeOfT()), request.getTypeOfT(), true);
            returnResponse.setCode(RES_CODE_CACHE);
            returnResponse.setError(false);
            return Observable.just(returnResponse);
        } catch (Exception e) {
            //For any kind of error body always be null,
            //E.g. JSONException while serializing json to POJO.
            returnResponse = new RestResponseIntermediate(null, request.getTypeOfT(), true);
            returnResponse.setCode(RestConstant.INTERNAL_EXCEPTION);
            returnResponse.setErrorBody("Caught Exception please fix it--> Responsible class : " + e.getClass().toString() + " Detailed Message: " + e.getMessage() + ", Cause by: " + e.getCause());
            returnResponse.setError(true);
        }

        return Observable.just(returnResponse);
    }
}

