package com.tokopedia.core.network.retrofit.interceptors;

import android.util.Log;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by hangnadi on 7/11/17.
 */

public class ResolutionInterceptor extends TkpdAuthInterceptor {

    private static final String TAG = ResolutionInterceptor.class.getSimpleName();

    public ResolutionInterceptor() {

    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        final Request originRequest = chain.request();
        Request.Builder newRequest = chain.request().newBuilder();

        generateHmacAuthRequest(originRequest, newRequest);

        final Request finalRequest = newRequest.build();
        Response response = getResponse(chain, finalRequest);

        if (isNeedRelogin(response)) {
            doRelogin();
            response = getResponse(chain, finalRequest);
        }

        if (!response.isSuccessful()) {
            throwChainProcessCauseHttpError(response);
        }

        String bodyResponse = response.body().string();
        if (isMaintenance(bodyResponse)) {
            showMaintenancePage();
        } else if (isRequestDenied(bodyResponse)) {
            showForceLogoutDialog();
            sendForceLogoutAnalytics(response);
        } else if (isServerError(response.code()) && !isHasErrorMessage(bodyResponse)) {
            showServerErrorSnackbar();
            sendErrorNetworkAnalytics(response);
        } else if (isForbiddenRequest(bodyResponse, response.code())
                && isTimezoneNotAutomatic()) {
            showTimezoneErrorSnackbar();
        }

        return createNewResponse(response, bodyResponse);
    }

    @Override
    protected Response getResponse(Chain chain, Request request) throws IOException {
        Response response = chain.proceed(request);
        if (response.code() != 413) {
            Log.d("hangnadi", "getResponse: " + response.code());
            int count = 0;
            while (!response.isSuccessful() && count < maxRetryAttempt) {
                Log.d(TAG, "Request is not successful - " + count + " Error code : " + response.code());
                count++;
                response = chain.proceed(request);
            }
        }
        return response;
    }
}
