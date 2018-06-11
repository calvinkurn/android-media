package com.tokopedia.networklib.util;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.network.CommonNetwork;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.networklib.data.source.cloud.api.RestApi;
import com.tokopedia.user.session.UserSession;

import retrofit2.Retrofit;

public class RestClient {
    private static Retrofit sRetrofit = null;
    private static RestApi sRestApi = null;

    private RestClient() {

    }

    public synchronized static void init(@NonNull Context context) {
        if (sRetrofit == null) {
            UserSession userSession = new UserSession(context.getApplicationContext());
            sRetrofit = CommonNetwork.createRetrofit(context.getApplicationContext(),
                    "", (NetworkRouter) context.getApplicationContext(),
                    userSession);
//            sFingerprintManager = new FingerprintManager(userSession);
//
//            FlowManager.initModule(graphqlGeneratedDatabaseHolder.class);

        }
    }

    private static Retrofit getRetrofit() {
        if (sRetrofit == null) {
            throw new RuntimeException("Please call init() before using graphql library");
        }

        return sRetrofit;
    }

    public static RestApi getApiInterface() {
        if (sRestApi == null) {
            sRestApi = getRetrofit().create(RestApi.class);
        }
        return sRestApi;
    }

//    public static synchronized FingerprintManager getFingerPrintManager() {
//        if (sFingerprintManager == null) {
//            throw new RuntimeException("Please call init() before using graphql library");
//        }
//        return sFingerprintManager;
//    }
}
