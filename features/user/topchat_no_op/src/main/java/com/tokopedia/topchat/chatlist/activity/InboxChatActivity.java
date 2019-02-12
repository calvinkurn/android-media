package com.tokopedia.topchat.chatlist.activity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * @author by nisie on 17/01/19.
 */
public class InboxChatActivity {

    public static Intent getCallingIntent(Context context) {
        Log.d("ERR", "Please implement topchat instead of topchat no op");
        return null;
    }


    public static Intent getChannelCallingIntent(Context context) {
        return new Intent(context, InboxChatActivity.class);
    }
}
