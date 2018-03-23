package com.tokopedia.payment.fingerprint.data;

import com.tokopedia.payment.fingerprint.domain.FingerprintRepository;

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
    public Observable<Boolean> saveFingerprint(HashMap<String, String> params) {
        return fingerprintDataSourceCloud.saveFingerPrint(params);
    }
}
