package com.tokopedia.topchat.common.di;


import android.content.Context;

import com.google.gson.Gson;
import com.readystatesoftware.chuck.ChuckInterceptor;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.OkHttpRetryPolicy;
import com.tokopedia.abstraction.common.network.converter.TokopediaWsV4ResponseConverter;
import com.tokopedia.abstraction.common.network.exception.HeaderErrorListResponse;
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor;
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.broadcast.message.common.domain.interactor.GetChatBlastSellerMetaDataUseCase;
import com.tokopedia.cacheapi.interceptor.CacheApiInterceptor;
import com.tokopedia.chat_common.network.ChatUrl;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.constant.TkpdBaseURL;
import com.tokopedia.network.converter.StringResponseConverter;
import com.tokopedia.network.interceptor.FingerprintInterceptor;
import com.tokopedia.shop.common.data.repository.ShopCommonRepositoryImpl;
import com.tokopedia.shop.common.data.source.ShopCommonDataSource;
import com.tokopedia.shop.common.data.source.cloud.ShopCommonCloudDataSource;
import com.tokopedia.shop.common.data.source.cloud.api.ShopCommonApi;
import com.tokopedia.shop.common.data.source.cloud.api.ShopCommonWSApi;
import com.tokopedia.shop.common.domain.interactor.GetShopInfoUseCase;
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase;
import com.tokopedia.shop.common.domain.repository.ShopCommonRepository;
import com.tokopedia.topchat.chatlist.data.factory.MessageFactory;
import com.tokopedia.topchat.chatlist.data.factory.SearchFactory;
import com.tokopedia.topchat.chatlist.data.mapper.DeleteMessageMapper;
import com.tokopedia.topchat.chatlist.data.mapper.GetMessageMapper;
import com.tokopedia.topchat.chatlist.data.mapper.SearchChatMapper;
import com.tokopedia.topchat.chatlist.data.repository.MessageRepository;
import com.tokopedia.topchat.chatlist.data.repository.MessageRepositoryImpl;
import com.tokopedia.topchat.chatlist.data.repository.SearchRepository;
import com.tokopedia.topchat.chatlist.data.repository.SearchRepositoryImpl;
import com.tokopedia.topchat.chatroom.view.listener.ChatSettingsInterface;
import com.tokopedia.topchat.chatroom.view.presenter.ChatSettingsPresenter;
import com.tokopedia.topchat.chattemplate.data.factory.TemplateChatFactory;
import com.tokopedia.topchat.chattemplate.data.mapper.TemplateChatMapper;
import com.tokopedia.topchat.chattemplate.data.repository.TemplateRepository;
import com.tokopedia.topchat.chattemplate.data.repository.TemplateRepositoryImpl;
import com.tokopedia.topchat.common.analytics.ChatSettingsAnalytics;
import com.tokopedia.topchat.common.chat.api.ChatApi;
import com.tokopedia.topchat.common.di.qualifier.InboxQualifier;
import com.tokopedia.topchat.common.di.qualifier.RetrofitTomeDomainQualifier;
import com.tokopedia.topchat.common.di.qualifier.RetrofitWsDomainQualifier;
import com.tokopedia.topchat.common.network.XUserIdInterceptor;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by stevenfredian on 9/14/17.
 */

@Module
public class InboxChatModule {

    private static final int NET_READ_TIMEOUT = 60;
    private static final int NET_WRITE_TIMEOUT = 60;
    private static final int NET_CONNECT_TIMEOUT = 60;
    private static final int NET_RETRY = 1;

    @InboxChatScope
    @Provides
    AnalyticTracker provideAnalyticTracker(@ApplicationContext Context context) {
        return ((AbstractionRouter) context).getAnalyticTracker();
    }

    @InboxChatScope
    @Provides
    ChuckInterceptor provideChuckInterceptor(@ApplicationContext Context context) {
        return new ChuckInterceptor(context);
    }

    @InboxChatScope
    @Provides
    MessageFactory provideMessageFactory(
            ChatApi chatApi,
            GetMessageMapper getMessageMapper,
            DeleteMessageMapper deleteMessageMapper) {
        return new MessageFactory(chatApi, getMessageMapper, deleteMessageMapper);
    }

    @InboxChatScope
    @Provides
    SearchFactory provideSearchFactory(
            ChatApi chatApi,
            SearchChatMapper searchChatMapper) {
        return new SearchFactory(chatApi, searchChatMapper);
    }


    @InboxChatScope
    @Provides
    TemplateChatFactory provideTemplateFactory(
            ChatApi chatApi,
            TemplateChatMapper templateChatMapper) {
        return new TemplateChatFactory(templateChatMapper, chatApi);
    }

    @InboxChatScope
    @Provides
    MessageRepository provideMessageRepository(MessageFactory messageFactory) {
        return new MessageRepositoryImpl(messageFactory);
    }

    @InboxChatScope
    @Provides
    SearchRepository provideSearchRepository(SearchFactory searchFactory) {
        return new SearchRepositoryImpl(searchFactory);
    }


    @InboxChatScope
    @Provides
    TemplateRepository provideTemplateRepository(TemplateChatFactory templateChatFactory) {
        return new TemplateRepositoryImpl(templateChatFactory);
    }

    //Add ShopInfo provides.. Change it to use component after shop_common Component & module are
    // Implemented
    @InboxChatScope
    @Provides
    public UserSessionInterface providesUserSessionAbstraction(@ApplicationContext Context context) {
        return new UserSession(context);
    }

    @InboxChatScope
    @Provides
    public ShopCommonWSApi provideShopCommonWsApi(@RetrofitWsDomainQualifier Retrofit retrofit) {
        return retrofit.create(ShopCommonWSApi.class);
    }

    @InboxChatScope
    @Provides
    public ShopCommonApi provideShopCommonApi(@RetrofitTomeDomainQualifier Retrofit retrofit) {
        return retrofit.create(ShopCommonApi.class);
    }


    @InboxChatScope
    @Provides
    public ShopCommonCloudDataSource provideShopCommonCloudDataSource(ShopCommonApi shopCommonApi,
                                                                      ShopCommonWSApi shopCommonWS4Api,
                                                                      com.tokopedia.abstraction.common.data.model.session.UserSession userSession) {
        return new ShopCommonCloudDataSource(shopCommonApi, shopCommonWS4Api, userSession);
    }

    @InboxChatScope
    @Provides
    public ShopCommonDataSource provideShopCommonDataSource(ShopCommonCloudDataSource shopInfoCloudDataSource) {
        return new ShopCommonDataSource(shopInfoCloudDataSource);
    }

    @InboxChatScope
    @Provides
    public ShopCommonRepository provideShopCommonRepository(ShopCommonDataSource shopInfoDataSource) {
        return new ShopCommonRepositoryImpl(shopInfoDataSource);
    }

    @InboxChatScope
    @Provides
    GetShopInfoUseCase provideGetShopInfoUseCase(ShopCommonRepository shopCommonRepository) {
        return new GetShopInfoUseCase(shopCommonRepository);
    }

//    @InboxChatScope
//    @Provides
//    ToggleFavouriteShopUseCase provideToggleFavouriteShopUseCase(ShopCommonRepository shopCommonRepository) {
//        return new ToggleFavouriteShopUseCase(shopCommonRepository);
//    }

//    @InboxChatScope
//    @Provides
//    GetChatShopInfoUseCase provideGetChatShopInfo(GetShopInfoUseCase getShopInfoUseCase) {
//        return new GetChatShopInfoUseCase(getShopInfoUseCase);
//    }

    @Provides
    public ErrorResponseInterceptor provideResponseInterceptor() {
        return new HeaderErrorResponseInterceptor(HeaderErrorListResponse.class);
    }

    @Provides
    public NetworkRouter provideNetworkRouter(@ApplicationContext Context context) {
        return (NetworkRouter) context;
    }


    @Provides
    public XUserIdInterceptor provideXUserIdInterceptor(@ApplicationContext Context context,
                                                        NetworkRouter networkRouter,
                                                        UserSession userSession) {
        return new XUserIdInterceptor(context, networkRouter, userSession);
    }

    @InboxChatScope
    @Provides
    OkHttpClient provideOkHttpClient(@ApplicationContext Context context,
                                     @InboxQualifier OkHttpRetryPolicy retryPolicy,
                                     ErrorResponseInterceptor errorResponseInterceptor,
                                     ChuckInterceptor chuckInterceptor,
                                     HttpLoggingInterceptor httpLoggingInterceptor,
                                     NetworkRouter networkRouter,
                                     UserSessionInterface userSessionInterface,
                                     XUserIdInterceptor xUserIdInterceptor) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(new FingerprintInterceptor(networkRouter, userSessionInterface))
                .addInterceptor(new CacheApiInterceptor())
                .addInterceptor(xUserIdInterceptor)
                .addInterceptor(errorResponseInterceptor)
                .connectTimeout(retryPolicy.connectTimeout, TimeUnit.SECONDS)
                .readTimeout(retryPolicy.readTimeout, TimeUnit.SECONDS)
                .writeTimeout(retryPolicy.writeTimeout, TimeUnit.SECONDS);

        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(chuckInterceptor)
                    .addInterceptor(httpLoggingInterceptor);
        }
        return builder.build();
    }

    @InboxChatScope
    @InboxQualifier
    @Provides
    OkHttpRetryPolicy provideOkHttpRetryPolicy() {
        return new OkHttpRetryPolicy(NET_READ_TIMEOUT,
                NET_WRITE_TIMEOUT,
                NET_CONNECT_TIMEOUT,
                NET_RETRY);
    }

    @InboxChatScope
    @InboxQualifier
    @Provides
    Retrofit provideChatRetrofit(OkHttpClient okHttpClient,
                                 Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(ChatUrl.Companion.getTOPCHAT())
                .client(okHttpClient)
                .build();
    }

    @InboxChatScope
    @RetrofitWsDomainQualifier
    @Provides
    Retrofit provideWsRetrofitDomain(OkHttpClient okHttpClient,
                                     Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(TkpdBaseURL.BASE_DOMAIN)
                .addConverterFactory(new TokopediaWsV4ResponseConverter())
                .addConverterFactory(new StringResponseConverter())
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    @InboxChatScope
    @RetrofitTomeDomainQualifier
    @Provides
    Retrofit provideTomeRetrofitDomain(OkHttpClient okHttpClient,
                                       Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(TkpdBaseURL.TOME_DOMAIN)
                .addConverterFactory(new TokopediaWsV4ResponseConverter())
                .addConverterFactory(new StringResponseConverter())
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    @InboxChatScope
    @Provides
    ChatApi provideChatApi(@InboxQualifier Retrofit retrofit) {
        return retrofit.create(ChatApi.class);
    }

    @InboxChatScope
    @Provides
    UserSession provideUserSession(@ApplicationContext Context context) {
        return new UserSession(context);
    }

    @InboxChatScope
    @Provides
    GraphqlUseCase provideGraphqlUseCase() {
        return new GraphqlUseCase();
    }

    @Provides
    GetChatBlastSellerMetaDataUseCase provideGetChatBlastSellerMetaDataUseCase(GraphqlUseCase graphqlUseCase,
                                                                               @ApplicationContext Context context) {
        return new GetChatBlastSellerMetaDataUseCase(graphqlUseCase, context);
    }

    @InboxChatScope
    @Provides
    ChatSettingsInterface.Presenter provideChatSettingsPresenter(GraphqlUseCase graphqlUseCase, ChatSettingsAnalytics chatSettingsAnalytics) {
        return new ChatSettingsPresenter(graphqlUseCase, chatSettingsAnalytics);
    }
}
