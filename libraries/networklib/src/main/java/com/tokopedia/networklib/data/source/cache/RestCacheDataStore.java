package com.tokopedia.networklib.data.source.cache;

import com.tokopedia.networklib.data.model.RestCacheStrategy;
import com.tokopedia.networklib.data.model.RestRequest;
import com.tokopedia.networklib.data.model.RestResponseInternal;
import com.tokopedia.networklib.data.source.RestDataStore;
import com.tokopedia.networklib.util.FingerprintManager;
import com.tokopedia.networklib.util.RestCacheManager;
import com.tokopedia.networklib.util.RestClient;

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
    public Observable<RestResponseInternal> getResponse(RestRequest requests, RestCacheStrategy cacheStrategy) {
        try {
            String rowJson = mCacheManager.get(mFingerprintManager.generateFingerPrint(requests.toString(), cacheStrategy.isSessionIncluded()));
            return Observable.just(new RestResponseInternal(rowJson, true));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Observable.just(null);
    }
}

