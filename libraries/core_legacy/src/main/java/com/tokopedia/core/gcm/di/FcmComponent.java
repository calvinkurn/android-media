package com.tokopedia.core.gcm.di;

import com.tokopedia.core.gcm.base.BaseNotificationMessagingService;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = FcmModule.class)
public interface FcmComponent {
    void inject(BaseNotificationMessagingService messagingService);
}
