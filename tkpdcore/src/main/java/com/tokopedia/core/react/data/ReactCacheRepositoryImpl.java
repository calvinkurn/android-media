package com.tokopedia.core.react.data;

import com.tokopedia.core.react.domain.ReactCacheRepository;

import rx.Observable;

/**
 * Created by okasurya on 8/25/17.
 */

public class ReactCacheRepositoryImpl implements ReactCacheRepository {
    @Override
    public Observable<String> getData(String tableName, String id) {
        return null;
    }

    @Override
    public Observable<String> getDataList(String tableName, int offset, int limit) {
        return null;
    }
}
