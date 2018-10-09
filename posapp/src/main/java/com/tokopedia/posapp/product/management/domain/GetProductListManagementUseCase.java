package com.tokopedia.posapp.product.management.domain;

import com.tokopedia.posapp.product.common.domain.model.ProductDomain;
import com.tokopedia.posapp.product.management.data.repository.ProductManagementCloudRepository;
import com.tokopedia.posapp.product.management.data.repository.ProductManagementRepository;
import com.tokopedia.posapp.product.productlist.domain.model.ProductListDomain;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author okasurya on 3/20/18.
 */

public class GetProductListManagementUseCase extends UseCase<List<ProductDomain>> {
    private ProductManagementRepository productManagementRepository;

    @Inject
    GetProductListManagementUseCase(ProductManagementCloudRepository productManagementRepository) {
        this.productManagementRepository = productManagementRepository;
    }

    @Override
    public Observable<List<ProductDomain>> createObservable(RequestParams requestParams) {
        return productManagementRepository.getList(requestParams);
    }
}
