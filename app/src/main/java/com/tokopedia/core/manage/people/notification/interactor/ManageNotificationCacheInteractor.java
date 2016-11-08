package com.tokopedia.core.manage.people.notification.interactor;

import com.tokopedia.core.manage.people.notification.model.SettingNotification;

/**
 * Created by Nisie on 6/22/16.
 */
public interface ManageNotificationCacheInteractor {

    void getSettingNotificationCache(SettingNotificationCacheListener listener);

    void setSettingNotificationCache(SettingNotification result);

    interface SettingNotificationCacheListener {
        void onSuccess(SettingNotification result);

        void onError(Throwable e);
    }

}
