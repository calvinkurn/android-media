package com.tokopedia.common_digital.common.data.api;

import android.content.Context;
import android.util.Log;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.network.exception.HttpErrorException;
import com.tokopedia.abstraction.common.utils.network.AuthUtil;
import com.tokopedia.common_digital.product.data.response.TkpdDigitalResponse;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.exception.ResponseErrorException;
import com.tokopedia.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.user.session.UserSession;

import java.io.IOException;
import java.util.Map;

import okhttp3.Response;

/**
 * Created by Rizky on 16/08/18.
 */
public class DigitalInterceptor extends com.tokopedia.abstraction.common.network.interceptor.TkpdAuthInterceptor {

    private static final String TAG = DigitalInterceptor.class.getSimpleName();

    public DigitalInterceptor(Context context, AbstractionRouter abstractionRouter, com.tokopedia.abstraction.common.data.model.session.UserSession userSession) {
        super(context, abstractionRouter, userSession);
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
//                if (digitalErrorResponse.getStatus().equalsIgnoreCase(
//                        ServerErrorHandler.STATUS_UNDER_MAINTENANCE
//                )) {
//                    throw new ServerErrorMaintenanceException(
//                            digitalErrorResponse.getServerErrorMessageFormatted(), errorBody,
//                            response.code(), response.request().url().toString()
//                    );
//                } else if (digitalErrorResponse.getStatus().equalsIgnoreCase(
//                        ServerErrorHandler.STATUS_REQUEST_DENIED
//                )) {
//                    throw new ServerErrorRequestDeniedException(
//                            digitalErrorResponse.getServerErrorMessageFormatted(), errorBody,
//                            response.code(), response.request().url().toString()
//                    );
//                } else if (digitalErrorResponse.getStatus().equalsIgnoreCase(
//                        ServerErrorHandler.STATUS_FORBIDDEN
//                ) && MethodChecker.isTimezoneNotAutomatic()) {
//                    throw new ServerErrorTimeZoneException(
//                            digitalErrorResponse.getServerErrorMessageFormatted(), errorBody,
//                            response.code(), response.request().url().toString()
//                    );
//                } else {
//                    throw new HttpErrorException(response.code());
//                }
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
                path, strParam, method, authKey, contentTypeHeader, userSession.getUserId()
        );
    }

}
