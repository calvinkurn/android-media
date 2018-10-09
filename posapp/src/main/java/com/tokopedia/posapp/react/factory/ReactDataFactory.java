package com.tokopedia.posapp.react.factory;

import com.tokopedia.posapp.react.PosReactConst;
import com.tokopedia.posapp.react.datasource.cache.ReactBankCacheSource;
import com.tokopedia.posapp.react.datasource.ReactDataSource;
import com.tokopedia.posapp.react.datasource.cache.ReactCartCacheSource;
import com.tokopedia.posapp.react.datasource.cache.ReactGlobalCacheSource;
import com.tokopedia.posapp.react.datasource.cloud.ReactEtalaseCloudSource;
import com.tokopedia.posapp.react.datasource.cloud.ReactProductCloudSource;
import com.tokopedia.posapp.react.exception.TableNotFoundException;

import javax.inject.Inject;

/**
 * Created by okasurya on 8/28/17.
 */

public class ReactDataFactory {
    private ReactDataSource cartSource;
    private ReactDataSource productSource;
    private ReactDataSource bankSource;
    private ReactDataSource etalaseSource;
    private ReactDataSource globalSource;

    @Inject
    public ReactDataFactory(ReactCartCacheSource cartSource,
                            ReactProductCloudSource productSource,
                            ReactBankCacheSource bankSource,
                            ReactEtalaseCloudSource etalaseSource,
                            ReactGlobalCacheSource globalSource) {
        this.cartSource = cartSource;
        this.productSource = productSource;
        this.bankSource = bankSource;
        this.etalaseSource = etalaseSource;
        this.globalSource = globalSource;
    }

    public ReactDataSource createDataSource(String tableName) throws TableNotFoundException {
        switch (tableName) {
            case PosReactConst.CacheTable.CART:
                return cartSource;
            case PosReactConst.CacheTable.PRODUCT:
                return productSource;
            case PosReactConst.CacheTable.BANK:
                return bankSource;
            case PosReactConst.CacheTable.ETALASE:
                return etalaseSource;
            case PosReactConst.CacheTable.GLOBAL:
                return globalSource;
            default:
                throw new TableNotFoundException();
        }
    }
}
