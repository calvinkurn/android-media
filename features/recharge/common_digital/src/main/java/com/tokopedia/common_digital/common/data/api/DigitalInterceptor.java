package com.tokopedia.common_digital.common.data.api;

import android.content.Context;
import android.util.Log;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.exception.HttpErrorException;
import com.tokopedia.abstraction.common.utils.network.AuthUtil;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.authentication.AuthKey;
import com.tokopedia.common_digital.product.data.response.TkpdDigitalResponse;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.exception.ResponseErrorException;
import com.tokopedia.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.url.Env;
import com.tokopedia.url.TokopediaUrl;
import com.tokopedia.user.session.UserSessionInterface;

import java.io.IOException;
import java.util.Map;

import okhttp3.Response;

/**
 * Created by Rizky on 16/08/18.
 */
public class DigitalInterceptor extends TkpdAuthInterceptor {

    private static final String TAG = DigitalInterceptor.class.getSimpleName();
    private Context context;

    public DigitalInterceptor(@ApplicationContext Context context,
                              NetworkRouter networkRouter,
                              UserSessionInterface userSessionInterface) {
        super(context, networkRouter, userSessionInterface);
        this.authKey = getDigitalAuthKey();
        this.context = context;
    }

    public String getDigitalAuthKey() {
        if (TokopediaUrl.getInstance().getTYPE() == Env.STAGING) {
            return AuthKey.RECHARGE_HMAC_API_KEY_STAGING;
        } else {
            return AuthKey.RECHARGE_HMAC_API_KEY_PROD;
        }
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
                        DigitalError.Companion.getSTATUS_UNDER_MAINTENANCE()
                )) {
                    throw new ResponseErrorException(
                            digitalErrorResponse.getServerErrorMessageFormatted()
                    );
                } else if (digitalErrorResponse.getStatus().equalsIgnoreCase(
                        DigitalError.Companion.getSTATUS_REQUEST_DENIED()
                )) {
                    throw new ResponseErrorException(
                            digitalErrorResponse.getServerErrorMessageFormatted()
                    );
                } else if (digitalErrorResponse.getStatus().equalsIgnoreCase(
                        DigitalError.Companion.getSTATUS_FORBIDDEN()
                ) && MethodChecker.isTimezoneNotAutomatic(context)) {
                    throw new ResponseErrorException(
                            digitalErrorResponse.getServerErrorMessageFormatted()
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
                path, strParam, method, authKey, contentTypeHeader, userSession.getUserId(), userSession
        );
    }

}
