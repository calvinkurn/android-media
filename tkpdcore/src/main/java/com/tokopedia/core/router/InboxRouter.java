package com.tokopedia.core.router;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.onboarding.FreeReturnOnboardingActivity;
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
    private static final String INBOX_RESCENTER_FRAGMENT = "com.tokopedia.inbox.rescenter.inbox.fragment.InboxResCenterFragment";

    public static final String ARG_PARAM_EXTRA_INSTANCE_TYPE = "ARG_PARAM_EXTRA_INSTANCE_TYPE";
    public static final String EXTRA_STATE_TAB_POSITION = "EXTRA_STATE_TAB_POSITION";
    public static final String EXTRA_ORDER_ID = "EXTRA_ORDER_ID";
    public static final String EXTRA_RESOLUTION_ID = "resolution_id";
    public static final String EXTRA_STATE_FLAG_RECEIVED = "EXTRA_STATE_FLAG_RECEIVED";
    public static final String EXTRA_TROUBLE_ID = "EXTRA_TROUBLE_ID";
    public static final String EXTRA_SOLUTION_ID = "EXTRA_SOLUTION_ID";
    public static final int RESO_ALL = 2;
    public static final int RESO_BUYER = 1;
    public static final int RESO_MINE = 0;

    private static final String INBOX_TICKET_ACTIVITY = "com.tokopedia.contactus.inboxticket2.view.activity.InboxListActivity";
    private static final String INBOX_TICKET_FRAGMENT = "com.tokopedia.contactus.inboxticket.fragment.InboxTicketFragment";

    private static final String INBOX_MESSAGE_ACTIVITY = "com.tokopedia.inbox.inboxmessage.activity.InboxMessageActivity";
    private static final String INBOX_MESSAGE_FRAGMENT = "com.tokopedia.inbox.inboxmessage.fragment.InboxMessageFragment";
    public static final java.lang.String PARAM_OWNER_FULLNAME = "owner_fullname";
    public static final java.lang.String PARAM_SHOP_ID = "to_shop_id";
    public static final String PARAM_URL = "PARAM_URL";

    //Trouble ID

    //Solution ID
    public static final int SOLUTION_REFUND = 1;
    public static final int SOLUTION_CHECK_COURIER = 6;


    /////////// INTENT

    public static Intent getContactUsActivityIntent(Context context) {
        return RouterUtils.getActivityIntent(context, INBOX_CONTACT_US_ACTIVITY);
    }

    public static Intent getInboxTicketActivityIntent(Context context) {
        return RouterUtils.getActivityIntent(context, INBOX_TICKET_ACTIVITY);
    }

    /////////// Class
    public static Class<?> getInboxticketActivityClass() {
        Class<?> parentIndexHomeClass = null;
        try {
            parentIndexHomeClass = RouterUtils.getActivityClass(INBOX_TICKET_ACTIVITY);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return parentIndexHomeClass;
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

    public static Intent getCreateResCenterActivityIntent(Context context, String orderID, int troubleID, int solutionID) {
        Intent intent;
        if (MainApplication.getAppContext() instanceof TkpdCoreRouter) {
            intent = ((TkpdCoreRouter)MainApplication.getAppContext()).getCreateResCenterActivityIntent(context, orderID, troubleID, solutionID);
        } else {
            intent = RouterUtils.getActivityIntent(context, CREATE_RESCENTER_ACTIVITY);
            Bundle bundle = new Bundle();
            bundle.putString(InboxRouter.EXTRA_ORDER_ID, orderID);
            bundle.putInt(InboxRouter.EXTRA_STATE_FLAG_RECEIVED, 0);
            bundle.putInt(InboxRouter.EXTRA_TROUBLE_ID, troubleID);
            bundle.putInt(InboxRouter.EXTRA_SOLUTION_ID, solutionID);
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

    public static Intent getFreeReturnOnBoardingActivityIntent(Context context, String orderID) {
        Intent intent = new Intent(context, FreeReturnOnboardingActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_ORDER_ID, orderID);
        intent.putExtras(bundle);
        return intent;
    }
}
