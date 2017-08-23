package com.tokopedia.core.analytics.fingerprint.data;

import com.tokopedia.core.analytics.fingerprint.domain.model.FingerPrint;

import rx.Observable;

/**
 * Created by Herdi_WORK on 20.06.17.
 */

public interface FingerprintDataStore {

    Observable<String> getFingerprint();

}
