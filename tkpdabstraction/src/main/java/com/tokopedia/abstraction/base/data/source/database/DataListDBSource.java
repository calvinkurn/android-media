package com.tokopedia.abstraction.base.data.source.database;


import java.util.List;

import rx.Observable;

/**
 * Created by nathan on 10/23/17.
 */

public interface DataListDBSource<T> {

    Observable<Boolean> isDataAvailable();

    Observable<Boolean> deleteAll();

    Observable<Boolean> insertAll(List<T> list);
}
