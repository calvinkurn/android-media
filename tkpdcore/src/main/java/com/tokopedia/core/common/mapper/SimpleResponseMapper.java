package com.tokopedia.core.common.mapper;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by Hendry on 4/21/2017.
 */

public class SimpleResponseMapper<T> implements Func1<Response<T>, T> {
    @Override
    public T call(Response<T> dataResponseResponse) {
        return mappingResponse(dataResponseResponse);
    }

    private T mappingResponse(Response<T> dataResponseResponse) {
        if (dataResponseResponse.isSuccessful() && dataResponseResponse.body() != null) {
            return dataResponseResponse.body();
        } else {
            return null;
        }
    }
}
