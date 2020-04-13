package com.tokopedia.topads.sdk.utils;

import android.content.Context;

import com.tokopedia.analyticsdebugger.debugger.TopAdsLogger;

public class TopAdsUrlHitter {

    private static final String TYPE_CLICK = "click";
    private static final String TYPE_IMPRESSION = "impression";

    ImpresionTask impresionTask;

    public TopAdsUrlHitter() {
        impresionTask = new ImpresionTask();
    }

    public void hitClickUrl(Context context, String url, String sourceClassName) {
        impresionTask.execute(url);
        TopAdsLogger.getInstance(context).save(url, TYPE_CLICK, sourceClassName);
    }

    public void hitImpressionUrl(Context context, String url, String sourceClassName) {
        impresionTask.execute(url);
        TopAdsLogger.getInstance(context).save(url, TYPE_IMPRESSION, sourceClassName);
    }
}
