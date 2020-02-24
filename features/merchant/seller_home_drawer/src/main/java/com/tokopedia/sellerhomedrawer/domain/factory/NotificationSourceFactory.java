package com.tokopedia.sellerhomedrawer.domain.factory;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.sellerhomedrawer.domain.datasource.CloudNotificationSource;
import com.tokopedia.sellerhomedrawer.domain.mapper.NotificationMapper;
import com.tokopedia.sellerhomedrawer.domain.service.NotificationService;
import com.tokopedia.user.session.UserSession;

import javax.inject.Inject;

public class NotificationSourceFactory {

    private final Context context;
    private NotificationService notificationService;
    private NotificationMapper notificationMapper;
    private LocalCacheHandler drawerCache;
    private UserSession userSession;
    private CloudNotificationSource cloudNotificationSource;

    @Inject
    public NotificationSourceFactory(@ApplicationContext Context context,
                                     NotificationService notificationService,
                                     NotificationMapper notificationMapper,
                                     LocalCacheHandler drawerCache,
                                     UserSession userSession,
                                     CloudNotificationSource cloudNotificationSource) {
        this.context = context;
        this.notificationService = notificationService;
        this.notificationMapper = notificationMapper;
        this.drawerCache = drawerCache;
        this.userSession = userSession;
        this.cloudNotificationSource = cloudNotificationSource;
    }

    public CloudNotificationSource getCloudNotificationSource() {
        return cloudNotificationSource;
    }
}
