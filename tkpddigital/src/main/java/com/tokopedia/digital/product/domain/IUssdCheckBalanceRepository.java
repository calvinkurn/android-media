package com.tokopedia.digital.product.domain;

import com.tokopedia.digital.product.data.entity.requestbody.pulsabalance.RequestBodyPulsaBalance;
import com.tokopedia.digital.product.view.model.PulsaBalance;

import rx.Observable;

/**
 * Created by ashwanityagi on 04/07/17.
 */

public interface IUssdCheckBalanceRepository {

    Observable<PulsaBalance> processPulsaBalanceUssdResponse(
            RequestBodyPulsaBalance requestBodyPulsaBalance
    );

}