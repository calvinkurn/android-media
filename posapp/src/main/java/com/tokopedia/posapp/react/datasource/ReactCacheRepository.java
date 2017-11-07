package com.tokopedia.posapp.react.datasource;

import rx.Observable;

/**
 * Created by okasurya on 8/25/17.
 */

public interface ReactCacheRepository {

    Observable<String> getData(String tableName, String id);

    Observable<String> getDataList(String tableName, int offset, int limit);

    Observable<String> getDataAll(String tableName);

    Observable<String> deleteAll(String tableName);

    Observable<String> deleteItem(String tableName, String id);

    Observable<String> update(String tableName, String data);

    Observable<String> insert(String tableName, String data);
}
