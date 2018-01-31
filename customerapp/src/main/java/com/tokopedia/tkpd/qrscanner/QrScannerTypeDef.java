package com.tokopedia.tkpd.qrscanner;

import android.support.annotation.StringDef;

import static com.tokopedia.tkpd.qrscanner.QrScannerTypeDef.CAMPAIGN_QR_CODE;
import static com.tokopedia.tkpd.qrscanner.QrScannerTypeDef.PAYMENT_QR_CODE;

/**
 * Created by nabillasabbaha on 1/31/18.
 */

@StringDef({CAMPAIGN_QR_CODE, PAYMENT_QR_CODE})
public @interface QrScannerTypeDef {
    String CAMPAIGN_QR_CODE = "tc";
    String PAYMENT_QR_CODE = "w";
}
