package com.tokopedia.challenges.data;

import android.content.Context;
import android.text.TextUtils;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.challenges.common.IndiSession;
import com.tokopedia.challenges.data.source.ChallengesUrl;
import com.tokopedia.user.session.UserSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Ashwani Tyagi on 14/09/18.
 */
public class IndiAWSInterceptor implements Interceptor {


    HashMap<String, Object> headers;
    public IndiAWSInterceptor(HashMap<String, Object> headers) {
        this.headers = headers;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        final Request originRequest = chain.request();


        Headers.Builder builder = new Headers.Builder();

        for (Map.Entry<String, Object> entry : headers.entrySet()) {
            builder.add(entry.getKey(), String.valueOf(entry.getValue()));
        }
        //new request
        Request.Builder newRequest = chain.request().newBuilder()
                .headers(builder.build())
                .method(originRequest.method(), originRequest.body());


        Response response = chain.proceed(newRequest.build());

        //check if response is not successful throw error
        if (!response.isSuccessful()) {
            throwChainProcessCauseHttpError(response);
        }


        return response;
    }

    public void throwChainProcessCauseHttpError(Response response) throws IOException {
        /* this can override for throw error */
    }


}

