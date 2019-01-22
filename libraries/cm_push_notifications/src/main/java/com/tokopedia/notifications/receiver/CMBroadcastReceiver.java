package com.tokopedia.notifications.receiver;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationManagerCompat;
import android.text.TextUtils;
import android.widget.Toast;

import com.tokopedia.applink.RouteManager;
import com.tokopedia.notifications.R;
import com.tokopedia.notifications.common.CMConstant;
import com.tokopedia.notifications.common.CMEvents;
import com.tokopedia.notifications.common.CarousalUtilities;
import com.tokopedia.notifications.common.CmEventPost;
import com.tokopedia.notifications.factory.BaseNotification;
import com.tokopedia.notifications.factory.CMNotificationFactory;
import com.tokopedia.notifications.model.Carousal;
import com.tokopedia.notifications.model.PersistentButton;

import java.util.List;

/**
 * @author lalit.singh
 */
public class CMBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = CMBroadcastReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (!intent.hasExtra(CMConstant.EXTRA_NOTIFICATION_ID))
            return;
        int notificationId = intent.getIntExtra(CMConstant.EXTRA_NOTIFICATION_ID, 0);
        if (null != action) {
            switch (action) {
                case CMConstant.ReceiverAction.ACTION_BUTTON:
                    handleActionButtonClick(context, intent, notificationId);
                    break;
                case CMConstant.ReceiverAction.ACTION_PERSISTENT_CLICK:
                    handlePersistentClick(context, intent);
                    break;
                case CMConstant.ReceiverAction.ACTION_CANCEL_PERSISTENT:
                    cancelPersistentNotification(context, notificationId);
                    break;
                case CMConstant.ReceiverAction.ACTION_ON_NOTIFICATION_DISMISS:
                    NotificationManagerCompat.from(context).cancel(notificationId);
                    break;
                case CMConstant.ReceiverAction.ACTION_NOTIFICATION_CLICK:
                    handleNotificationClick(context, intent, notificationId);
                    break;
                case CMConstant.ReceiverAction.ACTION_RIGHT_ARROW_CLICK:
                    handleCarousalButtonClick(context, intent, notificationId, true);
                    break;
                case CMConstant.ReceiverAction.ACTION_LEFT_ARROW_CLICK:
                    handleCarousalButtonClick(context, intent, notificationId, false);
                    break;
                case CMConstant.ReceiverAction.ACTION_CAROUSAL_IMAGE_CLICK:
                    handleCarousalImageClick(context, intent, notificationId);
                    break;
                case CMConstant.ReceiverAction.ACTION_GRID_CLICK:
                    handleGridNotificationClick(context, intent, notificationId);
                    break;
            }
        }
    }

    private void handleGridNotificationClick(Context context, Intent intent, int notificationId) {
        String appLinks = intent.getStringExtra(CMConstant.ReceiverExtraData.ACTION_APP_LINK);
        Intent appLinkIntent = RouteManager.getIntent(context.getApplicationContext(), appLinks);
        appLinkIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        appLinkIntent.putExtras(intent.getExtras());
        context.getApplicationContext().startActivity(appLinkIntent);
        context.getApplicationContext().sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
        NotificationManagerCompat.from(context).cancel(notificationId);
    }

    private void cancelPersistentNotification(Context context, int notificationId) {
        CmEventPost.postEvent(context, CMEvents.PersistentEvent.EVENT, CMEvents.PersistentEvent.EVENT_CATEGORY,
                CMEvents.PersistentEvent.EVENT_ACTION_CANCELED, CMEvents.PersistentEvent.EVENT_LABEL);
        context.sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
        NotificationManagerCompat.from(context).cancel(notificationId);
    }

    private void handlePersistentClick(Context context, Intent intent) {
        if (intent.hasExtra(CMConstant.ReceiverExtraData.PERSISTENT_BUTTON_DATA)) {
            PersistentButton persistentButton = intent.getParcelableExtra(CMConstant.ReceiverExtraData.PERSISTENT_BUTTON_DATA);
            if (null == persistentButton)
                return;
            if (persistentButton.isAppLogo()) {
                CmEventPost.postEvent(context, CMEvents.PersistentEvent.EVENT, CMEvents.PersistentEvent.EVENT_CATEGORY,
                        CMEvents.PersistentEvent.EVENT_ACTION_LOGO_CLICK, persistentButton.getAppLink());
            } else {
                CmEventPost.postEvent(context, CMEvents.PersistentEvent.EVENT, CMEvents.PersistentEvent.EVENT_CATEGORY,
                        persistentButton.getText(), persistentButton.getAppLink());
            }

            Intent appLinkIntent = RouteManager.getIntent(context.getApplicationContext(), persistentButton.getAppLink());
            appLinkIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            appLinkIntent.putExtras(intent.getExtras());
            context.startActivity(appLinkIntent);
        }
        context.sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
    }

    private void copyToClipboard(Context context, String contents) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Activity.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Tokopedia", contents);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(context, context.getString(R.string.tv_coupon_code_copied) + contents, Toast.LENGTH_LONG).show();
    }

    private void handleNotificationClick(Context context, Intent intent, int notificationId) {

        String appLinks = intent.getStringExtra(CMConstant.ReceiverExtraData.ACTION_APP_LINK);
        Intent appLinkIntent = RouteManager.getIntent(context.getApplicationContext(), appLinks);
        appLinkIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        appLinkIntent.putExtras(intent.getExtras());
        context.startActivity(appLinkIntent);
        NotificationManagerCompat.from(context.getApplicationContext()).cancel(notificationId);
        context.sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
        if (intent.hasExtra(CMConstant.CouponCodeExtra.COUPON_CODE)) {
            String coupon = intent.getStringExtra(CMConstant.CouponCodeExtra.COUPON_CODE);
            if (!TextUtils.isEmpty(coupon))
                copyToClipboard(context, coupon);
        }
    }

    private void handleActionButtonClick(Context context, Intent intent, int notificationId) {
        if (!intent.hasExtra(CMConstant.ReceiverExtraData.ACTION_BUTTON_APP_LINK))
            return;
        String appLinks = intent.getStringExtra(CMConstant.ReceiverExtraData.ACTION_BUTTON_APP_LINK);
        Intent appLinkIntent = RouteManager.getIntent(context.getApplicationContext(), appLinks);
        appLinkIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        appLinkIntent.putExtras(intent.getExtras());
        context.startActivity(appLinkIntent);
        NotificationManagerCompat.from(context.getApplicationContext()).cancel(notificationId);
        context.sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
    }

    private void handleCarousalButtonClick(Context context, Intent intent, int notificationId, boolean isNext) {
        try {
            List<Carousal> carousalList = intent.getParcelableArrayListExtra(CMConstant.ReceiverExtraData.CAROUSAL_DATA);
            int index = intent.getIntExtra(CMConstant.PayloadKeys.CAROUSAL_INDEX, 0);
            if (null == carousalList || carousalList.size() == 0)
                return;
            index = isNext ? index + 1 : index - 1;
            Bundle bundle = intent.getExtras();
            bundle.putString(CMConstant.PayloadKeys.NOTIFICATION_ID, String.valueOf(notificationId));
            bundle.putInt(CMConstant.PayloadKeys.CAROUSAL_INDEX, index);
            bundle.putString(CMConstant.PayloadKeys.NOTIFICATION_TYPE, CMConstant.NotificationType.CAROUSAL_NOTIFICATION);
            BaseNotification baseNotification = CMNotificationFactory.getNotification(context.getApplicationContext(), bundle);
            postNotification(context, baseNotification);
        } catch (Exception e) {
        }

    }

    private void handleCarousalImageClick(Context context, Intent intent, int notificationId) {
        try {
            Carousal carousal = intent.getParcelableExtra(CMConstant.ReceiverExtraData.CAROUSAL_DATA_ITEM);
            Intent appLinkIntent = RouteManager.getIntent(context.getApplicationContext(), carousal.getAppLink());
            appLinkIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            appLinkIntent.putExtras(intent.getExtras());
            context.startActivity(appLinkIntent);
            NotificationManagerCompat.from(context.getApplicationContext()).cancel(notificationId);
            context.sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
            CarousalUtilities.deleteCarousalImageDirectory(context);
        } catch (Exception e) {
        }

    }

    private void postNotification(Context context, BaseNotification baseNotification) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = baseNotification.createNotification();
        if (null != notificationManager)
            notificationManager.notify(baseNotification.baseNotificationModel.getNotificationId(), notification);
    }

}
