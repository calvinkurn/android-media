package com.tokopedia.challenges.data;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tokopedia.challenges.common.IndiSession;
import com.tokopedia.challenges.data.model.IndiTokenModel;
import com.tokopedia.challenges.data.model.IndiUserModel;
import com.tokopedia.challenges.data.source.ChallengesUrl;
import com.tokopedia.network.converter.StringResponseConverter;
import com.tokopedia.network.utils.AuthUtil;
import com.tokopedia.user.session.UserSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
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
    private final UserSession userSession;
    private final IndiSession indiSession;

    IndiTokenRefresh(UserSession userSession, IndiSession indiSession) {
        this.userSession = userSession;
        this.indiSession = indiSession;
    }

    public void refreshToken() throws IOException {
        indiSession.clearToken();
        Call<String> responseCall = getRetrofit().create(IndiApi.class).getAccessToken(ChallengesUrl.API_KEY);

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
        if (model != null && TextUtils.isEmpty(indiSession.getUserId())) {
            mapUserWithIndi(indiSession, userSession, model.getAccessToken());
        }
    }

    private void mapUserWithIndi(IndiSession indiSession, UserSession userSession, String token) {
        Map<String, String> params = new HashMap<>();
        params.put(CLIENT_USER_ID, userSession.getUserId());
        params.put(CLIENT_USER_NAME, userSession.getName());
        params.put(CLIENT_USER_IMAGE_URL, userSession.getProfilePicture());

        Call<String> responseCall = getRetrofit().create(IndiApi.class).mapUser(params, token, ChallengesUrl.API_KEY);

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
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .addInterceptor(new HttpLoggingInterceptor())
                .build();

        return new Retrofit.Builder()
                .baseUrl(ChallengesUrl.INDI_DOMAIN)
                .addConverterFactory(new StringResponseConverter())
                .client(okHttpClient)
                .build();
    }
}
