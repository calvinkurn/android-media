package com.tokopedia.core.network.retrofit.interceptors;

import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.util.GlobalConfig;
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

    private final static String AUTHORIZATION = "authorization";
    private final static String BEARER = "Bearer";
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

        if (isUnauthorizeWalletToken(finalRequest, response)) {
            WalletTokenRefresh walletTokenRefresh = new WalletTokenRefresh();
            try {
                walletTokenRefresh.refreshToken();
                Request newRequestWallet = reCreateRequestWithNewAccessToken(chain);
                Response responseNew = chain.proceed(newRequestWallet);
                return responseNew;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return super.intercept(chain);
    }

    private boolean isUnauthorizeWalletToken(Request request, Response response) {
        return response.code() == 401 && request.header(AUTHORIZATION).contains(BEARER);
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
