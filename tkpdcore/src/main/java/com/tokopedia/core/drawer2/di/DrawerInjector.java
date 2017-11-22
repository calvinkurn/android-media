package com.tokopedia.core.drawer2.di;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.apollographql.apollo.ApolloClient;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.analytics.data.UserAttributesRepository;
import com.tokopedia.core.analytics.data.UserAttributesRepositoryImpl;
import com.tokopedia.core.analytics.data.factory.UserAttributesFactory;
import com.tokopedia.core.analytics.domain.usecase.GetUserAttributesUseCase;
import com.tokopedia.core.analytics.handler.AnalyticsCacheHandler;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.drawer2.data.factory.DepositSourceFactory;
import com.tokopedia.core.drawer2.data.factory.NotificationSourceFactory;
import com.tokopedia.core.drawer2.data.factory.ProfileSourceFactory;
import com.tokopedia.core.drawer2.data.factory.TokoCashSourceFactory;
import com.tokopedia.core.drawer2.data.factory.TopPointsSourceFactory;
import com.tokopedia.core.drawer2.data.mapper.DepositMapper;
import com.tokopedia.core.drawer2.data.mapper.NotificationMapper;
import com.tokopedia.core.drawer2.data.mapper.ProfileMapper;
import com.tokopedia.core.drawer2.data.mapper.TokoCashMapper;
import com.tokopedia.core.drawer2.data.mapper.TopChatNotificationMapper;
import com.tokopedia.core.drawer2.data.mapper.TopPointsMapper;
import com.tokopedia.core.drawer2.data.repository.DepositRepositoryImpl;
import com.tokopedia.core.drawer2.data.repository.NotificationRepositoryImpl;
import com.tokopedia.core.drawer2.data.repository.ProfileRepositoryImpl;
import com.tokopedia.core.drawer2.data.repository.TokoCashRepositoryImpl;
import com.tokopedia.core.drawer2.data.repository.TopPointsRepositoryImpl;
import com.tokopedia.core.drawer2.data.source.TopChatNotificationSource;
import com.tokopedia.core.drawer2.domain.DepositRepository;
import com.tokopedia.core.drawer2.domain.NotificationRepository;
import com.tokopedia.core.drawer2.domain.ProfileRepository;
import com.tokopedia.core.drawer2.domain.TokoCashRepository;
import com.tokopedia.core.drawer2.domain.TopPointsRepository;
import com.tokopedia.core.drawer2.domain.datamanager.DrawerDataManager;
import com.tokopedia.core.drawer2.domain.datamanager.DrawerDataManagerImpl;
import com.tokopedia.core.drawer2.domain.interactor.DepositUseCase;
import com.tokopedia.core.drawer2.domain.interactor.NotificationUseCase;
import com.tokopedia.core.drawer2.domain.interactor.ProfileUseCase;
import com.tokopedia.core.drawer2.domain.interactor.TokoCashUseCase;
import com.tokopedia.core.drawer2.domain.interactor.TopChatNotificationUseCase;
import com.tokopedia.core.drawer2.domain.interactor.TopPointsUseCase;
import com.tokopedia.core.drawer2.view.DrawerDataListener;
import com.tokopedia.core.drawer2.view.DrawerHelper;
import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.network.apiservices.chat.ChatService;
import com.tokopedia.core.network.apiservices.clover.CloverService;
import com.tokopedia.core.network.apiservices.transaction.DepositService;
import com.tokopedia.core.network.apiservices.user.NotificationService;
import com.tokopedia.core.network.apiservices.user.PeopleService;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.util.SessionHandler;

/**
 * @author by nisie on 6/6/17.
 */

public class DrawerInjector {
    private static final String BEARER_TOKEN = "Bearer ";

    public static DrawerHelper getDrawerHelper(AppCompatActivity activity,
                                               SessionHandler sessionHandler,
                                               LocalCacheHandler drawerCache) {

        return ((TkpdCoreRouter) activity.getApplication()).getDrawer(activity,
                sessionHandler, drawerCache, new GlobalCacheManager());
    }

    public static DrawerDataManager getDrawerDataManager(Context context,
                                                         DrawerDataListener drawerDataListener,
                                                         SessionHandler sessionHandler,
                                                         LocalCacheHandler drawerCache) {

        GlobalCacheManager profileCache = new GlobalCacheManager();

        ProfileSourceFactory profileSourceFactory = new ProfileSourceFactory(
                context,
                new PeopleService(),
                new ProfileMapper(),
                profileCache,
                new AnalyticsCacheHandler(),
                sessionHandler
        );

        UserAttributesRepository userAttributesRepository = new UserAttributesRepositoryImpl(
                new UserAttributesFactory(
                        ApolloClient.builder()
                                .okHttpClient(OkHttpFactory.create().buildClientDefaultAuth())
                                .serverUrl(TkpdBaseURL.GRAPHQL_DOMAIN)
                                .build())
        );

        GetUserAttributesUseCase getUserAttributesUseCase = new GetUserAttributesUseCase(new JobExecutor(),
                new UIThread(), userAttributesRepository);

        ProfileRepository profileRepository = new ProfileRepositoryImpl(profileSourceFactory);

        ProfileUseCase profileUseCase = new ProfileUseCase(
                new JobExecutor(),
                new UIThread(),
                profileRepository
        );

        GlobalCacheManager topPointsCache = new GlobalCacheManager();

        TopPointsSourceFactory topPointsSourceFactory = new TopPointsSourceFactory(
                context,
                new CloverService(),
                new TopPointsMapper(),
                topPointsCache);

        TopPointsRepository topPointsRepository = new TopPointsRepositoryImpl(topPointsSourceFactory);

        TopPointsUseCase topPointsUseCase = new TopPointsUseCase(
                new JobExecutor(),
                new UIThread(),
                topPointsRepository
        );

        Bundle bundle = new Bundle();
        String authKey = sessionHandler.getAccessToken(context);
        authKey = BEARER_TOKEN + authKey;
        bundle.putString(AccountsService.AUTH_KEY, authKey);
        AccountsService accountsService = new AccountsService(bundle);

        GlobalCacheManager walletCache = new GlobalCacheManager();

        TokoCashSourceFactory tokoCashSourceFactory = new TokoCashSourceFactory(
                context,
                accountsService,
                new TokoCashMapper(),
                walletCache);

        TokoCashRepository tokoCashRepository = new TokoCashRepositoryImpl(tokoCashSourceFactory);
        TokoCashUseCase tokoCashUseCase = new TokoCashUseCase(
                new JobExecutor(),
                new UIThread(),
                tokoCashRepository
        );

        ChatService chatService = new ChatService();
        TopChatNotificationMapper topChatNotificationMapper = new TopChatNotificationMapper();

        TopChatNotificationSource topChatNotificationSource = new TopChatNotificationSource(
                chatService, topChatNotificationMapper, drawerCache
        );

        NotificationSourceFactory notificationSourceFactory = new NotificationSourceFactory(
                context,
                new NotificationService(),
                new NotificationMapper(),
                drawerCache
        );
        NotificationRepository notificationRepository = new NotificationRepositoryImpl
                (notificationSourceFactory, topChatNotificationSource);

        NotificationUseCase notificationUseCase = new NotificationUseCase(
                new JobExecutor(),
                new UIThread(),
                notificationRepository
        );

        DepositSourceFactory depositSourceFactory = new DepositSourceFactory(
                context,
                new DepositService(),
                new DepositMapper(),
                drawerCache);

        DepositRepository depositRepository = new DepositRepositoryImpl(depositSourceFactory);
        DepositUseCase depositUseCase = new DepositUseCase(
                new JobExecutor(),
                new UIThread(),
                depositRepository);

        TopChatNotificationUseCase topChatNotificationUseCase = new TopChatNotificationUseCase(
                new JobExecutor(),
                new UIThread(),
                notificationRepository
        );

        return new DrawerDataManagerImpl(
                drawerDataListener,
                profileUseCase,
                depositUseCase,
                notificationUseCase,
                tokoCashUseCase,
                topPointsUseCase,
                getUserAttributesUseCase,
                topChatNotificationUseCase);
    }


}
