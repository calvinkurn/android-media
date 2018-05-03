package com.tokopedia.tkpdreactnative.react.fingerprint.data;


import com.tokopedia.tkpdreactnative.react.fingerprint.domain.FingerprintRepository;

import java.util.HashMap;

import rx.Observable;

/**
 * Created by zulfikarrahman on 3/23/18.
 */

public class FingerprintRepositoryImpl implements FingerprintRepository {

    private FingerprintDataSourceCloud fingerprintDataSourceCloud;

    public FingerprintRepositoryImpl(FingerprintDataSourceCloud fingerprintDataSourceCloud) {
        this.fingerprintDataSourceCloud = fingerprintDataSourceCloud;
    }

    @Override
    public Observable<Boolean> saveFingerprint(HashMap<String, Object> params) {
        return fingerprintDataSourceCloud.saveFingerPrint(params);
    }

    @Override
    public Observable<Boolean> savePublicKey(HashMap<String, String> params) {
        return fingerprintDataSourceCloud.savePublicKey(params);
    }
}
