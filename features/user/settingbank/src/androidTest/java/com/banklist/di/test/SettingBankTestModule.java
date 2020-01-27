package com.banklist.di.test;

import com.tokopedia.settingbank.banklist.data.SettingBankUrl;
import com.tokopedia.settingbank.banklist.di.SettingBankModule;
import com.tokopedia.settingbank.banklist.di.SettingBankScope;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import dagger.Module;
import okhttp3.OkHttpClient;
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
}
