package com.tokopedia.core.network.retrofit.interceptors;

import android.util.Log;

import com.google.gson.JsonSyntaxException;
import com.tokopedia.core.network.exception.HttpErrorException;
import com.tokopedia.core.network.exception.ResponseErrorException;
import com.tokopedia.core.network.retrofit.response.TkpdDigitalResponse;

import java.io.IOException;

import okhttp3.Response;

/**
 * @author anggaprasetiyo on 3/3/17.
 */

public class DigitalHmacAuthInterceptor extends TkpdAuthInterceptor {
    private static final String TAG = DigitalHmacAuthInterceptor.class.getSimpleName();

    public DigitalHmacAuthInterceptor(String hmacKey) {
        super(hmacKey);
    }

    @Override
    public void throwChainProcessCauseHttpError(Response response) throws IOException {
        String errorBody = response.body().string();
        response.body().close();
        Log.d(TAG, "Error body response : " + errorBody);
        if (!errorBody.isEmpty()) {
            try {
                throw new ResponseErrorException(
                        TkpdDigitalResponse.DigitalErrorResponse.factory(errorBody)
                                .getErrorMessageFormatted()
                );
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
                throw new ResponseErrorException();
            }
        }
        throw new HttpErrorException(response.code());
    }
}
