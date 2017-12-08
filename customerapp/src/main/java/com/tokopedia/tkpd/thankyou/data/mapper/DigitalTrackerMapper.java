package com.tokopedia.tkpd.thankyou.data.mapper;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by okasurya on 12/7/17.
 */

public class DigitalTrackerMapper implements Func1<Response<String>, String> {
    @Override
    public String call(Response<String> stringResponse) {
        return null;
    }
}
