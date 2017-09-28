package com.tokopedia.posapp.database.manager.base;

import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;
import com.tokopedia.posapp.domain.model.DataStatus;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.functions.Func2;

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

    public abstract Observable<DataStatus> delete(T domain);

    public Observable<DataStatus> delete(ConditionGroup conditions) {
        return executeDelete(getDbClass(), conditions);
    }

    public Observable<DataStatus> deleteAll() {
        return executeDeleteAll(getDbClass());
    }

    public Observable<DataStatus> store(T domain) {
        return Observable.just(domain)
                .map(new Func1<T, D>() {
                    @Override
                    public D call(T domain) {
                        return mapToDb(domain);
                    }
                })
                .flatMap(new Func1<D, Observable<DataStatus>>() {
                    @Override
                    public Observable<DataStatus> call(D db) {
                        return executeStore(db);
                    }
                });
    }

    public Observable<DataStatus> store(List<T> domains) {
        return Observable.just(domains)
                .map(new Func1<List<T>, List<D>>() {
                    @Override
                    public List<D> call(List<T> domains) {
                        List<D> dbs = new ArrayList<D>();
                        for (T domain : domains) {
                            D db = mapToDb(domain);
                            dbs.add(db);
                        }
                        return dbs;
                    }
                })
                .flatMap(new Func1<List<D>, Observable<DataStatus>>() {
                    @Override
                    public Observable<DataStatus> call(List<D> dbs) {
                        return executeStore(dbs);
                    }
                });
    }

    public Observable<DataStatus> update(T domain) {
        return Observable.just(domain)
                .map(new Func1<T, D>() {
                    @Override
                    public D call(T domain) {
                        return mapToDb(domain);
                    }
                })
                .flatMap(new Func1<D, Observable<DataStatus>>() {
                    @Override
                    public Observable<DataStatus> call(D db) {
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

    public Observable<List<T>> getListData(int offset, int limit) {
        return Observable.zip(
                Observable.just(offset),
                Observable.just(limit),
                new Func2<Integer, Integer, List<T>>() {
                    @Override
                    public List<T> call(Integer offset, Integer limit) {
                        return mapToDomain(
                                SQLite.select().from(getDbClass()).offset(offset).limit(limit).queryList()
                        );
                    }
                }
        );
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

    protected Observable<DataStatus> executeStore(final D data) {
        return Observable.create(new Observable.OnSubscribe<DataStatus>() {
            @Override
            public void call(Subscriber<? super DataStatus> subscriber) {
                getDatabase().beginTransactionAsync(new ITransaction() {
                    @Override
                    public void execute(DatabaseWrapper databaseWrapper) {
                        if (data != null) data.save();
                    }
                }).success(defaultSuccessListener(subscriber))
                        .error(defaultErrorListener(subscriber))
                        .build().execute();
            }
        });
    }

    protected Observable<DataStatus> executeStore(final List<D> datas) {
        return Observable.create(new Observable.OnSubscribe<DataStatus>() {
            @Override
            public void call(Subscriber<? super DataStatus> subscriber) {
                getDatabase().beginTransactionAsync(new ITransaction() {
                    @Override
                    public void execute(DatabaseWrapper databaseWrapper) {
                        for (D data : datas) {
                            if (data != null) data.save();
                        }
                    }
                }).success(defaultSuccessListener(subscriber))
                        .error(defaultErrorListener(subscriber))
                        .build().execute();
            }
        });
    }

    protected Observable<DataStatus> executeUpdate(final D data) {
        return Observable.create(new Observable.OnSubscribe<DataStatus>() {
            @Override
            public void call(Subscriber<? super DataStatus> subscriber) {
                getDatabase().beginTransactionAsync(new ITransaction() {
                    @Override
                    public void execute(DatabaseWrapper databaseWrapper) {
                        if (data != null) data.update();
                    }
                }).success(defaultSuccessListener(subscriber))
                        .error(defaultErrorListener(subscriber))
                        .build().execute();
            }
        });
    }

    protected Observable<DataStatus> executeDeleteAll(final Class<D> clz) {
        return Observable.create(new Observable.OnSubscribe<DataStatus>() {
            @Override
            public void call(Subscriber<? super DataStatus> subscriber) {
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

    protected Observable<DataStatus> executeDelete(final Class<D> clz, final ConditionGroup conditions) {
        return Observable.create(new Observable.OnSubscribe<DataStatus>() {
            @Override
            public void call(Subscriber<? super DataStatus> subscriber) {
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

    protected Transaction.Success defaultSuccessListener(final Subscriber<? super DataStatus> subscriber) {
        return new Transaction.Success() {
            @Override
            public void onSuccess(Transaction transaction) {
                subscriber.onNext(DataStatus.defaultOkResult());
            }
        };
    }

    protected Transaction.Error defaultErrorListener(final Subscriber<? super DataStatus> subscriber) {
        return new Transaction.Error() {
            @Override
            public void onError(Transaction transaction, Throwable error) {
                error.printStackTrace();
                subscriber.onNext(DataStatus.defaultErrorResult(error));
            }
        };
    }
}
