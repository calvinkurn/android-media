package com.tokopedia.graphql.data;

import android.content.Context;

import androidx.annotation.AnyThread;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;

import com.akamai.botman.CYFMonitor;
import com.google.gson.GsonBuilder;
import com.tokopedia.akamai_bot_lib.UtilsKt;
import com.tokopedia.akamai_bot_lib.interceptor.GqlAkamaiBotInterceptor;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.fakeresponse.FakeResponseInterceptorProvider;
import com.tokopedia.graphql.FingerprintManager;
import com.tokopedia.graphql.data.db.GraphqlDatabase;
import com.tokopedia.graphql.data.source.cloud.api.GraphqlApi;
import com.tokopedia.graphql.data.source.cloud.api.GraphqlApiSuspend;
import com.tokopedia.graphql.data.source.cloud.api.GraphqlUrl;
import com.tokopedia.graphql.util.BrotliKotlinCustomObject;
import com.tokopedia.grapqhl.beta.notif.BetaInterceptor;
import com.tokopedia.network.CommonNetwork;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.converter.StringResponseConverter;
import com.tokopedia.network.interceptor.DeprecatedApiInterceptor;
import com.tokopedia.network.interceptor.FingerprintInterceptor;
import com.tokopedia.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.network.interceptor.TkpdAuthenticator;
import com.tokopedia.network.utils.TkpdOkHttpBuilder;
import com.tokopedia.user.session.UserSession;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

import static com.tokopedia.akamai_bot_lib.UtilsKt.getExpiredTime;
import static com.tokopedia.akamai_bot_lib.UtilsKt.setExpiredTime;
import com.tokopedia.fakeresponse.FakeResponseInterceptorProvider;

public class GraphqlClient {
    private static Retrofit sRetrofit = null;
    private static FingerprintManager sFingerprintManager;
    private static GraphqlDatabase sGraphqlDatabase;
    private static Context applicationContext;
    private static Function function;

    private GraphqlClient() {

    }

    @MainThread
    /***Marking important method for main thread.
     * Idea behind this method is A library initialisation may need some critical data.
     * In this case it was context and we need to mark on which thread the critical data will be set.
     * This approach decouples the library initialisation, we can use the any thread to initialise the library.
    **/
    public static void setContextData(Context context){
        applicationContext = context.getApplicationContext();
    }

    @AnyThread
    public synchronized static void init(@NonNull Context context) {
        if (sRetrofit == null) {
            UserSession userSession = new UserSession(context.getApplicationContext());
            TkpdOkHttpBuilder tkpdOkHttpBuilder = getTkpdOkHttpBuilder(context);
            initializeRetrofit(tkpdOkHttpBuilder, context, userSession);
        }
    }

    @AnyThread
    public synchronized static void init(@NonNull Context context, boolean addBrotliInterceptor) {
        if (sRetrofit == null) {
            UserSession userSession = new UserSession(context.getApplicationContext());

            TkpdOkHttpBuilder tkpdOkHttpBuilder = getTkpdOkHttpBuilder(context);
            //This interceptor should always be added in the end
            if(addBrotliInterceptor){
                tkpdOkHttpBuilder.addInterceptor(BrotliKotlinCustomObject.INSTANCE);
            }
            if (GlobalConfig.isAllowDebuggingTools()) {
                tkpdOkHttpBuilder.addInterceptor(new DeprecatedApiInterceptor(context.getApplicationContext()));

                FakeResponseInterceptorProvider provider = new FakeResponseInterceptorProvider();
                Interceptor interceptor = provider.getGqlInterceptor(context.getApplicationContext());
                if (interceptor != null) {
                    tkpdOkHttpBuilder.addInterceptor(interceptor);
                }
            }
            initializeRetrofit(tkpdOkHttpBuilder, context, userSession);
        }
    }

    private static void initializeRetrofit(TkpdOkHttpBuilder tkpdOkHttpBuilder, Context context, UserSession userSession){
        sRetrofit = CommonNetwork.createRetrofit(
                GraphqlUrl.BASE_URL,
                tkpdOkHttpBuilder,
                new TkpdAuthInterceptor(context, (NetworkRouter) context.getApplicationContext(), userSession),
                new FingerprintInterceptor((NetworkRouter) context.getApplicationContext(), userSession),
                TkpdAuthenticator.Companion.createAuthenticator(context, (NetworkRouter) context.getApplicationContext(), userSession),
                    new StringResponseConverter(),
                    new GsonBuilder());
            sFingerprintManager = new FingerprintManager(userSession);

        sGraphqlDatabase = GraphqlDatabase.getInstance(context);

        function = new Function(context);
    }

    @AnyThread
    public static Function getFunction() {
        if (function == null) {
            if(canBeInitialized()){
                init(applicationContext);
            }
            else {
                throw new RuntimeException("Please call init() before using graphql library");
            }
        }
        return function;
    }

    @AnyThread
    //Use this method for safe usage
    public static Function getFunctionWithContext(Context context) {
        if (!isInitialized()) {
            init(context);
        }
        return function;
    }

    @AnyThread
    public static boolean isInitialized(){
        return function != null;
    }

    @AnyThread
    public static boolean canBeInitialized(){
        return applicationContext != null;
    }

    public static void reInitRetrofitWithInterceptors(@NonNull List<Interceptor> interceptors,
                                                      @NonNull Context context) {
        UserSession userSession = new UserSession(context.getApplicationContext());
        TkpdOkHttpBuilder tkpdOkHttpBuilder = new TkpdOkHttpBuilder(context.getApplicationContext(), new OkHttpClient.Builder());

        for (Interceptor interceptor : interceptors) {
            tkpdOkHttpBuilder.addInterceptor(interceptor);
        }

        tkpdOkHttpBuilder.addInterceptor(new GqlAkamaiBotInterceptor());
        tkpdOkHttpBuilder.addInterceptor(new BetaInterceptor(context));

        sRetrofit = CommonNetwork.createRetrofit(
                GraphqlUrl.BASE_URL,
                tkpdOkHttpBuilder,
                new TkpdAuthInterceptor(context, (NetworkRouter) context.getApplicationContext(), userSession),
                new FingerprintInterceptor((NetworkRouter) context.getApplicationContext(), userSession),
                new TkpdAuthenticator(context, (NetworkRouter) context.getApplicationContext(), userSession),
                new StringResponseConverter(),
                new GsonBuilder());
    }

    @NotNull
    protected static TkpdOkHttpBuilder getTkpdOkHttpBuilder(@NonNull Context context) {
        TkpdOkHttpBuilder tkpdOkHttpBuilder = new TkpdOkHttpBuilder(context.getApplicationContext(), new OkHttpClient.Builder());
        tkpdOkHttpBuilder.addInterceptor(new GqlAkamaiBotInterceptor());
        tkpdOkHttpBuilder.addInterceptor(new BetaInterceptor(context));

        if (GlobalConfig.isAllowDebuggingTools()) {
            tkpdOkHttpBuilder.addInterceptor(new DeprecatedApiInterceptor(context.getApplicationContext()));
            FakeResponseInterceptorProvider provider = new FakeResponseInterceptorProvider();
            Interceptor interceptor = provider.getGqlInterceptor(context.getApplicationContext());
            if (interceptor != null) {
                tkpdOkHttpBuilder.addInterceptor(interceptor);
            }
        }
        return tkpdOkHttpBuilder;
    }

    public static class Function {
        private WeakReference<Context> context;

        public Function(Context mContext) {
            this.context = new WeakReference<>(mContext);
        }

        public String getAkamaiValue() {
            return UtilsKt.setExpire(
                    () -> System.currentTimeMillis(),
                    () -> getExpiredTime(context.get()),
                    (time) -> {
                        setExpiredTime(context.get(), time);
                        return Unit.INSTANCE;
                    },
                    () -> {
                        UtilsKt.setAkamaiValue(context.get(), CYFMonitor.getSensorData());
                        return Unit.INSTANCE;
                    },
                    () -> UtilsKt.getAkamaiValue(context.get())
            );
        }
    }

    public static Retrofit getRetrofit() {
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
