package com.tokopedia.core.analytics;

import android.content.Context;

import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AFInAppEventType;
import com.tokopedia.core.analytics.appsflyer.Jordan;
import com.tokopedia.track.TrackApp;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

/**
 * @author by Herdi_WORK on 25.10.16.
 */

public class PaymentTracking extends TrackingUtils {

    /* new from TopPayActivity revamped*/
    public static void eventTransactionAF(Context context,
                                          String paymentId,
                                          String grandTotalBeforeFee,
                                          JSONArray afJSON,
                                          int qty,
                                          Map[] productList
    ) {
        Map<String, Object> afValue = new HashMap<>();
        afValue.put(AFInAppEventParameterName.REVENUE, grandTotalBeforeFee);
        afValue.put(AFInAppEventParameterName.CONTENT_ID, afJSON.toString());
        afValue.put(AFInAppEventParameterName.QUANTITY, qty);
        afValue.put(AFInAppEventParameterName.RECEIPT_ID, paymentId);
        afValue.put(AFInAppEventType.ORDER_ID, paymentId);

        afValue.put(Jordan.AF_PURCHASE_SITE,Jordan.VALUE_ANDROID);
        afValue.put(AFInAppEventParameterName.CURRENCY, Jordan.VALUE_IDR);
        afValue.put(Jordan.AF_VALUE_PRODUCT_TYPE, productList);
        if(productList != null && productList.length>1) {
            afValue.put(AFInAppEventParameterName.CONTENT_TYPE, Jordan.AF_VALUE_PRODUCTGROUPTYPE);
        }else {
            afValue.put(AFInAppEventParameterName.CONTENT_TYPE, Jordan.AF_VALUE_PRODUCTTYPE);
        }

        TrackApp.getInstance().getAppsFlyer().sendTrackEvent(AFInAppEventType.PURCHASE, afValue);
        TrackApp.getInstance().getAppsFlyer().sendTrackEvent(Jordan.AF_KEY_CRITEO, afValue);
    }
}
