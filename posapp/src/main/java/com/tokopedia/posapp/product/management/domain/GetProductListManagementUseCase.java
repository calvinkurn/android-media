package com.tokopedia.posapp.product.management.domain;

import com.tokopedia.posapp.product.management.data.repository.ProductManagementCloudRepository;
import com.tokopedia.posapp.product.management.data.repository.ProductManagementRepository;
import com.tokopedia.posapp.product.productlist.domain.model.ProductListDomain;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author okasurya on 3/20/18.
 */

public class GetProductListManagementUseCase extends UseCase<ProductListDomain> {
    private ProductManagementRepository productManagementRepository;

    @Inject
    GetProductListManagementUseCase(ProductManagementCloudRepository productManagementRepository) {
        this.productManagementRepository = productManagementRepository;
    }

    @Override
    public Observable<ProductListDomain> createObservable(RequestParams requestParams) {
        return productManagementRepository.getList(requestParams);
    }
}
