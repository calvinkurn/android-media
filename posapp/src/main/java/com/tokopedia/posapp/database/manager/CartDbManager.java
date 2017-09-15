package com.tokopedia.posapp.database.manager;

import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.tokopedia.posapp.database.QueryParameter;
import com.tokopedia.posapp.database.manager.base.DbStatus;
import com.tokopedia.posapp.database.manager.base.PosDbOperation;
import com.tokopedia.posapp.database.model.CartDb;
import com.tokopedia.posapp.database.model.CartDb_Table;
import com.tokopedia.posapp.domain.model.cart.CartDomain;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by okasurya on 9/15/17.
 */

public class CartDbManager extends PosDbOperation<CartDomain, CartDb> {
    public CartDbManager() {
        super();
    }

    @Override
    public Observable<DbStatus> store(final CartDomain domain) {
        return Observable.just(domain)
                .map(new Func1<CartDomain, CartDb>() {
                    @Override
                    public CartDb call(CartDomain cartDomain) {
                        return mapToDb(domain);
                    }
                })
                .flatMap(new Func1<CartDb, Observable<DbStatus>>() {
                    @Override
                    public Observable<DbStatus> call(CartDb cartDb) {
                        return executeStore(cartDb);
                    }
                });
    }

    @Override
    public Observable<DbStatus> store(List<CartDomain> data) {
        return Observable.just(data)
                .map(new Func1<List<CartDomain>, List<CartDb>>() {
                    @Override
                    public List<CartDb> call(List<CartDomain> cartDomains) {
                        return mapToDb(cartDomains);
                    }
                })
                .flatMap(new Func1<List<CartDb>, Observable<DbStatus>>() {
                    @Override
                    public Observable<DbStatus> call(List<CartDb> cartDbs) {
                        return executeStore(cartDbs);
                    }
                });
    }

    @Override
    public Observable<DbStatus> update(CartDomain data) {
        return Observable.just(data)
                .map(new Func1<CartDomain, CartDb>() {
                    @Override
                    public CartDb call(CartDomain cartDomain) {
                        return mapToDb(cartDomain);
                    }
                })
                .flatMap(new Func1<CartDb, Observable<DbStatus>>() {
                    @Override
                    public Observable<DbStatus> call(CartDb cartDb) {
                        return executeUpdate(cartDb);
                    }
                });
    }

    @Override
    public Observable<DbStatus> delete(ConditionGroup conditions) {
        return executeDelete(CartDb.class, conditions);
    }

    @Override
    public Observable<DbStatus> delete(CartDomain data) {
        return executeDelete(
            CartDb.class,
            ConditionGroup.clause().and(CartDb_Table.productId.eq(data.getProductId()))
        );
    }

    @Override
    public Observable<DbStatus> deleteAll() {
        return executeDeleteAll(CartDb.class);
    }

    @Override
    public Observable<CartDomain> getData(ConditionGroup conditions) {
        return Observable.just(conditions)
                .map(new Func1<ConditionGroup, CartDomain>() {
                    @Override
                    public CartDomain call(ConditionGroup conditions) {
                        return mapToDomain(SQLite.select().from(CartDb.class).where(conditions).querySingle());
                    }
                });
    }

    @Override
    public Observable<List<CartDomain>> getListData(ConditionGroup conditions) {
        return Observable.just(true)
                .map(new Func1<Boolean, List<CartDomain>>() {
                    @Override
                    public List<CartDomain> call(Boolean aBoolean) {
                        return mapToDomain(SQLite.select().from(CartDb.class).queryList());
                    }
                });
    }

    @Override
    public Observable<List<CartDomain>> getListData(QueryParameter queryParameter) {
        return Observable.just(queryParameter)
                .map(new Func1<QueryParameter, List<CartDomain>>() {
                    @Override
                    public List<CartDomain> call(QueryParameter q) {
                        return mapToDomain(
                            SQLite.select()
                                .from(CartDb.class)
                                .offset(q.getOffset())
                                .limit(q.getLimit())
                                .queryList()
                        );
                    }
                });
    }

    @Override
    public Observable<List<CartDomain>> getAllData() {
        return Observable.just(true)
                .map(new Func1<Boolean, List<CartDomain>>() {
                    @Override
                    public List<CartDomain> call(Boolean aBoolean) {
                        return mapToDomain(SQLite.select().from(CartDb.class).queryList());
                    }
                });
    }

    private List<CartDomain> mapToDomain(List<CartDb> cartDbs) {
        List<CartDomain> cartDomains = new ArrayList<>();

        if(cartDbs != null) {
            for (CartDb cartDb : cartDbs) {
                CartDomain cartDomain = mapToDomain(cartDb);
                if (cartDomain != null) cartDomains.add(cartDomain);
            }
        }

        return cartDomains;
    }

    private CartDomain mapToDomain(CartDb cartDb) {
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

    private List<CartDb> mapToDb(List<CartDomain> cartDomains) {
        List<CartDb> cartDbs = new ArrayList<>();

        if(cartDomains != null) {
            for (CartDomain cartDomain : cartDomains) {
                CartDb cartDb = mapToDb(cartDomain);
                if (cartDb != null) cartDbs.add(cartDb);
            }
        }

        return cartDbs;
    }

    private CartDb mapToDb(CartDomain cartDomain) {
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
}