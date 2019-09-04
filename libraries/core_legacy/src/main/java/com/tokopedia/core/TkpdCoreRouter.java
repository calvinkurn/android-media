package com.tokopedia.core;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.core.deprecated.SessionHandler;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.gcm.base.IAppNotificationReceiver;
import com.tokopedia.core.gcm.model.NotificationPass;
import com.tokopedia.core.gcm.utils.RouterUtils;

public interface TkpdCoreRouter {
    String CART_ACTIVITY_OLD
            = "com.tokopedia.transaction.cart.activity.CartActivity";

    String CART_ACTIVITY_NEW
            = "com.tokopedia.checkout.view.feature.cartlist.CartActivity";

    String INBOX_TICKET_ACTIVITY = "com.tokopedia.contactus.inboxticket2.view.activity.InboxListActivity";

    String ACTIVITY_SIMPLE_HOME = "com.tokopedia.tkpd.home.SimpleHomeActivity";

    String FRAGMENT_TYPE = "FRAGMENT_TYPE";
    int INVALID_FRAGMENT = 0;
    int WISHLIST_FRAGMENT = 1;
    int PRODUCT_HISTORY_FRAGMENT = 2;

    String EXTRA_STATE_TAB_POSITION = "EXTRA_STATE_TAB_POSITION";
    int RESO_ALL = 2;

    static Intent createInstanceCartActivity(Context context) {
        return RouterUtils.getActivityIntent(context, CART_ACTIVITY_NEW);
    }

    static Class<?> createInstanceCartClass() throws ClassNotFoundException {
        return RouterUtils.getActivityClass(CART_ACTIVITY_NEW);
    }

    static Intent getInboxTicketActivityIntent(Context mContext) {
        return RouterUtils.getActivityIntent(mContext, INBOX_TICKET_ACTIVITY);
    }

    static Class<?> getInboxticketActivityClass() {
        Class<?> parentIndexHomeClass = null;
        try {
            parentIndexHomeClass = RouterUtils.getActivityClass(INBOX_TICKET_ACTIVITY);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return parentIndexHomeClass;
    }

    static Class<?> getSimpleHomeActivityClass() {
        try {
            return RouterUtils.getActivityClass(ACTIVITY_SIMPLE_HOME);
        } catch (ClassNotFoundException e) {

            e.printStackTrace();
            return null;
        }
    }

    @Deprecated
    static Intent getActivitySellingTransactionList(Context mContext) {
        return RouterUtils.getRouterFromContext(mContext).getActivitySellingTransactionListReal(mContext);
    }

    @Deprecated
    static Class<?> getSellingActivityClass(Context mContext) {
        return RouterUtils.getRouterFromContext(mContext).getSellingActivityClassReal();
    }

    @Deprecated
    static Intent getActivitySellingTransactionShippingStatus(Context mContext) {
        return RouterUtils.getRouterFromContext(mContext).getActivitySellingTransactionShippingStatusReal(mContext);
    }

    @Deprecated
    static Class<?> getInboxResCenterActivityClass(Context mContext) {
        return RouterUtils.getRouterFromContext(mContext).getInboxResCenterActivityClassReal();
    }

    static Class<?> getInboxMessageActivityClass(Context mContext) {
        return RouterUtils.getRouterFromContext(mContext).getInboxMessageActivityClass();
    }

    static IAppNotificationReceiver getAppNotificationReceiver(Context mContext) {
        return RouterUtils.getRouterFromContext(mContext).getAppNotificationReceiver();
    }

    static Intent getInboxTalkActivityIntentWrapper(Context mContext) {
        return RouterUtils.getRouterFromContext(mContext).getInboxTalkCallingIntent(mContext);
    }

    static Intent getSellerHomeActivity(Context context) {
        return RouterUtils.getRouterFromContext(context).getSellerHomeActivityReal(context);
    }

    static Class<?> getDeeplinkClass(Context mContext) {
        return RouterUtils.getRouterFromContext(mContext).getDeeplinkClass();
    }

    Class<?> getDeeplinkClass();

    Intent getSellerHomeActivityReal(Context context);

    Intent getInboxTalkCallingIntent(Context mContext);

    IAppNotificationReceiver getAppNotificationReceiver();

    Class<?> getInboxMessageActivityClass();

    Class<?> getInboxResCenterActivityClassReal();

    Intent getActivitySellingTransactionShippingStatusReal(Context mContext);

    Class getSellingActivityClassReal();

    Intent getActivitySellingTransactionListReal(Context mContext);

    String getDesktopLinkGroupChat();

    Intent getHomeIntent(Context context);

    Class<?> getHomeClass(Context context) throws ClassNotFoundException;

    NotificationPass setNotificationPass(Context mContext, NotificationPass mNotificationPass, Bundle data, String notifTitle);

    Intent getInboxMessageIntent(Context mContext);

    void onAppsFlyerInit();

    SessionHandler legacySessionHandler();

    GCMHandler legacyGCMHandler();

    void refreshFCMTokenFromBackgroundToCM(String token, boolean force);

    void refreshFCMFromInstantIdService(String token);

    void refreshFCMTokenFromForegroundToCM();
}
