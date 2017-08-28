package com.tokopedia.core.react.data.datasource.cache;

import rx.Observable;

/**
 * Created by okasurya on 8/28/17.
 */

public class ReactProductCacheSource implements ReactCacheSource {
    @Override
    public Observable<String> getData(String id) {
        return null;
    }

    @Override
    public Observable<String> getListData(int offset, int limit) {
        return null;
    }

    @Override
    public Observable<String> getAllData() {
        return null;
    }
}
