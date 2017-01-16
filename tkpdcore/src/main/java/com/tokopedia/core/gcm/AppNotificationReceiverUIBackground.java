package com.tokopedia.core.gcm;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.tkpd.library.utils.URLParser;
import com.tokopedia.core.Cart;
import com.tokopedia.core.ManageGeneral;
import com.tokopedia.core.R;
import com.tokopedia.core.database.manager.DbManagerImpl;
import com.tokopedia.core.database.model.CategoryDB;
import com.tokopedia.core.gcm.model.NotificationPass;
import com.tokopedia.core.gcm.model.notification.NewDiscussionNotification;
import com.tokopedia.core.gcm.model.notification.NewMessageNotification;
import com.tokopedia.core.inboxmessage.activity.InboxMessageActivity;
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
import com.tokopedia.core.talk.inboxtalk.activity.InboxTalkActivity;
import com.tokopedia.core.util.RouterUtils;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_CODE;
import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_DESCRIPTION;
import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_IMAGE;
import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_TITLE;
import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_URL;

/**
 * @author by alvarisi on 1/9/17.
 */

public class AppNotificationReceiverUIBackground {
    private INotificationAnalyticsReceiver mNotificationAnalyticsReceiver;
    private FCMCacheManager cacheManager;
    private Context mContext;
    private BuildAndShowNotification mBuildAndShowNotification;

    public AppNotificationReceiverUIBackground(Application application) {
        cacheManager = new FCMCacheManager(application.getBaseContext());
        mContext = application.getBaseContext();
        mNotificationAnalyticsReceiver = new NotificationAnalyticsReceiver();
        mBuildAndShowNotification = new BuildAndShowNotification(mContext);
    }

    void prepareAndExecuteDedicationNotification(Bundle data) {
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
                componentName = InboxRouter.getActivityInboxResCenterName(mContext);
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
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("from_notif", true);
        intent.putExtra("unread", false);

        final NotificationPass notificationPass = new NotificationPass();
        notificationPass.title = title;
        notificationPass.ticker = ticker;
        notificationPass.description = description;
        notificationPass.componentNameParentStack = componentName;
        cacheManager.processNotifData(data, title, description, new FCMCacheManager.CacheProcessListener() {
            @Override
            public void onDataProcessed(ArrayList<String> content, ArrayList<String> desc, ArrayList<Integer> code) {
                notificationPass.savedNotificationContents.addAll(content);
                notificationPass.savedNotificationCodes.addAll(code);
                if (code.size() == 1) {
                    notificationPass.isAllowedBigStyle = true;
                }
            }
        });

        if (isPromoNotification(data)) {
            notificationPass.isAllowedBigStyle = true;
            intent.putExtras(this.mNotificationAnalyticsReceiver.buildAnalyticNotificationData(data));
        } else {
            intent.putExtras(bundle);
        }

        mBuildAndShowNotification.buildAndShowNotification(notificationPass, data, intent);

        Map<Integer, Visitable> dedicatedNotification = new HashMap<>();
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_MESSAGE, new NewMessageNotification(mContext));
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_TALK, new NewDiscussionNotification(mContext));

        TypeFactory typeFactory = new TypeFactoryForList();

        Visitable visitable = dedicatedNotification.get(
                Integer.parseInt(data.getString(ARG_NOTIFICATION_CODE))
        );

        visitable.proccessReceivedNotification(data);
    }

    private boolean isPromoNotification(Bundle data) {
        return Integer.parseInt(data.getString(ARG_NOTIFICATION_CODE)) == TkpdState.GCMServiceState.GCM_PROMO
                || Integer.parseInt(data.getString(ARG_NOTIFICATION_CODE)) == TkpdState.GCMServiceState.GCM_GENERAL;
    }

    void prepareAndExecuteUpdateAppNotification(Bundle data) {
        mBuildAndShowNotification.sendUpdateAppsNotification(data);
    }

    void prepareAndExecutePromoNotification(Bundle data) {
        Class<?> intentClass = null;
        switch (GCMUtils.getCode(data)) {
            case TkpdState.GCMServiceState.GCM_UPDATE_NOTIFICATION:
                mBuildAndShowNotification.sendUpdateAppsNotification(data);
                return;
            case TkpdState.GCMServiceState.GCM_PROMO:
                intentClass = HomeRouter.getHomeActivityClass();
                break;
            case TkpdState.GCMServiceState.GCM_GENERAL:
                intentClass = HomeRouter.getHomeActivityClass();
                break;
            case TkpdState.GCMServiceState.GCM_SHOP:
                intentClass = ShopInfoActivity.class;
                break;
            case TkpdState.GCMServiceState.GCM_DEEPLINK:
                if (CustomerRouter.getDeeplinkClass() != null) {
                    intentClass = CustomerRouter.getDeeplinkClass();
                } else {
                    return;
                }
                break;
            case TkpdState.GCMServiceState.GCM_CART:
                if (SessionHandler.isV4Login(mContext)) {
                    intentClass = Cart.class;
                } else {
                    return;
                }
                break;
            case TkpdState.GCMServiceState.GCM_WISHLIST:
                if (SessionHandler.isV4Login(mContext)) {
                    intentClass = SimpleHomeRouter.getSimpleHomeActivityClass();
                } else {
                    return;
                }
                break;
            case TkpdState.GCMServiceState.GCM_VERIFICATION:
                if (SessionHandler.isV4Login(mContext)) {
                    intentClass = ManageGeneral.class;
                } else {
                    data.putInt("keylogin1", TkpdState.DrawerPosition.LOGIN);
                    data.putInt("keylogin2", SessionView.HOME);
                    intentClass = SessionRouter.getLoginActivityClass();
                }
                break;
            default:
                return;
        }

        Intent intent = createIntent(intentClass, data);

        if (data.getInt("keylogin1", -99) != -99) {
            intent.putExtra(
                    com.tokopedia.core.session.presenter.Session.WHICH_FRAGMENT_KEY,
                    data.getInt("keylogin1")
            );
            intent.putExtra(
                    SessionView.MOVE_TO_CART_KEY,
                    data.getInt("keylogin2")
            );
        }

        NotificationPass notificationPass = new NotificationPass();
        notificationPass.title = data.getString(ARG_NOTIFICATION_TITLE);
        notificationPass.description = data.getString(ARG_NOTIFICATION_DESCRIPTION);
        notificationPass.ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
        notificationPass.componentNameParentStack = null;
        notificationPass.isAllowedBigStyle = true;
        notificationPass.classParentStack = intentClass;

        mBuildAndShowNotification.buildAndShowNotification(notificationPass, data, intent);
    }

    private Intent createIntent(Class<?> intentClass, Bundle data) {
        Intent intent = new Intent(mContext, intentClass);
        if (TextUtils.isEmpty(data.getString(ARG_NOTIFICATION_IMAGE))) {
            data.putString("img_uri", data.getString(ARG_NOTIFICATION_IMAGE, ""));
            data.putString("img_uri_600", data.getString(ARG_NOTIFICATION_IMAGE, ""));
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
}
