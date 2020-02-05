package com.tokopedia.digital.product.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.common_digital.product.presentation.model.Product;
import com.tokopedia.digital.product.domain.interactor.GetProductsByOperatorIdUseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by Rizky on 12/21/17.
 */

public class ProductChooserPresenter extends BaseDaggerPresenter<ProductChooserContract.View>
        implements ProductChooserContract.Presenter {

    private GetProductsByOperatorIdUseCase getProductsByOperatorId;

    @Inject
    public ProductChooserPresenter(GetProductsByOperatorIdUseCase getProductsByOperatorId) {
        this.getProductsByOperatorId = getProductsByOperatorId;
    }

    @Override
    public void getProductsByCategoryIdAndOperatorId(String categoryId, String operatorId) {
        getView().showInitialProgressLoading();

        getProductsByOperatorId.execute(
                getProductsByOperatorId.createRequestParam(categoryId, operatorId),
                new Subscriber<List<Product>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<Product> products) {
                        getView().hideInitialProgressLoading();
                        if (!products.isEmpty()) {
                            getView().showProducts(products);
                        }
                    }
                }
        );
    }
}
