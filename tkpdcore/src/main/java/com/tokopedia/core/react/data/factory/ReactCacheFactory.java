package com.tokopedia.core.react.data.factory;

import com.tokopedia.core.database.o2o.CartDbManager;
import com.tokopedia.core.react.data.Constants;
import com.tokopedia.core.react.data.datasource.cache.ReactCacheSource;
import com.tokopedia.core.react.data.datasource.cache.ReactCartCacheSource;
import com.tokopedia.core.react.data.datasource.cache.ReactProductCacheSource;
import com.tokopedia.core.react.data.datasource.cache.TableNotFoundException;

/**
 * Created by okasurya on 8/28/17.
 */

public class ReactCacheFactory {
    public ReactCacheFactory() {}

    public ReactCacheSource createCacheDataSource(String tableName) throws TableNotFoundException {
        switch (tableName) {
            case Constants.Table.CART:
                return new ReactCartCacheSource();
            case Constants.Table.PRODUCT:
                return new ReactProductCacheSource();
            default:
                throw new TableNotFoundException();
        }
    }
}
