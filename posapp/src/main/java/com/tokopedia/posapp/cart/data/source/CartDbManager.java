package com.tokopedia.posapp.cart.data.source;

import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.tokopedia.posapp.base.domain.model.DataStatus;
import com.tokopedia.posapp.database.manager.base.PosDbOperation;
import com.tokopedia.posapp.database.model.CartDb;
import com.tokopedia.posapp.database.model.CartDb_Table;
import com.tokopedia.posapp.cart.domain.model.CartDomain;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by okasurya on 9/15/17.
 */

public class CartDbManager extends PosDbOperation<CartDomain, CartDb> {
    @Override
    protected List<CartDomain> mapToDomain(List<CartDb> cartDbs) {
        List<CartDomain> cartDomains = new ArrayList<>();

        if(cartDbs != null) {
            for (CartDb cartDb : cartDbs) {
                CartDomain cartDomain = mapToDomain(cartDb);
                if (cartDomain != null) cartDomains.add(cartDomain);
            }
        }

        return cartDomains;
    }

    @Override
    protected CartDomain mapToDomain(CartDb cartDb) {
        if (cartDb != null) {
            CartDomain cartDomain = new CartDomain();
            cartDomain.setId(cartDb.getId());
            cartDomain.setProductId(cartDb.getProductId());
            cartDomain.setQuantity(cartDb.getQuantity());
            cartDomain.setOutletId(cartDb.getOutletId());
            return cartDomain;
        }

        return null;
    }

    @Override
    protected List<CartDb> mapToDb(List<CartDomain> cartDomains) {
        List<CartDb> cartDbs = new ArrayList<>();

        if(cartDomains != null) {
            for (CartDomain cartDomain : cartDomains) {
                CartDb cartDb = mapToDb(cartDomain);
                if (cartDb != null) cartDbs.add(cartDb);
            }
        }

        return cartDbs;
    }

    @Override
    protected CartDb mapToDb(CartDomain cartDomain) {
        if (cartDomain != null) {
            CartDb cartDb = new CartDb();
            if(cartDomain.getId() != null) cartDb.setId(cartDomain.getId());
            cartDb.setProductId(cartDomain.getProductId());
            cartDb.setQuantity(cartDomain.getQuantity());
            cartDb.setOutletId(cartDomain.getOutletId());
            return cartDb;
        }

        return null;
    }

    @Override
    protected Class<CartDb> getDbClass() {
        return CartDb.class;
    }

    @Override
    public Observable<DataStatus> delete(CartDomain data) {
        return executeDelete(
                CartDb.class,
                ConditionGroup.clause().and(CartDb_Table.productId.eq(data.getProductId()))
        );
    }
}