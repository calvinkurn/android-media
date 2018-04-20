package com.tokopedia.posapp.product.productdetail.domain.usecase;

import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.posapp.product.common.data.repository.ProductCloudRepository;
import com.tokopedia.posapp.product.common.data.repository.ProductRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by okasurya on 8/9/17.
 */

public class GetProductUseCase extends UseCase<ProductDetailData> {
    private ProductRepository productRepository;

    @Inject
    public GetProductUseCase(ProductCloudRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Observable<ProductDetailData> createObservable(RequestParams requestParams) {
        return productRepository.getProduct(requestParams);
    }
}
