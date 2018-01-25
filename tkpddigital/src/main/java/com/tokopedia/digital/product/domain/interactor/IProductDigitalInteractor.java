package com.tokopedia.digital.product.domain.interactor;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.digital.common.data.entity.requestbody.pulsabalance.RequestBodyPulsaBalance;
import com.tokopedia.digital.product.view.model.ProductDigitalData;
import com.tokopedia.digital.product.view.model.PulsaBalance;

import rx.Subscriber;

/**
 * @author anggaprasetiyo on 4/26/17.
 */

public interface IProductDigitalInteractor {

    void porcessPulsaUssdResponse(RequestBodyPulsaBalance requestBodyPulsaBalance, Subscriber<PulsaBalance> subscriber);

}
