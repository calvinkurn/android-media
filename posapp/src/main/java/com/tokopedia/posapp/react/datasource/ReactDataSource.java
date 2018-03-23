package com.tokopedia.posapp.react.datasource;

import rx.Observable;

/**
 * Created by okasurya on 8/28/17.
 */

public interface ReactDataSource {
    Observable<String> getData(String id);

    Observable<String> getDataList(int offset, int limit);

    Observable<String> getDataAll();

    Observable<String> deleteAll();

    Observable<String> deleteItem(String id);

    Observable<String> update(String data);

    Observable<String> insert(String data);
}
