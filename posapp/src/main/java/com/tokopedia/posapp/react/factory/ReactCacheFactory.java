package com.tokopedia.posapp.react.factory;

import com.tokopedia.posapp.react.ReactConst;
import com.tokopedia.posapp.react.datasource.cache.ReactBankCacheSource;
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
            case ReactConst.CacheTable.CART:
                return new ReactCartCacheSource();
            case ReactConst.CacheTable.PRODUCT:
                return new ReactProductCacheSource();
            case ReactConst.CacheTable.BANK:
                return new ReactBankCacheSource();
            default:
                throw new TableNotFoundException();
        }
    }
}
