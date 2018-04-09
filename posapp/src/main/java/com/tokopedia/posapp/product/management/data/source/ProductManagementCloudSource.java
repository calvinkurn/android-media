package com.tokopedia.posapp.product.management.data.source;

import com.tokopedia.posapp.base.domain.model.DataStatus;
import com.tokopedia.posapp.product.common.domain.model.ProductDomain;
import com.tokopedia.posapp.product.management.data.mapper.EditProductMapper;
import com.tokopedia.posapp.product.productlist.data.mapper.GetProductListMapper;
import com.tokopedia.posapp.product.productlist.domain.model.ProductListDomain;
import com.tokopedia.usecase.RequestParams;

import org.json.JSONObject;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;

/**
 * @author okasurya on 3/20/18.
 */

public class ProductManagementCloudSource {
    private ProductManagementApi productManagementApi;
    private GetProductListMapper getProductListManagementMapper;
    private EditProductMapper editProductMapper;

    @Inject
    public ProductManagementCloudSource(ProductManagementApi productManagementApi,
                                        GetProductListMapper getProductListManagementMapper,
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

    public Observable<DataStatus> editProduct(String outletId, String jsonObject) {
        return productManagementApi.editProduct(outletId, jsonObject)
                .map(editProductMapper);
    }
}
