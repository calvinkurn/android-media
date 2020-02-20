package com.tokopedia.core.drawer2.di;

import android.content.Context;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.drawer2.data.factory.NotificationSourceFactory;
import com.tokopedia.core.drawer2.data.mapper.TopChatNotificationMapper;
import com.tokopedia.core.drawer2.data.repository.NotificationRepositoryImpl;
import com.tokopedia.core.drawer2.data.source.TopChatNotificationSource;
import com.tokopedia.core.drawer2.domain.NotificationRepository;
import com.tokopedia.core.drawer2.domain.interactor.DeleteNotificationCacheUseCase;
import com.tokopedia.core.drawer2.domain.interactor.GetChatNotificationUseCase;
import com.tokopedia.core.drawer2.domain.interactor.GetInfoPenjualNotificationUseCase;
import com.tokopedia.core.drawer2.domain.interactor.NewNotificationUseCase;
import com.tokopedia.core.drawer2.domain.interactor.NotificationUseCase;
import com.tokopedia.core.drawer2.view.DrawerHelper;
import com.tokopedia.core.network.apiservices.chat.ChatService;
import com.tokopedia.core2.R;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

import static com.tokopedia.core.drawer2.domain.GqlQueryConstant.GET_CHAT_NOTIFICATION_QUERY;
import static com.tokopedia.core.drawer2.domain.GqlQueryConstant.GET_INFO_PENJUAL_NOTIFICATION_QUERY;

@DrawerScope
@Module
public class DrawerModule {

    @DrawerScope
    @Provides
    ChatService provideChatService() {
        return new ChatService();
    }

    @DrawerScope
    @Provides
    TopChatNotificationMapper provideTopChatNotificationMapper() {
        return new TopChatNotificationMapper();
    }

    @DrawerScope
    @Provides
    TopChatNotificationSource provideTopChatNotificationSource(ChatService chatService,
                                                               TopChatNotificationMapper
                                                                       topChatNotificationMapper,
                                                               LocalCacheHandler drawerCache) {
        return new TopChatNotificationSource(chatService, topChatNotificationMapper, drawerCache);
    }

    @DrawerScope
    @Provides
    NotificationRepository provideNotificationRepository(NotificationSourceFactory
                                                                 notificationSourceFactory,
                                                         TopChatNotificationSource topChatNotificationSource) {
        return new NotificationRepositoryImpl(notificationSourceFactory, topChatNotificationSource);
    }

    @DrawerScope
    @Provides
    LocalCacheHandler provideLocalCacheHandler(@ApplicationContext Context context) {
        return new LocalCacheHandler(context, DrawerHelper.DRAWER_CACHE);
    }

    @DrawerScope
    @Provides
    NewNotificationUseCase provideNewNotificationUseCase(ThreadExecutor threadExecutor,
                                                         PostExecutionThread postExecutionThread,
                                                         NotificationUseCase
                                                                 notificationUseCase,
                                                         DeleteNotificationCacheUseCase
                                                                 deleteNotificationCacheUseCase,
                                                         GetChatNotificationUseCase
                                                                 getChatNotificationUseCase,
                                                         GetInfoPenjualNotificationUseCase getInfoPenjualNotificationUseCase,
                                                         LocalCacheHandler drawerCache) {
        return new NewNotificationUseCase(
                threadExecutor,
                postExecutionThread,
                notificationUseCase,
                deleteNotificationCacheUseCase,
                getChatNotificationUseCase,
                getInfoPenjualNotificationUseCase,
                drawerCache
        );
    }

    @DrawerScope
    @Provides
    @Named(GET_CHAT_NOTIFICATION_QUERY)
    String provideGetChatNotificationQuery(@ApplicationContext Context context){
        return GraphqlHelper.loadRawString(context.getResources(), R.raw.query_get_chat_notification);
    }

    @DrawerScope
    @Provides
    @Named(GET_INFO_PENJUAL_NOTIFICATION_QUERY)
    String provideGET_INFO_PENJUAL_NOTIFICATION_QUERY(@ApplicationContext Context context){
        return GraphqlHelper.loadRawString(context.getResources(), R.raw.query_notification_center_total_unread);
    }
}
