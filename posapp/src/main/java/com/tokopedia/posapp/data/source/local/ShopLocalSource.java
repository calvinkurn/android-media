package com.tokopedia.posapp.data.source.local;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.database.model.ProductDB;
import com.tokopedia.core.shopinfo.models.productmodel.ProductModel;
import com.tokopedia.posapp.data.mapper.StoreProductMapper;
import com.tokopedia.posapp.database.ProductSavedResult;
import com.tokopedia.posapp.database.manager.DbManagerOperation;
import com.tokopedia.posapp.database.manager.ProductDbManager;
import com.tokopedia.posapp.domain.model.shop.ShopProductListDomain;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by okasurya on 8/29/17.
 */

public class ShopLocalSource {
    private ProductDbManager productDbManager;
    private StoreProductMapper storeProductMapper;

    public ShopLocalSource() {
        productDbManager = new ProductDbManager();
        storeProductMapper = new StoreProductMapper();
    }

    public Observable<ProductModel> getProductList(RequestParams params) {
        return null;
    }

    public Observable<ProductSavedResult> storeProduct(ShopProductListDomain productListDomain) {
        return Observable.just(productListDomain)
                .flatMap(new Func1<ShopProductListDomain, Observable<ProductSavedResult>>() {
                    @Override
                    public Observable<ProductSavedResult> call(final ShopProductListDomain productListDomain) {
                        return Observable.create(new Observable.OnSubscribe<ProductSavedResult>() {
                            @Override
                            public void call(final Subscriber<? super ProductSavedResult> subscriber) {
                                List<ProductDB> data = new ArrayList<>();
                                for (com.tokopedia.core.shopinfo.models.productmodel.List item : productListDomain.getProductList()) {
                                    ProductDB productDB = new ProductDB();
                                    productDB.setProductId(item.productId);
                                    productDB.setNameProd(item.productName);
                                    productDB.setPriceFormatted(item.productPrice);
                                    productDB.setProductUrl(item.productUrl);
                                    data.add(productDB);
                                }

                                productDbManager.store(data, new DbManagerOperation.TransactionListener() {
                                    @Override
                                    public void onTransactionSuccess() {
                                        ProductSavedResult productSavedResult = new ProductSavedResult();
                                        productSavedResult.setStatus(true);
                                        productSavedResult.setNextUri(productListDomain.getNextUri());
                                        subscriber.onNext(productSavedResult);
                                    }

                                    @Override
                                    public void onError(Throwable throwable) {
                                        ProductSavedResult productSavedResult = new ProductSavedResult();
                                        productSavedResult.setStatus(false);
                                        productSavedResult.setNextUri(null);
                                        subscriber.onNext(productSavedResult);
                                    }
                                });
                            }
                        });
                    }
                });
    }
}
