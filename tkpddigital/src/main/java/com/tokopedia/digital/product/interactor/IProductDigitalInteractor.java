package com.tokopedia.digital.product.interactor;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.digital.product.data.entity.requestbody.pulsabalance.RequestBodyPulsaBalance;
import com.tokopedia.digital.product.model.ProductDigitalData;
import com.tokopedia.digital.product.model.PulsaBalance;

import rx.Subscriber;

/**
 * @author anggaprasetiyo on 4/26/17.
 */

public interface IProductDigitalInteractor {

    void getCategoryAndBanner(
            String pathCategoryId,
            TKPDMapParam<String, String> paramQueryCategory,
            TKPDMapParam<String, String> paramQueryBanner,
            TKPDMapParam<String, String> paramQueryLastNumber,
            TKPDMapParam<String, String> paramQueryLastOrder,
            Subscriber<ProductDigitalData> subscriber
    );

    void porcessPulsaUssdResponse(RequestBodyPulsaBalance requestBodyPulsaBalance, Subscriber<PulsaBalance> subscriber);
}
