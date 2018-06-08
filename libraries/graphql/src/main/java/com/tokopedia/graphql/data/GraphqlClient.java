package com.tokopedia.graphql.data;

import android.content.Context;
import android.support.annotation.NonNull;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.config.graphqlGeneratedDatabaseHolder;
import com.tokopedia.graphql.FingerprintManager;
import com.tokopedia.graphql.data.source.cloud.api.GraphqlApi;
import com.tokopedia.graphql.data.source.cloud.api.GraphqlUrl;
import com.tokopedia.network.CommonNetwork;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.user.session.UserSession;

import retrofit2.Retrofit;

public class GraphqlClient {
    private static Retrofit sRetrofit = null;
    private static GraphqlApi sGraphqlApi = null;
    private static FingerprintManager sFingerprintManager;

    private GraphqlClient() {

    }

    public synchronized static void init(@NonNull Context context) {
        if (sRetrofit == null) {
            UserSession userSession = new UserSession(context.getApplicationContext());
            sRetrofit = CommonNetwork.createRetrofit(context.getApplicationContext(),
                    GraphqlUrl.BASE_URL, (NetworkRouter) context.getApplicationContext(),
                    userSession);
            sFingerprintManager = new FingerprintManager(userSession);

            FlowManager.initModule(graphqlGeneratedDatabaseHolder.class);

        }
    }

    private static Retrofit getRetrofit() {
        if (sRetrofit == null) {
            throw new RuntimeException("Please call init() before using graphql library");
        }

        return sRetrofit;
    }

    public static GraphqlApi getApiInterface() {
        if (sGraphqlApi == null) {
            sGraphqlApi = getRetrofit().create(GraphqlApi.class);
        }
        return sGraphqlApi;
    }

    public static synchronized FingerprintManager getFingerPrintManager() {
        if (sFingerprintManager == null) {
            throw new RuntimeException("Please call init() before using graphql library");
        }
        return sFingerprintManager;
    }
}
