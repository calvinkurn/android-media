package com.tokopedia.product.manage.item.common.domain;

import com.tokopedia.product.manage.item.main.base.data.model.ProductViewModel;
import com.tokopedia.product.manage.item.main.base.domain.ProductRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

public class GetProductDetailUseCase extends UseCase<ProductViewModel> {

    public static final String PARAM_PRODUCT_ID = "prd_id";

    private final ProductRepository productRepository;

    @Inject
    public GetProductDetailUseCase(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    @Override
    public Observable<ProductViewModel> createObservable(RequestParams requestParams) {
        return productRepository.getProductDetail(requestParams.getString(PARAM_PRODUCT_ID, ""));
    }

    public static RequestParams createParams(String productId){
        RequestParams params = RequestParams.create();
        params.putString(PARAM_PRODUCT_ID, productId);
        return params;
    }
}
