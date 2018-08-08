package com.tokopedia.core.network.retrofit.interceptors;

import android.util.Log;

import com.tokopedia.core.network.exception.HttpErrorException;
import com.tokopedia.core.network.exception.ResponseErrorException;
import com.tokopedia.core.network.exception.ServerErrorRequestDeniedException;
import com.tokopedia.core.network.retrofit.exception.ServerErrorMaintenanceException;
import com.tokopedia.core.network.retrofit.exception.ServerErrorTimeZoneException;
import com.tokopedia.core.network.retrofit.response.TkpdDigitalResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.ServerErrorHandler;
import com.tokopedia.core.util.MethodChecker;

import java.io.IOException;
import java.util.Map;

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
            TkpdDigitalResponse.DigitalErrorResponse digitalErrorResponse =
                    TkpdDigitalResponse.DigitalErrorResponse.factory(errorBody, response.code());
            if (digitalErrorResponse.getTypeOfError()
                    == TkpdDigitalResponse.DigitalErrorResponse.ERROR_DIGITAL) {
                throw new ResponseErrorException(digitalErrorResponse.getDigitalErrorMessageFormatted());
            } else if (digitalErrorResponse.getTypeOfError()
                    == TkpdDigitalResponse.DigitalErrorResponse.ERROR_SERVER) {
                if (digitalErrorResponse.getStatus().equalsIgnoreCase(
                        ServerErrorHandler.STATUS_UNDER_MAINTENANCE
                )) {
                    throw new ServerErrorMaintenanceException(
                            digitalErrorResponse.getServerErrorMessageFormatted(), errorBody,
                            response.code(), response.request().url().toString()
                    );
                } else if (digitalErrorResponse.getStatus().equalsIgnoreCase(
                        ServerErrorHandler.STATUS_REQUEST_DENIED
                )) {
                    throw new ServerErrorRequestDeniedException(
                            digitalErrorResponse.getServerErrorMessageFormatted(), errorBody,
                            response.code(), response.request().url().toString()
                    );
                } else if (digitalErrorResponse.getStatus().equalsIgnoreCase(
                        ServerErrorHandler.STATUS_FORBIDDEN
                ) && MethodChecker.isTimezoneNotAutomatic()) {
                    throw new ServerErrorTimeZoneException(
                            digitalErrorResponse.getServerErrorMessageFormatted(), errorBody,
                            response.code(), response.request().url().toString()
                    );
                } else {
                    throw new HttpErrorException(response.code());
                }
            } else {
                throw new HttpErrorException(response.code());
            }

        }
        throw new HttpErrorException(response.code());
    }

    @Override
    protected Map<String, String> getHeaderMap(
            String path, String strParam, String method, String authKey, String contentTypeHeader
    ) {
        return AuthUtil.generateHeadersWithXUserId(
                path, strParam, method, authKey, contentTypeHeader
        );
    }
}
