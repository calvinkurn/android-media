package com.tokopedia.core.gcm.di;

import com.tokopedia.core.gcm.FCMInstanceIDService;
import com.tokopedia.core.gcm.base.BaseNotificationMessagingService;
import com.tokopedia.fcmcommon.di.FcmComponent;

import dagger.Component;

@FcmServiceScope
@Component(dependencies = FcmComponent.class)
public interface FcmServiceComponent {
    void inject(BaseNotificationMessagingService baseNotificationMessagingService);
    void inject(FCMInstanceIDService fcmInstanceIDService);
}
