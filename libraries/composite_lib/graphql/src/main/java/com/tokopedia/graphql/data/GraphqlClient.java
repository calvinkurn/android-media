package com.tokopedia.graphql.data;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.gson.GsonBuilder;
import com.tokopedia.akamai_bot_lib.interceptor.GqlAkamaiBotInterceptor;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.graphql.FingerprintManager;
import com.tokopedia.graphql.TestingInterceptorProvider;
import com.tokopedia.graphql.data.db.GraphqlDatabase;
import com.tokopedia.graphql.data.source.cloud.api.GraphqlApi;
import com.tokopedia.graphql.data.source.cloud.api.GraphqlApiSuspend;
import com.tokopedia.graphql.data.source.cloud.api.GraphqlUrl;
import com.tokopedia.grapqhl.beta.notif.BetaInterceptor;
import com.tokopedia.network.CommonNetwork;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.converter.StringResponseConverter;
import com.tokopedia.network.interceptor.DeprecatedApiInterceptor;
import com.tokopedia.network.interceptor.FingerprintInterceptor;
import com.tokopedia.network.interceptor.RiskAnalyticsInterceptor;
import com.tokopedia.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.network.utils.TkpdOkHttpBuilder;
import com.tokopedia.user.session.UserSession;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

public class GraphqlClient {
    private static Retrofit sRetrofit = null;
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
            tkpdOkHttpBuilder.addInterceptor(new BetaInterceptor(context));

            if (GlobalConfig.isAllowDebuggingTools()) {
                tkpdOkHttpBuilder.addInterceptor(new DeprecatedApiInterceptor(context.getApplicationContext()));

                TestingInterceptorProvider provider = new TestingInterceptorProvider();
                Interceptor interceptor = provider.getGqlTestingInterceptor(context.getApplicationContext());
                if (interceptor != null) {
                    tkpdOkHttpBuilder.addInterceptor(interceptor);
                }
            }

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

    @NonNull
    public static GraphqlApi getApiInterface() {
        return getRetrofit().create(GraphqlApi.class);
    }

    @NonNull
    public static GraphqlApiSuspend getApi() {
        return getRetrofit().create(GraphqlApiSuspend.class);
    }

    public static synchronized FingerprintManager getFingerPrintManager() {
        if (sFingerprintManager == null) {
            throw new RuntimeException("Please call init() before using graphql library");
        }
        return sFingerprintManager;
    }
}
