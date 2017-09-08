package com.tokopedia.posapp.data.source.local;

import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.tokopedia.posapp.database.model.CartDB_Table;
import com.tokopedia.posapp.data.mapper.AddToCartMapper;
import com.tokopedia.posapp.database.manager.CartDbManager;
import com.tokopedia.posapp.database.model.CartDB;
import com.tokopedia.posapp.domain.model.cart.AddToCartStatusDomain;
import com.tokopedia.posapp.domain.model.cart.CartDomain;

import rx.Observable;
import rx.functions.Action1;

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

    public Observable<AddToCartStatusDomain> addToCart(CartDomain cartDomain) {
        return Observable.just(cartDomain)
                .doOnNext(storeToDb())
                .map(addToCartMapper);
    }

    private Action1<CartDomain> storeToDb() {
        return new Action1<CartDomain>() {
            @Override
            public void call(CartDomain cartDomain) {
                CartDB cartDB = generateExistingData(cartDomain);
                if(cartDB != null) {
                    cartDbManager.update(cartDB, null);
                } else {
                    cartDB = new CartDB();
                    cartDB.setProductId(cartDomain.getProductId());
                    cartDB.setQuantity(cartDomain.getQuantity());
                    cartDB.setOutletId(cartDomain.getOutletId());
                    cartDbManager. store(cartDB, null);
                }
            }
        };
    }

    private CartDB generateExistingData(CartDomain cartDomain) {
        CartDB cartDB = cartDbManager.first(
                ConditionGroup.clause().and(CartDB_Table.productId.eq(cartDomain.getProductId()))
        );

        if(cartDB != null) cartDB.setQuantity(cartDB.getQuantity()+cartDomain.getQuantity());

        return cartDB;
    }
}
