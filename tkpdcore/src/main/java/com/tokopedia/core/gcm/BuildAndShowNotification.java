package com.tokopedia.core.gcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.NotificationCenter;
import com.tokopedia.core.R;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.gcm.data.entity.NotificationEntity;
import com.tokopedia.core.gcm.model.ApplinkNotificationPass;
import com.tokopedia.core.gcm.model.NotificationPass;
import com.tokopedia.core.gcm.utils.GCMUtils;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.var.TkpdState;

import java.io.File;
import java.util.List;

import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_DESCRIPTION;
import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_ICON;
import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_IMAGE;
import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_TITLE;
import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_UPDATE_APPS_TITLE;
import static com.tokopedia.core.gcm.Constants.EXTRA_PLAYSTORE_URL;

/**
 * @author by alvarisi on 1/11/17.
 */

public class BuildAndShowNotification {
    private final Context mContext;
    private FCMCacheManager cacheManager;

    public BuildAndShowNotification(Context context) {
        mContext = context;
        cacheManager = new FCMCacheManager(context);
    }

    public interface OnGetFileListener {
        void onFileReady(File file);
    }

    public void buildAndShowNotification(ApplinkNotificationPass applinkNotificationPass) {
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle(applinkNotificationPass.getTitle());
        inboxStyle.setSummaryText(applinkNotificationPass.getDescription());
        if (applinkNotificationPass.getContents() != null) {
            for (String content : applinkNotificationPass.getContents()) {
                inboxStyle.addLine(content);
            }
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext)
                .setSmallIcon(getDrawableIcon())
                .setAutoCancel(true);

        mBuilder.setContentTitle(applinkNotificationPass.getTitle());
        mBuilder.setContentText(applinkNotificationPass.getDescription());
        mBuilder.setStyle(inboxStyle);
        mBuilder.setTicker(applinkNotificationPass.getDescription());
        mBuilder.setContentInfo(applinkNotificationPass.getInfo());
        mBuilder.setCategory(applinkNotificationPass.getCategory());
        mBuilder.setGroup(applinkNotificationPass.getGroup());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mBuilder.setColor(mContext.getColor(R.color.tkpd_dark_green));
        } else {
            mBuilder.setColor(mContext.getResources().getColor(R.color.tkpd_dark_green));
        }

        if (isAllowBell()) {
            mBuilder.setSound(getSoundUri());
            if (isAllowedVibrate()) {
                mBuilder.setVibrate(getVibratePattern());
            }
        }
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
        stackBuilder.addNextIntent(applinkNotificationPass.getIntent());
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_CANCEL_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        downloadImageAndShowNotification(applinkNotificationPass, mBuilder);
    }

    private void downloadImageAndShowNotification(final ApplinkNotificationPass applinkNotificationPass,
                                                  final NotificationCompat.Builder mBuilder) {
        Glide
                .with(mBuilder.mContext)
                .load(applinkNotificationPass.getImageUrl())
                .asBitmap()
                .into(new SimpleTarget<Bitmap>(100, 100) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                        mBuilder.setLargeIcon(
                                ImageHandler.getRoundedCornerBitmap(resource, 100)
                        );

                        NotificationManager mNotificationManager =
                                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
                        Notification notif = mBuilder.build();
                        if (isAllowedVibrate() && isAllowBell()) {
                            notif.defaults |= Notification.DEFAULT_VIBRATE;
                        }
                        mNotificationManager.notify(applinkNotificationPass.getNotificationId(), notif);
                    }
                });

    }

    public void buildAndShowNotification(NotificationPass notificationPass, Bundle data, Intent intent) {
        NotificationManager mNotificationManager =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext)
                .setSmallIcon(getDrawableIcon())
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), getDrawableLargeIcon()))
                .setAutoCancel(true);
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        NotificationCompat.BigTextStyle bigStyle = new NotificationCompat.BigTextStyle();

        if (notificationPass.isAllowedBigStyle) {
            mBuilder.setContentTitle(notificationPass.title);
            mBuilder.setContentText(notificationPass.description);
            bigStyle.bigText(notificationPass.description);
            mBuilder.setStyle(bigStyle);
            mBuilder.setTicker(notificationPass.ticker);
        } else {
            inboxStyle.setBigContentTitle(mContext.getString(R.string.title_new_notif_general));
            if (notificationPass.savedNotificationContents != null) {
                for (String content : notificationPass.savedNotificationContents) {
                    inboxStyle.addLine(content);
                }
            }
            mBuilder.setContentTitle(mContext.getString(R.string.title_new_notif_general));
            mBuilder.setContentText(notificationPass.description);
            mBuilder.setStyle(inboxStyle);
            mBuilder.setTicker(notificationPass.ticker);
        }

        mBuilder = configureBigPictureNotification(data, mBuilder);

        mBuilder = configureIconNotification(data, mBuilder);

        if (isAllowBell()) {
            mBuilder.setSound(getSoundUri());
            if (isAllowedVibrate()) {
                mBuilder.setVibrate(getVibratePattern());
            }
        }
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
        if (notificationPass.classParentStack != null) {
            stackBuilder.addParentStack(notificationPass.classParentStack);
        } else {
            stackBuilder.addParentStack(NotificationCenter.class);
        }

        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_CANCEL_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        Notification notif = mBuilder.build();
        if (isAllowedVibrate() && isAllowBell()) {
            notif.defaults |= Notification.DEFAULT_VIBRATE;
        }
        mNotificationManager.notify(100, notif);
    }

    public void buildAndShowNotification(NotificationPass notificationPass, Bundle data) {
        //TODO : create flow again
        saveIncomingNotification(notificationPass, data);

        NotificationManager mNotificationManager =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext)
                .setSmallIcon(getDrawableIcon())
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), getDrawableLargeIcon()))
                .setAutoCancel(true);
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        NotificationCompat.BigTextStyle bigStyle = new NotificationCompat.BigTextStyle();

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);

        if (notificationPass.isAllowedBigStyle || isSingleNotification()) {
            mBuilder.setContentTitle(notificationPass.title);
            mBuilder.setContentText(notificationPass.description);
            bigStyle.bigText(notificationPass.description);
            mBuilder.setStyle(bigStyle);
            mBuilder.setTicker(notificationPass.ticker);
            if (notificationPass.classParentStack != null) {
                stackBuilder.addParentStack(notificationPass.classParentStack);
            }
        } else {
            notificationPass.setIntent(new Intent(mContext, NotificationCenter.class));
            stackBuilder.addParentStack(NotificationCenter.class);
            inboxStyle.setBigContentTitle(mContext.getString(R.string.title_new_notif_general));
            if (getUnOpenedNotification() != null) {
                for (NotificationEntity entity : getUnOpenedNotification()) {
                    inboxStyle.addLine(entity.getTitle());
                }
            }
            mBuilder.setContentTitle(mContext.getString(R.string.title_new_notif_general));
            mBuilder.setContentText(notificationPass.description);
            mBuilder.setStyle(inboxStyle);
            mBuilder.setTicker(notificationPass.ticker);
        }

        mBuilder = configureBigPictureNotification(data, mBuilder);

        mBuilder = configureIconNotification(data, mBuilder);

        if (isAllowBell()) {
            mBuilder.setSound(getSoundUri());
            if (isAllowedVibrate()) {
                mBuilder.setVibrate(getVibratePattern());
            }
        }

        stackBuilder.addNextIntent(notificationPass.getIntent());
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_CANCEL_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        Notification notif = mBuilder.build();
        if (isAllowedVibrate() && isAllowBell()) {
            notif.defaults |= Notification.DEFAULT_VIBRATE;
        }
        mNotificationManager.notify(100, notif);
    }

    private void saveIncomingNotification(NotificationPass notificationPass, Bundle data) {
        NotificationEntity notificationEntity = new NotificationEntity();
        notificationEntity.setCode(String.valueOf(GCMUtils.getCode(data)));
        notificationEntity.setTitle(notificationPass.title);
        cacheManager.saveIncomingNotification(notificationEntity);
    }

    private boolean isSingleNotification() {
        return cacheManager.getHistoryPushNotification().size() <= 1;
    }

    private List<NotificationEntity> getUnOpenedNotification() {
        return cacheManager.getHistoryPushNotification();
    }

    private NotificationCompat.Builder configureIconNotification(String iconUrl, final NotificationCompat.Builder mBuilder) {
        Glide
                .with(mBuilder.mContext)
                .load(iconUrl)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>(48, 48) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                        mBuilder.setLargeIcon(
                                Bitmap.createScaledBitmap(
                                        resource,
                                        mContext.getResources().getDimensionPixelSize(R.dimen.icon_size),
                                        mContext.getResources().getDimensionPixelSize(R.dimen.icon_size),
                                        true
                                )
                        );
                    }
                });/*
        ImageHandler.loadImageBitmapNotification(
                mBuilder.mContext,
                iconUrl,
                new OnGetFileListener() {
                    @Override
                    public void onFileReady(File file) {
                        mBuilder.setLargeIcon(
                                Bitmap.createScaledBitmap(
                                        BitmapFactory.decodeFile(file.getAbsolutePath()),
                                        mContext.getResources().getDimensionPixelSize(R.dimen.icon_size),
                                        mContext.getResources().getDimensionPixelSize(R.dimen.icon_size),
                                        true
                                )
                        );
                    }
                }
        );*/
        return mBuilder;
    }

    private NotificationCompat.Builder configureIconNotification(Bundle data, final NotificationCompat.Builder mBuilder) {
        if (!TextUtils.isEmpty(data.getString(ARG_NOTIFICATION_ICON))) {
            ImageHandler.loadImageBitmapNotification(
                    mContext,
                    data.getString(ARG_NOTIFICATION_ICON), new OnGetFileListener() {
                        @Override
                        public void onFileReady(File file) {
                            mBuilder.setLargeIcon(
                                    Bitmap.createScaledBitmap(
                                            BitmapFactory.decodeFile(file.getAbsolutePath()),
                                            mContext.getResources().getDimensionPixelSize(R.dimen.icon_size),
                                            mContext.getResources().getDimensionPixelSize(R.dimen.icon_size),
                                            true
                                    )
                            );
                        }
                    }
            );
        } else {
            mBuilder.setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), getDrawableLargeIcon()));
        }
        return mBuilder;
    }

    private NotificationCompat.Builder configureBigPictureNotification(final Bundle data, final NotificationCompat.Builder mBuilder) {
        if (!TextUtils.isEmpty(data.getString(ARG_NOTIFICATION_IMAGE))) {
            ImageHandler.loadImageBitmapNotification(
                    mContext,
                    data.getString(ARG_NOTIFICATION_IMAGE), new OnGetFileListener() {
                        @Override
                        public void onFileReady(File file) {
                            NotificationCompat.BigPictureStyle bigStyle =
                                    new NotificationCompat.BigPictureStyle();

                            bigStyle.bigPicture(
                                    Bitmap.createScaledBitmap(
                                            BitmapFactory.decodeFile(file.getAbsolutePath()),
                                            mContext.getResources().getDimensionPixelSize(R.dimen.notif_width),
                                            mContext.getResources().getDimensionPixelSize(R.dimen.notif_height),
                                            true
                                    )
                            );
                            bigStyle.setBigContentTitle(data.getString(ARG_NOTIFICATION_TITLE));
                            bigStyle.setSummaryText(data.getString(ARG_NOTIFICATION_DESCRIPTION));

                            mBuilder.setStyle(bigStyle);
                        }
                    }
            );

        }
        return mBuilder;
    }


    private int getDrawableLargeIcon() {
        if (GlobalConfig.isSellerApp())
            return R.drawable.qc_launcher2;
        else
            return R.drawable.qc_launcher;
    }

    private int getDrawableIcon() {
        if (GlobalConfig.isSellerApp())
            return R.drawable.ic_stat_notify2;
        else
            return R.drawable.ic_stat_notify;
    }

    private long[] getVibratePattern() {
        return new long[]{500, 500, 500, 500, 500, 500, 500, 500, 500};
    }

    private Boolean isAllowedVibrate() {
        return cacheManager.isVibrate();
    }

    private Uri getSoundUri() {
        return cacheManager.getSoundUri();
    }

    private Boolean isAllowBell() {
        return cacheManager.isAllowBell();
    }

    void sendUpdateAppsNotification(Bundle data) {
        if (MainApplication.getCurrentVersion(mContext) < Integer.parseInt(data.getString("app_version", "0"))) {
            NotificationManager mNotificationManager =
                    (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext)
                    .setSmallIcon(getDrawableIcon())
                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), getDrawableLargeIcon()))
                    .setAutoCancel(true);
            NotificationCompat.BigTextStyle bigStyle = new NotificationCompat.BigTextStyle();
            mBuilder.setContentTitle(data.getString(ARG_NOTIFICATION_UPDATE_APPS_TITLE));
            mBuilder.setContentText(mContext.getString(R.string.msg_new_update));
            mBuilder.setStyle(bigStyle);
            mBuilder.setTicker(data.getString(ARG_NOTIFICATION_UPDATE_APPS_TITLE));

            if (cacheManager.isAllowBell()) {
                mBuilder.setSound(cacheManager.getSoundUri());
                if (cacheManager.isVibrate()) {
                    mBuilder.setVibrate(getVibratePattern());
                }
            }

            Intent notificationIntent = new Intent(Intent.ACTION_VIEW);
            notificationIntent.setData(Uri.parse(EXTRA_PLAYSTORE_URL));

            PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0, notificationIntent, 0);
            mBuilder.setContentIntent(contentIntent);
            Notification notif = mBuilder.build();
            if (cacheManager.isVibrate() && cacheManager.isAllowBell())
                notif.defaults |= Notification.DEFAULT_VIBRATE;
            mNotificationManager.notify(TkpdState.GCMServiceState.GCM_UPDATE_NOTIFICATION, notif);

            cacheManager.updateUpdateAppStatus(data);
        }
    }
}
