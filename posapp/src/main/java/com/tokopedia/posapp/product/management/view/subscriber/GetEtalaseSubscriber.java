package com.tokopedia.posapp.product.management.view.subscriber;

import com.tokopedia.posapp.product.management.view.ProductManagement;
import com.tokopedia.posapp.shop.domain.model.EtalaseDomain;

import java.util.List;

import rx.Subscriber;

/**
 * @author okasurya on 4/2/18.
 */

public class GetEtalaseSubscriber extends Subscriber<List<EtalaseDomain>> {
    private ProductManagement.View view;

    public GetEtalaseSubscriber(ProductManagement.View view) {
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    @Override
    public void onNext(List<EtalaseDomain> etalaseDomains) {
        view.onGetEtalaseCompleted(etalaseDomains);
    }
}
