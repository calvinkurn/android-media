package com.tokopedia.graphql.data;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.akamai_bot_lib.interceptor.AkamaiBotInterceptor;
import com.tokopedia.akamai_bot_lib.interceptor.GqlAkamaiBotInterceptor;
import com.google.gson.GsonBuilder;
import com.tokopedia.graphql.FingerprintManager;
import com.tokopedia.graphql.data.db.GraphqlDatabase;
import com.tokopedia.graphql.data.source.cloud.api.GraphqlApi;
import com.tokopedia.graphql.data.source.cloud.api.GraphqlUrl;
import com.tokopedia.network.CommonNetwork;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.converter.StringResponseConverter;
import com.tokopedia.network.interceptor.FingerprintInterceptor;
import com.tokopedia.network.interceptor.RiskAnalyticsInterceptor;
import com.tokopedia.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.network.utils.TkpdOkHttpBuilder;
import com.tokopedia.user.session.UserSession;

import okhttp3.OkHttpClient;
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

            TkpdOkHttpBuilder tkpdOkHttpBuilder = new TkpdOkHttpBuilder(context.getApplicationContext(), new OkHttpClient.Builder());
            tkpdOkHttpBuilder.addInterceptor(new RiskAnalyticsInterceptor(context));
            tkpdOkHttpBuilder.addInterceptor(new GqlAkamaiBotInterceptor());

            sRetrofit = CommonNetwork.createRetrofit(
                    GraphqlUrl.BASE_URL,
                    tkpdOkHttpBuilder,
                    new TkpdAuthInterceptor(context, (NetworkRouter) context.getApplicationContext(), userSession),
                    new FingerprintInterceptor((NetworkRouter) context.getApplicationContext(), userSession),
                    new StringResponseConverter(),
                    new GsonBuilder());
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
