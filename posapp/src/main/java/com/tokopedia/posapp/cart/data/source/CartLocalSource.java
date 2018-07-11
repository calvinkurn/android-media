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

    @Inject
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
}
