package com.tokopedia.core.router;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.core.util.RouterUtils;

/**
 * Created by Nathaniel on 11/11/2016.
 */

public class InboxRouter {

    private static final String INBOX_CONTACT_US_ACTIVITY = "com.tokopedia.inbox.contactus.activity.ContactUsActivity";

    public static Intent getContactUsActivityIntent(Context context) {
        Intent intent = RouterUtils.getActivityIntent(context, INBOX_CONTACT_US_ACTIVITY);
        return intent;
    }
}
