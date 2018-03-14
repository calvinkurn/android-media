package com.tokopedia.core.gcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.gcm.data.entity.NotificationEntity;
import com.tokopedia.core.gcm.model.ApplinkNotificationPass;
import com.tokopedia.core.gcm.model.NotificationPass;
import com.tokopedia.core.gcm.utils.NotificationChannelId;
import com.tokopedia.core.router.SellerAppRouter;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.MethodChecker;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_DESCRIPTION;
import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_ICON;
import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_IMAGE;
import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_TITLE;

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

    public void buildAndShowNotification(ApplinkNotificationPass applinkNotificationPass,
                                         NotificationConfiguration configuration) {
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle(applinkNotificationPass.getTitle());
        inboxStyle.setSummaryText(applinkNotificationPass.getDescription());
        if (applinkNotificationPass.getContents() != null) {
            for (String content : applinkNotificationPass.getContents()) {
                inboxStyle.addLine(content);
            }
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext, NotificationChannelId.GENERAL)
                .setSmallIcon(getDrawableIcon())
                .setAutoCancel(true);

        mBuilder.setContentTitle(applinkNotificationPass.getTitle());
        mBuilder.setContentText(applinkNotificationPass.getDescription());
        mBuilder.setStyle(inboxStyle);
        mBuilder.setTicker(applinkNotificationPass.getDescription());
        mBuilder.setContentInfo(applinkNotificationPass.getInfo());
        mBuilder.setCategory(applinkNotificationPass.getCategory());
        mBuilder.setGroup(applinkNotificationPass.getGroup());
        mBuilder.setGroupSummary(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mBuilder.setColor(mContext.getColor(R.color.tkpd_dark_green));
        } else {
            mBuilder.setColor(mContext.getResources().getColor(R.color.tkpd_dark_green));
        }

        if (configuration.isBell()) {
            mBuilder.setSound(configuration.getSoundUri());
            if (configuration.isVibrate()) {
                mBuilder.setVibrate(configuration.getVibratePattern());
            }
        }

        PendingIntent resultPendingIntent;
        if (applinkNotificationPass.getTaskStackBuilder() != null) {
            resultPendingIntent = applinkNotificationPass.getTaskStackBuilder().getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                resultPendingIntent = PendingIntent.getActivity(
                        mContext,
                        0,
                        applinkNotificationPass.getIntent(),
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
            } else {
                resultPendingIntent = PendingIntent.getActivity(
                        mContext,
                        applinkNotificationPass.getNotificationId(),
                        applinkNotificationPass.getIntent(),
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
            }
        }

        mBuilder.setContentIntent(resultPendingIntent);
        if (applinkNotificationPass.isMultiSender()) {
            mBuilder.setLargeIcon(
                    BitmapFactory.decodeResource(mContext.getResources(), R.drawable.qc_launcher)
            );

            NotificationManager mNotificationManager =
                    (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification notif = mBuilder.build();
            if (configuration.isVibrate() && configuration.isBell()) {
                notif.defaults |= Notification.DEFAULT_VIBRATE;
            }
            mNotificationManager.notify(applinkNotificationPass.getNotificationId(), notif);
        } else if(!TextUtils.isEmpty(applinkNotificationPass.getBannerUrl())){
            configureLargeImageNotification(applinkNotificationPass, mBuilder, configuration);
        } else
            {
            mBuilder.setLargeIcon(getBitmap(applinkNotificationPass.getImageUrl()));

            NotificationManager mNotificationManager =
                    (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification notif = mBuilder.build();
            if (configuration.isVibrate() && configuration.isBell()) {
                notif.defaults |= Notification.DEFAULT_VIBRATE;
            }
            mNotificationManager.notify(applinkNotificationPass.getNotificationId(), notif);
        }
    }

    private void downloadImageAndShowNotification(final ApplinkNotificationPass applinkNotificationPass,
                                                  final NotificationCompat.Builder mBuilder,
                                                  final NotificationConfiguration configuration) {
        Glide
                .with(mBuilder.mContext)
                .load(applinkNotificationPass.getImageUrl())
                .asBitmap()
                .into(new SimpleTarget<Bitmap>(60, 60) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                        mBuilder.setLargeIcon(
                                ImageHandler.getRoundedCornerBitmap(resource, 60)
                        );

                        NotificationManager mNotificationManager =
                                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
                        Notification notif = mBuilder.build();
                        if (configuration.isVibrate() && configuration.isBell()) {
                            notif.defaults |= Notification.DEFAULT_VIBRATE;
                        }
                        mNotificationManager.notify(applinkNotificationPass.getNotificationId(), notif);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        mBuilder.setLargeIcon(
                                BitmapFactory.decodeResource(mContext.getResources(), R.drawable.qc_launcher)
                        );

                        NotificationManager mNotificationManager =
                                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
                        Notification notif = mBuilder.build();
                        if (configuration.isVibrate() && configuration.isBell()) {
                            notif.defaults |= Notification.DEFAULT_VIBRATE;
                        }
                        mNotificationManager.notify(applinkNotificationPass.getNotificationId(), notif);
                    }
                });

    }

    public void buildAndShowNotification(NotificationPass notificationPass, Bundle data, NotificationConfiguration configuration) {
        //TODO : create flow again
        saveIncomingNotification(notificationPass.title, data);

        NotificationManager mNotificationManager =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext, NotificationChannelId.GENERAL)
                .setSmallIcon(getDrawableIcon())
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), getDrawableLargeIcon()))
                .setAutoCancel(true);
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        NotificationCompat.BigTextStyle bigStyle = new NotificationCompat.BigTextStyle();

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
        Intent homeIntent = null;
        if (GlobalConfig.isSellerApp()) {
            homeIntent = SellerAppRouter.getSellerHomeActivity(mContext);
        } else {
            homeIntent = HomeRouter.getHomeActivity(mContext);
        }
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        stackBuilder.addNextIntent(homeIntent);

        if (notificationPass.isAllowedBigStyle || isSingleNotification()) {
            mBuilder.setContentTitle(notificationPass.title);
            mBuilder.setContentText(notificationPass.description);
            bigStyle.bigText(notificationPass.description);
            mBuilder.setStyle(bigStyle);
            mBuilder.setTicker(notificationPass.ticker);
        } else {
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

        if (configuration.isBell()) {
            mBuilder.setSound(configuration.getSoundUri());
            if (configuration.isVibrate()) {
                mBuilder.setVibrate(configuration.getVibratePattern());
            }
        }

        stackBuilder.addNextIntent(notificationPass.getIntent());

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        Notification notif = mBuilder.build();
        if (configuration.isVibrate() && configuration.isBell()) {
            notif.defaults |= Notification.DEFAULT_VIBRATE;
        }
        mNotificationManager.notify(configuration.getNotificationId(), notif);
    }

    public void buildAndShowApplinkNotification(ApplinkNotificationPass applinkNotificationPass, Bundle data, NotificationConfiguration configuration) {
        NotificationManager mNotificationManager =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext, NotificationChannelId.GENERAL)
                .setSmallIcon(getDrawableIcon())
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), getDrawableLargeIcon()))
                .setAutoCancel(true);
        NotificationCompat.BigTextStyle bigStyle = new NotificationCompat.BigTextStyle();

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);

        mBuilder.setContentTitle(MethodChecker.fromHtml(applinkNotificationPass.getTitle()));
        mBuilder.setContentText(applinkNotificationPass.getDescription());
        bigStyle.bigText(MethodChecker.fromHtml(applinkNotificationPass.getDescription()));
        mBuilder.setStyle(bigStyle);
        mBuilder.setTicker(applinkNotificationPass.getTicker());

        mBuilder = configureBigPictureNotification(data, mBuilder);

        mBuilder = configureIconNotification(data, mBuilder);

        if (configuration.isBell()) {
            mBuilder.setSound(configuration.getSoundUri());
            if (configuration.isVibrate()) {
                mBuilder.setVibrate(configuration.getVibratePattern());
            }
        }
        PendingIntent resultPendingIntent;
        if (applinkNotificationPass.getTaskStackBuilder() != null) {
            resultPendingIntent = applinkNotificationPass.getTaskStackBuilder().getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                resultPendingIntent = PendingIntent.getActivity(
                        mContext,
                        0,
                        applinkNotificationPass.getIntent(),
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
            } else {
                resultPendingIntent = PendingIntent.getActivity(
                        mContext,
                        applinkNotificationPass.getNotificationId(),
                        applinkNotificationPass.getIntent(),
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
            }
        }

        stackBuilder.addNextIntent(applinkNotificationPass.getIntent());

//        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        Notification notif = mBuilder.build();
        if (configuration.isVibrate() && configuration.isBell()) {
            notif.defaults |= Notification.DEFAULT_VIBRATE;
        }
        mNotificationManager.notify(configuration.getNotificationId(), notif);
    }

    private void saveIncomingNotification(String title, Bundle data) {
        NotificationEntity notificationEntity = new NotificationEntity();
        notificationEntity.setCode(String.valueOf(getCode(data)));
        notificationEntity.setTitle(title);
        cacheManager.saveIncomingNotification(notificationEntity);
    }

    private boolean isSingleNotification() {
        return cacheManager.getHistoryPushNotification().size() <= 1;
    }

    private List<NotificationEntity> getUnOpenedNotification() {
        return cacheManager.getHistoryPushNotification();
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

    private void configureLargeImageNotification(final ApplinkNotificationPass applinkNotificationPass,
                                                 final NotificationCompat.Builder mBuilder,
                                                 final NotificationConfiguration configuration){

        if (!TextUtils.isEmpty(applinkNotificationPass.getBannerUrl())) {

            ImageHandler.loadImageBitmapNotification(
                    mContext,
                    applinkNotificationPass.getBannerUrl(), new OnGetFileListener() {

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
                            bigStyle.setBigContentTitle(MethodChecker.fromHtml(applinkNotificationPass.getTitle()));
                            bigStyle.setSummaryText(MethodChecker.fromHtml(applinkNotificationPass.getDescription()));

                            mBuilder.setStyle(bigStyle);

                            NotificationManager mNotificationManager =
                                    (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
                            Notification notif = mBuilder.build();
                            if (configuration.isVibrate() && configuration.isBell()) {
                                notif.defaults |= Notification.DEFAULT_VIBRATE;
                            }
                            mNotificationManager.notify(applinkNotificationPass.getNotificationId(), notif);

                        }
                    }
            );
        }

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
            return R.drawable.ic_status_bar_toped_topseller;
        else
            return R.drawable.ic_stat_notify_white;
    }

    private int getCode(Bundle data) {
        int code;
        try {
            code = Integer.parseInt(data.getString("tkp_code", "0"));
        } catch (NumberFormatException e) {
            code = 0;
        }
        return code;
    }

    public interface OnGetFileListener {
        void onFileReady(File file);
    }

    private Bitmap getBitmap(String url) {
        try {
            return Glide.with(mContext).load(url)
                    .asBitmap()
                    .into(60, 60)
                    .get(3, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e ) {
            return BitmapFactory.decodeResource(mContext.getResources(), getDrawableLargeIcon());
        }
    }
}
