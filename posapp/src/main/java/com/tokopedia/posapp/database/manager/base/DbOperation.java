package com.tokopedia.posapp.database.manager.base;

import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;
import com.tokopedia.posapp.database.QueryParameter;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by okasurya on 9/15/17.
 */

public abstract class DbOperation<T, D extends BaseModel> {
    protected DatabaseDefinition database;

    abstract DatabaseDefinition getDatabase();

    protected abstract D mapToDb(T data);

    protected abstract List<D> mapToDb(List<T> data);

    protected abstract T mapToDomain(D db);

    protected abstract List<T> mapToDomain(List<D> db);

    protected abstract Class<D> getDbClass();

    public abstract Observable<DbStatus> delete(T domain);

    public Observable<DbStatus> delete(ConditionGroup conditions) {
        return executeDelete(getDbClass(), conditions);
    }

    public Observable<DbStatus> deleteAll() {
        return executeDeleteAll(getDbClass());
    }

    public Observable<DbStatus> store(T domain) {
        return Observable.just(domain)
                .map(new Func1<T, D>() {
                    @Override
                    public D call(T domain) {
                        return mapToDb(domain);
                    }
                })
                .flatMap(new Func1<D, Observable<DbStatus>>() {
                    @Override
                    public Observable<DbStatus> call(D db) {
                        return executeStore(db);
                    }
                });
    }

    public Observable<DbStatus> update(T domain) {
        return Observable.just(domain)
                .map(new Func1<T, D>() {
                    @Override
                    public D call(T domain) {
                        return mapToDb(domain);
                    }
                })
                .flatMap(new Func1<D, Observable<DbStatus>>() {
                    @Override
                    public Observable<DbStatus> call(D db) {
                        return executeUpdate(db);
                    }
                });
    }

    public Observable<T> getData(ConditionGroup conditions) {
        return Observable.just(conditions)
                .map(new Func1<ConditionGroup, T>() {
                    @Override
                    public T call(ConditionGroup conditions) {
                        return mapToDomain(
                                SQLite.select().from(getDbClass()).where(conditions).querySingle()
                        );
                    }
                });
    }

    public Observable<List<T>> getListData(ConditionGroup conditions) {
        return Observable.just(conditions)
                .map(new Func1<ConditionGroup, List<T>>() {
                    @Override
                    public List<T> call(ConditionGroup conditions) {
                        return mapToDomain(
                            SQLite.select().from(getDbClass()).where(conditions).queryList()
                        );
                    }
                });
    }

    public Observable<List<T>> getListData(QueryParameter queryParameter) {
        return Observable.just(queryParameter)
                .map(new Func1<QueryParameter, List<T>>() {
                    @Override
                    public List<T> call(QueryParameter q) {
                        return mapToDomain(
                            SQLite.select().from(getDbClass()).offset(q.getOffset()).limit(q.getLimit()).queryList()
                        );
                    }
                });
    }

    public Observable<List<T>> getAllData() {
        return Observable.just(true)
                .map(new Func1<Boolean, List<T>>() {
                    @Override
                    public List<T> call(Boolean aBoolean) {
                        return mapToDomain(SQLite.select().from(getDbClass()).queryList());
                    }
                });
    }

    protected Observable<DbStatus> executeStore(final D data) {
        return Observable.create(new Observable.OnSubscribe<DbStatus>() {
            @Override
            public void call(Subscriber<? super DbStatus> subscriber) {
                getDatabase().beginTransactionAsync(new ITransaction() {
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
                getDatabase().beginTransactionAsync(new ITransaction() {
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
                getDatabase().beginTransactionAsync(new ITransaction() {
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
                getDatabase().beginTransactionAsync(new ITransaction() {
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
                getDatabase().beginTransactionAsync(new ITransaction() {
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
