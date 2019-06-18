package com.tokopedia.notifications.common;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.view.CommonUtils;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;

/**
 * @author lalit.singh
 */
public class CmEventPost {


    private static final String TAG = CmEventPost.class.getSimpleName();

    public static void postEvent(Context context, String event, String category, String action, String label) {
        CommonUtils.dumper(TAG + "-" + event + "&" + category + "&" + action + "&" + label);
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                event, category, action, label));

    }
}
