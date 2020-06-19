package com.tokopedia.topads.sdk.utils;

import android.content.Context;

import com.tokopedia.analyticsdebugger.debugger.TopAdsLogger;

public class TopAdsUrlHitter {

    private static final String TYPE_CLICK = "click";
    private static final String TYPE_IMPRESSION = "impression";

    private ImpresionTask impresionTask;
    private String sourceClassName;

    public TopAdsUrlHitter(String className) {
        impresionTask = new ImpresionTask(className);
        this.sourceClassName = className;
    }

    public void hitClickUrl(Context context, String url, String productId, String productName, String imageUrl) {
        impresionTask.execute(url);
        TopAdsLogger.getInstance(context).save(url, TYPE_CLICK, sourceClassName, productId, productName, imageUrl);
    }

    public void hitImpressionUrl(Context context, String url, String productId, String productName, String imageUrl) {
        impresionTask.execute(url);
        TopAdsLogger.getInstance(context).save(url, TYPE_IMPRESSION, sourceClassName, productId, productName, imageUrl);
    }
}
