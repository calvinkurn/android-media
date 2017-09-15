package com.tokopedia.posapp.database.manager;

import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;
import com.tokopedia.posapp.database.PosDatabase;
import com.tokopedia.posapp.database.model.CartDb;
import com.tokopedia.posapp.domain.model.cart.CartDomain;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by okasurya on 9/15/17.
 */

public class CartDbManager2 implements DbOperation<CartDomain> {
    DatabaseDefinition database;

    public CartDbManager2() {
        database = FlowManager.getDatabase(PosDatabase.class);
    }

    @Override
    public Observable<DbStatus> store(CartDomain domain) {
        return Observable.just(domain)
                .flatMap(new Func1<CartDomain, Observable<DbStatus>>() {
                    @Override
                    public Observable<DbStatus> call(final CartDomain cartDomain) {
                        return Observable.create(new Observable.OnSubscribe<DbStatus>() {
                            @Override
                            public void call(final Subscriber<? super DbStatus> subscriber) {
                                Transaction transaction = database.beginTransactionAsync(new ITransaction() {
                                    @Override
                                    public void execute(DatabaseWrapper databaseWrapper) {
                                        mapToDb(cartDomain).save();
                                    }
                                }).success(new Transaction.Success() {
                                    @Override
                                    public void onSuccess(Transaction transaction) {
                                        DbStatus dbStatus = new DbStatus();
                                        dbStatus.setStatus(DbStatus.OK);
                                        subscriber.onNext(dbStatus);
                                    }
                                }).error(new Transaction.Error() {
                                    @Override
                                    public void onError(Transaction transaction, Throwable error) {
                                        DbStatus dbStatus = new DbStatus();
                                        dbStatus.setStatus(DbStatus.ERROR);
                                        dbStatus.setMessage(error.getMessage());
                                        subscriber.onNext(dbStatus);
                                    }
                                }).build();

                                transaction.execute();
                            }
                        });
                    }
                });
    }

    @Override
    public Observable<DbStatus> store(List<CartDomain> data) {
        return null;
    }

    @Override
    public Observable<DbStatus> update(CartDomain data) {
        return null;
    }

    @Override
    public Observable<DbStatus> delete(ConditionGroup conditions) {
        return null;
    }

    @Override
    public Observable<DbStatus> delete(CartDomain data) {
        return null;
    }

    @Override
    public Observable<DbStatus> deleteAll() {
        return null;
    }

    @Override
    public Observable<CartDomain> first(ConditionGroup conditions) {
        return null;
    }

    @Override
    public Observable<CartDomain> first() {
        return null;
    }

    @Override
    public Observable<List<CartDomain>> getListData(ConditionGroup conditions) {
        return null;
    }

    @Override
    public Observable<List<CartDomain>> getAllData() {
        return null;
    }

    @Override
    public boolean isTableEmpty() {
        return false;
    }

    private CartDb mapToDb(CartDomain cartDomain) {
        return null;
    }
}
