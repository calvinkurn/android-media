package com.tokopedia.abstraction.base.data.source.cloud;

import com.tokopedia.abstraction.common.network.services.BaseService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Observable;

/**
 * Created by nathan on 10/23/17.
 */

public abstract class DataListCloudSource<T> {

    public abstract Observable<List<T>> getData(HashMap<String, Object> params);
}
