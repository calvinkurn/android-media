package com.tokopedia.imageuploader.data.entity;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author sebastianuskh on 3/1/17.
 */
public class GetData<T> implements Func1<Response<T>, T> {
    @Override
    public T call(Response<T> tResponse) {
        return tResponse.body();
    }
}
