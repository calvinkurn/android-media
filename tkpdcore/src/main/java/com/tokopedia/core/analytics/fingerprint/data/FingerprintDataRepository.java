package com.tokopedia.core.analytics.fingerprint.data;

import com.tokopedia.core.analytics.fingerprint.domain.FingerprintRepository;
import com.tokopedia.core.analytics.fingerprint.domain.model.FingerPrint;
import com.tokopedia.core.app.MainApplication;

import rx.Observable;

/**
 * Created by Herdi_WORK on 20.06.17.
 */

public class FingerprintDataRepository implements FingerprintRepository {

    private final FingerprintDataStoreFactory fingerprintDataStoreFactory;

    public FingerprintDataRepository(){
        fingerprintDataStoreFactory = new FingerprintDataStoreFactory(MainApplication.getAppContext());
    }

    @Override
    public Observable<String> getFingerPrint() {
        return fingerprintDataStoreFactory
                .createDiskFingerprintDataStore()
                .getFingerprint();
    }
}
