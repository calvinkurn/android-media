package com.tokopedia.posapp.data.source.local;

import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.tokopedia.posapp.PosConstants;
import com.tokopedia.posapp.database.manager.CartDbManager2;
import com.tokopedia.posapp.database.manager.DbManager;
import com.tokopedia.posapp.data.mapper.AddToCartMapper;
import com.tokopedia.posapp.database.manager.CartDbManager;
import com.tokopedia.posapp.database.manager.DbStatus;
import com.tokopedia.posapp.database.model.CartDb_Table;
import com.tokopedia.posapp.database.model.CartDb;
import com.tokopedia.posapp.domain.model.cart.ATCStatusDomain;
import com.tokopedia.posapp.domain.model.cart.CartDomain;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by okasurya on 8/22/17.
 */

public class CartLocalSource {
    private AddToCartMapper addToCartMapper;
    private CartDbManager cartDbManager;

    public CartLocalSource(AddToCartMapper addToCartMapper) {
        this.cartDbManager = new CartDbManager();
        this.addToCartMapper = addToCartMapper;
    }

    public Observable<ATCStatusDomain> addToCart(CartDomain cartDomain) {
        CartDbManager2 cartDbManager2 = new CartDbManager2();
        return cartDbManager2.store(cartDomain)
                .map(new Func1<DbStatus, ATCStatusDomain>() {
                    @Override
                    public ATCStatusDomain call(DbStatus dbStatus) {
                        ATCStatusDomain atcStatus = new ATCStatusDomain();
                        if(dbStatus.isOk()) {
                            atcStatus.setStatus(ATCStatusDomain.RESULT_ADD_TO_CART_SUCCESS);
                            atcStatus.setMessage(ATCStatusDomain.DEFAULT_SUCCESS_MESSAGE);
                        } else {
                            atcStatus.setStatus(ATCStatusDomain.RESULT_ADD_TO_CART_ERROR);
                            atcStatus.setMessage(ATCStatusDomain.DEFAULT_ERROR_MESSAGE);

                        }
                        return atcStatus;
                    }
                });
    }

    private DbManager.TransactionListener getATCListener(final Subscriber<? super ATCStatusDomain> subscriber) {
        return new DbManager.TransactionListener() {
            @Override
            public void onTransactionSuccess() {
                ATCStatusDomain atcStatus = new ATCStatusDomain();
                atcStatus.setStatus(ATCStatusDomain.RESULT_ADD_TO_CART_SUCCESS);
                atcStatus.setMessage(ATCStatusDomain.DEFAULT_SUCCESS_MESSAGE);

                subscriber.onNext(atcStatus);
            }

            @Override
            public void onError(Throwable throwable) {
                ATCStatusDomain atcStatus = new ATCStatusDomain();
                atcStatus.setStatus(ATCStatusDomain.RESULT_ADD_TO_CART_ERROR);
                atcStatus.setMessage(throwable.getMessage());

                subscriber.onNext(atcStatus);
            }
        };
    }

    public Observable<ATCStatusDomain> increaseItem(String productId, final int quantity) {
        return Observable.just(productId)
                .map(new Func1<String, CartDomain>() {
                    @Override
                    public CartDomain call(String productId) {
                        CartDomain cartDomain = cartDbManager.first(
                                ConditionGroup.clause().and(CartDb_Table.productId.eq(productId))
                        );
                        cartDomain.setQuantity(cartDomain.getQuantity() + quantity);
                        return cartDomain;
                    }
                })
                .flatMap(new Func1<CartDomain, Observable<ATCStatusDomain>>() {
                    @Override
                    public Observable<ATCStatusDomain> call(final CartDomain cartDomain) {
                        return Observable.create(new Observable.OnSubscribe<ATCStatusDomain>() {
                            @Override
                            public void call(Subscriber<? super ATCStatusDomain> subscriber) {
                                cartDbManager.update(cartDomain, getATCListener(subscriber));
                            }
                        });
                    }
                });
    }

    public Observable<ATCStatusDomain> decreaseItem(String productId, final int quantity) {
        return Observable.just(productId)
                .map(new Func1<String, CartDomain>() {
                    @Override
                    public CartDomain call(String productId) {
                        CartDomain cartDomain = cartDbManager.first(
                            ConditionGroup.clause().and(CartDb_Table.productId.eq(productId))
                        );
                        cartDomain.setQuantity(cartDomain.getQuantity() - quantity);
                        return cartDomain;
                    }
                })
                .flatMap(new Func1<CartDomain, Observable<ATCStatusDomain>>() {
                    @Override
                    public Observable<ATCStatusDomain> call(final CartDomain cartDomain) {
                        return Observable.create(new Observable.OnSubscribe<ATCStatusDomain>() {
                            @Override
                            public void call(Subscriber<? super ATCStatusDomain> subscriber) {
                                if(cartDomain.getQuantity() < 1) {
                                    cartDbManager.delete(
                                        ConditionGroup.clause().and(CartDb_Table.productId.eq(cartDomain.getProductId())),
                                        getATCListener(subscriber)
                                    );
                                } else {
                                    cartDbManager.update(cartDomain, getATCListener(subscriber));
                                }
                            }
                        });
                    }
                });
    }



}
