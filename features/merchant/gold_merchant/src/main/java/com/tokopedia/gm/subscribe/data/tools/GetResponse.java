package com.tokopedia.gm.subscribe.data.tools;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by sebastianuskh on 3/1/17.
 */
public class GetResponse<T> implements Func1<Response<T>, T> {
    @Override
    public T call(Response<T> response) {
        return response.body();
    }
}
