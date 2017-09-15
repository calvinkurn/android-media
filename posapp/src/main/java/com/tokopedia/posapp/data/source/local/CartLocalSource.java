package com.tokopedia.posapp.data.source.local;

import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.tokopedia.posapp.database.QueryParameter;
import com.tokopedia.posapp.database.manager.CartDbManager;
import com.tokopedia.posapp.database.manager.base.DbStatus;
import com.tokopedia.posapp.database.model.CartDb_Table;
import com.tokopedia.posapp.domain.model.cart.ATCStatusDomain;
import com.tokopedia.posapp.domain.model.cart.CartDomain;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by okasurya on 8/22/17.
 */

public class CartLocalSource {
    private CartDbManager cartDbManager;

    public CartLocalSource() {
        this.cartDbManager = new CartDbManager();
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

    public Observable<CartDomain> getCartProduct(String productId) {
        return cartDbManager.getData(
                ConditionGroup.clause().and(CartDb_Table.productId.eq(productId))
        );
    }

    public Observable<List<CartDomain>> getCartProducts(int offset, int limit) {
        QueryParameter q = new QueryParameter();
        q.setOffset(offset);
        q.setLimit(limit);

        return cartDbManager.getListData(q);
    }

    private Func1<DbStatus, ATCStatusDomain> getATCDefaultStatus() {
        return new Func1<DbStatus, ATCStatusDomain>() {
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
        };
    }
}
