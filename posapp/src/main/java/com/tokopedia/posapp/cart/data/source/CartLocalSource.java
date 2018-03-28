package com.tokopedia.posapp.cart.data.source;

import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.tokopedia.posapp.cart.data.source.db.CartDbManager;
import com.tokopedia.posapp.product.common.data.source.local.ProductDbManager;
import com.tokopedia.posapp.base.domain.model.DataStatus;
import com.tokopedia.posapp.database.model.CartDb_Table;
import com.tokopedia.posapp.database.model.ProductDb_Table;
import com.tokopedia.posapp.cart.domain.model.ATCStatusDomain;
import com.tokopedia.posapp.cart.domain.model.CartDomain;
import com.tokopedia.posapp.product.common.domain.model.ProductDomain;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * Created by okasurya on 8/22/17.
 */

public class CartLocalSource {
    private CartDbManager cartDbManager;
    private ProductDbManager productDbManager;

    @Inject
    public CartLocalSource() {
        this.cartDbManager = new CartDbManager();
        this.productDbManager = new ProductDbManager();
    }

    public Observable<ATCStatusDomain> storeCartProduct(CartDomain cartDomain) {
        return cartDbManager.store(cartDomain).map(getATCDefaultStatus());
    }

    public Observable<ATCStatusDomain> updateCartProduct(CartDomain cartDomain) {
        return cartDbManager.update(cartDomain).map(getATCDefaultStatus());
    }

    public Observable<ATCStatusDomain> deleteCartProduct(CartDomain cartDomain) {
        return cartDbManager.delete(cartDomain).map(getATCDefaultStatus());
    }

    public Observable<ATCStatusDomain> deleteCart() {
        return cartDbManager.deleteAll().map(getATCDefaultStatus());
    }

    public Observable<List<CartDomain>> getAllCartProducts() {
        return cartDbManager.getAllData();
    }

    public Observable<CartDomain> getCartProduct(long productId) {
        return cartDbManager.getData(
                ConditionGroup.clause().and(CartDb_Table.productId.eq(productId))
        );
    }

    public Observable<List<CartDomain>> getCartProducts(int offset, int limit) {
        return cartDbManager.getListData(offset, limit);
    }

    private Func1<DataStatus, ATCStatusDomain> getATCDefaultStatus() {
        return new Func1<DataStatus, ATCStatusDomain>() {
            @Override
            public ATCStatusDomain call(DataStatus dataStatus) {
                ATCStatusDomain atcStatus = new ATCStatusDomain();
                if(dataStatus.isOk()) {
                    atcStatus.setStatus(ATCStatusDomain.RESULT_ADD_TO_CART_SUCCESS);
                    atcStatus.setMessage(ATCStatusDomain.DEFAULT_SUCCESS_MESSAGE);
                } else {
                    atcStatus.setStatus(ATCStatusDomain.RESULT_ADD_TO_CART_ERROR);
                    atcStatus.setMessage(ATCStatusDomain.DEFAULT_ERROR_MESSAGE);
                }

                return atcStatus;
            }
        };
    }

    public Func1<List<CartDomain>,? extends Observable<List<CartDomain>>> getProducts() {
        return new Func1<List<CartDomain>, Observable<List<CartDomain>>>() {
            @Override
            public Observable<List<CartDomain>> call(List<CartDomain> cartDomains) {
                return Observable.just(cartDomains)
                        .flatMapIterable(new Func1<List<CartDomain>, Iterable<CartDomain>>() {
                            @Override
                            public Iterable<CartDomain> call(List<CartDomain> cartDomains) {
                                return cartDomains;
                            }
                        })
                        .flatMap(new Func1<CartDomain, Observable<CartDomain>>() {
                            @Override
                            public Observable<CartDomain> call(CartDomain cartDomain) {
                                return Observable.zip(
                                    Observable.just(cartDomain),
                                    productDbManager.getData(ConditionGroup.clause().and(ProductDb_Table.productId.eq(cartDomain.getProductId()))),
                                    new Func2<CartDomain, ProductDomain, CartDomain>() {
                                        @Override
                                        public CartDomain call(CartDomain cartDomain, ProductDomain productDomain) {
                                            cartDomain.setProduct(productDomain);
                                            return cartDomain;
                                        }
                                    }
                                );
                            }
                        })
                        .toList();
            }
        };
    }
}
