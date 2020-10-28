package com.tokopedia.core;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.gcm.base.IAppNotificationReceiver;
import com.tokopedia.core.gcm.model.NotificationPass;
import com.tokopedia.core.gcm.utils.RouterUtils;

public interface TkpdCoreRouter {

    String CART_ACTIVITY_NEW
            = "com.tokopedia.purchase_platform.features.checkout.view.feature.cartlist.CartActivity";

    String INBOX_TICKET_ACTIVITY = "com.tokopedia.contactus.inboxticket2.view.activity.InboxListActivity";

    String FRAGMENT_TYPE = "FRAGMENT_TYPE";
    int WISHLIST_FRAGMENT = 1;

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

    @Deprecated
    static Class<?> getInboxResCenterActivityClass(Context mContext) {
        return RouterUtils.getRouterFromContext(mContext).getInboxResCenterActivityClassReal();
    }

    static Class<?> getInboxMessageActivityClass(Context mContext) {
        return RouterUtils.getRouterFromContext(mContext).getInboxMessageActivityClass();
    }

    static Intent getInboxTalkActivityIntentWrapper(Context mContext) {
        return RouterUtils.getRouterFromContext(mContext).getInboxTalkCallingIntent(mContext);
    }

    Class<?> getDeeplinkClass();

    Intent getInboxTalkCallingIntent(Context mContext);

    IAppNotificationReceiver getAppNotificationReceiver();

    Class<?> getInboxMessageActivityClass();

    Class<?> getInboxResCenterActivityClassReal();

    Intent getHomeIntent(Context context);

    Class<?> getHomeClass();

    NotificationPass setNotificationPass(Context mContext, NotificationPass mNotificationPass, Bundle data, String notifTitle);

    void onAppsFlyerInit();

    void refreshFCMTokenFromBackgroundToCM(String token, boolean force);

    void refreshFCMFromInstantIdService(String token);
}
