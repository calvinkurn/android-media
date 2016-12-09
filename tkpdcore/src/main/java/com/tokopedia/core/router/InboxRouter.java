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

public class InboxRouter {

    private static final String INBOX_CONTACT_US_ACTIVITY = "com.tokopedia.inbox.contactus.activity.ContactUsActivity";
    private static final String CREATE_RESCENTER_ACTIVITY = "com.tokopedia.inbox.rescenter.create.activity.CreateResCenterActivity";
    private static final String DETAIL_RESCENTER_ACTIVITY = "com.tokopedia.inbox.rescenter.detail.activity.ResCenterActivity";
    private static final String INBOX_RESCENTER_ACTIVITY = "com.tokopedia.inbox.rescenter.inbox.activity.InboxResCenterActivity";
    private static final String INBOX_RESCENTER_FRAGMENT = "com.tokopedia.inbox.rescenter.inbox.fragment.InboxResCenterFragment";

    public static final String ARG_PARAM_EXTRA_INSTANCE_TYPE = "ARG_PARAM_EXTRA_INSTANCE_TYPE";
    public static final String EXTRA_STATE_TAB_POSITION = "EXTRA_STATE_TAB_POSITION";
    public static final String EXTRA_ORDER_ID = "EXTRA_ORDER_ID";
    public static final String EXTRA_RESOLUTION_ID = "EXTRA_RESOLUTION_ID";
    public static final String EXTRA_STATE_FLAG_RECEIVED = "EXTRA_STATE_FLAG_RECEIVED";
    public static final String EXTRA_TROUBLE_ID = "EXTRA_TROUBLE_ID";
    public static final String EXTRA_SOLUTION_ID = "EXTRA_SOLUTION_ID";
    public static final int RESO_ALL = 2;
    public static final int RESO_BUYER = 1;
    public static final int RESO_MINE = 0;

    private static final String INBOX_TICKET_ACTIVITY = "com.tokopedia.inbox.inboxticket.activity.InboxTicketActivity";
    private static final String INBOX_TICKET_FRAGMENT = "com.tokopedia.inbox.inboxticket.fragment.InboxTicketFragment";

    /////////// INTENT

    public static Intent getContactUsActivityIntent(Context context) {
        Intent intent = RouterUtils.getActivityIntent(context, INBOX_CONTACT_US_ACTIVITY);
        return intent;
    }

    public static Intent getInboxTicketActivityIntent(Context context) {
        Intent intent = RouterUtils.getActivityIntent(context, INBOX_TICKET_ACTIVITY);
        return intent;
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

    public static Intent getInboxResCenterActivityIntent(Context context) {
        Intent intent = RouterUtils.getActivityIntent(context, INBOX_RESCENTER_ACTIVITY);
        return intent;
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

    public static Intent getDetailResCenterActivityIntent(Context context, String resolutionID) {
        Intent intent = RouterUtils.getActivityIntent(context, DETAIL_RESCENTER_ACTIVITY);
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_RESOLUTION_ID, resolutionID);
        intent.putExtras(bundle);
        return intent;
    }

    public static ComponentName getInboxResCenterActivityComponentName(Context context) {
        return RouterUtils.getActivityComponentName(context, INBOX_RESCENTER_ACTIVITY);
    }

    public static Intent getFreeReturnOnBoardingActivityIntent(Context context, String orderID) {
        Intent intent = new Intent(context, FreeReturnOnboardingActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_ORDER_ID, orderID);
        intent.putExtras(bundle);
        return intent;
    }

    public static Fragment instanceInboxResCenterFromNotification(Context context, int state) {
        Fragment fragment = Fragment.instantiate(context, INBOX_RESCENTER_FRAGMENT);
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_PARAM_EXTRA_INSTANCE_TYPE, state);
        fragment.setArguments(bundle);
        return fragment;
    }
}
