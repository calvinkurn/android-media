package com.tokopedia.challenges.data;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tokopedia.challenges.common.IndiSession;
import com.tokopedia.challenges.data.source.ChallengesUrl;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.network.converter.StringResponseConverter;
import com.tokopedia.user.session.UserSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * @author Vishal Gupta on 06/08/2018.
 */

public class IndiTokenRefresh {

    private static final String CLIENT_USER_ID = "client_user_id";
    private static final String CLIENT_USER_NAME = "client_user_name";
    private static final String CLIENT_USER_IMAGE_URL = "client_user_image_url";

    private static final String AUTHORIZATION = "authorization";
    private final Context context;
    private final UserSession sessionHandler;
    private final IndiSession indiSession;

    IndiTokenRefresh(Context context, UserSession userSession, IndiSession indiSession) {
        this.context = context;
        this.sessionHandler = userSession;
        this.indiSession = indiSession;
    }

    public String refreshToken() throws IOException {
        indiSession.clearToken();
        Call<String> responseCall = getRetrofit().create(IndiApi.class).getAccessToken();

        String tokenResponse = null;
        try {
            Response<String> response = responseCall.clone().execute();
            tokenResponse = response.body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //parse and set access token
        IndiTokenModel model = null;
        if (tokenResponse != null) {
            model = new GsonBuilder().create().fromJson(tokenResponse, IndiTokenModel.class);
            indiSession.setAccessToken(model.getAccessToken());
        }

        //if access token is valid, then map the tokopedia user with indi
        if (model != null) {
            mapUserWithIndi(context, indiSession, sessionHandler, model.getAccessToken());
            return model.getAccessToken();
        }

        return "";
    }

    private void mapUserWithIndi(Context context, IndiSession indiSession, UserSession userSession, String token) {
        Map<String, String> params = new HashMap<>();
        params.put(CLIENT_USER_ID, userSession.getUserId());
        params.put(CLIENT_USER_NAME, userSession.getName());
        params.put(CLIENT_USER_IMAGE_URL, userSession.getProfilePicture());

        Call<String> responseCall = getRetrofit().create(IndiApi.class).mapUser(params, token);

        String responseString = null;
        try {
            Response<String> response = responseCall.clone().execute();
            responseString = response.body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //parse and set access token
        IndiUserModel model = null;
        if (responseString != null) {
            model = new GsonBuilder().create().fromJson(responseString, IndiUserModel.class);
            indiSession.setUserId(model.getUserId());
        }
    }

    private Retrofit getRetrofit() {
        Gson gson = new Gson();
        return new Retrofit.Builder()
                .baseUrl(ChallengesUrl.INDI_DOMAIN)
                .addConverterFactory(new StringResponseConverter())
                .client(OkHttpFactory.create().getClientBuilder()
                        .addInterceptor(new HttpLoggingInterceptor())
                        .build())
                .build();
    }
}
