package com.tokopedia.challenges.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.challenges.common.IndiSession;
import com.tokopedia.challenges.data.source.ChallengesUrl;
import com.tokopedia.user.session.UserSession;

import java.io.IOException;

import javax.inject.Inject;

import dagger.Provides;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.http2.Header;

/**
 * @author Vishal Gupta on 06/08/2018.
 */
public class IndiAuthInterceptor implements Interceptor {
    private static final int ERROR_FORBIDDEN_REQUEST = 401;

    private static final String X_API_KEY = "x-api-key";
    private static final String AUTHORIZATION = "authorization";
    private static final String INDI_USER_ID = "indi-user-id";

    protected UserSession userSession;
    private IndiSession indiSession;

    @Inject
    public IndiAuthInterceptor(@ApplicationContext Context context,
                               IndiSession indiSession) {
        this.userSession = new UserSession(context);
        this.indiSession = indiSession;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        final Request originRequest = chain.request();


        //check if
        if (TextUtils.isEmpty(indiSession.getAccessToken()) || TextUtils.isEmpty(indiSession.getUserId())) {
            refreshToken();
        }

        //new request
        String cache = chain.request().header("Cache-Control");
        Request.Builder newRequest = chain.request().newBuilder()
                .headers(getHeaderBuilder(cache).build())
                .method(originRequest.method(), originRequest.body());

        Response response = chain.proceed(newRequest.build());


        //refresh access token and recreate request with new token if response code is 401
        if (response.code() == ERROR_FORBIDDEN_REQUEST) {
            refreshToken();
            Request newestRequest = recreateRequestWithNewAccessToken(chain);
            response = chain.proceed(newestRequest);
        }

        //check if response is not successful throw error
        if (!response.isSuccessful()) {
            throwChainProcessCauseHttpError(response);
        }
        return response;
    }

    public void throwChainProcessCauseHttpError(Response response) throws IOException {
        /* this can override for throw error */
    }

    /**
     * regenerates Indi new access token and maps indi user ID
     *
     * @return
     */
    protected boolean refreshToken() {
        IndiTokenRefresh accessTokenRefresh = new IndiTokenRefresh(userSession, indiSession);
        try {
            accessTokenRefresh.refreshToken();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Request recreateRequestWithNewAccessToken(Chain chain) {
        Request original = chain.request();
        String cache = chain.request().header("Cache-Control");
        return chain.request().newBuilder()
                .headers(getHeaderBuilder(cache).build())
                .method(original.method(), original.body())
                .build();
    }

    @NonNull
    private Headers.Builder getHeaderBuilder(String cache) {
        Headers.Builder builder = new Headers.Builder();
        builder.add(X_API_KEY, ChallengesUrl.API_KEY);
        builder.add(AUTHORIZATION, indiSession.getAccessToken());
        builder.add(INDI_USER_ID, indiSession.getUserId());
        if (!TextUtils.isEmpty(cache)) {
            builder.add("Cache-Control", cache);

        }
        return builder;
    }
}
