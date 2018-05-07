package com.tokopedia.tkpdreactnative.react.fingerprint.domain;

import java.util.HashMap;

import rx.Observable;

/**
 * Created by zulfikarrahman on 3/23/18.
 */

public interface FingerprintRepository {
    Observable<Boolean> saveFingerprint(HashMap<String, Object> params);

    Observable<Boolean> savePublicKey(HashMap<String, String> params);
}
