package com.tokopedia.posapp.product.management.view.subscriber;

import com.tokopedia.posapp.base.domain.model.DataStatus;
import com.tokopedia.posapp.product.common.ProductConstant;
import com.tokopedia.posapp.product.management.view.ProductManagement;
import com.tokopedia.posapp.product.management.view.viewmodel.ProductViewModel;

import rx.Subscriber;

/**
 * @author okasurya on 4/13/18.
 */

public class EditProductStatusSubscriber extends Subscriber<DataStatus> {
    private ProductManagement.View view;
    private int position;
    private ProductViewModel productViewModel;

    public EditProductStatusSubscriber(ProductManagement.View view, int position, ProductViewModel productViewModel) {
        this.view = view;
        this.position = position;
        this.productViewModel = productViewModel;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        view.onErorEditStatus(position);
    }

    @Override
    public void onNext(DataStatus dataStatus) {
        view.onSuccessEditStatus(position, productViewModel);
    }
}
