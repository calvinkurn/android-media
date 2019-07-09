package com.tokopedia.product.manage.item.main.base.domain;

import android.text.TextUtils;

import com.tokopedia.product.manage.item.main.base.data.model.ProductViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

public class SubmitRawProductUseCase extends UseCase<Integer> {

    private static final String PRODUCT_VIEW_MODEL = "PRODUCT_VIEW_MODEL";

    private final ProductRepository productRepository;

    @Inject
    public SubmitRawProductUseCase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Observable<Integer> createObservable(RequestParams requestParams) {
        ProductViewModel productViewModel = (ProductViewModel) requestParams.getObject(PRODUCT_VIEW_MODEL);
        if (TextUtils.isEmpty(productViewModel.getProductId())) {
            productViewModel.resetPictureId();
            productViewModel.setProductPosition(null);
            productViewModel.setProductLastUpdatePrice(null);
            return productRepository.addProductSubmit(productViewModel);
        } else {
            return productRepository.editProductSubmit(productViewModel);
        }
    }

    public static RequestParams createParams(ProductViewModel productViewModel) {
        RequestParams params = RequestParams.create();
        params.putObject(PRODUCT_VIEW_MODEL, productViewModel);
        return params;
    }
}
