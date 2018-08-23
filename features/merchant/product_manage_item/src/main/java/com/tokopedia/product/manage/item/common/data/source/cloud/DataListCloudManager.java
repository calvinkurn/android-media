package com.tokopedia.product.manage.item.common.data.source.cloud;

import java.util.List;

import rx.Observable;

/**
 * Created by nathan on 10/23/17.
 */

public interface DataListCloudManager<T> {

    Observable<List<T>> getData();
}
