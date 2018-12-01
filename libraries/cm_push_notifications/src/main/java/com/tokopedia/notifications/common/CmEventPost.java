package com.tokopedia.notifications.common;

import android.content.Context;
import android.util.Log;

import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.notifications.CMRouter;

/**
 * @author lalit.singh
 */
public class CmEventPost {


    private static final String TAG = CmEventPost.class.getSimpleName();

    public static void postEvent(Context context, String event, String category, String action, String label) {
        Log.d(TAG, event + "&" + category + "&" + action + "&" + label);
        CMRouter cmRouter = (CMRouter) context.getApplicationContext();
        AnalyticTracker analyticTracker = cmRouter.getAnalyticTracker();
        if (analyticTracker == null)
            return;
        analyticTracker.sendEventTracking(event, category, action, label);

    }
}
