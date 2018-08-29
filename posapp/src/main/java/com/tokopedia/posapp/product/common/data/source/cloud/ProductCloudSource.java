package com.tokopedia.posapp.product.common.data.source.cloud;

import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.posapp.PosSessionHandler;
import com.tokopedia.posapp.product.productdetail.data.mapper.GetProductDetailMapper;
import com.tokopedia.posapp.product.common.domain.model.ProductDomain;
import com.tokopedia.posapp.product.productlist.data.mapper.GetProductListMapper;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by okasurya on 8/9/17.
 */

public class ProductCloudSource {

    private ProductApi productApi;
    private GetProductDetailMapper getProductDetailMapper;
    private GetProductListMapper getProductListMapper;
    private PosSessionHandler posSessionHandler;

    @Inject
    public ProductCloudSource(ProductApi productApi,
                              GetProductDetailMapper getProductDetailMapper,
                              GetProductListMapper getProductListMapper,
                              PosSessionHandler posSessionHandler) {
        this.productApi = productApi;
        this.getProductDetailMapper = getProductDetailMapper;
        this.getProductListMapper = getProductListMapper;
        this.posSessionHandler = posSessionHandler;
    }

    public Observable<ProductDetailData> getProduct(RequestParams params) {
        return productApi.getProductDetail(posSessionHandler.getOutletId(), params.getParamsAllValueInString()).map(getProductDetailMapper);
    }

    public Observable<List<ProductDomain>> getProductList(String outletId, RequestParams params) {
        return productApi.getProductList(outletId, params.getParamsAllValueInString()).flatMap(getProductListMapper);
    }
}
