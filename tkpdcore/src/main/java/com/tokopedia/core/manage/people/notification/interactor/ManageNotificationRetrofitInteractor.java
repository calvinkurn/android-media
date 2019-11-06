package com.tokopedia.core.manage.people.notification.interactor;

import android.content.Context;
import androidx.annotation.NonNull;

import com.tokopedia.core.manage.people.notification.model.SettingNotification;

import java.util.Map;

/**
 * Created by Nisie on 6/22/16.
 */
public interface ManageNotificationRetrofitInteractor {

    void getNotificationSetting(@NonNull Context context, @NonNull Map<String, String> params,
                         @NonNull NotificationSettingListener listener);

    void setNotificationSetting(@NonNull Context context, @NonNull Map<String, String> params,
                                @NonNull SetNotificationSettingListener listener);

    interface NotificationSettingListener {

        void onSuccess(@NonNull SettingNotification result);

        void onTimeout();

        void onError(String error);

        void onNullData();

        void onNoNetworkConnection();
    }

    interface SetNotificationSettingListener {

        void onSuccess(String status);

        void onTimeout();

        void onError(String error);

        void onNullData();

        void onNoNetworkConnection();
    }

    void unsubscribe();

}
