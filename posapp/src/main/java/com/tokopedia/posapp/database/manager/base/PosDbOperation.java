package com.tokopedia.posapp.database.manager.base;

import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;
import com.tokopedia.posapp.database.PosDatabase;
import com.tokopedia.posapp.database.QueryParameter;

import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by okasurya on 9/15/17.
 */

public abstract class PosDbOperation<T, D extends BaseModel> implements DbOperation<T> {
    protected DatabaseDefinition database;

    protected PosDbOperation() {
        this.database = FlowManager.getDatabase(PosDatabase.class);
    }

    protected Observable<DbStatus> executeStore(final D data) {
        return Observable.create(new Observable.OnSubscribe<DbStatus>() {
            @Override
            public void call(Subscriber<? super DbStatus> subscriber) {
                database.beginTransactionAsync(new ITransaction() {
                    @Override
                    public void execute(DatabaseWrapper databaseWrapper) {
                        if(data != null) data.save();
                    }
                }).success(defaultSuccessListener(subscriber))
                        .error(defaultErrorListener(subscriber))
                        .build().execute();
            }
        });
    }

    protected Observable<DbStatus> executeStore(final List<D> datas) {
        return Observable.create(new Observable.OnSubscribe<DbStatus>() {
            @Override
            public void call(Subscriber<? super DbStatus> subscriber) {
                database.beginTransactionAsync(new ITransaction() {
                    @Override
                    public void execute(DatabaseWrapper databaseWrapper) {
                        for(D data : datas) {
                            if(data != null) data.save();
                        }
                    }
                }).success(defaultSuccessListener(subscriber))
                        .error(defaultErrorListener(subscriber))
                        .build().execute();
            }
        });
    }

    protected Observable<DbStatus> executeUpdate(final D data) {
        return Observable.create(new Observable.OnSubscribe<DbStatus>() {
            @Override
            public void call(Subscriber<? super DbStatus> subscriber) {
                database.beginTransactionAsync(new ITransaction() {
                    @Override
                    public void execute(DatabaseWrapper databaseWrapper) {
                        if(data != null) data.update();
                    }
                }).success(defaultSuccessListener(subscriber))
                        .error(defaultErrorListener(subscriber))
                        .build().execute();
            }
        });
    }

    protected Observable<DbStatus> executeDeleteAll(final Class<D> clz) {
        return Observable.create(new Observable.OnSubscribe<DbStatus>() {
            @Override
            public void call(Subscriber<? super DbStatus> subscriber) {
                database.beginTransactionAsync(new ITransaction() {
                    @Override
                    public void execute(DatabaseWrapper databaseWrapper) {
                        Delete.table(clz);
                    }
                }).success(defaultSuccessListener(subscriber))
                        .error(defaultErrorListener(subscriber))
                        .build().execute();
            }
        });
    }

    protected Observable<DbStatus> executeDelete(final Class<D> clz, final ConditionGroup conditions) {
        return Observable.create(new Observable.OnSubscribe<DbStatus>() {
            @Override
            public void call(Subscriber<? super DbStatus> subscriber) {
                database.beginTransactionAsync(new ITransaction() {
                    @Override
                    public void execute(DatabaseWrapper databaseWrapper) {
                        Delete.table(clz, conditions);
                    }
                }).success(defaultSuccessListener(subscriber))
                        .error(defaultErrorListener(subscriber))
                        .build().execute();
            }
        });
    }

    private Transaction.Success defaultSuccessListener(final Subscriber<? super DbStatus> subscriber) {
        return new Transaction.Success() {
            @Override
            public void onSuccess(Transaction transaction) {
                subscriber.onNext(DbStatus.defaultOkResult());
            }
        };
    }

    private Transaction.Error defaultErrorListener(final Subscriber<? super DbStatus> subscriber) {
        return new Transaction.Error() {
            @Override
            public void onError(Transaction transaction, Throwable error) {
                subscriber.onNext(DbStatus.defaultErrorResult(error));
            }
        };
    }

}
