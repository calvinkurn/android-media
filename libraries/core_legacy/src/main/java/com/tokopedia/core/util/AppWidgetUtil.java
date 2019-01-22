package com.tokopedia.core.util;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;

import com.tokopedia.abstraction.common.utils.GlobalConfig;

/**
 * Created by zulfikarrahman on 7/20/17.
 */

public class AppWidgetUtil {
    public static void sendBroadcastToAppWidget(Context context) {
        if(GlobalConfig.isSellerApp()) {
            Intent i = new Intent();
            i.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            context.sendBroadcast(i);
        }
    }
}
