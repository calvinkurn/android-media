package com.tokopedia.tkpd.analytics.container;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.tagmanager.TagManager;

import java.util.Map;

/**
 * Created by ricoharisin on 7/8/15.
 * modified by alvarisi
 */
public class GTMDataLayer {

    static void pushGeneral(Context context, Map<String, Object> values) {
        Log.i("Tag Manager", "UA-9801603-15: Send General");
        TagManager.getInstance(context).getDataLayer().push(values);
    }

    static void pushEvent(Context context, String eventName, Map<String, Object> values) {
        Log.i("Tag Manager", "UA-9801603-15: Send Event");
        TagManager.getInstance(context).getDataLayer().pushEvent(eventName, values);
    }
}
