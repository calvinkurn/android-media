package com.tokopedia.posapp.product.management.view.subscriber;

import com.tokopedia.posapp.base.domain.model.DataStatus;
import com.tokopedia.posapp.product.management.view.ProductManagement;

import rx.Subscriber;

/**
 * @author okasurya on 4/13/18.
 */

public class EditProductStatusSubscriber extends Subscriber<DataStatus> {
    private ProductManagement.View view;

    public EditProductStatusSubscriber(ProductManagement.View view) {
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        view.hideLoadingDialog();
    }

    @Override
    public void onNext(DataStatus dataStatus) {
        view.hideLoadingDialog();
        view.onSuccessEditStatus();
    }
}
