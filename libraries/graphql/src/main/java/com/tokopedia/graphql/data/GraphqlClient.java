package com.tokopedia.graphql.data;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.graphql.FingerprintManager;
import com.tokopedia.graphql.data.db.GraphqlDatabase;
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
    private static GraphqlDatabase sGraphqlDatabase;

    private GraphqlClient() {

    }

    public synchronized static void init(@NonNull Context context) {
        if (sRetrofit == null) {
            UserSession userSession = new UserSession(context.getApplicationContext());
            sRetrofit = CommonNetwork.createRetrofit(context.getApplicationContext(),
                    GraphqlUrl.BASE_URL, (NetworkRouter) context.getApplicationContext(),
                    userSession);
            sFingerprintManager = new FingerprintManager(userSession);

            sGraphqlDatabase = GraphqlDatabase.getInstance(context);

        }
    }

    private static Retrofit getRetrofit() {
        if (sRetrofit == null) {
            throw new RuntimeException("Please call init() before using graphql library");
        }

        return sRetrofit;
    }

    public static GraphqlDatabase getGraphqlDatabase() {
        if (sGraphqlDatabase == null) {
            throw new RuntimeException("Please call init() before using graphql library");
        }

        return sGraphqlDatabase;
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
