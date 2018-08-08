package com.tokopedia.core.widgets;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;


public class TkpdWidgetReceiver {
	
	public static void UpdateWidget(Activity context){
		Intent intent = new Intent(context, TkpdWidgetProvider.class);
		intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
		int ids[] = AppWidgetManager.getInstance(context.getApplication()).getAppWidgetIds(new ComponentName(context.getApplication(), TkpdWidgetProvider.class));
		Bundle bundle = new Bundle();
		bundle.putIntArray("ids", ids);
		intent.putExtras(bundle);
		context.sendBroadcast(intent);
	}

}
