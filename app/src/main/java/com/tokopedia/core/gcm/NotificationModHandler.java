package com.tokopedia.core.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.home.ParentIndexHome;
import com.tokopedia.core.inboxmessage.activity.InboxMessageActivity;
import com.tokopedia.core.inboxreputation.activity.InboxReputationActivity;
import com.tokopedia.core.selling.view.activity.ActivitySellingTransaction;
import com.tokopedia.core.inboxticket.activity.InboxTicketActivity;
import com.tokopedia.core.rescenter.inbox.activity.InboxResCenterActivity;
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
    
    public void modifyNotification () {
    	Class<?> resultclass = null;
    	Intent intent = null;
    	NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
        .setSmallIcon(R.drawable.ic_stat_notify)
        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_stat_notify))
        .setAutoCancel(true);
        NotificationCompat.InboxStyle inboxStyle = null;
        NotificationCompat.BigTextStyle bigStyle = null;
        
        LocalCacheHandler cache = new LocalCacheHandler(context, TkpdCache.GCM_NOTIFICATION);
        Content = cache.getArrayListString(TkpdCache.Key.NOTIFICATION_CONTENT);
        Desc = cache.getArrayListString(TkpdCache.Key.NOTIFICATION_DESC);
        Code = cache.getArrayListInteger(TkpdCache.Key.NOTIFICATION_CODE);
        for (int i = 0; i < Code.size(); i++) {
        	//Rico: kalo review karena ada 3 dibikin special case, yang reply edit juga diilangin
        	if (NotificationCode == 103) {
        		if (Code.get(i) == NotificationCode || Code.get(i) == 113 || Code.get(i) == 123) {
            		Content.remove(i);
            		Code.remove(i);
            		Desc.remove(i);
            	}
        	} else {
	        	if (Code.get(i) == NotificationCode) {
	        		Content.remove(i);
	        		Code.remove(i);
	        		Desc.remove(i);
	        	}
        	}
        }   
          
        cache.putArrayListString(TkpdCache.Key.NOTIFICATION_CONTENT, Content);
        cache.putArrayListString(TkpdCache.Key.NOTIFICATION_DESC, Desc);
        cache.putArrayListInteger(TkpdCache.Key.NOTIFICATION_CODE, Code);
        cache.applyEditor();
        
        if (Code.size() == 0) {
        	cancelNotif();
        } else {
            if (Code.size() == 1) {	
	        	bigStyle = new NotificationCompat.BigTextStyle();
	    		mBuilder.setContentTitle(Content.get(0));
	    		mBuilder.setContentText(Desc.get(0));
	    		bigStyle.bigText(Desc.get(0));
	    		mBuilder.setStyle(bigStyle);
	            //mBuilder.setTicker(Desc.get(0)); 
	            switch (NotificationCode) {
	            case 101:
	            	resultclass = InboxMessageActivity.class;
	            	break;
	            case 102:
	            	resultclass = InboxTalkActivity.class;
	            	break;
	            case 103:
	            	resultclass = InboxReputationActivity.class;
	            	break;
	            case 104:
	            	resultclass = InboxTicketActivity.class;
	            	break;
	            case 105:
	            	resultclass = InboxResCenterActivity.class;
	            	break;
	            case 401:
//	            	resultclass = ShopTransactionV2.class;
					resultclass = ActivitySellingTransaction.class;
	            	break;
	            }
	        } else {
	        	resultclass = ParentIndexHome.class;
	        	inboxStyle = new NotificationCompat.InboxStyle();
	        	inboxStyle.setBigContentTitle(context.getString(R.string.title_new_notif_general));
	        	for (int i = 0; i < Content.size(); i++) {
	        		inboxStyle.addLine(Content.get(i));
	        	}
	        	mBuilder.setContentTitle(context.getString(R.string.title_new_notif_general));
	        	mBuilder.setContentText(Desc.get(0));
	        	mBuilder.setStyle(inboxStyle);
	        	//mBuilder.setTicker(Desc.get(0)); 
	        }
	        intent = new Intent (context, resultclass);
	        if(resultclass.getName() == ParentIndexHome.class.getName())
	        	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
	        // Adds the back stack
	        stackBuilder.addParentStack(resultclass);
	        // Adds the Intent to the top of the stack
	        stackBuilder.addNextIntent(intent);
	        // Gets a PendingIntent containing the entire back stack
	        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
	
	        //PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);
	        mBuilder.setContentIntent(resultPendingIntent);
	        mNotificationManager.notify(100, mBuilder.build());
        }
    }
    
    public void cancelNotif() {
    	mNotificationManager.cancel(100);
    	LocalCacheHandler.clearCache(context, TkpdCache.GCM_NOTIFICATION);
    }

}
