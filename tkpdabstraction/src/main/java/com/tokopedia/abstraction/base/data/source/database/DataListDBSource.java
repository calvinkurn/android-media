package com.tokopedia.abstraction.base.data.source.database;


import java.util.HashMap;
import java.util.List;

import rx.Observable;

/**
 * Created by nathan on 10/23/17.
 */

public interface DataListDBSource<T,U> {

    Observable<Boolean> isDataAvailable();

    Observable<Boolean> deleteAll();

    Observable<Boolean> insertAll(List<T> list);

    Observable<List<U>> getData(HashMap<String, Object> params);
    Observable<Integer> getDataCount(HashMap<String, Object> params);
}
