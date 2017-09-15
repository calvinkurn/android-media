package com.tokopedia.posapp.database.manager;

import com.raizlabs.android.dbflow.sql.language.ConditionGroup;

import java.util.List;

import rx.Observable;

/**
 * Created by okasurya on 9/15/17.
 */

interface DbOperation<T> {
    Observable<DbStatus> store(T data);

    Observable<DbStatus> store(List<T> data);

    Observable<DbStatus> update(T data);

    Observable<DbStatus> delete(ConditionGroup conditions);

    Observable<DbStatus> delete(T data);

    Observable<DbStatus> deleteAll();

    Observable<T> first(ConditionGroup conditions);

    Observable<T> first();

    Observable<List<T>> getListData(ConditionGroup conditions);

    Observable<List<T>> getAllData();

    boolean isTableEmpty();
}
