package com.tokopedia.posapp.product.management.domain;

import com.tokopedia.posapp.base.domain.model.DataStatus;
import com.tokopedia.posapp.product.management.data.repository.ProductManagementCloudRepository;
import com.tokopedia.posapp.product.management.data.repository.ProductManagementRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author okasurya on 3/20/18.
 */

public class EditProductUseCase extends UseCase<DataStatus> {
    private ProductManagementRepository productManagementRepository;

    @Inject
    EditProductUseCase(ProductManagementCloudRepository productManagementRepository) {
        this.productManagementRepository = productManagementRepository;
    }

    @Override
    public Observable<DataStatus> createObservable(RequestParams requestParams) {
        return productManagementRepository.edit(requestParams);
    }
}
