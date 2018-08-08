package com.tokopedia.core.network.apiservices.chat;

import com.tokopedia.core.network.apiservices.chat.api.ChatApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.core.RetrofitFactory;
import com.tokopedia.core.network.retrofit.services.AuthService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;

import retrofit2.Retrofit;

/**
 * Created by stevenfredian on 8/31/17.
 */

public class ChatService extends AuthService<ChatApi> {

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(ChatApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.CHAT_DOMAIN;
    }

    @Override
    public ChatApi getApi() {
        return api;
    }

    @Override
    protected Retrofit createRetrofitInstance(String processedBaseUrl) {
        return RetrofitFactory.createRetrofitDefaultConfig(processedBaseUrl)
                .client(OkHttpFactory.create()
                        .addOkHttpRetryPolicy(getOkHttpRetryPolicy())
                        .buildClientTopChatAuth(AuthUtil.KEY.KEY_WSV4))
                .build();
    }
}
