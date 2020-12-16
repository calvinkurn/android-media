package com.tokopedia.core.analytics.appsflyer;

import android.text.TextUtils;
import android.util.Log;

import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AFInAppEventType;

import org.json.JSONArray;
import org.json.JSONException;

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

    public static String TAG = "P2#APPSFLYER_VALIDATION#";

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
            logging("error;reason=exception_validatePurchase;data='"+eventValue+"';ex='"+ Log.getStackTraceString(e)+"'");
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
            logging("error;reason=exception_validateAddToCart;data='"+eventValue+"'ex='"+ Log.getStackTraceString(e) +"'");
        }
    }

    private void validatePaymentId(String paymentId, String orderID) {
        if (TextUtils.isEmpty(paymentId)) {
            logging("validation;reason=paymentId_blank;eventName='';data='"+orderID+"'");
        }
        if (TextUtils.isEmpty(orderID)) {
            logging("validation;reason=orderId_blank;eventName='';data='"+ paymentId+"'");
        }
    }

    private void validateRevenue(String revenuePrice) {
        double price = convertToDouble(revenuePrice, "revenue");
        if (price < 0) {
            logging("validation;reason=revenue_blank;eventName='';data='"+revenuePrice+"'");
        }
    }


    private void validateShipping(String shippingPrice) {
        double price = convertToDouble(shippingPrice, "shippingPrice");
        if (price < 0) {
            logging("validation;reason=shippingPrice_blank;eventName='';data='"+shippingPrice+"'");
        }
    }


    private void exceptionStringToDouble(String ex, String type) {
        logging("error;reason=exceptionStringToDouble;data='"+type+"';err='"+ex+"'");
    }

    private void validateQuantity(String eventName, String quantity) {
        if (convertToDouble(quantity, eventName + " quantity") <= 0) {
            logging("validation;reason=quantity_is_0;eventName='"+eventName+"';data='"+quantity+"'");
        }
    }

    private void validateContentId(String eventName, String ids) {
        if (TextUtils.isEmpty(ids)) {
            logging("validation;reason=ContentId_blank;eventName='"+eventName+"';data=''");
        }
    }

    private void validateCurrency(String eventName,String currency) {
        if (!VALUE_IDR.equals(currency)) {
            logging("validation;reason=currency_invalid;eventName='"+ eventName+"';data='"+ currency+"'");
        }
    }

    private void validateCategory(String eventName, String category) {
        if (TextUtils.isEmpty(category)) {
            logging("validation;reason=category_blank;eventName='"+eventName+"';data=''");
        }
    }

    private void validateProductList(String productList) {
        if (TextUtils.isEmpty(productList)) {
            logging("validation;reason=product_list_blank;eventName='';data=''");
        } else {
            try {
                JSONArray productarray = new JSONArray(productList);
                if (productarray.length() < 1) {
                    logging("validation;reason=product_array_invalid;eventName='';data='"+ productList+"'");
                }
            } catch (JSONException e) {
                //logging("error;reason=productList_array_exception;eventName='';data='$productList'");
            }

        }
    }

    private void validateProductCategory(String productCategory) {
        if (TextUtils.isEmpty(productCategory)) {
            logging("validation;reason=ProductCategory_blank;eventName='';data=''");
        } else {
            try {
                JSONArray productCatList = new JSONArray(productCategory);
                if (productCatList.length() < 1) {
                    logging("validation;reason=productCategory_array_invalid;eventName='';data='"+productCategory+"'");
                }
            } catch (JSONException e) {
                logging("error;reason=productCategory_array_exception;eventName='';data='" + productCategory+"'");
            }

        }
    }

    private void validateContentType(String eventName, String contentType) {
        if (!AF_VALUE_PRODUCTTYPE.equals(contentType)) {
            logging("validation;reason=contentType_invalid;eventName='"+eventName+"';data='"+ contentType+"'");
        }
    }

    private void validateContent(String eventName, String content) {
        if (TextUtils.isEmpty(content)) {
            logging("validation;reason=content_array_blank;eventName='"+eventName+"'data=''");
        } else {
            try {
                JSONArray contentarray = new JSONArray(content);
                if (contentarray.length() < 1) {
                    logging("validation;reason=content_array_invalid;eventName='"+eventName+"';data='"+content+"'");
                }
            } catch (JSONException e) {
                logging("error;reason=content_array_exception;eventName='"+eventName+"';data='"+content+"'");
            }

        }
    }


    private double convertToDouble(String value, String type) {
        double result = 0;
        try {
            result = Double.valueOf(value);
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            exceptionStringToDouble("" + ex.getMessage(), type + "=" + value);
        }
        return result;
    }

    private void logging(String log) {
        Timber.w(TAG + log);
    }
}
