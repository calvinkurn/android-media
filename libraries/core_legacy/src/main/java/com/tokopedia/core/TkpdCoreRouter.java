package com.tokopedia.core;

import android.content.Context;
import android.content.Intent;

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

    setNotificationPass

    Class getSellingActivityClassReal();

    Intent getActivitySellingTransactionListReal(Context mContext);

    String getDesktopLinkGroupChat();

    Intent getHomeIntent(Context context);

    Class<?> getHomeClass(Context context) throws ClassNotFoundException;
}
