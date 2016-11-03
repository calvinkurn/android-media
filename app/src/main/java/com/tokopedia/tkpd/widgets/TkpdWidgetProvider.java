package com.tokopedia.tkpd.widgets;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.var.TkpdCache;

public class TkpdWidgetProvider extends AppWidgetProvider{

	/* (non-Javadoc)
	 * @see android.appwidget.AppWidgetProvider#onReceive(android.content.Context, android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		if(intent.getExtras()!=null){
			System.out.println("Updating content");
			RemoteViews rem = new RemoteViews(context.getPackageName(), R.layout.widget_home);
			LocalCacheHandler cache = new LocalCacheHandler(context, TkpdCache.NOTIFICATION_DATA);
			if(cache.getArrayListInteger(TkpdCache.Key.SALES_COUNT).size()>0){
			rem.setTextViewText(R.id.order, ""+cache.getArrayListInteger(TkpdCache.Key.SALES_COUNT).get(0));
			rem.setTextViewText(R.id.status, ""+cache.getArrayListInteger(TkpdCache.Key.SALES_COUNT).get(1));
			rem.setTextViewText(R.id.shipping, ""+cache.getArrayListInteger(TkpdCache.Key.SALES_COUNT).get(2));
			}
			AppWidgetManager awm = AppWidgetManager.getInstance(context);
			awm.updateAppWidget(intent.getExtras().getIntArray("ids"), rem);
		}
		super.onReceive(context, intent);
	}

	/* (non-Javadoc)
	 * @see android.appwidget.AppWidgetProvider#onUpdate(android.content.Context, android.appwidget.AppWidgetManager, int[])
	 */
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		// TODO Auto-generated method stub
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		RemoteViews rem = new RemoteViews(context.getPackageName(), R.layout.widget_home);
		LocalCacheHandler cache = new LocalCacheHandler(context,TkpdCache.NOTIFICATION_DATA);
//		rem.setTextViewText(R.id.test, "xXXx");
		if(cache.getArrayListInteger(TkpdCache.Key.SALES_COUNT).size()>0){
		rem.setTextViewText(R.id.order, ""+cache.getArrayListInteger(TkpdCache.Key.SALES_COUNT).get(0));
		rem.setTextViewText(R.id.status, ""+cache.getArrayListInteger(TkpdCache.Key.SALES_COUNT).get(1));
		rem.setTextViewText(R.id.shipping, ""+cache.getArrayListInteger(TkpdCache.Key.SALES_COUNT).get(2));
		}
		AppWidgetManager awm = AppWidgetManager.getInstance(context);
		awm.updateAppWidget(appWidgetIds, rem);
	}



	/* (non-Javadoc)
	 * @see android.appwidget.AppWidgetProvider#onAppWidgetOptionsChanged(android.content.Context, android.appwidget.AppWidgetManager, int, android.os.Bundle)
	 */
	@Override
	public void onAppWidgetOptionsChanged(Context context,
			AppWidgetManager appWidgetManager, int appWidgetId,
			Bundle newOptions) {
		super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId,
				newOptions);
	}

	/* (non-Javadoc)
	 * @see android.appwidget.AppWidgetProvider#onDisabled(android.content.Context)
	 */
	@Override
	public void onDisabled(Context context) {
		super.onDisabled(context);
	}

}
