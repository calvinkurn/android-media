package com.tokopedia.common.network.data.source.cache;

import com.tokopedia.common.network.data.model.RestRequest;
import com.tokopedia.common.network.data.model.RestResponseIntermediate;
import com.tokopedia.common.network.data.source.RestDataStore;
import com.tokopedia.common.network.util.CommonUtil;
import com.tokopedia.common.network.util.FingerprintManager;
import com.tokopedia.common.network.util.RestCacheManager;
import com.tokopedia.common.network.util.RestClient;

import javax.inject.Inject;

import rx.Observable;

/**
 * Retrieve the response from cache only
 */
public class RestCacheDataStore implements RestDataStore {

    private RestCacheManager mCacheManager;
    private FingerprintManager mFingerprintManager;

    @Inject
    public RestCacheDataStore() {
        this.mCacheManager = new RestCacheManager();
        this.mFingerprintManager = RestClient.getFingerPrintManager();
    }

    @Override
    public Observable<RestResponseIntermediate> getResponse(RestRequest request) {
        try {
            String rowJson = mCacheManager.get(mFingerprintManager.generateFingerPrint(request.toString(), request.getCacheStrategy().isSessionIncluded()));
            RestResponseIntermediate returnResponse = new RestResponseIntermediate(CommonUtil.fromJson(rowJson, request.getTypeOfT()), request.getTypeOfT(), true);
            returnResponse.setCode(1);
            returnResponse.setError(false);
            return Observable.just(returnResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Observable.just(null);
    }
}

