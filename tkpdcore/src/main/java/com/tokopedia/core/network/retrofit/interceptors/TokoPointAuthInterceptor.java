package com.tokopedia.core.network.retrofit.interceptors;

import android.util.Log;

import com.tokopedia.core.network.exception.HttpErrorException;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;

import java.io.IOException;
import java.util.Map;

import okhttp3.Response;

/**
 * @author anggaprasetiyo on 29/11/17.
 */

public class TokoPointAuthInterceptor extends TkpdAuthInterceptor {
    private static final String TAG = TokoPointAuthInterceptor.class.getSimpleName();

    public TokoPointAuthInterceptor(String hmacKey) {
        super(hmacKey);
    }

    @Override
    public void throwChainProcessCauseHttpError(Response response) throws IOException {
        String responseError = response.body().string();
        if (responseError != null)
            Log.d("HCK RESPONSE ERROR: ", responseError);
        throw new HttpErrorException(response.code());
    }

    @Override
    protected Map<String, String> getHeaderMap(
            String path, String strParam, String method, String authKey, String contentTypeHeader
    ) {
        Log.d("TPOINT PATH = ", path);
        Log.d("TPOINT PARAM QUERY = ", strParam);
        Log.d("TPOINT METHOD = ", method);
        Log.d("TPOINT AUTH KEY = ", authKey);
        Log.d("TPOINT CONTENT TYPE = ", contentTypeHeader);
        Map<String, String> mapHeader = AuthUtil.generateHeadersWithPath(
                path, strParam, method, authKey, contentTypeHeader
        );
        for (Map.Entry<String, String> entry : mapHeader.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            Log.d("TPOINT HEADER = ", "KEY = " + key + "| VALUE = " + value);
        }
        return mapHeader;
    }
}
