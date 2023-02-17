package com.tokopedia.contactus.common.analytics;

import android.content.Context;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.nishikino.model.EventTracking;


/**
 * Created by baghira on 16/07/18.
 */

public class ContactUsTracking extends UnifyTracking {

    public static void sendGTMInboxTicket(Context context, String event, String category, String action, String label) {
        sendGTMEvent(context, new EventTracking(event, category, action, label).getEvent());
    }
}
