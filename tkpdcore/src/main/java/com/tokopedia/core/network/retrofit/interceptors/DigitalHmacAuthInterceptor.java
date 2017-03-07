package com.tokopedia.core.network.retrofit.interceptors;

import com.google.gson.Gson;
import com.tokopedia.core.network.exception.ResponseHttpErrorException;
import com.tokopedia.core.network.retrofit.response.TkpdDigitalResponse;

import java.io.IOException;

import okhttp3.Response;

/**
 * @author anggaprasetiyo on 3/3/17.
 */

public class DigitalHmacAuthInterceptor extends AuthHmacInterceptor {
    private static final String TAG = DigitalHmacAuthInterceptor.class.getSimpleName();

    public DigitalHmacAuthInterceptor(String hmacKey) {
        super(hmacKey);
    }

    @Override
    protected void throwChainProcessCauseHttpError(Response response) throws IOException {
        String errorBody = response.body().string();
        if (!errorBody.isEmpty()) {
            try {
                TkpdDigitalResponse.DigitalErrorResponse errorResponse =
                        new Gson().fromJson(
                                errorBody, TkpdDigitalResponse.DigitalErrorResponse.class
                        );
                throw new ResponseHttpErrorException(
                        errorResponse.getError().getStatus(),
                        errorResponse.getError().getTitle()
                );
            } catch (Exception e) {
                throw new ResponseHttpErrorException(response.code(), null);
            }
        }
        throw new ResponseHttpErrorException(response.code(), null);
    }
}
