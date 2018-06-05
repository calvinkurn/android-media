package com.tokopedia.digital.product.domain.interactor;

import com.tokopedia.digital.product.data.entity.requestbody.pulsabalance.RequestBodyPulsaBalance;
import com.tokopedia.digital.product.view.model.PulsaBalance;

import rx.Subscriber;

/**
 * @author anggaprasetiyo on 4/26/17.
 */

public interface IProductDigitalInteractor {

    void porcessPulsaUssdResponse(RequestBodyPulsaBalance requestBodyPulsaBalance, Subscriber<PulsaBalance> subscriber);

}
