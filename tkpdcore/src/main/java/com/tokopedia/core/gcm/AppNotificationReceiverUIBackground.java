package com.tokopedia.core.gcm;

import android.annotation.TargetApi;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
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

import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.ImageHandler;
import com.tkpd.library.utils.URLParser;
import com.tokopedia.core.NotificationCenter;
import com.tokopedia.core.R;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.database.manager.DbManagerImpl;
import com.tokopedia.core.database.model.CategoryDB;
import com.tokopedia.core.inboxmessage.activity.InboxMessageActivity;
import com.tokopedia.core.inboxreputation.activity.InboxReputationActivity;
import com.tokopedia.core.router.InboxRouter;
import com.tokopedia.core.router.SellerRouter;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.router.home.SimpleHomeRouter;
import com.tokopedia.core.router.transactionmodule.TransactionPurchaseRouter;
import com.tokopedia.core.session.presenter.SessionView;
import com.tokopedia.core.talk.inboxtalk.activity.InboxTalkActivity;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.RouterUtils;
import com.tokopedia.core.var.TkpdState;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_CODE;
import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_DESCRIPTION;
import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_ICON;
import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_IMAGE;
import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_TITLE;
import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_UPDATE_APPS_TITLE;
import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_URL;
import static com.tokopedia.core.gcm.Constants.EXTRA_PLAYSTORE_URL;

/**
 * @author by alvarisi on 1/9/17.
 */

class AppNotificationReceiverUIBackground {
    private INotificationAnalyticsReceiver mNotificationAnalyticsReceiver;
    private FCMCacheManager cacheManager;
    private Application mApplication;
    private Context mContext;
    private long[] notificationVibratePattern = {500, 500, 500, 500, 500, 500, 500, 500, 500};

    AppNotificationReceiverUIBackground(Application application) {
        cacheManager = new FCMCacheManager(application.getBaseContext());
        mApplication = application;
        mContext = application.getBaseContext();
        mNotificationAnalyticsReceiver = new NotificationAnalyticsReceiver();
    }

    void sendNotification(Bundle data) {
        int tkpCode = Integer.parseInt(data.getString(ARG_NOTIFICATION_CODE));

        if (!cacheManager.checkLocalNotificationAppSettings(tkpCode)) {
            return;
        }
        if (!GCMUtils.isValidForSellerApp(tkpCode, mApplication)) {
            return;
        }

        String title;
        String ticker;
        String description;
        Intent intent = null;
        Bundle bundle = new Bundle();
        ComponentName componentName = null;
        switch (Integer.parseInt(data.getString(ARG_NOTIFICATION_CODE))) {
            case TkpdState.GCMServiceState.GCM_MESSAGE:
                componentName = RouterUtils.getActivityComponentName(mContext, InboxMessageActivity.class);
                title = String.format("%s %s", data.getString("counter"), mContext.getString(R.string.title_new_message));
                ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                description = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                break;
            case TkpdState.GCMServiceState.GCM_TALK:
                componentName = RouterUtils.getActivityComponentName(mContext, InboxTalkActivity.class);
                title = String.format("%s %s", data.getString("counter"), mContext.getString(R.string.title_new_talk));
                ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                description = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                break;
            case TkpdState.GCMServiceState.GCM_REVIEW:
                componentName = RouterUtils.getActivityComponentName(mContext, InboxReputationActivity.class);
                title = String.format("%s %s", data.getString("counter"), mContext.getString(R.string.title_new_review));
                ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                description = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                break;
            case TkpdState.GCMServiceState.GCM_REVIEW_EDIT:
                componentName = RouterUtils.getActivityComponentName(mContext, InboxReputationActivity.class);
                title = mContext.getString(R.string.title_edit_review);
                ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                description = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                break;
            case TkpdState.GCMServiceState.GCM_REVIEW_REPLY:
                componentName = RouterUtils.getActivityComponentName(mContext, InboxReputationActivity.class);
                title = mContext.getString(R.string.title_reply_review);
                ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                description = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                break;
            case TkpdState.GCMServiceState.GCM_TICKET:
                componentName = InboxRouter.getInboxticketActivityComponentName(mContext);
                intent = InboxRouter.getInboxTicketActivityIntent(mContext);
                title = mContext.getString(R.string.title_new_ticket);
                ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                description = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                break;
            case TkpdState.GCMServiceState.GCM_RES_CENTER:
                intent = InboxRouter.getInboxResCenterActivityIntent(mContext);
                title = mContext.getString(R.string.title_new_rescenter);
                ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                description = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                break;
            case TkpdState.GCMServiceState.GCM_NEWORDER:
                componentName = SellerRouter.getActivitySellingTransactionName(mContext);
                intent = SellerRouter.getActivitySellingTransaction(mContext);
                title = String.format("%s %s", data.getString("counter"), mContext.getString(R.string.title_new_order));
                description = mContext.getString(R.string.msg_new_order);
                ticker = mContext.getString(R.string.msg_new_order);
                break;
            case TkpdState.GCMServiceState.GCM_PROMO:
                title = data.getString(ARG_NOTIFICATION_TITLE);
                description = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                componentName = HomeRouter.getActivityHomeName(mContext);
                break;
            case TkpdState.GCMServiceState.GCM_GENERAL:
                title = data.getString(ARG_NOTIFICATION_TITLE);
                description = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                componentName = HomeRouter.getActivityHomeName(mContext);
                break;
            case TkpdState.GCMServiceState.GCM_REPUTATION_SMILEY:
                componentName = RouterUtils.getActivityComponentName(mContext, InboxReputationActivity.class);
                title = mContext.getString(R.string.title_get_reputation);
                ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                description = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                break;
            case TkpdState.GCMServiceState.GCM_REPUTATION_EDIT_SMILEY:
                componentName = RouterUtils.getActivityComponentName(mContext, InboxReputationActivity.class);
                title = mContext.getString(R.string.title_get_edit_reputation);
                ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                description = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                break;
            case TkpdState.GCMServiceState.GCM_REPUTATION_SMILEY_TO_BUYER:
                componentName = RouterUtils.getActivityComponentName(mContext, InboxReputationActivity.class);
                title = mContext.getString(R.string.title_get_reputation);
                ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                description = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                break;
            case TkpdState.GCMServiceState.GCM_REPUTATION_EDIT_SMILEY_TO_BUYER:
                componentName = RouterUtils.getActivityComponentName(mContext, InboxReputationActivity.class);
                title = mContext.getString(R.string.title_get_edit_reputation);
                ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                description = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                break;
            case TkpdState.GCMServiceState.GCM_REPUTATION_SMILEY_TO_SELLER:
                componentName = RouterUtils.getActivityComponentName(mContext, InboxReputationActivity.class);
                title = mContext.getString(R.string.title_get_reputation);
                ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                description = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                break;
            case TkpdState.GCMServiceState.GCM_REPUTATION_EDIT_SMILEY_TO_SELLER:
                componentName = RouterUtils.getActivityComponentName(mContext, InboxReputationActivity.class);
                title = mContext.getString(R.string.title_get_edit_reputation);
                ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                description = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                break;
            case TkpdState.GCMServiceState.GCM_PURCHASE_VERIFIED:
                componentName = TransactionPurchaseRouter.getPurchaseActivityComponentName(mContext);
                intent = TransactionPurchaseRouter.createIntentPurchaseActivity(mContext);
                bundle.putInt(TransactionPurchaseRouter.EXTRA_STATE_TAB_POSITION,
                        TransactionPurchaseRouter.TAB_POSITION_PURCHASE_STATUS_ORDER);
                title = mContext.getString(R.string.title_notif_purchase_confirmed);
                ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                description = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                break;
            case TkpdState.GCMServiceState.GCM_PURCHASE_ACCEPTED:
                componentName = TransactionPurchaseRouter.getPurchaseActivityComponentName(mContext);
                intent = TransactionPurchaseRouter.createIntentPurchaseActivity(mContext);
                bundle.putInt(TransactionPurchaseRouter.EXTRA_STATE_TAB_POSITION,
                        TransactionPurchaseRouter.TAB_POSITION_PURCHASE_STATUS_ORDER);
                title = mContext.getString(R.string.title_notif_purchase_accepted);
                ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                description = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                break;
            case TkpdState.GCMServiceState.GCM_PURCHASE_PARTIAL_PROCESSED:
                componentName = TransactionPurchaseRouter.getPurchaseActivityComponentName(mContext);
                intent = TransactionPurchaseRouter.createIntentPurchaseActivity(mContext);
                bundle.putInt(TransactionPurchaseRouter.EXTRA_STATE_TAB_POSITION,
                        TransactionPurchaseRouter.TAB_POSITION_PURCHASE_STATUS_ORDER);
                title = mContext.getString(R.string.title_notif_purchase_partial_accepted);
                ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                description = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                break;
            case TkpdState.GCMServiceState.GCM_PURCHASE_REJECTED:
                componentName = TransactionPurchaseRouter.getPurchaseActivityComponentName(mContext);
                intent = TransactionPurchaseRouter.createIntentPurchaseActivity(mContext);
                bundle.putInt(TransactionPurchaseRouter.EXTRA_STATE_TAB_POSITION,
                        TransactionPurchaseRouter.TAB_POSITION_PURCHASE_ALL_ORDER);
                bundle.putString(TransactionPurchaseRouter.EXTRA_STATE_TX_FILTER,
                        TransactionPurchaseRouter.TRANSACTION_CANCELED_FILTER_ID);
                title = mContext.getString(R.string.title_notif_purchase_rejected);
                ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                description = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                break;
            case TkpdState.GCMServiceState.GCM_PURCHASE_DELIVERED:
                componentName = TransactionPurchaseRouter.getPurchaseActivityComponentName(mContext);
                intent = TransactionPurchaseRouter.createIntentPurchaseActivity(mContext);
                bundle.putInt(TransactionPurchaseRouter.EXTRA_STATE_TAB_POSITION,
                        TransactionPurchaseRouter.TAB_POSITION_PURCHASE_DELIVER_ORDER);
                title = mContext.getString(R.string.title_notif_purchase_delivered);
                ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                description = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                break;
            case TkpdState.GCMServiceState.GCM_PURCHASE_DISPUTE:
                componentName = InboxRouter.getInboxResCenterActivityComponentName(mContext);
                intent = InboxRouter.getInboxResCenterActivityIntent(mContext);
                bundle.putInt(InboxRouter.EXTRA_STATE_TAB_POSITION,
                        TkpdState.InboxResCenter.RESO_BUYER);
                title = mContext.getString(R.string.title_notif_rescenter);
                ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                description = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                break;
            case TkpdState.GCMServiceState.GCM_RESCENTER_BUYER_REPLY:
                componentName = InboxRouter.getInboxResCenterActivityComponentName(mContext);
                intent = InboxRouter.getInboxResCenterActivityIntent(mContext);
                bundle.putInt(InboxRouter.EXTRA_STATE_TAB_POSITION,
                        TkpdState.InboxResCenter.RESO_BUYER);
                title = mContext.getString(R.string.title_notif_rescenter);
                ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                description = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                break;
            case TkpdState.GCMServiceState.GCM_RESCENTER_SELLER_REPLY:
                componentName = InboxRouter.getInboxResCenterActivityComponentName(mContext);
                intent = InboxRouter.getInboxResCenterActivityIntent(mContext);
                bundle.putInt(InboxRouter.EXTRA_STATE_TAB_POSITION,
                        TkpdState.InboxResCenter.RESO_MINE);
                title = mContext.getString(R.string.title_notif_rescenter);
                ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                description = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                break;
            case TkpdState.GCMServiceState.GCM_RESCENTER_SELLER_AGREE:
                componentName = InboxRouter.getInboxResCenterActivityComponentName(mContext);
                intent = InboxRouter.getInboxResCenterActivityIntent(mContext);
                bundle.putInt(InboxRouter.EXTRA_STATE_TAB_POSITION,
                        TkpdState.InboxResCenter.RESO_MINE);
                title = mContext.getString(R.string.title_notif_rescenter);
                ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                description = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                break;
            case TkpdState.GCMServiceState.GCM_RESCENTER_BUYER_AGREE:
                componentName = InboxRouter.getInboxResCenterActivityComponentName(mContext);
                intent = InboxRouter.getInboxResCenterActivityIntent(mContext);
                bundle.putInt(InboxRouter.EXTRA_STATE_TAB_POSITION,
                        TkpdState.InboxResCenter.RESO_BUYER);
                title = mContext.getString(R.string.title_notif_rescenter);
                ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                description = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                break;
            case TkpdState.GCMServiceState.GCM_RESCENTER_ADMIN_BUYER_REPLY:
                componentName = InboxRouter.getInboxResCenterActivityComponentName(mContext);
                intent = InboxRouter.getInboxResCenterActivityIntent(mContext);
                bundle.putInt(InboxRouter.EXTRA_STATE_TAB_POSITION,
                        TkpdState.InboxResCenter.RESO_MINE);
                title = mContext.getString(R.string.title_notif_rescenter);
                ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                description = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                break;
            case TkpdState.GCMServiceState.GCM_RESCENTER_ADMIN_SELLER_REPLY:
                componentName = InboxRouter.getInboxResCenterActivityComponentName(mContext);
                intent = InboxRouter.getInboxResCenterActivityIntent(mContext);
                bundle.putInt(InboxRouter.EXTRA_STATE_TAB_POSITION,
                        TkpdState.InboxResCenter.RESO_BUYER);
                title = mContext.getString(R.string.title_notif_rescenter);
                ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                description = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                break;
            default:
                return;
        }
        if (intent == null) {
            intent = new Intent();
            intent.setComponent(componentName);
        }
        createNotification(intent, title, ticker, description, bundle, data, componentName);
    }

    private void createNotification(Intent intent, String title, String ticker,
                                    String desc, Bundle bundle, final Bundle data, ComponentName componentName) {
        NotificationManager mNotificationManager =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        final ArrayList<String> contents = new ArrayList<>(), descriptions = new ArrayList<>();
        final ArrayList<Integer> codes = new ArrayList<>();
        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext)
                .setSmallIcon(getDrawableIcon())
                .setAutoCancel(true);
        NotificationCompat.InboxStyle inboxStyle;
        NotificationCompat.BigTextStyle bigStyle;

        cacheManager.processNotifData(data, title, desc, new FCMCacheManager.CacheProcessListener() {
            @Override
            public void onDataProcessed(ArrayList<String> content, ArrayList<String> desc, ArrayList<Integer> code) {
                contents.addAll(content);
                codes.addAll(code);
                descriptions.addAll(desc);
            }
        });

        if (codes.size() == 1) {
            bigStyle = new NotificationCompat.BigTextStyle();
            mBuilder.setContentTitle(title);
            mBuilder.setContentText(desc);
            bigStyle.bigText(desc);
            mBuilder.setStyle(bigStyle);
            mBuilder.setTicker(ticker);
        } else if (isPromoNotification(data)) {
            bigStyle = new NotificationCompat.BigTextStyle();
            mBuilder.setContentTitle(title);
            mBuilder.setContentText(desc);
            bigStyle.bigText(desc);
            mBuilder.setStyle(bigStyle);
            mBuilder.setTicker(ticker);
        } else {
            componentName = RouterUtils.getActivityComponentName(mContext, NotificationCenter.class);
            bundle.putInt("notif_call", 0);
            inboxStyle = new NotificationCompat.InboxStyle();
            inboxStyle.setBigContentTitle(mContext.getString(R.string.title_new_notif_general));
            for (int i = 0; i < contents.size(); i++) {
                inboxStyle.addLine(contents.get(i));
            }
            mBuilder.setContentTitle(mContext.getString(R.string.title_new_notif_general));
            mBuilder.setContentText(desc);
            mBuilder.setStyle(inboxStyle);
            mBuilder.setTicker(ticker);
        }

        if (!TextUtils.isEmpty(data.getString(ARG_NOTIFICATION_IMAGE))) {
            ImageHandler.loadImageBitmapNotification(
                    mContext,
                    data.getString(ARG_NOTIFICATION_IMAGE), new FCMMessagingService.OnGetFileListener() {
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

        if (!TextUtils.isEmpty(data.getString(ARG_NOTIFICATION_ICON))) {
            ImageHandler.loadImageBitmapNotification(
                    mContext,
                    data.getString(ARG_NOTIFICATION_ICON), new FCMMessagingService.OnGetFileListener() {
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

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("from_notif", true);
        intent.putExtra("unread", false);
        if (isPromoNotification(data)) {
            Bundle dataLocalytics = this.mNotificationAnalyticsReceiver.buildAnalyticNotificationData(data);
            intent.putExtras(dataLocalytics);
        } else {
            intent.putExtras(bundle);
        }

        if (cacheManager.isAllowBell()) {
            mBuilder.setSound(cacheManager.getSoundUri());
            if (cacheManager.isVibrate()) {
                mBuilder.setVibrate(notificationVibratePattern);
            }
        }
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
        if (componentName != null) {
            stackBuilder.addParentStack(componentName);
        }
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_CANCEL_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        Notification notif = mBuilder.build();
        if (cacheManager.isVibrate() && cacheManager.isAllowBell())
            notif.defaults |= Notification.DEFAULT_VIBRATE;
        mNotificationManager.notify(100, notif);
    }

    private boolean isPromoNotification(Bundle data) {
        return Integer.parseInt(data.getString(ARG_NOTIFICATION_CODE)) == TkpdState.GCMServiceState.GCM_PROMO
                || Integer.parseInt(data.getString(ARG_NOTIFICATION_CODE)) == TkpdState.GCMServiceState.GCM_GENERAL;
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

    void createNotification(Bundle data, Class<?> intentClass) {
        if (!cacheManager.checkLocalNotificationAppSettings(Integer.parseInt(data.getString(ARG_NOTIFICATION_CODE)))) {
            return;
        }
        if (data.getString(ARG_NOTIFICATION_IMAGE) != null) {
            createAndShowImageNotification(data, intentClass);
        } else {
            createAndShowTextNotification(data, intentClass);
        }
    }

    private void createAndShowTextNotification(Bundle data, Class<?> intentClass) {
        try {
            NotificationManager mNotificationManager =
                    (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext)
                    .setSmallIcon(getDrawableIcon())
                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), getDrawableLargeIcon()))
                    .setAutoCancel(true);
            NotificationCompat.BigTextStyle bigStyle;
            bigStyle = new NotificationCompat.BigTextStyle();
            mBuilder.setContentTitle(data.getString(ARG_NOTIFICATION_TITLE));
            mBuilder.setContentText(data.getString(ARG_NOTIFICATION_DESCRIPTION));
            bigStyle.bigText(data.getString(ARG_NOTIFICATION_DESCRIPTION));
            mBuilder.setStyle(bigStyle);
            mBuilder.setTicker(data.getString(ARG_NOTIFICATION_DESCRIPTION));

            if (cacheManager.isAllowBell()) {
                mBuilder.setSound(cacheManager.getSoundUri());
                if (cacheManager.isVibrate()) {
                    mBuilder.setVibrate(notificationVibratePattern);
                }
            }

            Intent intent = createIntent(intentClass, data);

            if (data.getInt("keylogin1", -99) != -99) {
                intent.putExtra(
                        com.tokopedia.core.session.presenter.Session.WHICH_FRAGMENT_KEY, data.getInt("keylogin1")
                );
                intent.putExtra(SessionView.MOVE_TO_CART_KEY, data.getInt("keylogin2"));
            }

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
            stackBuilder.addParentStack(intentClass);
            stackBuilder.addNextIntent(intent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_CANCEL_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);
            Notification notif = mBuilder.build();
            if (cacheManager.isVibrate() && cacheManager.isAllowBell())
                notif.defaults |= Notification.DEFAULT_VIBRATE;
            mNotificationManager.notify(100, notif);
        } catch (NullPointerException e) {
            CommonUtils.dumper("NotifTag : Null Value" + e.toString());
        }
    }

    private void createAndShowImageNotification(final Bundle data, final Class<?> intentClass)
            throws NullPointerException {

        final NotificationManager mNotificationManager =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        final Notification.Builder mBuilder = new Notification.Builder(mContext)
                .setSmallIcon(getDrawableIcon())
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), getDrawableLargeIcon()))
                .setAutoCancel(true);

        if (!TextUtils.isEmpty(data.getString(ARG_NOTIFICATION_ICON))) {
            ImageHandler.loadImageBitmapNotification(
                    mContext,
                    data.getString(ARG_NOTIFICATION_ICON), new FCMMessagingService.OnGetFileListener() {
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
            mBuilder.setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), getDrawableIcon()));
        }

        ImageHandler.loadImageBitmapNotification(
                mContext,
                data.getString(ARG_NOTIFICATION_IMAGE),
                new FCMMessagingService.OnGetFileListener() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onFileReady(File file) {
                        Notification.BigPictureStyle bigStyle = new Notification.BigPictureStyle();
                        bigStyle.bigPicture(
                                Bitmap.createScaledBitmap(
                                        BitmapFactory.decodeFile(file.getAbsolutePath()),
                                        mContext.getResources().getDimensionPixelSize(R.dimen.notif_width),
                                        mContext.getResources().getDimensionPixelSize(R.dimen.notif_height),
                                        true)
                        );
                        bigStyle.setSummaryText(data.getString(ARG_NOTIFICATION_DESCRIPTION));

                        mBuilder.setContentTitle(data.getString(ARG_NOTIFICATION_TITLE));
                        mBuilder.setContentText(data.getString(ARG_NOTIFICATION_DESCRIPTION));
                        mBuilder.setStyle(bigStyle);
                        mBuilder.setTicker(data.getString(ARG_NOTIFICATION_DESCRIPTION));

                        if (cacheManager.isAllowBell()) {
                            mBuilder.setSound(cacheManager.getSoundUri());
                            if (cacheManager.isVibrate()) {
                                mBuilder.setVibrate(notificationVibratePattern);
                            }
                        }

                        Intent intent = createIntent(intentClass, data);

                        if (data.getInt("keylogin1", -99) != -99) {
                            intent.putExtra(
                                    com.tokopedia.core.session.presenter.Session.WHICH_FRAGMENT_KEY,
                                    data.getInt("keylogin1")
                            );
                            intent.putExtra(SessionView.MOVE_TO_CART_KEY, data.getInt("keylogin2"));
                        }

                        TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
                        stackBuilder.addParentStack(intentClass);
                        stackBuilder.addNextIntent(intent);
                        PendingIntent resultPendingIntent =
                                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_CANCEL_CURRENT);
                        mBuilder.setContentIntent(resultPendingIntent);
                        Notification notif = mBuilder.build();

                        if (cacheManager.isVibrate() && cacheManager.isAllowBell())
                            notif.defaults |= Notification.DEFAULT_VIBRATE;
                        mNotificationManager.notify(
                                Integer.parseInt(data.getString(ARG_NOTIFICATION_CODE)),
                                notif
                        );
                    }
                }
        );
    }


    private Intent createIntent(Class<?> intentClass, Bundle data) {
        Intent intent = new Intent(mContext, intentClass);

        try {
            data.putString("img_uri", data.getString(ARG_NOTIFICATION_IMAGE));
            data.putString("img_uri_600", data.getString(ARG_NOTIFICATION_IMAGE));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        switch (Integer.parseInt(data.getString(ARG_NOTIFICATION_CODE))) {
            case TkpdState.GCMServiceState.GCM_HOT_LIST: {
                URLParser urlp = new URLParser(data.getString(ARG_NOTIFICATION_URL));
                data.putString("alias", urlp.getHotAlias());
                break;
            }
            case TkpdState.GCMServiceState.GCM_DEEPLINK: {
                intent.setData(Uri.parse(data.getString(ARG_NOTIFICATION_URL)));
                break;
            }
            case TkpdState.GCMServiceState.GCM_CATEGORY: {
                Uri uri = Uri.parse(data.getString(ARG_NOTIFICATION_URL));
                List<String> linkSegment = uri.getPathSegments();
                String iden = linkSegment.get(1);
                for (int i = 2; i < linkSegment.size(); i++) {
                    iden = iden + "_" + linkSegment.get(i);
                }
                CategoryDB dep =
                        DbManagerImpl.getInstance().getCategoryDb(iden);
                String dep_id = dep.getDepartmentId() + "";
                data.putString("d_id", dep_id);
                data.putInt("state", 0);
                break;
            }
            case TkpdState.GCMServiceState.GCM_SHOP: {
                Uri uri = Uri.parse(data.getString(ARG_NOTIFICATION_URL));
                intent.putExtra("shop_domain", uri.getLastPathSegment());
                break;
            }
            case TkpdState.GCMServiceState.GCM_WISHLIST: {
                data.putInt(SimpleHomeRouter.FRAGMENT_TYPE, SimpleHomeRouter.WISHLIST_FRAGMENT);
                break;
            }
            default:
                break;
        }
        data = this.mNotificationAnalyticsReceiver.buildAnalyticNotificationData(data);
        intent.putExtras(data);
        return intent;
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
                    mBuilder.setVibrate(notificationVibratePattern);
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
