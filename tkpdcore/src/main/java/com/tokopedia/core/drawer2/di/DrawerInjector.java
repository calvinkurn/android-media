package com.tokopedia.core.drawer2.di;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.apollographql.apollo.ApolloClient;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.analytics.data.UserAttributesRepository;
import com.tokopedia.core.analytics.data.UserAttributesRepositoryImpl;
import com.tokopedia.core.analytics.data.factory.UserAttributesFactory;
import com.tokopedia.core.analytics.domain.usecase.GetSellerUserAttributesUseCase;
import com.tokopedia.core.analytics.domain.usecase.GetUserAttributesUseCase;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.drawer2.data.factory.DepositSourceFactory;
import com.tokopedia.core.drawer2.data.factory.NotificationSourceFactory;
import com.tokopedia.core.drawer2.data.factory.ProfileSourceFactory;
import com.tokopedia.core.drawer2.data.mapper.DepositMapper;
import com.tokopedia.core.drawer2.data.mapper.NotificationMapper;
import com.tokopedia.core.drawer2.data.mapper.ProfileMapper;
import com.tokopedia.core.drawer2.data.mapper.TopChatNotificationMapper;
import com.tokopedia.core.drawer2.data.pojo.topcash.TokoCashData;
import com.tokopedia.core.drawer2.data.repository.DepositRepositoryImpl;
import com.tokopedia.core.drawer2.data.repository.NotificationRepositoryImpl;
import com.tokopedia.core.drawer2.data.repository.ProfileRepositoryImpl;
import com.tokopedia.core.drawer2.data.source.TopChatNotificationSource;
import com.tokopedia.core.drawer2.domain.DepositRepository;
import com.tokopedia.core.drawer2.domain.NotificationRepository;
import com.tokopedia.core.drawer2.domain.ProfileRepository;
import com.tokopedia.core.drawer2.domain.datamanager.DrawerDataManager;
import com.tokopedia.core.drawer2.domain.datamanager.DrawerDataManagerImpl;
import com.tokopedia.core.drawer2.domain.interactor.DepositUseCase;
import com.tokopedia.core.drawer2.domain.interactor.NewNotificationUseCase;
import com.tokopedia.core.drawer2.domain.interactor.NotificationUseCase;
import com.tokopedia.core.drawer2.domain.interactor.ProfileUseCase;
import com.tokopedia.core.drawer2.domain.interactor.TokoCashUseCase;
import com.tokopedia.core.drawer2.domain.interactor.TopChatNotificationUseCase;
import com.tokopedia.core.drawer2.view.DrawerDataListener;
import com.tokopedia.core.drawer2.view.DrawerHelper;
import com.tokopedia.core.network.apiservices.chat.ChatService;
import com.tokopedia.core.network.apiservices.user.NotificationService;
import com.tokopedia.core.network.apiservices.user.PeopleService;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.util.SessionHandler;

import rx.Observable;

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
        JobExecutor jobExecutor = new JobExecutor();
        PostExecutionThread uiThread = new UIThread();

        ProfileSourceFactory profileSourceFactory = new ProfileSourceFactory(
                context,
                new PeopleService(),
                new ProfileMapper(),
                profileCache,
                sessionHandler
        );

        UserAttributesRepository userAttributesRepository = new UserAttributesRepositoryImpl(
                new UserAttributesFactory(
                        ApolloClient.builder()
                                .okHttpClient(OkHttpFactory.create().buildClientDefaultAuth())
                                .serverUrl(TkpdBaseURL.HOME_DATA_BASE_URL)
                                .build())
        );

        GetUserAttributesUseCase getUserAttributesUseCase = new GetUserAttributesUseCase(jobExecutor,
                new UIThread(), userAttributesRepository);

        GetSellerUserAttributesUseCase getSellerrAttributesUseCase = new GetSellerUserAttributesUseCase(jobExecutor,
                new UIThread(), userAttributesRepository);

        Observable<TokoCashData> tokoCashModelObservable = ((TkpdCoreRouter) context.getApplicationContext()).getTokoCashBalance();
        TokoCashUseCase tokoCashUseCase = new TokoCashUseCase(
                jobExecutor,
                uiThread,
                tokoCashModelObservable
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
                jobExecutor,
                uiThread,
                notificationRepository
        );

        TopChatNotificationUseCase topChatNotificationUseCase = new TopChatNotificationUseCase(
                jobExecutor,
                uiThread,
                notificationRepository
        );

        NewNotificationUseCase newNotificationUseCase = new NewNotificationUseCase(
                jobExecutor,
                uiThread,
                notificationUseCase,
                topChatNotificationUseCase
        );

        return new DrawerDataManagerImpl(
                drawerDataListener,
                newNotificationUseCase,
                tokoCashUseCase,
                getUserAttributesUseCase,
                getSellerrAttributesUseCase);
    }


}
