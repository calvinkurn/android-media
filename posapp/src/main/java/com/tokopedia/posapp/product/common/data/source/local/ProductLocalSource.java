package com.tokopedia.posapp.product.common.data.source.local;

import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.tokopedia.posapp.database.model.ProductDb_Table;
import com.tokopedia.posapp.base.domain.model.DataStatus;
import com.tokopedia.posapp.product.common.domain.model.ProductDomain;
import com.tokopedia.posapp.product.productlist.data.pojo.ProductList;
import com.tokopedia.posapp.product.productlist.domain.model.ProductListDomain;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by okasurya on 9/16/17.
 */

public class ProductLocalSource {
    private ProductDbManager productDbManager;

    @Inject
    public ProductLocalSource() {
        productDbManager = new ProductDbManager();
    }

    public Observable<DataStatus> storeProduct(ProductListDomain productListDomain) {
        return Observable.just(productListDomain)
                .flatMap(new Func1<ProductListDomain, Observable<DataStatus>>() {
                    @Override
                    public Observable<DataStatus> call(ProductListDomain productListDomain) {
                        return productDbManager.store(productListDomain.getProductDomains());
                    }
                });
    }

    public Observable<List<ProductDomain>> getAllProduct() {
        return productDbManager.getAllData();
    }

    public Observable<List<ProductDomain>> searchProduct(String keyword, String etalaseId) {
        if(!isEmpty(keyword)) {
            if(!isEmpty(etalaseId)) {
                return productDbManager.getListData(ConditionGroup.clause()
                        .and(ProductDb_Table.productName.like(getKeyword(keyword)))
                        .and(ProductDb_Table.etalaseId.eq(etalaseId)));
            } else {
                return productDbManager.getListData(ConditionGroup.clause()
                        .and(ProductDb_Table.productName.like(getKeyword(keyword))));
            }
        } else {
            if(!isEmpty(etalaseId)) {
                return productDbManager.getListData(ConditionGroup.clause()
                        .and(ProductDb_Table.etalaseId.eq(etalaseId)));
            } else {
                return getAllProduct();
            }
        }
    }

    private String getKeyword(String keyword) {
        return "%%" + keyword + "%%";
    }

    private boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
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
