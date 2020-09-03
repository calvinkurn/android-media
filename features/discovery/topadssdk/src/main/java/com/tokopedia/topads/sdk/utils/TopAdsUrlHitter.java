package com.tokopedia.topads.sdk.utils;

import android.content.Context;

import com.tokopedia.analyticsdebugger.debugger.TopAdsLogger;

import javax.inject.Inject;

public class TopAdsUrlHitter {

    private static final String TYPE_CLICK = "click";
    private static final String TYPE_IMPRESSION = "impression";

    private ImpresionTask impresionTask;
    private String sourceClassName;
    private Context context;

    public TopAdsUrlHitter(Context context){
        this.context = context;
    }

    @Deprecated
    public TopAdsUrlHitter(String className) {
        impresionTask = new ImpresionTask(className);
        this.sourceClassName = className;
    }

    @Deprecated
    public void hitClickUrl(Context context, String url, String productId, String productName, String imageUrl) {
        hitClickUrl(context, url, productId, productName, imageUrl, "");
    }

    @Deprecated
    public void hitClickUrl(Context context, String url, String productId, String productName, String imageUrl, String componentName) {
        impresionTask.execute(url);
        TopAdsLogger.getInstance(context).save(url, TYPE_CLICK, sourceClassName, productId, productName, imageUrl, componentName);
    }

    @Deprecated
    public void hitImpressionUrl(Context context, String url, String productId, String productName, String imageUrl) {
        hitImpressionUrl(context, url, productId, productName, imageUrl, "");
    }

    @Deprecated
    public void hitImpressionUrl(Context context, String url, String productId, String productName, String imageUrl, String componentName) {
        impresionTask.execute(url);
        TopAdsLogger.getInstance(context).save(url, TYPE_IMPRESSION, sourceClassName, productId, productName, imageUrl, componentName);
    }

    public void hitClickUrl(String className, String url, String productId, String productName, String imageUrl) {
        hitClickUrl(className, url, productId, productName, imageUrl, "");
    }

    public void hitImpressionUrl(String className, String url, String productId, String productName, String imageUrl) {
        hitImpressionUrl(className, url, productId, productName, imageUrl, "");
    }

    public void hitClickUrl(String className, String url, String productId, String productName, String imageUrl, String componentName) {
        new ImpresionTask(className).execute(url);
        TopAdsLogger.getInstance(context).save(url, TYPE_CLICK, className, productId, productName, imageUrl, componentName);
    }

    public void hitImpressionUrl(String className, String url, String productId, String productName, String imageUrl, String componentName) {
        new ImpresionTask(className).execute(url);
        TopAdsLogger.getInstance(context).save(url, TYPE_IMPRESSION, className, productId, productName, imageUrl, componentName);
    }
}
