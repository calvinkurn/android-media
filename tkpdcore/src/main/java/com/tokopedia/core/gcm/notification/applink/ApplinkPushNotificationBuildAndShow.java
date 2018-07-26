package com.tokopedia.core.gcm.notification.applink;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;

import com.tokopedia.core.R;
import com.tokopedia.core.gcm.BuildAndShowNotification;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.gcm.NotificationConfiguration;
import com.tokopedia.core.gcm.model.ApplinkNotificationPass;
import com.tokopedia.core.router.SellerAppRouter;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.util.GlobalConfig;

/**
 * Created by alvarisi on 5/22/17.
 */
@Deprecated
public class ApplinkPushNotificationBuildAndShow extends AbstractApplinkBuildAndShowNotification {

    private static final String NOTIFICATION_GROUP = "general_group";
    private static final String NOTIFICATION_CATEGORY = "applink";
    private Bundle data;

    public ApplinkPushNotificationBuildAndShow(Bundle bundle) {
        data = bundle;
    }

    @Override
    public void process(Context context, Intent handlerIntent) {
        BuildAndShowNotification buildAndShowNotification = new BuildAndShowNotification(context);
        String title = data.getString(Constants.ARG_NOTIFICATION_TITLE, context.getResources().getString(R.string.title_new_notif_general));
        String description = data.getString(Constants.ARG_NOTIFICATION_DESCRIPTION);
        String image = data.getString(Constants.ARG_NOTIFICATION_IMAGE, null);
        String applink = data.getString(Constants.ARG_NOTIFICATION_APPLINK, "");
        String banner = data.getString(Constants.ARG_NOTIFICATION_BANNER, "");
        ApplinkNotificationPass.ApplinkNotificationPassBuilder builder =
                ApplinkNotificationPass.ApplinkNotificationPassBuilder.builder();
        NotificationConfiguration configuration = buildDefaultConfiguration(context);
        configuration.setNetworkIcon(false);
        Uri url = Uri.parse(applink);
        handlerIntent.setData(url);
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.EXTRA_APPLINK_FROM_PUSH, true);
        handlerIntent.putExtras(bundle);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        Intent homeIntent = null;
        if (GlobalConfig.isSellerApp()) {
            homeIntent = SellerAppRouter.getSellerHomeActivity(context);
        } else {
            homeIntent = HomeRouter.getHomeActivity(context);
        }
        stackBuilder.addNextIntentWithParentStack(homeIntent);
        stackBuilder.addNextIntent(handlerIntent);

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
                .taskStackBuilder(stackBuilder)
                .build();
        buildAndShowNotification.buildAndShowApplinkNotification(applinkNotificationPass, data, configuration);
    }

    @Override
    public void process(Context context, Intent handlerIntent, boolean isNew) {

    }
}