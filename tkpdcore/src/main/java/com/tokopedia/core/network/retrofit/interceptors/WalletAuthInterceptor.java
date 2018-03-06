package com.tokopedia.core.network.retrofit.interceptors;

import com.tokopedia.core.network.exception.HttpErrorException;
import com.tokopedia.core.network.exception.ResponseErrorException;
import com.tokopedia.core.network.exception.ServerErrorRequestDeniedException;
import com.tokopedia.core.network.retrofit.exception.ServerErrorMaintenanceException;
import com.tokopedia.core.network.retrofit.exception.ServerErrorTimeZoneException;
import com.tokopedia.core.network.retrofit.response.TkpdDigitalResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.ServerErrorHandler;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.util.WalletTokenRefresh;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by nabillasabbaha on 10/12/17.
 */

public class WalletAuthInterceptor extends TkpdAuthInterceptor {

    public final static String BEARER = "Bearer";
    private final static String AUTHORIZATION = "authorization";
    private final static String DEVICE = "android-";

    public WalletAuthInterceptor(String authKey) {
        super(authKey);
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

        if (isUnauthorizeWalletToken(finalRequest, response)) {
            WalletTokenRefresh walletTokenRefresh = new WalletTokenRefresh();
            walletTokenRefresh.refreshToken();
            Request newRequestWallet = reCreateRequestWithNewAccessToken(chain);
            Response responseNew = chain.proceed(newRequestWallet);
            if (isUnauthorizeWalletToken(newRequestWallet, responseNew)) {
                throwChainProcessCauseHttpError(responseNew);
            }
            return responseNew;
        }

        if (!response.isSuccessful()) {
            throwChainProcessCauseHttpError(response);
        }

        String bodyResponse = response.body().string();
        checkResponse(bodyResponse, response);

        return createNewResponse(response, bodyResponse);
    }

    @Override
    public void throwChainProcessCauseHttpError(Response response) throws IOException {
        String errorBody = response.body().string();
        response.body().close();
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

    private boolean isUnauthorizeWalletToken(Request request, Response response) {
        try {
            String responseString = response.peekBody(512).string();
            return (responseString.toLowerCase().contains("invalid token") ||
                    responseString.toLowerCase().contains("Invalid token"))
                    && request.header(AUTHORIZATION).contains(BEARER);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Request reCreateRequestWithNewAccessToken(Chain chain) {
        String newAccessToken = SessionHandler.getAccessTokenTokoCash();
        return chain.request().newBuilder()
                .header(AUTHORIZATION, BEARER + " " + newAccessToken)
                .build();
    }

    @Override
    protected Map<String, String> getHeaderMap(
            String path, String strParam, String method, String authKey, String contentTypeHeader) {
        Map<String, String> header = new HashMap<>();
        header.put(AuthUtil.HEADER_AUTHORIZATION, authKey);
        header.put(AuthUtil.HEADER_DEVICE, DEVICE + GlobalConfig.VERSION_NAME);
        return header;
    }
}
