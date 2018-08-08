package com.tokopedia.product.manage.item.common.data.source.database;


import java.util.List;

import rx.Observable;

/**
 * Created by nathan on 10/23/17.
 */

public interface DataListDBManager<T> {

    Observable<Boolean> deleteAll();

    Observable<Boolean> insertAll(List<T> list);
}
