package com.tokopedia.digital.common.data.apiservice;

import android.content.Context;
import android.util.Log;

import com.tokopedia.abstraction.common.network.exception.HttpErrorException;
import com.tokopedia.common_digital.product.data.response.TkpdDigitalResponse;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.exception.ResponseErrorException;
import com.tokopedia.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.user.session.UserSession;

import java.io.IOException;

import okhttp3.Response;

/**
 * @author anggaprasetiyo on 3/3/17.
 */

public class DigitalHmacAuthInterceptor extends TkpdAuthInterceptor {
    private static final String TAG = DigitalHmacAuthInterceptor.class.getSimpleName();

    public DigitalHmacAuthInterceptor(Context context,
                                      NetworkRouter networkRouter,
                                      UserSession userSession,
                                      String authKey) {
        super(context, networkRouter, userSession, authKey);
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
                /*if (digitalErrorResponse.getStatus().equalsIgnoreCase(
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
                } else {*/
                    throw new HttpErrorException(response.code());
//                }
            } else {
                throw new HttpErrorException(response.code());
            }

        }
        throw new HttpErrorException(response.code());
    }
}
