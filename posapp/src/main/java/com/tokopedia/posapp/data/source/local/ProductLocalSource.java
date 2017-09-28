package com.tokopedia.posapp.data.source.local;

import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.tokopedia.posapp.database.manager.ProductDbManager;
import com.tokopedia.posapp.database.model.ProductDb_Table;
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
    private ProductDbManager productDbManager;

    public ProductLocalSource() {
        productDbManager = new ProductDbManager();
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

    public Observable<List<ProductDomain>> getAllProduct() {
        return productDbManager.getAllData();
    }

    public Observable<List<ProductDomain>> searchProduct(String keyword, String etalaseId) {
        if(etalaseId != null && !etalaseId.isEmpty()) {
            return productDbManager.getListData(ConditionGroup.clause()
                    .and(ProductDb_Table.productName.like(keyword))
                    .and(ProductDb_Table.etalaseId.eq(etalaseId)));
        } else {
            return productDbManager.getListData(ConditionGroup.clause()
                    .and(ProductDb_Table.productName.like(keyword)));
        }
    }

    public Observable<ProductDomain> getProduct(int productId) {
        return productDbManager.getData(
                ConditionGroup.clause().and(ProductDb_Table.productId.eq(productId))
        );
    }

    public Observable<List<ProductDomain>> getListProduct(int offset, int limit) {
        return productDbManager.getListData(offset, limit);
    }
}
