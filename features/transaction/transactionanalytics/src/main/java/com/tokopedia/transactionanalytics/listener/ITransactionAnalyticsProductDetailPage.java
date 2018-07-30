package com.tokopedia.transactionanalytics.listener;

import java.util.Map;

/**
 * @author anggaprasetiyo on 20/07/18.
 */
public interface ITransactionAnalyticsProductDetailPage {
    void sendAnalyticsOnAddToCartSuccess(Map<String, Object> cartMap, String eventAction, String eventLabel);
}
