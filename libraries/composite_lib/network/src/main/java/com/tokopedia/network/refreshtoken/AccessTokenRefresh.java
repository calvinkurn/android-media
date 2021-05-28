package com.tokopedia.network.refreshtoken;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.GsonBuilder;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.converter.StringResponseConverter;
import com.tokopedia.network.interceptor.FingerprintInterceptor;
import com.tokopedia.network.interceptor.TkpdAuthenticator;
import com.tokopedia.network.interceptor.akamai.AkamaiBotInterceptor;
import com.tokopedia.network.utils.TkpdOkHttpBuilder;
import com.tokopedia.url.TokopediaUrl;
import com.tokopedia.user.session.UserSessionInterface;

import java.io.IOException;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * @author ricoharisin .
 */

public class AccessTokenRefresh {

    private static final String FORCE_LOGOUT = "forced_logout";
    private static final String INVALID_REQUEST = "invalid_request";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String GRANT_TYPE = "grant_type";
    private static final String REFRESH_TOKEN = "refresh_token";
    private static final String PATH = "path";

    public String refreshToken(Context context, UserSessionInterface userSession, NetworkRouter
            networkRouter) {
        return refreshToken(context, userSession, networkRouter, "/");
    }

    public String refreshToken(Context context, UserSessionInterface userSession, NetworkRouter
            networkRouter, String path) {

        Map<String, String> params = new HashMap<>();

        params.put(ACCESS_TOKEN, userSession.getAccessToken());
        params.put(GRANT_TYPE, REFRESH_TOKEN);
        params.put(REFRESH_TOKEN, EncoderDecoder.Decrypt(userSession.getFreshToken(), userSession.getRefreshTokenIV()));
        params.put(PATH, path);

        Call<String> responseCall = getRetrofit(context, userSession, networkRouter).create(AccountsBasicApi.class).getTokenSynchronous(params);

        String tokenResponse = null;
        String tokenResponseError = null;
        try {
            Response<String> response = responseCall.clone().execute();

            if (response.errorBody() != null) {
                tokenResponseError = response.errorBody().string();
                networkRouter.logRefreshTokenException(tokenResponseError, "error_refresh_token", path, userSession.getAccessToken());
                networkRouter.sendRefreshTokenAnalytics(tokenResponseError);
                checkShowForceLogout(tokenResponseError, networkRouter, path, userSession);
            } else if (response.body() != null) {
                tokenResponse = response.body();
                networkRouter.sendRefreshTokenAnalytics("");
            } else {
                return "";
            }
        } catch (SocketException e) {
            networkRouter.logRefreshTokenException(TkpdAuthenticator.Companion.formatThrowable(e), "socket_exception", path, "");
        } catch (IOException e) {
            networkRouter.logRefreshTokenException(TkpdAuthenticator.Companion.formatThrowable(e), "io_exception", path, "");
        }
        catch (Exception e) {
            e.printStackTrace();
            networkRouter.sendRefreshTokenAnalytics(e.toString());
            networkRouter.logRefreshTokenException(TkpdAuthenticator.Companion.formatThrowable(e), "failed_refresh_token", path, userSession.getAccessToken());
            forceLogoutAndShowDialogForLoggedInUsers(userSession, networkRouter, path);
        }

        TokenModel model = null;
        if (tokenResponse != null) {
            model = new GsonBuilder().create().fromJson(tokenResponse, TokenModel.class);
            if(model.getAccessToken()!= null && !model.getAccessToken().trim().isEmpty()) {
                userSession.setToken(model.getAccessToken(), model.getTokenType());
            }

            if(model.getRefreshToken()!= null && !model.getRefreshToken().trim().isEmpty()) {
                userSession.setRefreshToken( EncoderDecoder.Encrypt(model.getRefreshToken(),
                        userSession.getRefreshTokenIV()));
            }
        }

        if (model != null) {
            return model.getAccessToken();
        } else {
            return "";
        }
    }

    private void forceLogoutAndShowDialogForLoggedInUsers(UserSessionInterface userSession, NetworkRouter networkRouter, String path) {
        if(!TextUtils.isEmpty(userSession.getAccessToken())) {
            userSession.logoutSession();
            networkRouter.showForceLogoutTokenDialog(path);
        }
    }

    private Retrofit getRetrofit(Context context, UserSessionInterface userSession, NetworkRouter
            networkRouter) {
        TkpdOkHttpBuilder tkpdOkHttpBuilder = new TkpdOkHttpBuilder(context, new OkHttpClient.Builder());
        return new Retrofit.Builder()
                .baseUrl(TokopediaUrl.Companion.getInstance().getACCOUNTS())
                .addConverterFactory(new StringResponseConverter())
                .client(tkpdOkHttpBuilder.addInterceptor(new AccountsBasicInterceptor(context, networkRouter, userSession))
                        .addInterceptor(new FingerprintInterceptor(networkRouter, userSession))
                        .addInterceptor(new AkamaiBotInterceptor(context))
                        .build())
                .build();
    }

    protected Boolean isRequestDenied(String responseString) {
        return responseString.toLowerCase().contains(FORCE_LOGOUT);
    }

    protected void checkShowForceLogout(String response, NetworkRouter networkRouter, String path, UserSessionInterface userSession) {
        if (isRequestDenied(response)) {
            networkRouter.showForceLogoutTokenDialog(path);
            forceLogoutAndShowDialogForLoggedInUsers(userSession, networkRouter, path);
        }
    }
}
