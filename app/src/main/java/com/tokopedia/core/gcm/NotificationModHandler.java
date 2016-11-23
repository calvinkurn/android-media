package com.tokopedia.core.gcm;

import android.app.NotificationManager;
import android.content.Context;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.inboxmessage.activity.InboxMessageActivity;
import com.tokopedia.core.inboxreputation.activity.InboxReputationActivity;
import com.tokopedia.core.inboxticket.activity.InboxTicketActivity;
import com.tokopedia.core.rescenter.inbox.activity.InboxResCenterActivity;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.talk.inboxtalk.activity.InboxTalkActivity;
import com.tokopedia.core.var.TkpdCache;

import java.util.ArrayList;

public class NotificationModHandler {
	
	private NotificationManager mNotificationManager;
	private ArrayList<String> Content = new ArrayList<String>();
    private ArrayList<String> Desc = new ArrayList<String>();
    private ArrayList<Integer> Code = new ArrayList<Integer>();
    private int NotificationCode;
    private Context context;
    
    
    public NotificationModHandler(Context context, int code) {
    	NotificationCode = code;
    	mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    	this.context = context;
    }
    
    public NotificationModHandler(Context context) {
    	mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    	this.context = context;
    }

    public void cancelNotif() {
    	mNotificationManager.cancel(100);
    	LocalCacheHandler.clearCache(context, TkpdCache.GCM_NOTIFICATION);
    }

}
