package com.tokopedia.core.network.retrofit.interceptors;

import android.util.Log;

import com.tokopedia.core.network.exception.ResponseHttpErrorException;

import java.io.IOException;

/**
 * @author anggaprasetiyo on 3/3/17.
 */

public class DigitalHmacAuthInterceptor extends AuthHmacInterceptor {
    private static final String TAG = DigitalHmacAuthInterceptor.class.getSimpleName();

    public DigitalHmacAuthInterceptor(String hmacKey) {
        super(hmacKey);
    }

    @Override
    protected void throwChainProcessCauseHttpError(int code) throws IOException {
        Log.d(TAG, "throwChainProcessCauseHttpError, cause error Http");
        throw new ResponseHttpErrorException(code);
    }
}
