package com.tokopedia.home.account.presentation.view;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.home.account.data.model.AppNotificationSettingModel;
import com.tokopedia.home.account.data.model.SettingEditResponse;

public interface EmailNotificationView extends CustomerView {
    void onErrorGetNotifSetting(Throwable throwable);
    void setupNotifSetting(AppNotificationSettingModel notification);

    void onErrorSaveNotifSetting(Throwable throwable);
    void onSuccesSaveSetting(SettingEditResponse data);
}
