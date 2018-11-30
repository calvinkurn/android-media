package com.tokopedia.notifications.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

import com.tokopedia.applink.RouteManager;
import com.tokopedia.notifications.common.CMConstant;

/**
 * @author lalit.singh
 */
public class CMBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        int notificationId = intent.getIntExtra(CMConstant.EXTRA_NOTIFICATION_ID, 0);
        if (null != action) {
            switch (action) {
                case CMConstant.ReceiverAction.ACTION_BUTTON:
                    String appLinks = intent.getStringExtra(CMConstant.ActionButtonExtra.ACTION_BUTTON_APP_LINK);
                    Intent appLinkIntent = RouteManager.getIntent(context.getApplicationContext(), appLinks);
                    context.startActivity(appLinkIntent);
                    NotificationManagerCompat.from(context.getApplicationContext()).cancel(notificationId);
                    Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
                    context.sendBroadcast(it);
                    break;
                case CMConstant.ReceiverAction.ACTION_CANCEL_PERSISTENT:
                    context.sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
                    NotificationManagerCompat.from(context).cancel(notificationId);
                    break;
                case CMConstant.ReceiverAction.ACTION_ON_NOTIFICATION_DISMISS:
                    NotificationManagerCompat.from(context).cancel(notificationId);
                    break;
                case CMConstant.ReceiverAction.ACTION_ON_COPY_COUPON_CODE:
                    //Notification copy coupon code Event
                    String coupon = intent.getStringExtra(CMConstant.CouponCodeExtra.COUPON_CODE);
                    appLinks = intent.getStringExtra(CMConstant.ActionButtonExtra.ACTION_BUTTON_APP_LINK);
                    appLinkIntent = RouteManager.getIntent(context.getApplicationContext(), appLinks);
                    context.startActivity(appLinkIntent);
                    copyToClipboard(context, coupon);
                    NotificationManagerCompat.from(context.getApplicationContext()).cancel(notificationId);
                    break;
            }
        }
    }

    private void copyToClipboard(Context context, String contents) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Activity.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Tokopedia", contents);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(context, "Coupon code copied " + contents, Toast.LENGTH_LONG).show();
    }

}
