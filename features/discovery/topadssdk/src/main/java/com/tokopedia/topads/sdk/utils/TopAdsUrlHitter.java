package com.tokopedia.topads.sdk.utils;

public class TopAdsUrlHitter {
    ImpresionTask impresionTask;

    public TopAdsUrlHitter() {
        impresionTask = new ImpresionTask();
    }

    public void hitClickUrl(String url, String sourceClassName) {
        impresionTask.execute(url);
        //saveToDb
    }

    public void hitImpressionUrl(String url, String sourceClassName) {
        impresionTask.execute(url);
        //saveToDb
    }
}
