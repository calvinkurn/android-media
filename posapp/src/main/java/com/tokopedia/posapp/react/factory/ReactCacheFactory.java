package com.tokopedia.posapp.react.factory;

import com.tokopedia.core.react.data.Constants;
import com.tokopedia.posapp.react.datasource.cache.ReactCacheSource;
import com.tokopedia.posapp.react.datasource.cache.ReactCartCacheSource;
import com.tokopedia.posapp.react.datasource.cache.ReactProductCacheSource;
import com.tokopedia.posapp.react.exception.TableNotFoundException;

/**
 * Created by okasurya on 8/28/17.
 */

public class ReactCacheFactory {
    public ReactCacheFactory() {}

    public ReactCacheSource createCacheDataSource(String tableName) throws TableNotFoundException {
        switch (tableName) {
            case Constants.CacheTable.CART:
                return new ReactCartCacheSource();
            case Constants.CacheTable.PRODUCT:
                return new ReactProductCacheSource();
            default:
                throw new TableNotFoundException();
        }
    }
}
