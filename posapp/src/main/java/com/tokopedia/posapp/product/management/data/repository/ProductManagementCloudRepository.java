package com.tokopedia.posapp.product.management.data.repository;

import com.google.gson.Gson;
import com.tokopedia.posapp.base.domain.model.DataStatus;
import com.tokopedia.posapp.product.common.ProductConstant;
import com.tokopedia.posapp.product.common.domain.model.ProductDomain;
import com.tokopedia.posapp.product.management.data.pojo.request.EditProductRequest;
import com.tokopedia.posapp.product.management.data.source.ProductManagementCloudSource;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author okasurya on 3/20/18.
 */

public class ProductManagementCloudRepository implements ProductManagementRepository {
    private ProductManagementCloudSource productManagementCloudSource;
    private Gson gson;

    @Inject
    public ProductManagementCloudRepository(ProductManagementCloudSource productManagementCloudSource,
                                            Gson gson) {
        this.productManagementCloudSource = productManagementCloudSource;
        this.gson = gson;
    }

    @Override
    public Observable<List<ProductDomain>> getList(RequestParams requestParams) {
        return productManagementCloudSource.getProductList(requestParams);
    }

    @Override
    public Observable<ProductDomain> get(RequestParams requestParams) {
        return null;
    }

    @Override
    public Observable<DataStatus> edit(RequestParams requestParams) {
        EditProductRequest editProductRequest = (EditProductRequest) requestParams.getObject(ProductConstant.Key.EDIT_PRODUCT_REQUEST);
        return productManagementCloudSource.editProduct(
                requestParams.getString(ProductConstant.Key.OUTLET_ID, ""),
                gson.toJson(editProductRequest)
        );
    }
}
