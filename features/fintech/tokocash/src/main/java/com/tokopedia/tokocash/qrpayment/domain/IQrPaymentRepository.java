package com.tokopedia.tokocash.qrpayment.domain;

import com.tokopedia.tokocash.qrpayment.presentation.model.InfoQrTokoCash;
import com.tokopedia.tokocash.qrpayment.presentation.model.QrPaymentTokoCash;

import java.util.HashMap;

import rx.Observable;

/**
 * Created by nabillasabbaha on 1/2/18.
 */

public interface IQrPaymentRepository {

    Observable<InfoQrTokoCash> getInfoQrTokoCash(HashMap<String, Object> mapParams);

    Observable<QrPaymentTokoCash> postQrPayment(HashMap<String, Object> mapParams);
}
