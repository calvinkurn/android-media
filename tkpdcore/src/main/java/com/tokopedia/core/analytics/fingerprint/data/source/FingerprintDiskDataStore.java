package com.tokopedia.core.analytics.fingerprint.data.source;

import com.tokopedia.core.analytics.fingerprint.data.FingerPrintEndPoint;
import com.tokopedia.core.analytics.fingerprint.domain.model.FingerPrint;

import rx.Observable;

/**
 * Created by Herdi_WORK on 20.06.17.
 */

public class FingerPrintCloudEndpoint implements FingerPrintEndPoint{

    @Override
    public Observable<String> sendFingerPrint(FingerPrint data) {
        return null;
    }
}
