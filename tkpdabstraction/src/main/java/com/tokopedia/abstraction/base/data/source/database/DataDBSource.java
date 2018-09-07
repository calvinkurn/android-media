package com.tokopedia.abstraction.base.data.source.database;


import java.util.HashMap;

import rx.Observable;

/**
 * Created by nathan on 10/23/17.
 */

public interface DataDBSource<T, U> {

    Observable<Boolean> isDataAvailable();

    Observable<Boolean> deleteAll();

    Observable<Boolean> insertAll(T data);

    Observable<U> getData(HashMap<String, Object> params);

    Observable<Integer> getDataCount(HashMap<String, Object> params);
}
