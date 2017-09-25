package com.tokopedia.posapp.react.factory;

import com.tokopedia.posapp.react.PosReactConst;
import com.tokopedia.posapp.react.datasource.cache.ReactBankCacheSource;
import com.tokopedia.posapp.react.datasource.cache.ReactCacheSource;
import com.tokopedia.posapp.react.datasource.cache.ReactCartCacheSource;
import com.tokopedia.posapp.react.datasource.cache.ReactEtalaseCacheSource;
import com.tokopedia.posapp.react.datasource.cache.ReactProductCacheSource;
import com.tokopedia.posapp.react.exception.TableNotFoundException;

/**
 * Created by okasurya on 8/28/17.
 */

public class ReactCacheFactory {
    private ReactCartCacheSource reactCartCacheSource;
    private ReactProductCacheSource reactProductCacheSource;
    private ReactBankCacheSource reactBankCacheSource;
    private ReactEtalaseCacheSource reactEtalaseCacheSource;

    public ReactCacheFactory(ReactCartCacheSource reactCartCacheSource,
                             ReactProductCacheSource reactProductCacheSource,
                             ReactBankCacheSource reactBankCacheSource,
                             ReactEtalaseCacheSource reactEtalaseCacheSource) {
        this.reactCartCacheSource = reactCartCacheSource;
        this.reactProductCacheSource = reactProductCacheSource;
        this.reactBankCacheSource = reactBankCacheSource;
        this.reactEtalaseCacheSource = reactEtalaseCacheSource;
    }

    public ReactCacheSource createCacheDataSource(String tableName) throws TableNotFoundException {
        switch (tableName) {
            case PosReactConst.CacheTable.CART:
                return reactCartCacheSource;
            case PosReactConst.CacheTable.PRODUCT:
                return reactProductCacheSource;
            case PosReactConst.CacheTable.BANK:
                return reactBankCacheSource;
            case PosReactConst.CacheTable.ETALASE:
                return reactEtalaseCacheSource;
            default:
                throw new TableNotFoundException();
        }
    }
}
