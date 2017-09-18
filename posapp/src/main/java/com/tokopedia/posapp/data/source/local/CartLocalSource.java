package com.tokopedia.posapp.data.source.local;

import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.tokopedia.posapp.database.QueryParameter;
import com.tokopedia.posapp.database.manager.CartDbManager;
import com.tokopedia.posapp.database.manager.ProductDbManager;
import com.tokopedia.posapp.database.manager.base.DbStatus;
import com.tokopedia.posapp.database.model.CartDb_Table;
import com.tokopedia.posapp.database.model.ProductDb;
import com.tokopedia.posapp.database.model.ProductDb_Table;
import com.tokopedia.posapp.domain.model.cart.ATCStatusDomain;
import com.tokopedia.posapp.domain.model.cart.CartDomain;
import com.tokopedia.posapp.domain.model.product.ProductDomain;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by okasurya on 8/22/17.
 */

public class CartLocalSource {
    private CartDbManager cartDbManager;
    private ProductDbManager productDbManager;

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
        return cartDbManager.getAllData().map(getProductData());
    }

    public Observable<CartDomain> getCartProduct(int productId) {
        return cartDbManager.getData(
                ConditionGroup.clause().and(CartDb_Table.productId.eq(productId))
        );
    }

    public Observable<List<CartDomain>> getCartProducts(int offset, int limit) {
        QueryParameter q = new QueryParameter();
        q.setOffset(offset);
        q.setLimit(limit);

        return cartDbManager.getListData(q).map(getProductData());
    }

    private ProductDomain mapToDomain(ProductDb productDb) {
        if(productDb != null) {
            ProductDomain productDomain = new ProductDomain();
            productDomain.setProductId(productDb.getProductId());
            productDomain.setProductName(productDb.getProductName());
            productDomain.setProductUrl(productDb.getProductUrl());
            productDomain.setProductPrice(productDb.getProductPrice());
            productDomain.setProductPriceUnformatted(productDb.getProductPriceUnformatted());
            productDomain.setProductImage(productDb.getProductImage());
            productDomain.setProductImage300(productDb.getProductImage300());
            productDomain.setProductImageFull(productDb.getProductImageFull());
            return productDomain;
        }

        return null;
    }

    private ProductDb getProduct(int productId) {
        return productDbManager.first(
                ConditionGroup.clause().and(ProductDb_Table.productId.eq(productId))
        );
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

    private Func1<List<CartDomain>, List<CartDomain>> getProductData() {
        return new Func1<List<CartDomain>, List<CartDomain>>() {
            @Override
            public List<CartDomain> call(List<CartDomain> cartDomains) {
                for (int i = 0; i < cartDomains.size(); i++) {
                    ProductDomain productDomain = mapToDomain(getProduct(cartDomains.get(i).getProductId()));
                    if (productDomain != null) {
                        cartDomains.get(i).setProductDomain(productDomain);
                    }
                }
                return cartDomains;
            }
        };
    }
}
