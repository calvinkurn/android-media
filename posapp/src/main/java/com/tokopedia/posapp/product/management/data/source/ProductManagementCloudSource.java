package com.tokopedia.posapp.product.management.data.source;

import com.google.gson.JsonParser;
import com.tokopedia.posapp.base.domain.model.DataStatus;
import com.tokopedia.posapp.product.common.domain.model.ProductDomain;
import com.tokopedia.posapp.product.management.data.mapper.EditProductMapper;
import com.tokopedia.posapp.product.management.data.mapper.GetProductManagementMapper;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author okasurya on 3/20/18.
 */

public class ProductManagementCloudSource {
    private ProductManagementApi productManagementApi;
    private GetProductManagementMapper getProductListManagementMapper;
    private EditProductMapper editProductMapper;

    @Inject
    public ProductManagementCloudSource(ProductManagementApi productManagementApi,
                                        GetProductManagementMapper getProductListManagementMapper,
                                        EditProductMapper editProductMapper) {
        this.productManagementApi = productManagementApi;
        this.getProductListManagementMapper = getProductListManagementMapper;
        this.editProductMapper = editProductMapper;
    }

    public Observable<List<ProductDomain>> getProductList(RequestParams requestParams) {
        return productManagementApi
                .getProducts(requestParams.getParamsAllValueInString())
                .map(getProductListManagementMapper);
    }

    public Observable<DataStatus> editProduct(String outletId, String request) {
        return productManagementApi.editProduct(outletId, new JsonParser().parse(request).getAsJsonObject()).map(editProductMapper);
    }
}
