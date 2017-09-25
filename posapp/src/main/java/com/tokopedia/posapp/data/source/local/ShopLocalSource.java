package com.tokopedia.posapp.data.source.local;

import com.tokopedia.posapp.database.manager.DbManager;
import com.tokopedia.posapp.database.manager.ProductDbManager;
import com.tokopedia.posapp.database.model.ProductDb;
import com.tokopedia.posapp.domain.model.result.ProductSavedResult;
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

    public ShopLocalSource() {
        productDbManager = new ProductDbManager();
    }

    public Observable<ProductSavedResult> storeProduct(ShopProductListDomain productListDomain) {
        return Observable.just(productListDomain)
                .flatMap(new Func1<ShopProductListDomain, Observable<ProductSavedResult>>() {
                    @Override
                    public Observable<ProductSavedResult> call(final ShopProductListDomain productListDomain) {
                        return Observable.create(new Observable.OnSubscribe<ProductSavedResult>() {
                            @Override
                            public void call(final Subscriber<? super ProductSavedResult> subscriber) {
                                List<ProductDb> data = new ArrayList<>();
                                for (com.tokopedia.core.shopinfo.models.productmodel.List item : productListDomain.getProductList()) {
                                    ProductDb productDb = new ProductDb();
                                    productDb.setProductId(item.productId);
                                    productDb.setProductName(item.productName);
                                    productDb.setProductPrice(item.productPrice);
                                    productDb.setProductUrl(item.productUrl);
                                    productDb.setProductImage(item.productImage);
                                    productDb.setProductImage300(item.productImage300);
                                    productDb.setProductImageFull(item.productImageFull);
                                    data.add(productDb);
                                }

                                productDbManager.store(data, new DbManager.TransactionListener() {
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
