package com.tokopedia.posapp.product.management.view.subscriber;

import com.tokopedia.posapp.base.domain.model.DataStatus;
import com.tokopedia.posapp.product.management.ProductManagementConstant;
import com.tokopedia.posapp.product.management.view.EditProduct;

import rx.Subscriber;

/**
 * @author okasurya on 4/12/18.
 */

public class EditProductSubscriber extends Subscriber<DataStatus> {
    private EditProduct.View view;

    public EditProductSubscriber(EditProduct.View view) {
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        view.hideLoading();
        view.onErrorSave(ProductManagementConstant.Message.DEFAULT_ERROR_MESSAGE);
    }

    @Override
    public void onNext(DataStatus dataStatus) {
        view.hideLoading();
        if(dataStatus.isOk()) view.onSuccessSave();
        else view.onErrorSave(dataStatus.getMessage());
    }
}
