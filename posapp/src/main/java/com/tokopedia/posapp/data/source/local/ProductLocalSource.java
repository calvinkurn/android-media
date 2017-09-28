package com.tokopedia.posapp.data.source.local;

import com.tokopedia.posapp.database.manager.ProductDbManager2;
import com.tokopedia.posapp.domain.model.DataStatus;
import com.tokopedia.posapp.domain.model.product.ProductDomain;
import com.tokopedia.posapp.domain.model.shop.ProductListDomain;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by okasurya on 9/16/17.
 */

public class ProductLocalSource {
    private ProductDbManager2 productDbManager;

    public ProductLocalSource() {
        productDbManager = new ProductDbManager2();
    }

    public Observable<DataStatus> storeProduct(ProductListDomain productListDomain) {
        return Observable.just(productListDomain)
                .flatMap(new Func1<ProductListDomain, Observable<DataStatus>>() {
                    @Override
                    public Observable<DataStatus> call(ProductListDomain productListDomain) {
                        return productDbManager.store(mapToDomainList(productListDomain.getProductList()));
                    }
                });
    }

    private List<ProductDomain> mapToDomainList(List<com.tokopedia.core.shopinfo.models.productmodel.List> productList) {
        List<ProductDomain> domainList = new ArrayList<>();
        for (com.tokopedia.core.shopinfo.models.productmodel.List item : productList) {
            ProductDomain domain = new ProductDomain();
            domain.setProductId(item.productId);
            domain.setProductName(item.productName);
            domain.setProductPrice(item.productPrice);
            domain.setProductUrl(item.productUrl);
            domain.setProductImage(item.productImage);
            domain.setProductImage300(item.productImage300);
            domain.setProductImageFull(item.productImageFull);
            domainList.add(domain);
        }
        return domainList;
    }
}
