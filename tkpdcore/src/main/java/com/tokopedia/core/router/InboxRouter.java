package com.tokopedia.core.router;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.core.onboarding.FreeReturnOnboardingActivity;
import com.tokopedia.core.util.RouterUtils;


/**
 * Created by Nathaniel on 11/11/2016.
 */

/**
 * @deprecated do not use this class
 * please use TkpdInboxRouter instead
 * @author nisie
 */
@Deprecated
public class InboxRouter {

    private static final String INBOX_CONTACT_US_ACTIVITY = "com.tokopedia.inbox.contactus.activity.ContactUsActivity";
    private static final String CREATE_RESCENTER_ACTIVITY = "com.tokopedia.inbox.rescenter.create.activity.CreateResCenterActivity";
    private static final String INBOX_RESCENTER_ACTIVITY = "com.tokopedia.inbox.rescenter.inbox.activity.InboxResCenterActivity";
    private static final String INBOX_RESCENTER_FRAGMENT = "com.tokopedia.inbox.rescenter.inbox.fragment.InboxResCenterFragment";
    private static final String INBOX_TALK_ACTIVITY = "com.tokopedia.inbox.inboxtalk.activity.InboxTalkActivity";
    private static final String INBOX_TALK_FRAGMENT = "com.tokopedia.inbox.inboxtalk.fragment.InboxTalkFragment";


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

    private static final String INBOX_TICKET_ACTIVITY = "com.tokopedia.inbox.inboxticket.activity.InboxTicketActivity";
    private static final String INBOX_TICKET_FRAGMENT = "com.tokopedia.inbox.inboxticket.fragment.InboxTicketFragment";

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

    public static Intent getInboxTalkActivityIntent(Context context) {
        Intent intent = RouterUtils.getActivityIntent(context, INBOX_TALK_ACTIVITY);
        return intent;
    }

    public static Fragment instanceInboxTalkFromNotification(Context context) {
        Fragment fragment = Fragment.instantiate(context, INBOX_TALK_FRAGMENT);
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static Fragment instanceInboxMessageFromNotification(Context context) {
        Fragment fragment = Fragment.instantiate(context, INBOX_MESSAGE_FRAGMENT);
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
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

    public static Class<?> getInboxTalkActivityClass() {
        Class<?> parentIndexHomeClass = null;
        try {
            parentIndexHomeClass = RouterUtils.getActivityClass(INBOX_TALK_ACTIVITY);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return parentIndexHomeClass;
    }

    /////////// COMPONENT NAME

    public static ComponentName getInboxticketActivityComponentName(Context context) {
        return RouterUtils.getActivityComponentName(context, INBOX_TICKET_ACTIVITY);
    }

    public static Fragment instanceInboxTicketFragmentFromNotification(Context context) {
        Fragment fragment = Fragment.instantiate(context, INBOX_TICKET_FRAGMENT);
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public static ComponentName getInboxMessageActivityComponentName(Context context) {
        return RouterUtils.getActivityComponentName(context, INBOX_MESSAGE_ACTIVITY);
    }

    public static Intent getCreateResCenterActivityIntent(Context context, String orderID) {
        Intent intent = RouterUtils.getActivityIntent(context, CREATE_RESCENTER_ACTIVITY);
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_ORDER_ID, orderID);
        bundle.putInt(InboxRouter.EXTRA_STATE_FLAG_RECEIVED, 1);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent getCreateResCenterActivityIntent(Context context, String orderID, int troubleID, int solutionID) {
        Intent intent = RouterUtils.getActivityIntent(context, CREATE_RESCENTER_ACTIVITY);
        Bundle bundle = new Bundle();
        bundle.putString(InboxRouter.EXTRA_ORDER_ID, orderID);
        bundle.putInt(InboxRouter.EXTRA_STATE_FLAG_RECEIVED, 0);
        bundle.putInt(InboxRouter.EXTRA_TROUBLE_ID, troubleID);
        bundle.putInt(InboxRouter.EXTRA_SOLUTION_ID, solutionID);
        intent.putExtras(bundle);
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
