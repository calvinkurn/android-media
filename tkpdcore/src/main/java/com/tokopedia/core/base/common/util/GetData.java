package com.tokopedia.core.base.common.util;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by sebastianuskh on 3/8/17.
 */

public class GetData<T> implements Func1<Response<T>, T> {
    @Override
    public T call(Response<T> tResponse) {
        return tResponse.body();
    }
}
