package com.tokopedia.abstraction.base.data.source.cloud;

import java.util.HashMap;

import rx.Observable;

/**
 * Created by nathan on 10/23/17.
 */

public abstract class DataCloudSource<T> {

    public abstract Observable<T> getData(HashMap<String, Object> params);
}
