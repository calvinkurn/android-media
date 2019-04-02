package com.tokopedia.core.gcm.notification.applink;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.gcm.BuildAndShowNotification;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.gcm.NotificationConfiguration;
import com.tokopedia.core.gcm.model.ApplinkNotificationPass;

/**
 * Created by Herdi_WORK on 29.03.17.
 */

public class PromoPushNotificationBuildAndShow extends AbstractApplinkBuildAndShowNotification {

    private static final String NOTIFICATION_GROUP = "general_group";
    private static final String NOTIFICATION_CATEGORY = "promo";
    private Bundle data;

    public PromoPushNotificationBuildAndShow(Bundle bundle) {
        data = bundle;
    }

    @Override
    public void process(Context context, Intent handlerIntent) {
        BuildAndShowNotification buildAndShowNotification = new BuildAndShowNotification(context);
        String title = data.getString(Constants.ARG_NOTIFICATION_TITLE);
        String description = data.getString(Constants.ARG_NOTIFICATION_DESCRIPTION);
        String banner = data.getString(Constants.ARG_NOTIFICATION_BANNER);
        String image = data.getString(Constants.ARG_NOTIFICATION_IMAGE, null);
        String applink = data.getString(Constants.ARG_NOTIFICATION_APPLINK, "");
        ApplinkNotificationPass.ApplinkNotificationPassBuilder builder =
                ApplinkNotificationPass.ApplinkNotificationPassBuilder.builder();
        NotificationConfiguration configuration = buildDefaultConfiguration(context);
        configuration.setNetworkIcon(false);
        Uri url = Uri.parse(applink);
        handlerIntent.setData(url);

        ApplinkNotificationPass applinkNotificationPass = builder
                .description(description)
                .image(image)
                .banner(banner)
                .id(Constants.ARG_NOTIFICATION_APPLINK_PROMO)
                .title(title)
                .group(NOTIFICATION_GROUP)
                .category(NOTIFICATION_CATEGORY)
                .intent(handlerIntent)
                .multipleSender(false)
                .build();
        buildAndShowNotification.buildAndShowNotification(applinkNotificationPass, configuration);
    }

    @Override
    public void process(Context context, Intent handlerIntent, boolean isNew) {

    }
}
