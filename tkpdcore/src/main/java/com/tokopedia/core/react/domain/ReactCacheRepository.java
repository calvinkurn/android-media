package com.tokopedia.core.react.domain;

import rx.Observable;

/**
 * Created by okasurya on 8/25/17.
 */

public interface ReactCacheRepository {

    Observable<String> getData(String tableName, String id);

    Observable<String> getDataList(String tableName, int offset, int limit);

}
