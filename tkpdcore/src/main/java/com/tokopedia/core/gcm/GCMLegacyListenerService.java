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

import com.google.android.gms.gcm.GcmListenerService;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.ImageHandler;
import com.tkpd.library.utils.URLParser;
import com.tokopedia.core.ManageGeneral;
import com.tokopedia.core.NotificationCenter;
import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.database.manager.DbManagerImpl;
import com.tokopedia.core.database.model.CategoryDB;
import com.tokopedia.core.gcm.utils.GCMUtils;
import com.tokopedia.core.inboxmessage.activity.InboxMessageActivity;
import com.tokopedia.core.inboxreputation.activity.InboxReputationActivity;
import com.tokopedia.core.router.CustomerRouter;
import com.tokopedia.core.router.InboxRouter;
import com.tokopedia.core.router.SellerRouter;
import com.tokopedia.core.router.SessionRouter;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.router.home.SimpleHomeRouter;
import com.tokopedia.core.router.transactionmodule.TransactionCartRouter;
import com.tokopedia.core.router.transactionmodule.TransactionPurchaseRouter;
import com.tokopedia.core.session.presenter.SessionView;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdState;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Herdi_WORK on 15.12.16.
 */

public class GCMLegacyListenerService extends GcmListenerService{

    private GCMLegacyCacheManager gcmCache;
    private NotificationListener listener;
    private long[] pattern = {500, 500, 500, 500, 500, 500, 500, 500, 500};

    public interface NotificationListener {
        void onGetNotif();

        void onRefreshCart(int status);
    }

    @Override
    public void onMessageReceived(String from, Bundle data) {
        CommonUtils.dumper(data.toString());
        gcmCache = new GCMLegacyCacheManager(this);
        if (gcmCache.isAllowToHandleNotif(data)) {
            gcmCache.setCache();
            tunnelData(data);
        }
        setAnalyticHandler(data.getString("tkp_code"));
    }

    private void setAnalyticHandler(String code) {
        String eventName = "event : gcm notification";
        Map<String, String> params = new HashMap<>();
        params.put("Notification code", code);
        TrackingUtils.eventLocaNotification(eventName, params);
    }

    private void tunnelData(Bundle data) {
        switch (GCMUtils.getCode(data)) {
            case TkpdState.GCMServiceState.GCM_HOT_LIST:
                // TODO Handle with latest code
//                prepareAndExecuteDedicationNotification(data, BrowseHotDetail.class);
                break;
            case TkpdState.GCMServiceState.GCM_UPDATE_NOTIFICATION:
                sendUpdateNotification(data);
                break;
            case TkpdState.GCMServiceState.GCM_PROMO:
                sendMarketingPromoCode(data);
                break;
            case TkpdState.GCMServiceState.GCM_GENERAL:
                sendGeneralNotification(data);
                break;
            case TkpdState.GCMServiceState.GCM_CATEGORY:
                // TODO Handle with latest code
//                prepareAndExecuteDedicationNotification(data, BrowseCategory.class);
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
                if (SessionHandler.isV4Login(this))
                    try {
                        createNotification(data, TransactionCartRouter.createInstanceCartClass());
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                break;
            case TkpdState.GCMServiceState.GCM_WISHLIST:
                if (SessionHandler.isV4Login(this))
//                    prepareAndExecuteDedicationNotification(data, SimpleHomeActivity.class);
                    createNotification(data, SimpleHomeRouter.getSimpleHomeActivityClass());
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
                if (SessionHandler.isV4Login(this) && SessionHandler.getLoginID(this).equals(data.getString("to_user_id"))) {
                    resetData(data);
                    if (MainApplication.currentActivity() == null && Integer.parseInt(data.getString("tkp_code")) < 500) {
                        sendNotification(data);
                    } else {
                        listener = (NotificationListener) MainApplication.currentActivity();
                        if (listener != null) {
                            if (Integer.parseInt(data.getString("tkp_code")) == TkpdState.GCMServiceState.GCM_CART_UPDATE) {
                                listener.onRefreshCart(data.getInt("is_cart_exists", 0));
                            } else {
                                sendNotification(data);
                            }
                        } else {
                            if (Integer.parseInt(data.getString("tkp_code")) < 500) {
                                sendNotification(data);
                            }
                        }
                    }
                    gcmCache.resetCache(data);
                }
                break;
        }
    }

    private void sendNotification(Bundle data) {
        int tkpCode = Integer.parseInt(data.getString("tkp_code"));
        /**
         * Use this code to exclude deprecated code which still sent from server
         * if (!CheckSettings(tkpCode) && isValidForSellerApp(tkpCode) && !isDeprecated(tkpCode)) {
         */
        if (!gcmCache.checkSettings(tkpCode) && GCMUtils.isValidForSellerApp(tkpCode)) {
            return;
        }

        Class<?> resultclass = null;
        String title = null;
        String ticker = null;
        String desc = null;
        Intent intent = null;
        Bundle bundle = new Bundle();
        ComponentName componentName = null;
        switch (Integer.parseInt(data.getString("tkp_code"))) {
            case TkpdState.GCMServiceState.GCM_MESSAGE:
                componentName = InboxRouter.getInboxMessageActivityComponentName(this);
                intent = InboxRouter.getInboxMessageActivityIntent(this);
                //bundle.putInt("notif_call", NotificationCode);
                title = data.getString("counter") + " " + this.getString(R.string.title_new_message);
                ticker = data.getString("desc");
                desc = data.getString("desc");
                break;
            case TkpdState.GCMServiceState.GCM_TALK:
                intent = InboxRouter.getInboxTalkActivityIntent(this);
                //bundle.putInt("notif_call", NotificationCode);
                title = data.getString("counter") + " " + this.getString(R.string.title_new_talk);
                ticker = data.getString("desc");
                desc = data.getString("desc");
                break;
            case TkpdState.GCMServiceState.GCM_REVIEW:
                resultclass = InboxReputationActivity.class;
                //bundle.putInt("notif_call", NotificationCode);
                title = data.getString("counter") + " " + this.getString(R.string.title_new_review);
                ticker = data.getString("desc");
                desc = data.getString("desc");
                break;
            case TkpdState.GCMServiceState.GCM_REVIEW_EDIT:
                resultclass = InboxReputationActivity.class;
                //bundle.putInt("notif_call", NotificationCode);
                title = this.getString(R.string.title_edit_review);
                ticker = data.getString("desc");
                desc = data.getString("desc");
                break;
            case TkpdState.GCMServiceState.GCM_REVIEW_REPLY:
                resultclass = InboxReputationActivity.class;
                //bundle.putInt("notif_call", NotificationCode);
                title = this.getString(R.string.title_reply_review);
                ticker = data.getString("desc");
                desc = data.getString("desc");
                break;
            case TkpdState.GCMServiceState.GCM_TICKET:
                componentName = InboxRouter.getInboxticketActivityComponentName(this);
                intent = InboxRouter.getInboxTicketActivityIntent(this);
                //bundle.putInt("notif_call", NotificationCode);
                title = this.getString(R.string.title_new_ticket);
                ticker = data.getString("desc");
                desc = data.getString("desc");
                break;
            case TkpdState.GCMServiceState.GCM_RES_CENTER:
                intent = InboxRouter.getInboxResCenterActivityIntent(this);
                title = this.getString(R.string.title_new_rescenter);
                ticker = data.getString("desc");
                desc = data.getString("desc");
                break;
            case TkpdState.GCMServiceState.GCM_NEWORDER:
//                resultclass = ShopTransactionV2.class;
                componentName = SellerRouter.getActivitySellingTransactionName(this);
                intent = SellerRouter.getActivitySellingTransaction(this);
                //bundle.putInt("notif_call", NotificationCode);
                title = data.getString("counter") + " " + this.getString(R.string.title_new_order);
                desc = getString(R.string.msg_new_order);
                ticker = getString(R.string.msg_new_order);
                break;
            case TkpdState.GCMServiceState.GCM_PROMO:
                title = data.getString("title");
                desc = data.getString("desc");
                ticker = data.getString("desc");
//                resultclass = ParentIndexHome.class;
                resultclass = HomeRouter.getHomeActivityClass();
                break;
            case TkpdState.GCMServiceState.GCM_GENERAL:
                title = data.getString("title");
                desc = data.getString("desc");
                ticker = data.getString("desc");
//                resultclass = ParentIndexHome.class;
                resultclass = HomeRouter.getHomeActivityClass();
                break;
            case TkpdState.GCMServiceState.GCM_REPUTATION_SMILEY:
                resultclass = InboxReputationActivity.class;
                //bundle.putInt("notif_call", NotificationCode);
                title = this.getString(R.string.title_get_reputation);
                ticker = data.getString("desc");
                desc = data.getString("desc");
                break;
            case TkpdState.GCMServiceState.GCM_REPUTATION_EDIT_SMILEY:
                resultclass = InboxReputationActivity.class;
                //bundle.putInt("notif_call", NotificationCode);
                title = this.getString(R.string.title_get_edit_reputation);
                ticker = data.getString("desc");
                desc = data.getString("desc");
                break;
            case TkpdState.GCMServiceState.GCM_REPUTATION_SMILEY_TO_BUYER:
                resultclass = InboxReputationActivity.class;
                //bundle.putInt("notif_call", NotificationCode);
                title = this.getString(R.string.title_get_reputation);
                ticker = data.getString("desc");
                desc = data.getString("desc");
                break;
            case TkpdState.GCMServiceState.GCM_REPUTATION_EDIT_SMILEY_TO_BUYER:
                resultclass = InboxReputationActivity.class;
                //bundle.putInt("notif_call", NotificationCode);
                title = this.getString(R.string.title_get_edit_reputation);
                ticker = data.getString("desc");
                desc = data.getString("desc");
                break;
            case TkpdState.GCMServiceState.GCM_REPUTATION_SMILEY_TO_SELLER:
                resultclass = InboxReputationActivity.class;
                //bundle.putInt("notif_call", NotificationCode);
                title = this.getString(R.string.title_get_reputation);
                ticker = data.getString("desc");
                desc = data.getString("desc");
                break;
            case TkpdState.GCMServiceState.GCM_REPUTATION_EDIT_SMILEY_TO_SELLER:
                resultclass = InboxReputationActivity.class;
                //bundle.putInt("notif_call", NotificationCode);
                title = this.getString(R.string.title_get_edit_reputation);
                ticker = data.getString("desc");
                desc = data.getString("desc");
                break;
            case TkpdState.GCMServiceState.GCM_PURCHASE_VERIFIED:
                componentName = TransactionPurchaseRouter.getPurchaseActivityComponentName(this);
                intent = TransactionPurchaseRouter.createIntentPurchaseActivity(this);
                bundle.putInt(TransactionPurchaseRouter.EXTRA_STATE_TAB_POSITION,
                        TransactionPurchaseRouter.TAB_POSITION_PURCHASE_STATUS_ORDER);
                title = this.getString(R.string.title_notif_purchase_confirmed);
                ticker = data.getString("desc");
                desc = data.getString("desc");
                break;
            case TkpdState.GCMServiceState.GCM_PURCHASE_ACCEPTED:
                componentName = TransactionPurchaseRouter.getPurchaseActivityComponentName(this);
                intent = TransactionPurchaseRouter.createIntentPurchaseActivity(this);
                bundle.putInt(TransactionPurchaseRouter.EXTRA_STATE_TAB_POSITION,
                        TransactionPurchaseRouter.TAB_POSITION_PURCHASE_STATUS_ORDER);
                title = this.getString(R.string.title_notif_purchase_accepted);
                ticker = data.getString("desc");
                desc = data.getString("desc");
                break;
            case TkpdState.GCMServiceState.GCM_PURCHASE_PARTIAL_PROCESSED:
                componentName = TransactionPurchaseRouter.getPurchaseActivityComponentName(this);
                intent = TransactionPurchaseRouter.createIntentPurchaseActivity(this);
                bundle.putInt(TransactionPurchaseRouter.EXTRA_STATE_TAB_POSITION,
                        TransactionPurchaseRouter.TAB_POSITION_PURCHASE_STATUS_ORDER);
                title = this.getString(R.string.title_notif_purchase_partial_accepted);
                ticker = data.getString("desc");
                desc = data.getString("desc");
                break;
            case TkpdState.GCMServiceState.GCM_PURCHASE_REJECTED:
                componentName = TransactionPurchaseRouter.getPurchaseActivityComponentName(this);
                intent = TransactionPurchaseRouter.createIntentPurchaseActivity(this);
                bundle.putInt(TransactionPurchaseRouter.EXTRA_STATE_TAB_POSITION,
                        TransactionPurchaseRouter.TAB_POSITION_PURCHASE_ALL_ORDER);
                bundle.putString(TransactionPurchaseRouter.EXTRA_STATE_TX_FILTER,
                        TransactionPurchaseRouter.TRANSACTION_CANCELED_FILTER_ID);
                title = this.getString(R.string.title_notif_purchase_rejected);
                ticker = data.getString("desc");
                desc = data.getString("desc");
                break;
            case TkpdState.GCMServiceState.GCM_PURCHASE_DELIVERED:
                componentName = TransactionPurchaseRouter.getPurchaseActivityComponentName(this);
                intent = TransactionPurchaseRouter.createIntentPurchaseActivity(this);
                bundle.putInt(TransactionPurchaseRouter.EXTRA_STATE_TAB_POSITION,
                        TransactionPurchaseRouter.TAB_POSITION_PURCHASE_DELIVER_ORDER);
                title = this.getString(R.string.title_notif_purchase_delivered);
                ticker = data.getString("desc");
                desc = data.getString("desc");
                break;
            case TkpdState.GCMServiceState.GCM_PURCHASE_DISPUTE:
                componentName = InboxRouter.getInboxResCenterActivityComponentName(this);
                intent = InboxRouter.getInboxResCenterActivityIntent(this);
                bundle.putInt(InboxRouter.EXTRA_STATE_TAB_POSITION,
                        TkpdState.InboxResCenter.RESO_BUYER);
                title = this.getString(R.string.title_notif_rescenter);
                ticker = data.getString("desc");
                desc = data.getString("desc");
                break;
            case TkpdState.GCMServiceState.GCM_RESCENTER_BUYER_REPLY:
                componentName = InboxRouter.getInboxResCenterActivityComponentName(this);
                intent = InboxRouter.getInboxResCenterActivityIntent(this);
                bundle.putInt(InboxRouter.EXTRA_STATE_TAB_POSITION,
                        TkpdState.InboxResCenter.RESO_BUYER);
                title = this.getString(R.string.title_notif_rescenter);
                ticker = data.getString("desc");
                desc = data.getString("desc");
                break;
            case TkpdState.GCMServiceState.GCM_RESCENTER_SELLER_REPLY:
                componentName = InboxRouter.getInboxResCenterActivityComponentName(this);
                intent = InboxRouter.getInboxResCenterActivityIntent(this);
                bundle.putInt(InboxRouter.EXTRA_STATE_TAB_POSITION,
                        TkpdState.InboxResCenter.RESO_MINE);
                title = this.getString(R.string.title_notif_rescenter);
                ticker = data.getString("desc");
                desc = data.getString("desc");
                break;
            case TkpdState.GCMServiceState.GCM_RESCENTER_SELLER_AGREE:
                componentName = InboxRouter.getInboxResCenterActivityComponentName(this);
                intent = InboxRouter.getInboxResCenterActivityIntent(this);
                bundle.putInt(InboxRouter.EXTRA_STATE_TAB_POSITION,
                        TkpdState.InboxResCenter.RESO_MINE);
                title = this.getString(R.string.title_notif_rescenter);
                ticker = data.getString("desc");
                desc = data.getString("desc");
                break;
            case TkpdState.GCMServiceState.GCM_RESCENTER_BUYER_AGREE:
                componentName = InboxRouter.getInboxResCenterActivityComponentName(this);
                intent = InboxRouter.getInboxResCenterActivityIntent(this);
                bundle.putInt(InboxRouter.EXTRA_STATE_TAB_POSITION,
                        TkpdState.InboxResCenter.RESO_BUYER);
                title = this.getString(R.string.title_notif_rescenter);
                ticker = data.getString("desc");
                desc = data.getString("desc");
                break;
            case TkpdState.GCMServiceState.GCM_RESCENTER_ADMIN_BUYER_REPLY:
                componentName = InboxRouter.getInboxResCenterActivityComponentName(this);
                intent = InboxRouter.getInboxResCenterActivityIntent(this);
                bundle.putInt(InboxRouter.EXTRA_STATE_TAB_POSITION,
                        TkpdState.InboxResCenter.RESO_MINE);
                title = this.getString(R.string.title_notif_rescenter);
                ticker = data.getString("desc");
                desc = data.getString("desc");
                break;
            case TkpdState.GCMServiceState.GCM_RESCENTER_ADMIN_SELLER_REPLY:
                componentName = InboxRouter.getInboxResCenterActivityComponentName(this);
                intent = InboxRouter.getInboxResCenterActivityIntent(this);
                bundle.putInt(InboxRouter.EXTRA_STATE_TAB_POSITION,
                        TkpdState.InboxResCenter.RESO_BUYER);
                title = this.getString(R.string.title_notif_rescenter);
                ticker = data.getString("desc");
                desc = data.getString("desc");
                break;
            default:
                return;
        }
        if (intent == null) {
            intent = new Intent(this, resultclass);
        }
        createNotification(intent, resultclass, title, ticker, desc, bundle, data, componentName);
    }

    private void createNotification(Intent intent, Class<?> resultclass, String title, String ticker, String desc,
                                    Bundle bundle, final Bundle data, ComponentName componentName) {
        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        final ArrayList<String> Content =  new ArrayList<>(), Desc =  new ArrayList<>();
        final ArrayList<Integer>  Code = new ArrayList<>();
        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(getDrawableIcon())
                .setAutoCancel(true);
        NotificationCompat.InboxStyle inboxStyle;
        NotificationCompat.BigTextStyle bigStyle;

        gcmCache.processNotifData(data, title, desc, new FCMCacheManager.CacheProcessListener() {
            @Override
            public void onDataProcessed(ArrayList<String> content, ArrayList<String> desc, ArrayList<Integer> code) {
                Content.addAll(content);
                Code.addAll(code);
                Desc.addAll(desc);
            }
        });
        if (Code.size() == 1) {
            bigStyle = new NotificationCompat.BigTextStyle();
            mBuilder.setContentTitle(title);
            mBuilder.setContentText(desc);
            bigStyle.bigText(desc);
            mBuilder.setStyle(bigStyle);
            mBuilder.setTicker(ticker);
        } else if (Integer.parseInt(data.getString("tkp_code")) == TkpdState.GCMServiceState.GCM_PROMO || Integer.parseInt(data.getString("tkp_code")) == TkpdState.GCMServiceState.GCM_GENERAL) {
            bigStyle = new NotificationCompat.BigTextStyle();
            mBuilder.setContentTitle(title);
            mBuilder.setContentText(desc);
            bigStyle.bigText(desc);
            mBuilder.setStyle(bigStyle);
            mBuilder.setTicker(ticker);
        } else {
            resultclass = NotificationCenter.class;
            bundle.putInt("notif_call", 0);
            inboxStyle = new NotificationCompat.InboxStyle();
            inboxStyle.setBigContentTitle(getString(R.string.title_new_notif_general));
            for (int i = 0; i < Content.size(); i++) {
                inboxStyle.addLine(Content.get(i));
            }
            mBuilder.setContentTitle(getString(R.string.title_new_notif_general));
            mBuilder.setContentText(desc);
            mBuilder.setStyle(inboxStyle);
            mBuilder.setTicker(ticker);
        }


        if (!TextUtils.isEmpty(data.getString("url_img"))) {

            ImageHandler.loadImageBitmapNotification(
                    this,
                    data.getString("url_img"), new BuildAndShowNotification.OnGetFileListener() {
                        @Override
                        public void onFileReady(File file) {
                            NotificationCompat.BigPictureStyle bigStyle = new NotificationCompat.BigPictureStyle();
                            bigStyle.bigPicture(Bitmap.createScaledBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()), getResources().getDimensionPixelSize(R.dimen.notif_width), getResources().getDimensionPixelSize(R.dimen.notif_height), true));
                            bigStyle.setBigContentTitle(data.getString("title"));
                            bigStyle.setSummaryText(data.getString("desc"));

                            mBuilder.setStyle(bigStyle);
                        }
                    }
            );

        } else {

        }

        if (!TextUtils.isEmpty(data.getString("url_icon"))) {
            ImageHandler.loadImageBitmapNotification(
                    this,
                        data.getString("url_icon"),
                    new BuildAndShowNotification.OnGetFileListener() {
                        @Override
                        public void onFileReady(File file) {
                            mBuilder.setLargeIcon(Bitmap.createScaledBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()), getResources().getDimensionPixelSize(R.dimen.icon_size), getResources().getDimensionPixelSize(R.dimen.icon_size), true));
                        }
                    }
            );
        } else {
            mBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), getDrawableLargeIcon()));
        }


        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("from_notif", true);
        intent.putExtra("unread", false);
        if (Integer.parseInt(data.getString("tkp_code")) == TkpdState.GCMServiceState.GCM_PROMO || Integer.parseInt(data.getString("tkp_code")) == TkpdState.GCMServiceState.GCM_GENERAL) {
            Bundle dataLocalytics = localyticsIntent(data);
            intent.putExtras(dataLocalytics);
        } else {
            intent.putExtras(bundle);
        }

        if (gcmCache.isAllowBell()) {
            mBuilder.setSound(gcmCache.getSoundUri());
            if (gcmCache.isVibrate()) {
                mBuilder.setVibrate(pattern);
            }
        }
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        if (componentName != null) {
            stackBuilder.addParentStack(componentName);
        } else if (resultclass != null) {
            stackBuilder.addParentStack(resultclass);
        }
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_CANCEL_CURRENT);
        //PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);
        mBuilder.setContentIntent(resultPendingIntent);
        Notification notif = mBuilder.build();
        if (gcmCache.isVibrate() && gcmCache.isAllowBell()) notif.defaults |= Notification.DEFAULT_VIBRATE;
        mNotificationManager.notify(100, notif);
    }

    private void sendMarketingPromoCode(Bundle data) {
        sendNotification(data);
    }

    private void sendGeneralNotification(Bundle data) {
        sendNotification(data);
    }

    private void createNotification(Bundle data, Class<?> intentClass) {
        if (!gcmCache.checkSettings(Integer.parseInt(data.getString("tkp_code")))) {
            return;
        }
        if (data.getString("url_img") != null) {
            createImageNotification(data, intentClass);
        } else {
            createTextNotification(data, intentClass);
        }
    }

    private void createTextNotification(Bundle data, Class<?> intentClass) {
        try {
            NotificationManager mNotificationManager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(getDrawableIcon())
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), getDrawableLargeIcon()))
                    .setAutoCancel(true);
            NotificationCompat.BigTextStyle bigStyle;
            bigStyle = new NotificationCompat.BigTextStyle();
            mBuilder.setContentTitle(data.getString("title"));
            mBuilder.setContentText(data.getString("desc"));
            bigStyle.bigText(data.getString("desc"));
            mBuilder.setStyle(bigStyle);
            mBuilder.setTicker(data.getString("desc"));

            if (gcmCache.isAllowBell()) {
                mBuilder.setSound(gcmCache.getSoundUri());
                if (gcmCache.isVibrate()) {
                    mBuilder.setVibrate(pattern);
                }
            }

            Intent intent = createIntent(intentClass, data);

            if (data.getInt("keylogin1", -99) != -99) {
                intent.putExtra(com.tokopedia.core.session.presenter.Session.WHICH_FRAGMENT_KEY, data.getInt("keylogin1"));
                intent.putExtra(SessionView.MOVE_TO_CART_KEY, data.getInt("keylogin2"));
            }

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(getBaseContext());
            stackBuilder.addParentStack(intentClass);
            stackBuilder.addNextIntent(intent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_CANCEL_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);
            Notification notif = mBuilder.build();
            if (gcmCache.isVibrate() && gcmCache.isAllowBell()) notif.defaults |= Notification.DEFAULT_VIBRATE;
            mNotificationManager.notify(100, notif);
        } catch (NullPointerException e) {
            CommonUtils.dumper("NotifTag : Null Value" + e.toString());
        }

    }

    private Intent createIntent(Class<?> intentClass, Bundle data) {
        Intent intent = new Intent(getBaseContext(), intentClass);

        try {
            data.putString("img_uri", data.getString("url_img"));
            data.putString("img_uri_600", data.getString("url_img"));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        switch (Integer.parseInt(data.getString("tkp_code"))) {
            case TkpdState.GCMServiceState.GCM_HOT_LIST: {
                URLParser urlp = new URLParser(data.getString("url"));
                data.putString("alias", urlp.getHotAlias());
                break;
            }
            case TkpdState.GCMServiceState.GCM_DEEPLINK: {
                intent.setData(Uri.parse(data.getString("url")));
                break;
            }
            case TkpdState.GCMServiceState.GCM_CATEGORY: {
                Uri uri = Uri.parse(data.getString("url"));
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
                Uri uri = Uri.parse(data.getString("url"));
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
        data = localyticsIntent(data);
        intent.putExtras(data);
        return intent;
    }

    private void createImageNotification(final Bundle data, final Class<?> intentClass) throws NullPointerException {

        final NotificationManager mNotificationManager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);

        final Notification.Builder mBuilder = new Notification.Builder(getBaseContext())
                .setSmallIcon(getDrawableIcon())
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), getDrawableLargeIcon()))
                .setAutoCancel(true);

        if (!TextUtils.isEmpty(data.getString("url_icon"))) {
            ImageHandler.loadImageBitmapNotification(
                    this,
                    data.getString("url_icon"), new BuildAndShowNotification.OnGetFileListener() {
                        @Override
                        public void onFileReady(File file) {
                            mBuilder.setLargeIcon(Bitmap.createScaledBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()), getResources().getDimensionPixelSize(R.dimen.icon_size), getResources().getDimensionPixelSize(R.dimen.icon_size), true));
                        }
                    }
            );
        } else {
            mBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), getDrawableIcon()));
        }

        ImageHandler.loadImageBitmapNotification(
                getApplicationContext(),
                data.getString("url_img"),
                new BuildAndShowNotification.OnGetFileListener() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onFileReady(File file) {
                        Notification.BigPictureStyle bigStyle = new Notification.BigPictureStyle();
                        bigStyle.bigPicture(Bitmap.createScaledBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()), getResources().getDimensionPixelSize(R.dimen.notif_width), getResources().getDimensionPixelSize(R.dimen.notif_height), true));
                        bigStyle.setSummaryText(data.getString("desc"));

                        mBuilder.setContentTitle(data.getString("title"));
                        mBuilder.setContentText(data.getString("desc"));
                        mBuilder.setStyle(bigStyle);
                        mBuilder.setTicker(data.getString("desc"));

                        if (gcmCache.isAllowBell()) {
                            mBuilder.setSound(gcmCache.getSoundUri());
                            if (gcmCache.isVibrate()) {
                                mBuilder.setVibrate(pattern);
                            }
                        }

                        Intent intent = createIntent(intentClass, data);

                        if (data.getInt("keylogin1", -99) != -99) {
                            intent.putExtra(com.tokopedia.core.session.presenter.Session.WHICH_FRAGMENT_KEY, data.getInt("keylogin1"));
                            intent.putExtra(SessionView.MOVE_TO_CART_KEY, data.getInt("keylogin2"));
                        }

                        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getBaseContext());
                        stackBuilder.addParentStack(intentClass);
                        stackBuilder.addNextIntent(intent);
                        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_CANCEL_CURRENT);
                        mBuilder.setContentIntent(resultPendingIntent);
                        Notification notif = mBuilder.build();
                        if (gcmCache.isVibrate() && gcmCache.isAllowBell())
                            notif.defaults |= Notification.DEFAULT_VIBRATE;
                        mNotificationManager.notify(Integer.parseInt(data.getString("tkp_code")), notif);
                    }
                }
        );

    }

    private void sendUpdateNotification(Bundle data) {
        if (MainApplication.getCurrentVersion(this) < Integer.parseInt(data.getString("app_version", "0"))) {
            NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(getDrawableIcon())
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), getDrawableLargeIcon()))
                    .setAutoCancel(true);
            NotificationCompat.BigTextStyle bigStyle = new NotificationCompat.BigTextStyle();
            mBuilder.setContentTitle(data.getString("title_update"));
            mBuilder.setContentText(getString(R.string.msg_new_update));
            mBuilder.setStyle(bigStyle);
            mBuilder.setTicker(data.getString("title_update"));

            if (gcmCache.isAllowBell()) {
                mBuilder.setSound(gcmCache.getSoundUri());
                if (gcmCache.isVibrate()) {
                    mBuilder.setVibrate(pattern);
                }
            }

            Intent notificationIntent = new Intent(Intent.ACTION_VIEW);
            notificationIntent.setData(Uri.parse("market://details?shopId=com.tokopedia.tkpd"));

            PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
            mBuilder.setContentIntent(contentIntent);
            Notification notif = mBuilder.build();
            if (gcmCache.isVibrate() && gcmCache.isAllowBell()) notif.defaults |= Notification.DEFAULT_VIBRATE;
            mNotificationManager.notify(TkpdState.GCMServiceState.GCM_UPDATE_NOTIFICATION, notif);

            gcmCache.updateUpdateAppStatus(data);
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

    private void resetData(Bundle data) {
        if (SessionHandler.getLoginID(this).equals(data.getString("to_user_id"))) {
            switch (Integer.parseInt(data.getString("tkp_code"))) {
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

    private Bundle localyticsIntent(Bundle data) {
        if (data != null && data.containsKey(AppEventTracking.LOCA.NOTIFICATION_BUNDLE)) {
            HashMap<String, Object> maps = new HashMap<>();
            try {
                JSONObject llObject = new JSONObject(data.getString(AppEventTracking.LOCA.NOTIFICATION_BUNDLE));
                Iterator<?> keys = llObject.keys();
                while (keys.hasNext()) {
                    String key = (String) keys.next();
                    maps.put(key, llObject.getString(key));
                }
                data.putSerializable(AppEventTracking.LOCA.NOTIFICATION_BUNDLE, maps);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            sendLocalyticsPushReceived(data);
        }
        return data;
    }

    private void sendLocalyticsPushReceived(Bundle data) {
        TrackingUtils.eventLocaNotificationReceived(data);
    }

    public interface OnGetFileListener {
        void onFileReady(File file);
    }

}
