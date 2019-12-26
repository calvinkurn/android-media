package com.tokopedia.core.router;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.util.RouterUtils;


/**
 * Created by Nathaniel on 11/11/2016.
 */

/**
 * @author nisie
 * @deprecated do not use this class
 * please use TkpdInboxRouter instead
 */
@Deprecated
public class InboxRouter {

    private static final String INBOX_CONTACT_US_ACTIVITY = "com.tokopedia.contactus.createticket.activity.ContactUsActivity";
    private static final String CREATE_RESCENTER_ACTIVITY = "com.tokopedia.inbox.rescenter.create.activity.CreateResCenterActivity";
    private static final String INBOX_RESCENTER_ACTIVITY = "com.tokopedia.inbox.rescenter.inbox.activity.InboxResCenterActivity";

    public static final String EXTRA_STATE_TAB_POSITION = "EXTRA_STATE_TAB_POSITION";
    public static final String EXTRA_ORDER_ID = "EXTRA_ORDER_ID";
    public static final String EXTRA_STATE_FLAG_RECEIVED = "EXTRA_STATE_FLAG_RECEIVED";

    private static final String INBOX_TICKET_ACTIVITY = "com.tokopedia.contactus.inboxticket2.view.activity.InboxListActivity";

    private static final String INBOX_MESSAGE_ACTIVITY = "com.tokopedia.inbox.inboxmessage.activity.InboxMessageActivity";
    public static final java.lang.String PARAM_SHOP_ID = "to_shop_id";
    public static final String PARAM_URL = "PARAM_URL";

    //Trouble ID


    /////////// INTENT

    public static Intent getContactUsActivityIntent(Context context) {
        return RouterUtils.getActivityIntent(context, INBOX_CONTACT_US_ACTIVITY);
    }

    public static Intent getInboxTicketActivityIntent(Context context) {
        return RouterUtils.getActivityIntent(context, INBOX_TICKET_ACTIVITY);
    }

    public static Class<?> getInboxMessageActivityClass() {
        Class<?> parentIndexHomeClass = null;
        try {
            parentIndexHomeClass = RouterUtils.getActivityClass(INBOX_MESSAGE_ACTIVITY);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return parentIndexHomeClass;
    }

    public static Intent getCreateResCenterActivityIntent(Context context, String orderID) {
        Intent intent;
        if (MainApplication.getAppContext() instanceof TkpdCoreRouter) {
            intent = ((TkpdCoreRouter)MainApplication.getAppContext()).getCreateResCenterActivityIntent(context, orderID);
        } else {
            intent = RouterUtils.getActivityIntent(context, CREATE_RESCENTER_ACTIVITY);
            Bundle bundle = new Bundle();
            bundle.putString(EXTRA_ORDER_ID, orderID);
            bundle.putInt(InboxRouter.EXTRA_STATE_FLAG_RECEIVED, 1);
            intent.putExtras(bundle);
        }
        return intent;
    }

    public static Class<?> getInboxResCenterActivityClass() {
        Class<?> parentIndexHomeClass = null;
        try {
            parentIndexHomeClass = RouterUtils.getActivityClass(INBOX_RESCENTER_ACTIVITY);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return parentIndexHomeClass;
    }
}
