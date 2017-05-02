package com.tokopedia.core.base.utils;

import retrofit2.Response;
import rx.functions.Action1;

/**
 * @author madi on 4/10/17.
 */

public class HttpResponseValidator {

    private static final int HTTP_ERROR_500 = 500;
    private static final int HTTP_ERROR_600 = 600;
    private static final int HTTP_ERROR_400 = 400;

    public static Action1<Response<String>> validate(final HttpValidationListener listener) {

        return new Action1<Response<String>>() {
            @Override
            public void call(Response<String> stringResponse) {
                if (stringResponse.code() >= HTTP_ERROR_500
                        && stringResponse.code() < HTTP_ERROR_600) {
                    throw new RuntimeException("Server Error!");
                } else if (stringResponse.code() >= HTTP_ERROR_400
                        && stringResponse.code() < HTTP_ERROR_500) {

                    throw new RuntimeException("Client Error!");
                } else {
                    listener.OnPassValidation(stringResponse);
                }
            }
        };
    }

    public interface HttpValidationListener {
        void OnPassValidation(Response<String> response);
    }
}
