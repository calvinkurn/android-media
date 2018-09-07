package com.tokopedia.posapp.product.common.data.repository;

import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.posapp.base.domain.model.DataStatus;
import com.tokopedia.posapp.product.common.ProductConstant;
import com.tokopedia.posapp.product.common.data.source.local.ProductLocalSource;
import com.tokopedia.posapp.product.common.domain.model.ProductDomain;
import com.tokopedia.posapp.product.productlist.domain.model.ProductListDomain;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author okasurya on 3/22/18.
 */

public class ProductLocalRepository implements ProductRepository {
    private ProductLocalSource productLocalSource;

    @Inject
    public ProductLocalRepository(ProductLocalSource productLocalSource) {
        this.productLocalSource = productLocalSource;
    }

    @Override
    public Observable<ProductDetailData> getProduct(RequestParams requestParams) {
        return null;
    }

    @Override
    public Observable<ProductDomain> getProductDomain(RequestParams requestParams) {
        return productLocalSource.getProduct(requestParams.getInt(ProductConstant.Key.PRODUCT_ID, 0));
    }

    @Override
    public Observable<List<ProductDomain>> getProductList(RequestParams requestParams) {
        if(requestParams.getParameters().containsKey(ProductConstant.Key.KEYWORD) && requestParams.getParameters().containsKey(ProductConstant.Key.ETALASE_ID)) {
            return productLocalSource
                    .searchProduct(
                            requestParams.getString(ProductConstant.Key.KEYWORD, ""),
                            requestParams.getString(ProductConstant.Key.ETALASE_ID, "")
                    );
        } else if(requestParams.getParameters().containsKey(ProductConstant.Key.OFFSET) && requestParams.getParameters().containsKey(ProductConstant.Key.LIMIT)) {
            return productLocalSource
                    .getListProduct(
                            requestParams.getInt(ProductConstant.Key.OFFSET, 0),
                            requestParams.getInt(ProductConstant.Key.LIMIT, 0)
                    );
        } else {
            return productLocalSource.getAllProduct();
        }
    }

    @Override
    public Observable<DataStatus> store(ProductListDomain productModel) {
        return productLocalSource.storeProduct(productModel);
    }

    private Func1<List<ProductDomain>, ProductListDomain> mapProductListDomain() {
        return new Func1<List<ProductDomain>, ProductListDomain>() {
            @Override
            public ProductListDomain call(List<ProductDomain> productDomains) {
                ProductListDomain list = new ProductListDomain();
                list.setProductDomains(productDomains);
                return list;
            }
        };
    }
}
