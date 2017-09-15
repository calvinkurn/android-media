package com.tokopedia.posapp.database.manager.base;

import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.tokopedia.posapp.database.QueryParameter;

import java.util.List;

import rx.Observable;

/**
 * Created by okasurya on 9/15/17.
 */

public interface DbOperation<T> {
    abstract Observable<DbStatus> store(T data);

    abstract Observable<DbStatus> store(List<T> data);

    abstract Observable<DbStatus> update(T data);

    abstract Observable<DbStatus> delete(ConditionGroup conditions);

    abstract Observable<DbStatus> delete(T data);

    abstract Observable<DbStatus> deleteAll();

    abstract Observable<T> getData(ConditionGroup conditions);

    abstract Observable<List<T>> getListData(ConditionGroup conditions);

    abstract Observable<List<T>> getListData(QueryParameter queryParameter);

    abstract Observable<List<T>> getAllData();
}
