package com.tokopedia.digital.product.view.presenter;

import com.tokopedia.digital.product.domain.interactor.GetProductsByOperatorIdUseCase;
import com.tokopedia.digital.product.view.listener.IProductChooserView;
import com.tokopedia.digital.product.view.model.Product;

import java.util.List;

import rx.Subscriber;

/**
 * Created by Rizky on 12/21/17.
 */

public class ProductChooserPresenter implements IProductChooserPresenter {

    private IProductChooserView view;
    private GetProductsByOperatorIdUseCase getProductsByOperatorId;

    public ProductChooserPresenter(IProductChooserView view,
                                   GetProductsByOperatorIdUseCase getProductsByOperatorId) {
        this.view = view;
        this.getProductsByOperatorId = getProductsByOperatorId;
    }

    @Override
    public void getProductsByCategoryIdAndOperatorId(String categoryId, String operatorId) {
        view.showInitialProgressLoading();

        getProductsByOperatorId.execute(
                getProductsByOperatorId.createRequestParam(categoryId, operatorId),
                new Subscriber<List<com.tokopedia.digital.product.view.model.Product>>() {
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
                }
        );
    }
}
