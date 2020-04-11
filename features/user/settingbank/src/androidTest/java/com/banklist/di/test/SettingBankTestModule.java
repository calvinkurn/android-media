package com.banklist.di.test;

import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.interceptor.FingerprintInterceptor;
import com.tokopedia.settingbank.banklist.data.SettingBankUrl;
import com.tokopedia.settingbank.banklist.di.SettingBankModule;
import com.tokopedia.settingbank.banklist.di.SettingBankScope;
import com.tokopedia.user.session.UserSessionInterface;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import dagger.Module;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Retrofit;

@SettingBankScope
@Module
public class SettingBankTestModule extends SettingBankModule {

    MockWebServer mockWebServer;
    private String url;

    public SettingBankTestModule() {
        mockWebServer = new MockWebServer();
        try {
            mockWebServer.start();
            url = mockWebServer.url("/").toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @NotNull
    @Override
    public Retrofit realprovideSettingBankRetrofit(@NotNull Retrofit.Builder retrofitBuilder, @NotNull OkHttpClient okHttpClient) {
        return retrofitBuilder.baseUrl(url).client(okHttpClient).build();
    }

    public MockWebServer getMockWebServer() {
        return mockWebServer;
    }

    @NotNull
    @Override
    public FingerprintInterceptor realProvideFingerprintInterceptor(@NotNull NetworkRouter networkRouter, @NotNull UserSessionInterface userSession) {
        return new EmptyFingerprintInterceptor(networkRouter, userSession);
    }

    static class EmptyFingerprintInterceptor extends FingerprintInterceptor{

        public EmptyFingerprintInterceptor(NetworkRouter networkRouter, UserSessionInterface userSession) {
            super(networkRouter, userSession);
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            return chain.proceed(chain.request().newBuilder().build());
        }
    }
}
