package com.tokopedia.core.router;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tokopedia.core.GCMListenerService;
import com.tokopedia.core.NotificationCenter;
import com.tokopedia.core.util.RouterUtils;

/**
 * Created by Nathaniel on 11/11/2016.
 */

public class InboxRouter {

    private static final String INBOX_CONTACT_US_ACTIVITY = "com.tokopedia.inbox.contactus.activity.ContactUsActivity";

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
}
