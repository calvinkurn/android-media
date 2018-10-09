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

    String getDesktopLinkGroupChat();
}
