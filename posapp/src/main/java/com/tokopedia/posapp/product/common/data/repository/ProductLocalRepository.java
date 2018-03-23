package com.tokopedia.posapp.product.common.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.posapp.base.domain.model.DataStatus;
import com.tokopedia.posapp.product.common.data.source.local.ProductLocalSource;
import com.tokopedia.posapp.product.common.domain.model.ProductDomain;
import com.tokopedia.posapp.product.productlist.domain.model.ProductListDomain;

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
        return productLocalSource.getProduct(requestParams.getInt(PRODUCT_ID, 0));
    }

    @Override
    public Observable<ProductListDomain> getProductList(RequestParams requestParams) {
        if(requestParams.getParameters().containsKey(KEYWORD) && requestParams.getParameters().containsKey(ETALASE_ID)) {
            return productLocalSource
                    .searchProduct(
                            requestParams.getString(KEYWORD, ""),
                            requestParams.getString(ETALASE_ID, "")
                    ).map(mapProductListDomain());
        } else if(requestParams.getParameters().containsKey(OFFSET) && requestParams.getParameters().containsKey(LIMIT)) {
            return productLocalSource
                    .getListProduct(
                            requestParams.getInt(OFFSET, 0),
                            requestParams.getInt(LIMIT, 0)
                    )
                    .map(mapProductListDomain());
        } else {
            return productLocalSource.getAllProduct().map(mapProductListDomain());
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
