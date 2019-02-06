package com.tokopedia.core.analytics.fingerprint.data;

import android.content.Context;

import com.tokopedia.core.analytics.fingerprint.domain.FingerprintRepository;

import rx.Observable;

/**
 * Created by Herdi_WORK on 20.06.17.
 */

public class FingerprintDataRepository implements FingerprintRepository {

    private final FingerprintDataStoreFactory fingerprintDataStoreFactory;

    public FingerprintDataRepository(Context context){
        fingerprintDataStoreFactory = new FingerprintDataStoreFactory(context);
    }

    @Override
    public Observable<String> getFingerPrint() {
        return fingerprintDataStoreFactory
                .createDiskFingerprintDataStore()
                .getFingerprint();
    }
}
