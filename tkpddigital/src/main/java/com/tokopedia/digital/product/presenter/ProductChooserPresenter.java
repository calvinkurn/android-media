package com.tokopedia.digital.product.presenter;

import com.tokopedia.digital.product.listener.IProductChooserView;
import com.tokopedia.digital.widget.interactor.IDigitalWidgetInteractor;
import com.tokopedia.digital.widget.model.product.Product;

import java.util.List;

import rx.Subscriber;

/**
 * Created by Rizky on 12/21/17.
 */

public class ProductChooserPresenter implements IProductChooserPresenter {

    private IDigitalWidgetInteractor digitalWidgetInteractor;
    private IProductChooserView view;

    public ProductChooserPresenter(IProductChooserView view, IDigitalWidgetInteractor digitalWidgetInteractor) {
        this.view = view;
        this.digitalWidgetInteractor = digitalWidgetInteractor;
    }

    @Override
    public void getProductsByCategoryIdAndOperatorId(String categoryId, String operatorId) {
        view.showInitialProgressLoading();
        digitalWidgetInteractor.getProductsByOperatorId(new Subscriber<List<Product>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<Product> products) {
                view.hideInitialProgressLoading();
                if (!products.isEmpty()) {
                    view.showProducts(products);
                }
            }
        }, Integer.valueOf(categoryId), operatorId);
    }
}
