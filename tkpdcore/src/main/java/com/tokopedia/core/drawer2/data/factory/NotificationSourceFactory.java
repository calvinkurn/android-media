package com.tokopedia.core.drawer2.data.factory;

import android.content.Context;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.drawer2.data.mapper.NotificationMapper;
import com.tokopedia.core.drawer2.data.source.CloudNotificationSource;
import com.tokopedia.core.network.apiservices.user.NotificationService;

import javax.inject.Inject;

/**
 * Created by nisie on 5/5/17.
 */

public class NotificationSourceFactory {

    private final Context context;
    private NotificationService notificationService;
    private NotificationMapper notificationMapper;
    private LocalCacheHandler drawerCache;

    @Inject
    public NotificationSourceFactory(@ApplicationContext Context context,
                                NotificationService notificationService,
                                NotificationMapper notificationMapper,
                                LocalCacheHandler drawerCache) {
        this.context = context;
        this.notificationService = notificationService;
        this.notificationMapper = notificationMapper;
        this.drawerCache = drawerCache;
    }

    public CloudNotificationSource createCloudNotificationSource() {
        return new CloudNotificationSource(context, notificationService,
                notificationMapper, drawerCache);
    }
}
