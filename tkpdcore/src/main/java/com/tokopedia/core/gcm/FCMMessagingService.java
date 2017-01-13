package com.tokopedia.core.gcm;

import android.annotation.TargetApi;
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

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.ImageHandler;
import com.tkpd.library.utils.URLParser;
import com.tokopedia.core.Cart;
import com.tokopedia.core.ManageGeneral;
import com.tokopedia.core.NotificationCenter;
import com.tokopedia.core.R;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.database.manager.DbManagerImpl;
import com.tokopedia.core.database.model.CategoryDB;
import com.tokopedia.core.inboxreputation.activity.InboxReputationActivity;
import com.tokopedia.core.router.CustomerRouter;
import com.tokopedia.core.router.InboxRouter;
import com.tokopedia.core.router.SellerRouter;
import com.tokopedia.core.router.SessionRouter;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.router.home.SimpleHomeRouter;
import com.tokopedia.core.router.transactionmodule.TransactionPurchaseRouter;
import com.tokopedia.core.session.presenter.SessionView;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.SessionHandler;
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
 * @author by hangnadi on 9/7/15.
 *         modified by Hafizh
 *         modified by alvarisi
 */
public class FCMMessagingService extends FirebaseMessagingService {
    private FCMCacheManager cacheManager;
    private long[] pattern = {500, 500, 500, 500, 500, 500, 500, 500, 500};
    INotificationAnalyticsReceiver mNotificationAnalyticsReceiver;

    public interface NotificationListener {
        void onGetNotif();

        void onRefreshCart(int status);
    }

    @Override
    public void onMessageReceived(RemoteMessage message) {
        mNotificationAnalyticsReceiver = new NotificationAnalyticsReceiver();
        Bundle data = GCMUtils.convertMap(message.getData());

        CommonUtils.dumper("FCM messaging "+data.toString());
        cacheManager = new FCMCacheManager(this);
        if (cacheManager.isAllowToHandleNotif(data)) {
            cacheManager.setCache(this);
            tunnelData(data);
        }
        mNotificationAnalyticsReceiver.onNotificationReceived(data);
    }

    private void tunnelData(Bundle data) {
        switch (GCMUtils.getCode(data)) {
            case TkpdState.GCMServiceState.GCM_HOT_LIST:
                // TODO Handle with latest code
//                createNotification(data, BrowseHotDetail.class);
                break;
            case TkpdState.GCMServiceState.GCM_UPDATE_NOTIFICATION:
                sendUpdateAppsNotification(data);
                break;
            case TkpdState.GCMServiceState.GCM_PROMO:
                sendMarketingPromoCode(data);
                break;
            case TkpdState.GCMServiceState.GCM_GENERAL:
                sendGeneralNotification(data);
                break;
            case TkpdState.GCMServiceState.GCM_CATEGORY:
                // TODO Handle with latest code
//                createNotification(data, BrowseCategory.class);
                break;
            case TkpdState.GCMServiceState.GCM_SHOP:
                createNotification(data, ShopInfoActivity.class);
                break;
            case TkpdState.GCMServiceState.GCM_DEEPLINK:
                if (CustomerRouter.getDeeplinkClass() != null) {
                    createNotification(data, CustomerRouter.getDeeplinkClass());
                }
                break;
            case TkpdState.GCMServiceState.GCM_CART:
                if (SessionHandler.isV4Login(this)) createNotification(data, Cart.class);
                break;
            case TkpdState.GCMServiceState.GCM_WISHLIST:
                if (SessionHandler.isV4Login(this)) {
                    createNotification(data, SimpleHomeRouter.getSimpleHomeActivityClass());
                }
                break;
            case TkpdState.GCMServiceState.GCM_VERIFICATION:
                if (SessionHandler.isV4Login(this)) {
                    createNotification(data, ManageGeneral.class);
                } else {
                    data.putInt("keylogin1", TkpdState.DrawerPosition.LOGIN);
                    data.putInt("keylogin2", SessionView.HOME);
                    Intent intent = SessionRouter.getLoginActivityIntent(getApplicationContext());
                    createNotification(data, intent.getClass());
                }
                break;
            default:
                if (SessionHandler.isV4Login(this)
                        && SessionHandler.getLoginID(this).equals(data.getString("to_user_id"))) {

                    resetNotificationStatus(data);

                    if (MainApplication.currentActivity() == null
                            && Integer.parseInt(data.getString(ARG_NOTIFICATION_CODE)) < 500) {
                        sendNotification(data);
                    } else {
                        NotificationListener listener = (NotificationListener) MainApplication.currentActivity();
                        if (listener != null) {
                            if (Integer.parseInt(data.getString(ARG_NOTIFICATION_CODE))
                                    == TkpdState.GCMServiceState.GCM_CART_UPDATE) {
                                listener.onRefreshCart(data.getInt("is_cart_exists", 0));
                            } else {
                                sendNotification(data);
                            }
                        } else {
                            if (Integer.parseInt(data.getString(ARG_NOTIFICATION_CODE)) < 500) {
                                sendNotification(data);
                            }
                        }
                    }
                    cacheManager.resetCache(data);
                }
                break;
        }
    }

    private void sendNotification(Bundle data) {
        int tkpCode = Integer.parseInt(data.getString(ARG_NOTIFICATION_CODE));

        if (!cacheManager.checkLocalNotificationAppSettings(tkpCode)) {
            return;
        }
        if (!GCMUtils.isValidForSellerApp(tkpCode, getApplication())){
            return;
        }

        Class<?> targetClass = null;
        String title;
        String ticker;
        String description;
        Intent intent = null;
        Bundle bundle = new Bundle();
        ComponentName componentName = null;
        switch (Integer.parseInt(data.getString(ARG_NOTIFICATION_CODE))) {
            case TkpdState.GCMServiceState.GCM_MESSAGE:
                componentName = InboxRouter.getInboxMessageActivityComponentName(this);
                intent = InboxRouter.getInboxMessageActivityIntent(this);
                title = String.format("%s %s", data.getString("counter"), this.getString(R.string.title_new_message));
                ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                description = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                break;
            case TkpdState.GCMServiceState.GCM_TALK:
                intent = InboxRouter.getInboxTalkActivityIntent(this);
                title = String.format("%s %s", data.getString("counter"), this.getString(R.string.title_new_talk));
                ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                description = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                break;
            case TkpdState.GCMServiceState.GCM_REVIEW:
                targetClass = InboxReputationActivity.class;
                title = String.format("%s %s", data.getString("counter"), this.getString(R.string.title_new_review));
                ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                description = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                break;
            case TkpdState.GCMServiceState.GCM_REVIEW_EDIT:
                targetClass = InboxReputationActivity.class;
                title = this.getString(R.string.title_edit_review);
                ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                description = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                break;
            case TkpdState.GCMServiceState.GCM_REVIEW_REPLY:
                targetClass = InboxReputationActivity.class;
                title = this.getString(R.string.title_reply_review);
                ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                description = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                break;
            case TkpdState.GCMServiceState.GCM_TICKET:
                componentName = InboxRouter.getInboxticketActivityComponentName(this);
                intent = InboxRouter.getInboxTicketActivityIntent(this);
                title = this.getString(R.string.title_new_ticket);
                ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                description = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                break;
            case TkpdState.GCMServiceState.GCM_RES_CENTER:
                intent = InboxRouter.getInboxResCenterActivityIntent(this);
                title = this.getString(R.string.title_new_rescenter);
                ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                description = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                break;
            case TkpdState.GCMServiceState.GCM_NEWORDER:
                componentName = SellerRouter.getActivitySellingTransactionName(this);
                intent = SellerRouter.getActivitySellingTransaction(this);
                title = String.format("%s %s", data.getString("counter"), this.getString(R.string.title_new_order));
                description = getString(R.string.msg_new_order);
                ticker = getString(R.string.msg_new_order);
                break;
            case TkpdState.GCMServiceState.GCM_PROMO:
                title = data.getString(ARG_NOTIFICATION_TITLE);
                description = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                targetClass = HomeRouter.getHomeActivityClass();
                break;
            case TkpdState.GCMServiceState.GCM_GENERAL:
                title = data.getString(ARG_NOTIFICATION_TITLE);
                description = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                targetClass = HomeRouter.getHomeActivityClass();
                break;
            case TkpdState.GCMServiceState.GCM_REPUTATION_SMILEY:
                targetClass = InboxReputationActivity.class;
                title = this.getString(R.string.title_get_reputation);
                ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                description = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                break;
            case TkpdState.GCMServiceState.GCM_REPUTATION_EDIT_SMILEY:
                targetClass = InboxReputationActivity.class;
                title = this.getString(R.string.title_get_edit_reputation);
                ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                description = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                break;
            case TkpdState.GCMServiceState.GCM_REPUTATION_SMILEY_TO_BUYER:
                targetClass = InboxReputationActivity.class;
                title = this.getString(R.string.title_get_reputation);
                ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                description = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                break;
            case TkpdState.GCMServiceState.GCM_REPUTATION_EDIT_SMILEY_TO_BUYER:
                targetClass = InboxReputationActivity.class;
                title = this.getString(R.string.title_get_edit_reputation);
                ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                description = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                break;
            case TkpdState.GCMServiceState.GCM_REPUTATION_SMILEY_TO_SELLER:
                targetClass = InboxReputationActivity.class;
                title = this.getString(R.string.title_get_reputation);
                ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                description = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                break;
            case TkpdState.GCMServiceState.GCM_REPUTATION_EDIT_SMILEY_TO_SELLER:
                targetClass = InboxReputationActivity.class;
                title = this.getString(R.string.title_get_edit_reputation);
                ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                description = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                break;
            case TkpdState.GCMServiceState.GCM_PURCHASE_VERIFIED:
                componentName = TransactionPurchaseRouter.getPurchaseActivityComponentName(this);
                intent = TransactionPurchaseRouter.createIntentPurchaseActivity(this);
                bundle.putInt(TransactionPurchaseRouter.EXTRA_STATE_TAB_POSITION,
                        TransactionPurchaseRouter.TAB_POSITION_PURCHASE_STATUS_ORDER);
                title = this.getString(R.string.title_notif_purchase_confirmed);
                ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                description = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                break;
            case TkpdState.GCMServiceState.GCM_PURCHASE_ACCEPTED:
                componentName = TransactionPurchaseRouter.getPurchaseActivityComponentName(this);
                intent = TransactionPurchaseRouter.createIntentPurchaseActivity(this);
                bundle.putInt(TransactionPurchaseRouter.EXTRA_STATE_TAB_POSITION,
                        TransactionPurchaseRouter.TAB_POSITION_PURCHASE_STATUS_ORDER);
                title = this.getString(R.string.title_notif_purchase_accepted);
                ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                description = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                break;
            case TkpdState.GCMServiceState.GCM_PURCHASE_PARTIAL_PROCESSED:
                componentName = TransactionPurchaseRouter.getPurchaseActivityComponentName(this);
                intent = TransactionPurchaseRouter.createIntentPurchaseActivity(this);
                bundle.putInt(TransactionPurchaseRouter.EXTRA_STATE_TAB_POSITION,
                        TransactionPurchaseRouter.TAB_POSITION_PURCHASE_STATUS_ORDER);
                title = this.getString(R.string.title_notif_purchase_partial_accepted);
                ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                description = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                break;
            case TkpdState.GCMServiceState.GCM_PURCHASE_REJECTED:
                componentName = TransactionPurchaseRouter.getPurchaseActivityComponentName(this);
                intent = TransactionPurchaseRouter.createIntentPurchaseActivity(this);
                bundle.putInt(TransactionPurchaseRouter.EXTRA_STATE_TAB_POSITION,
                        TransactionPurchaseRouter.TAB_POSITION_PURCHASE_ALL_ORDER);
                bundle.putString(TransactionPurchaseRouter.EXTRA_STATE_TX_FILTER,
                        TransactionPurchaseRouter.TRANSACTION_CANCELED_FILTER_ID);
                title = this.getString(R.string.title_notif_purchase_rejected);
                ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                description = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                break;
            case TkpdState.GCMServiceState.GCM_PURCHASE_DELIVERED:
                componentName = TransactionPurchaseRouter.getPurchaseActivityComponentName(this);
                intent = TransactionPurchaseRouter.createIntentPurchaseActivity(this);
                bundle.putInt(TransactionPurchaseRouter.EXTRA_STATE_TAB_POSITION,
                        TransactionPurchaseRouter.TAB_POSITION_PURCHASE_DELIVER_ORDER);
                title = this.getString(R.string.title_notif_purchase_delivered);
                ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                description = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                break;
            case TkpdState.GCMServiceState.GCM_PURCHASE_DISPUTE:
                componentName = InboxRouter.getInboxResCenterActivityComponentName(this);
                intent = InboxRouter.getInboxResCenterActivityIntent(this);
                bundle.putInt(InboxRouter.EXTRA_STATE_TAB_POSITION,
                        TkpdState.InboxResCenter.RESO_BUYER);
                title = this.getString(R.string.title_notif_rescenter);
                ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                description = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                break;
            case TkpdState.GCMServiceState.GCM_RESCENTER_BUYER_REPLY:
                componentName = InboxRouter.getInboxResCenterActivityComponentName(this);
                intent = InboxRouter.getInboxResCenterActivityIntent(this);
                bundle.putInt(InboxRouter.EXTRA_STATE_TAB_POSITION,
                        TkpdState.InboxResCenter.RESO_BUYER);
                title = this.getString(R.string.title_notif_rescenter);
                ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                description = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                break;
            case TkpdState.GCMServiceState.GCM_RESCENTER_SELLER_REPLY:
                componentName = InboxRouter.getInboxResCenterActivityComponentName(this);
                intent = InboxRouter.getInboxResCenterActivityIntent(this);
                bundle.putInt(InboxRouter.EXTRA_STATE_TAB_POSITION,
                        TkpdState.InboxResCenter.RESO_MINE);
                title = this.getString(R.string.title_notif_rescenter);
                ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                description = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                break;
            case TkpdState.GCMServiceState.GCM_RESCENTER_SELLER_AGREE:
                componentName = InboxRouter.getInboxResCenterActivityComponentName(this);
                intent = InboxRouter.getInboxResCenterActivityIntent(this);
                bundle.putInt(InboxRouter.EXTRA_STATE_TAB_POSITION,
                        TkpdState.InboxResCenter.RESO_MINE);
                title = this.getString(R.string.title_notif_rescenter);
                ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                description = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                break;
            case TkpdState.GCMServiceState.GCM_RESCENTER_BUYER_AGREE:
                componentName = InboxRouter.getInboxResCenterActivityComponentName(this);
                intent = InboxRouter.getInboxResCenterActivityIntent(this);
                bundle.putInt(InboxRouter.EXTRA_STATE_TAB_POSITION,
                        TkpdState.InboxResCenter.RESO_BUYER);
                title = this.getString(R.string.title_notif_rescenter);
                ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                description = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                break;
            case TkpdState.GCMServiceState.GCM_RESCENTER_ADMIN_BUYER_REPLY:
                componentName = InboxRouter.getInboxResCenterActivityComponentName(this);
                intent = InboxRouter.getInboxResCenterActivityIntent(this);
                bundle.putInt(InboxRouter.EXTRA_STATE_TAB_POSITION,
                        TkpdState.InboxResCenter.RESO_MINE);
                title = this.getString(R.string.title_notif_rescenter);
                ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                description = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                break;
            case TkpdState.GCMServiceState.GCM_RESCENTER_ADMIN_SELLER_REPLY:
                componentName = InboxRouter.getInboxResCenterActivityComponentName(this);
                intent = InboxRouter.getInboxResCenterActivityIntent(this);
                bundle.putInt(InboxRouter.EXTRA_STATE_TAB_POSITION,
                        TkpdState.InboxResCenter.RESO_BUYER);
                title = this.getString(R.string.title_notif_rescenter);
                ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                description = data.getString(ARG_NOTIFICATION_DESCRIPTION);
                break;
            default:
                return;
        }
        if (intent == null) {
            intent = new Intent(this, targetClass);
        }
        createNotification(intent, targetClass, title, ticker, description, bundle, data, componentName);
    }

    private void sendMarketingPromoCode(Bundle data) {
        sendNotification(data);
    }

    private void sendGeneralNotification(Bundle data) {
        sendNotification(data);
    }

    private void createNotification(Intent intent, Class<?> targetClass, String title, String ticker,
                                    String desc, Bundle bundle, final Bundle data, ComponentName componentName) {
        NotificationManager mNotificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        final ArrayList<String> contents = new ArrayList<>(), descriptions = new ArrayList<>();
        final ArrayList<Integer> codes = new ArrayList<>();
        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
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
        } else if (Integer.parseInt(data.getString(ARG_NOTIFICATION_CODE)) == TkpdState.GCMServiceState.GCM_PROMO
                || Integer.parseInt(data.getString(ARG_NOTIFICATION_CODE)) == TkpdState.GCMServiceState.GCM_GENERAL) {
            bigStyle = new NotificationCompat.BigTextStyle();
            mBuilder.setContentTitle(title);
            mBuilder.setContentText(desc);
            bigStyle.bigText(desc);
            mBuilder.setStyle(bigStyle);
            mBuilder.setTicker(ticker);
        } else {
            targetClass = NotificationCenter.class;
            bundle.putInt("notif_call", 0);
            inboxStyle = new NotificationCompat.InboxStyle();
            inboxStyle.setBigContentTitle(getString(R.string.title_new_notif_general));
            for (int i = 0; i < contents.size(); i++) {
                inboxStyle.addLine(contents.get(i));
            }
            mBuilder.setContentTitle(getString(R.string.title_new_notif_general));
            mBuilder.setContentText(desc);
            mBuilder.setStyle(inboxStyle);
            mBuilder.setTicker(ticker);
        }

        if (!TextUtils.isEmpty(data.getString(ARG_NOTIFICATION_IMAGE))) {
            ImageHandler.loadImageBitmapNotification(
                    this,
                    data.getString(ARG_NOTIFICATION_IMAGE), new OnGetFileListener() {
                        @Override
                        public void onFileReady(File file) {
                            NotificationCompat.BigPictureStyle bigStyle =
                                    new NotificationCompat.BigPictureStyle();

                            bigStyle.bigPicture(
                                    Bitmap.createScaledBitmap(
                                            BitmapFactory.decodeFile(file.getAbsolutePath()),
                                            getResources().getDimensionPixelSize(R.dimen.notif_width),
                                            getResources().getDimensionPixelSize(R.dimen.notif_height),
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
                    this,
                    data.getString(ARG_NOTIFICATION_ICON), new OnGetFileListener() {
                        @Override
                        public void onFileReady(File file) {
                            mBuilder.setLargeIcon(
                                    Bitmap.createScaledBitmap(
                                            BitmapFactory.decodeFile(file.getAbsolutePath()),
                                            getResources().getDimensionPixelSize(R.dimen.icon_size),
                                            getResources().getDimensionPixelSize(R.dimen.icon_size),
                                            true
                                    )
                            );
                        }
                    }
            );
        } else {
            mBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), getDrawableLargeIcon()));
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("from_notif", true);
        intent.putExtra("unread", false);
        if (Integer.parseInt(data.getString(ARG_NOTIFICATION_CODE)) == TkpdState.GCMServiceState.GCM_PROMO
                || Integer.parseInt(data.getString(ARG_NOTIFICATION_CODE)) == TkpdState.GCMServiceState.GCM_GENERAL) {
            Bundle dataLocalytics = this.mNotificationAnalyticsReceiver.buildAnalyticNotificationData(data);
            intent.putExtras(dataLocalytics);
        } else {
            intent.putExtras(bundle);
        }

        if (cacheManager.isAllowBell()) {
            mBuilder.setSound(cacheManager.getSoundUri());
            if (cacheManager.isVibrate()) {
                mBuilder.setVibrate(pattern);
            }
        }
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        if (componentName != null) {
            stackBuilder.addParentStack(componentName);
        } else if (targetClass != null) {
            stackBuilder.addParentStack(targetClass);
        }
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_CANCEL_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        Notification notif = mBuilder.build();
        if (cacheManager.isVibrate() && cacheManager.isAllowBell())
            notif.defaults |= Notification.DEFAULT_VIBRATE;
        mNotificationManager.notify(100, notif);
    }

    private void createNotification(Bundle data, Class<?> intentClass) {
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
                    (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(getDrawableIcon())
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), getDrawableLargeIcon()))
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
                    mBuilder.setVibrate(pattern);
                }
            }

            Intent intent = createIntent(intentClass, data);

            if (data.getInt("keylogin1", -99) != -99) {
                intent.putExtra(
                        com.tokopedia.core.session.presenter.Session.WHICH_FRAGMENT_KEY, data.getInt("keylogin1")
                );
                intent.putExtra(SessionView.MOVE_TO_CART_KEY, data.getInt("keylogin2"));
            }

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(getBaseContext());
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

    private Intent createIntent(Class<?> intentClass, Bundle data) {
        Intent intent = new Intent(getBaseContext(), intentClass);

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
                Bundle bundle = new Bundle();
                bundle.putString("d_id", dep_id);
                bundle.putInt("state", 0);
                intent.putExtras(bundle);
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

    private void createAndShowImageNotification(final Bundle data, final Class<?> intentClass)
            throws NullPointerException {

        final NotificationManager mNotificationManager =
                (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);

        final Notification.Builder mBuilder = new Notification.Builder(getBaseContext())
                .setSmallIcon(getDrawableIcon())
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), getDrawableLargeIcon()))
                .setAutoCancel(true);

        if (!TextUtils.isEmpty(data.getString(ARG_NOTIFICATION_ICON))) {
            ImageHandler.loadImageBitmapNotification(
                    this,
                    data.getString(ARG_NOTIFICATION_ICON), new OnGetFileListener() {
                        @Override
                        public void onFileReady(File file) {
                            mBuilder.setLargeIcon(
                                    Bitmap.createScaledBitmap(
                                            BitmapFactory.decodeFile(file.getAbsolutePath()),
                                            getResources().getDimensionPixelSize(R.dimen.icon_size),
                                            getResources().getDimensionPixelSize(R.dimen.icon_size),
                                            true
                                    )
                            );
                        }
                    }
            );
        } else {
            mBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), getDrawableIcon()));
        }

        ImageHandler.loadImageBitmapNotification(
                getApplicationContext(),
                data.getString(ARG_NOTIFICATION_IMAGE),
                new OnGetFileListener() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onFileReady(File file) {
                        Notification.BigPictureStyle bigStyle = new Notification.BigPictureStyle();
                        bigStyle.bigPicture(
                                Bitmap.createScaledBitmap(
                                        BitmapFactory.decodeFile(file.getAbsolutePath()),
                                        getResources().getDimensionPixelSize(R.dimen.notif_width),
                                        getResources().getDimensionPixelSize(R.dimen.notif_height),
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
                                mBuilder.setVibrate(pattern);
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

                        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getBaseContext());
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

    private void sendUpdateAppsNotification(Bundle data) {
        if (MainApplication.getCurrentVersion(this) < Integer.parseInt(data.getString("app_version", "0"))) {
            NotificationManager mNotificationManager =
                    (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(getDrawableIcon())
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), getDrawableLargeIcon()))
                    .setAutoCancel(true);
            NotificationCompat.BigTextStyle bigStyle = new NotificationCompat.BigTextStyle();
            mBuilder.setContentTitle(data.getString(ARG_NOTIFICATION_UPDATE_APPS_TITLE));
            mBuilder.setContentText(getString(R.string.msg_new_update));
            mBuilder.setStyle(bigStyle);
            mBuilder.setTicker(data.getString(ARG_NOTIFICATION_UPDATE_APPS_TITLE));

            if (cacheManager.isAllowBell()) {
                mBuilder.setSound(cacheManager.getSoundUri());
                if (cacheManager.isVibrate()) {
                    mBuilder.setVibrate(pattern);
                }
            }

            Intent notificationIntent = new Intent(Intent.ACTION_VIEW);
            notificationIntent.setData(Uri.parse(EXTRA_PLAYSTORE_URL));

            PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
            mBuilder.setContentIntent(contentIntent);
            Notification notif = mBuilder.build();
            if (cacheManager.isVibrate() && cacheManager.isAllowBell())
                notif.defaults |= Notification.DEFAULT_VIBRATE;
            mNotificationManager.notify(TkpdState.GCMServiceState.GCM_UPDATE_NOTIFICATION, notif);

            cacheManager.updateUpdateAppStatus(data);
        }
    }

    private int getDrawableIcon() {
        if (GlobalConfig.isSellerApp())
            return R.drawable.ic_stat_notify2;
        else
            return R.drawable.ic_stat_notify;
    }

    private int getDrawableLargeIcon() {
        if (GlobalConfig.isSellerApp())
            return R.drawable.qc_launcher2;
        else
            return R.drawable.qc_launcher;
    }

    private void resetNotificationStatus(Bundle data) {
        if (SessionHandler.getLoginID(this).equals(data.getString("to_user_id"))) {
            switch (Integer.parseInt(data.getString(ARG_NOTIFICATION_CODE))) {
                case TkpdState.GCMServiceState.GCM_DRAWER_UPDATE:
                    MainApplication.resetDrawerStatus(true);
                    break;
                case TkpdState.GCMServiceState.GCM_CART_UPDATE:
                    MainApplication.resetCartStatus(true);
                    break;
                default:
                    MainApplication.resetNotificationStatus(true);
                    break;
            }
        }
    }

    public interface OnGetFileListener {
        void onFileReady(File file);
    }
}
