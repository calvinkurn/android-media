package com.tokopedia.core.analytics.appsflyer;

import android.text.TextUtils;
import android.util.Log;

import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AFInAppEventType;
import com.tokopedia.logger.ServerLogger;
import com.tokopedia.logger.utils.Priority;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

import rx.Completable;
import rx.functions.Action0;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class AppsflyerEventValidation {
    public static final String AF_SHIPPING_PRICE = "af_shipping_price";
    public static final String AF_VALUE_PRODUCTTYPE = "product";
    public static final String VALUE_IDR = "IDR";
    public static final String AF_KEY_CATEGORY_NAME = "category";
    public static final String AF_KEY_CRITEO = "criteo_track_transaction";

    public void validateAppsflyerData(String eventName, Map<String, Object> data) {

        Completable.fromAction(new Action0() {
            @Override
            public void call() {
                validateData(eventName, data);
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    private void validateData(String eventName, Map<String, Object> data) {
        switch (eventName) {
            case AFInAppEventType.PURCHASE:
                validatePurchase(eventName, data);
                break;
            case AFInAppEventType.ADD_TO_CART:
                validateAddToCart(eventName, data);
                break;

        }
    }

    private void validatePurchase(String eventName, Map<String, Object> eventValue) {
        try {
            validatePaymentId(String.valueOf(eventValue.get(AFInAppEventParameterName.RECEIPT_ID)), String.valueOf(eventValue.get(AFInAppEventType.ORDER_ID)));
            validateRevenue(String.valueOf(eventValue.get(AFInAppEventParameterName.REVENUE)));
            validateShipping(String.valueOf(eventValue.get(AF_SHIPPING_PRICE)));
            validateQuantity(eventName, String.valueOf(eventValue.get(AFInAppEventParameterName.QUANTITY)));
            validateContentId(eventName, String.valueOf(eventValue.get(AFInAppEventParameterName.CONTENT_ID)));
            validateCurrency(eventName, String.valueOf(eventValue.get(AFInAppEventParameterName.CURRENCY)));
            validateProductList(String.valueOf(eventValue.get(AF_VALUE_PRODUCTTYPE)));
            validateProductCategory(String.valueOf(eventValue.get(AF_KEY_CATEGORY_NAME)));
            validateContentType(eventName, String.valueOf(eventValue.get(AFInAppEventParameterName.CONTENT_TYPE)));
            validateContent(eventName, String.valueOf(eventValue.get(AFInAppEventParameterName.CONTENT)));
        } catch (Exception e) {
            Map<String, String> messageMap = new HashMap<>();
            messageMap.put("type", "error");
            messageMap.put("reason", "exception_validatePurchase");
            messageMap.put("data", String.valueOf(eventValue));
            messageMap.put("ex", Log.getStackTraceString(e));
            logging(messageMap);
        }
    }

    private void validateAddToCart(String eventName, Map<String, Object> eventValue) {
        try {
            validateQuantity(eventName, String.valueOf(eventValue.get(AFInAppEventParameterName.QUANTITY)));
            validateContentId(eventName, String.valueOf(eventValue.get(AFInAppEventParameterName.CONTENT_ID)));
            validateCurrency(eventName ,String.valueOf(eventValue.get(AFInAppEventParameterName.CURRENCY)));
            validateCategory(eventName, String.valueOf(eventValue.get("category")));
            validateContentType(eventName, String.valueOf(eventValue.get(AFInAppEventParameterName.CONTENT_TYPE)));
            validateContent(eventName, String.valueOf(eventValue.get(AFInAppEventParameterName.CONTENT)));
        } catch (Exception e) {
            Map<String, String> messageMap = new HashMap<>();
            messageMap.put("type", "error");
            messageMap.put("reason", "exception_validateAddToCart");
            messageMap.put("data", String.valueOf(eventValue));
            messageMap.put("ex", Log.getStackTraceString(e));
            logging(messageMap);
        }
    }

    private void validatePaymentId(String paymentId, String orderID) {
        if (TextUtils.isEmpty(paymentId)) {
            Map<String, String> messageMap = new HashMap<>();
            messageMap.put("type", "validation");
            messageMap.put("reason", "paymentId_blank");
            messageMap.put("eventName", "");
            messageMap.put("data", orderID);
            logging(messageMap);
        }
        if (TextUtils.isEmpty(orderID)) {
            Map<String, String> messageMap = new HashMap<>();
            messageMap.put("type", "validation");
            messageMap.put("reason", "orderId_blank");
            messageMap.put("eventName", "");
            messageMap.put("data", paymentId);
            logging(messageMap);
        }
    }

    private void validateRevenue(String revenuePrice) {
        double price = convertToDouble(revenuePrice, "revenue");
        if (price < 0) {
            Map<String, String> messageMap = new HashMap<>();
            messageMap.put("type", "validation");
            messageMap.put("reason", "revenue_blank");
            messageMap.put("eventName", "");
            messageMap.put("data", revenuePrice);
            logging(messageMap);
        }
    }


    private void validateShipping(String shippingPrice) {
        double price = convertToDouble(shippingPrice, "shippingPrice");
        if (price < 0) {
            Map<String, String> messageMap = new HashMap<>();
            messageMap.put("type", "validation");
            messageMap.put("reason", "shippingPrice_blank");
            messageMap.put("eventName", "");
            messageMap.put("data", shippingPrice);
            logging(messageMap);
        }
    }


    private void exceptionStringToDouble(String ex, String type) {
        Map<String, String> messageMap = new HashMap<>();
        messageMap.put("type", "error");
        messageMap.put("reason", "exceptionStringToDouble");
        messageMap.put("data", type);
        messageMap.put("err", ex);
        logging(messageMap);
    }

    private void validateQuantity(String eventName, String quantity) {
        if (convertToDouble(quantity, eventName + " quantity") <= 0) {
            Map<String, String> messageMap = new HashMap<>();
            messageMap.put("type", "validation");
            messageMap.put("reason", "quantity_is_0");
            messageMap.put("eventName", eventName);
            messageMap.put("data", quantity);
            logging(messageMap);
        }
    }

    private void validateContentId(String eventName, String ids) {
        if (TextUtils.isEmpty(ids)) {
            Map<String, String> messageMap = new HashMap<>();
            messageMap.put("type", "validation");
            messageMap.put("reason", "ContentId_blank");
            messageMap.put("eventName", eventName);
            messageMap.put("data", "");
            logging(messageMap);
        }
    }

    private void validateCurrency(String eventName,String currency) {
        if (!VALUE_IDR.equals(currency)) {
            Map<String, String> messageMap = new HashMap<>();
            messageMap.put("type", "validation");
            messageMap.put("reason", "currency_invalid");
            messageMap.put("eventName", eventName);
            messageMap.put("data", currency);
            logging(messageMap);
        }
    }

    private void validateCategory(String eventName, String category) {
        if (TextUtils.isEmpty(category)) {
            Map<String, String> messageMap = new HashMap<>();
            messageMap.put("type", "validation");
            messageMap.put("reason", "category_blank");
            messageMap.put("eventName", eventName);
            messageMap.put("data", "");
            logging(messageMap);
        }
    }

    private void validateProductList(String productList) {
        if (TextUtils.isEmpty(productList)) {
            Map<String, String> messageMap = new HashMap<>();
            messageMap.put("type", "validation");
            messageMap.put("reason", "product_list_blank");
            messageMap.put("eventName", "");
            messageMap.put("data", "");
            logging(messageMap);
        } else {
            try {
                JSONArray productarray = new JSONArray(productList);
                if (productarray.length() < 1) {
                    Map<String, String> messageMap = new HashMap<>();
                    messageMap.put("type", "validation");
                    messageMap.put("reason", "product_array_invalid");
                    messageMap.put("eventName", "");
                    messageMap.put("data", productList);
                    logging(messageMap);
                }
            } catch (JSONException e) {
                //logging("error;reason=productList_array_exception;eventName='';data='$productList'");
            }

        }
    }

    private void validateProductCategory(String productCategory) {
        if (TextUtils.isEmpty(productCategory)) {
            Map<String, String> messageMap = new HashMap<>();
            messageMap.put("type", "validation");
            messageMap.put("reason", "ProductCategory_blank");
            messageMap.put("eventName", "");
            messageMap.put("data", "");
            logging(messageMap);
        } else {
            try {
                JSONArray productCatList = new JSONArray(productCategory);
                if (productCatList.length() < 1) {
                    Map<String, String> messageMap = new HashMap<>();
                    messageMap.put("type", "validation");
                    messageMap.put("reason", "productCategory_array_invalid");
                    messageMap.put("eventName", "");
                    messageMap.put("data", productCategory);
                    logging(messageMap);
                }
            } catch (JSONException e) {
                Map<String, String> messageMap = new HashMap<>();
                messageMap.put("type", "error");
                messageMap.put("reason", "productCategory_array_exception");
                messageMap.put("eventName", "");
                messageMap.put("data", productCategory);
                logging(messageMap);
            }

        }
    }

    private void validateContentType(String eventName, String contentType) {
        if (!AF_VALUE_PRODUCTTYPE.equals(contentType)) {
            Map<String, String> messageMap = new HashMap<>();
            messageMap.put("type", "validation");
            messageMap.put("reason", "contentType_invalid");
            messageMap.put("eventName", eventName);
            messageMap.put("data", contentType);
            logging(messageMap);
        }
    }

    private void validateContent(String eventName, String content) {
        if (TextUtils.isEmpty(content)) {
            Map<String, String> messageMap = new HashMap<>();
            messageMap.put("type", "validation");
            messageMap.put("reason", "content_array_blank");
            messageMap.put("eventName", eventName);
            messageMap.put("data", "");
            logging(messageMap);
        } else {
            try {
                JSONArray contentarray = new JSONArray(content);
                if (contentarray.length() < 1) {
                    Map<String, String> messageMap = new HashMap<>();
                    messageMap.put("type", "validation");
                    messageMap.put("reason", "content_array_invalid");
                    messageMap.put("eventName", eventName);
                    messageMap.put("data", content);
                    logging(messageMap);
                }
            } catch (JSONException e) {
                Map<String, String> messageMap = new HashMap<>();
                messageMap.put("type", "error");
                messageMap.put("reason", "content_array_exception");
                messageMap.put("eventName", eventName);
                messageMap.put("data", content);
                logging(messageMap);
            }

        }
    }


    private double convertToDouble(String value, String type) {
        double result = 0;
        try {
            result = Double.valueOf(value);
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            exceptionStringToDouble("" + Log.getStackTraceString(ex), type + "=" + value);
        }
        return result;
    }

    private void logging(Map<String, String> messageMap) {
        ServerLogger.log(Priority.P2, "APPSFLYER_VALIDATION", messageMap);
    }
}
