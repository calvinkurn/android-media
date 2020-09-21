package com.tokopedia.core.analytics.appsflyer;

import android.text.TextUtils;
import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AFInAppEventType;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.Map;
import timber.log.Timber;

public class AppsflyerEventValidation {
    public static final String AF_SHIPPING_PRICE = "af_shipping_price";
    public static final String AF_VALUE_PRODUCTTYPE = "product";
    public static final String VALUE_IDR = "IDR";
    public static final String AF_KEY_CATEGORY_NAME = "category";
    public static final String AF_KEY_CRITEO = "criteo_track_transaction";

    public static String TAG = "P2#APPSFLYER_VALIDATION#";

    public void validateData(String eventName, Map<String, Object> eventValue) {
        switch (eventName) {
            case AFInAppEventType.PURCHASE:
                validatePurchase(eventName,eventValue);
                break;
            case AF_KEY_CRITEO:

                break;
            case AFInAppEventType.ADD_TO_CART:
                validateAddToCart(eventName, eventValue);
                break;

        }
    }

    private void validatePurchase(String eventName, Map<String, Object> eventValue){
        try {
            validatePaymentId(String.valueOf(eventValue.get(AFInAppEventParameterName.RECEIPT_ID)), String.valueOf(eventValue.get(AFInAppEventType.ORDER_ID)));
            validateRevenue(String.valueOf(eventValue.get(AFInAppEventParameterName.REVENUE)));
            validateShipping(String.valueOf(eventValue.get(AF_SHIPPING_PRICE)));
            validateQuantity(eventName, String.valueOf(eventValue.get(AFInAppEventParameterName.QUANTITY)));
            validateContentId(eventName, String.valueOf(eventValue.get(AFInAppEventParameterName.CONTENT_ID)));
            validateCurrency(String.valueOf(eventValue.get(AFInAppEventParameterName.CURRENCY)));
            validateProductList(String.valueOf(eventValue.get(AF_VALUE_PRODUCTTYPE)));
            validateProductCategory(String.valueOf(eventValue.get(AF_KEY_CATEGORY_NAME)));
            validateContentType(eventName, String.valueOf(eventValue.get(AFInAppEventParameterName.CONTENT_TYPE)));
            validateContent(eventName, String.valueOf(eventValue.get(AFInAppEventParameterName.CONTENT)));
        }catch (Exception e){
            logging("validation;reason=exception in validate purchase event;ex=$e;data=$eventValue");
        }
    }

    private void validateAddToCart(String eventName, Map<String, Object> eventValue){
        validateQuantity(eventName, String.valueOf(eventValue.get(AFInAppEventParameterName.QUANTITY)));
        validateContentId(eventName, String.valueOf(eventValue.get(AFInAppEventParameterName.CONTENT_ID)));
        validateCurrency(String.valueOf(eventValue.get(AFInAppEventParameterName.CURRENCY)));
        validateCategory(eventName,String.valueOf(eventValue.get("category")));
        validateContentType(eventName, String.valueOf(eventValue.get(AFInAppEventParameterName.CONTENT_TYPE)));
        validateContent(eventName, String.valueOf(eventValue.get(AFInAppEventParameterName.CONTENT)));

    }

    private void validatePaymentId(String paymentId, String orderID) {
        if (TextUtils.isEmpty(paymentId)) {
            logging("validation;reason=paymentId_blank;order_id=$orderID");
        }
        if (TextUtils.isEmpty(orderID)) {
            logging("validation;reason=orderId_blank;payment_id=$paymentId");
        }
    }

    private void validateRevenue(String revenuePrice) {
        double price =convertToDouble(revenuePrice,"revenue");
        if (price <= 0) {
            logging("validation;reason=revenue_blank;revenue=$revenuePrice");
        }
    }


    private void validateShipping(String shippingPrice) {
        double price =convertToDouble(shippingPrice,"shippingPrice");
        if (price <= 0) {
            logging("validation;reason=shippingPrice_blank;shipping_price=$shippingPrice");
        }
    }


    private void exceptionStringToDouble(String ex, String type) {
        logging("error;reason=exceptionStringToDouble;err=$ex;type=$type");
    }

    private void validateQuantity(String eventName,String quantity) {
        if (convertToDouble(quantity,eventName +" quantity" ) <= 0) {
            logging("validation;reason=quantity is 0;eventName-$eventName;quantity=$quantity");
        }
    }

    private void validateContentId(String eventName,String ids) {
        if (TextUtils.isEmpty(ids)) {
            logging("validation;reason=ContentId-product id is empty; eventName=$eventName");
        }
    }

    private void validateCurrency(String currency) {
        if (!VALUE_IDR.equals(currency)) {
            logging("validation;reason=currency is not valid;currency=$currency");
        }
    }

    private void validateCategory(String eventName, String category){
        if (TextUtils.isEmpty(category)) {
            logging("validation;reason=category is empty; eventName=$eventName");
        }
    }

    private void validateProductList(String productList) {
        if (TextUtils.isEmpty(productList)) {
            logging("validation;reason=product list is empty");
        } else {
            try {
                JSONArray productarray = new JSONArray(productList);
                if (productarray.length() < 1) {
                    logging("validation;reason=product array is not valid; productList=$productList");
                }
            } catch (JSONException e) {
                logging("validation;reason=productList array exception; productList=$productList");
            }

        }
    }

    private void validateProductCategory(String productCategory) {
        if (TextUtils.isEmpty(productCategory)) {
            logging("validation;reason=Product Category is empty");
        } else {
            try {
                JSONArray productCatList = new JSONArray(productCategory);
                if (productCatList.length() < 1) {
                    logging("validation;reason=productCategory array is not valid; productCategory=$productCategory");
                }
            } catch (JSONException e) {
                logging("validation;reason=productCategory array exception; productCategory=$productCategory");
            }

        }
    }

    private void validateContentType(String eventName, String contentType) {
        if (!AF_VALUE_PRODUCTTYPE.equals(contentType)) {
            logging("validation;reason=currency is not valid;eventName=$eventName;ContentType=$contentType");
        }
    }

    private void validateContent(String eventName, String content) {
        if (TextUtils.isEmpty(content)) {
            logging("validation;reason=content array is empty; eventName=$eventName");
        } else {
            try {
                JSONArray contentarray = new JSONArray(content);
                if (contentarray.length() < 1) {
                    logging("validation;reason=content array is not valid; eventName=$eventName; content=$content");
                }
            } catch (JSONException e) {
                logging("validation;reason=content array exception; eventName=$eventName;content=$content");
            }

        }
    }

    //
    private void logging(String log) {
        Timber.w(TAG + log);
    }
//
//
//
//    val orderIds: MutableList<String> = arrayListOf()
//
//    val afValue: MutableMap<String, Any> = mutableMapOf()
//    var quantity = 0
//    val productList: MutableList<String> = arrayListOf()
//    val productIds: MutableList<String> = arrayListOf()
//    val productCategory: MutableList<String> = arrayListOf()
//    val productArray = JSONArray()
//
//    var shipping = 0f
//
//    thanksPageData.shopOrder.forEach { shopOrder ->
//            orderIds.add(shopOrder.orderId)
//        shipping += shopOrder.shippingAmount
//        shopOrder.purchaseItemList.forEach { productItem ->
//                val productObj: org.json.JSONObject = org.json.JSONObject()
//            productIds.add(productItem.productId)
//            productList.add(productItem.productName)
//            productCategory.add(productItem.category)
//            productObj.put(ParentTrackingKey.KEY_ID, productItem.productId)
//            productObj.put(ParentTrackingKey.KEY_QTY, productItem.quantity)
//            quantity += productItem.quantity
//            productArray.put(productObj)
//        }
//    }
//
//    afValue[AFInAppEventParameterName.REVENUE] = thanksPageData.amount
//    afValue[AFInAppEventParameterName.CONTENT_ID] = productIds
//    afValue[AFInAppEventParameterName.QUANTITY] = quantity
//    afValue[AFInAppEventParameterName.RECEIPT_ID] = thanksPageData.paymentID
//    afValue[AFInAppEventType.ORDER_ID] = orderIds
//    afValue[ParentTrackingKey.AF_SHIPPING_PRICE] = shipping
//    afValue[ParentTrackingKey.AF_PURCHASE_SITE] = when(ThankPageTypeMapper.getThankPageType(thanksPageData)){
//        MarketPlaceThankPage -> MARKET_PLACE
//                    else -> DIGITAL
//    }
//    afValue[AFInAppEventParameterName.CURRENCY] = ParentTrackingKey.VALUE_IDR
//    afValue[ParentTrackingKey.AF_VALUE_PRODUCTTYPE] = productList
//    afValue[ParentTrackingKey.AF_KEY_CATEGORY_NAME] = productCategory
//    afValue[AFInAppEventParameterName.CONTENT_TYPE] = ParentTrackingKey.AF_VALUE_PRODUCTTYPE
//
//    val criteoAfValue: Map<String, Any> = java.util.HashMap(afValue)
//            if (productArray.length() > 0) {
//        val afContent: String = productArray.toString()
//        afValue[AFInAppEventParameterName.CONTENT] = afContent
//    }
//                TrackApp.getInstance().appsFlyer.sendTrackEvent(AFInAppEventType.PURCHASE, afValue)
//            TrackApp.getInstance().appsFlyer.sendTrackEvent(ParentTrackingKey.AF_KEY_CRITEO, criteoAfValue)


//val jsonArrayAfContent = JSONArray()
//        .put(JSONObject()
//                .put(AF_PARAM_CONTENT_ID, addToCartRequest.productId.toString())
//                .put(AF_PARAM_CONTENT_QUANTITY, addToCartRequest.quantity));
//            TrackApp.getInstance().appsFlyer.sendEvent(AFInAppEventType.ADD_TO_CART,
//            mutableMapOf<String, Any>(
//    AFInAppEventParameterName.CONTENT_ID to addToCartRequest.productId.toString(),
//    AFInAppEventParameterName.CONTENT_TYPE to AF_VALUE_CONTENT_TYPE,
//    AFInAppEventParameterName.DESCRIPTION to addToCartRequest.productName,
//    AFInAppEventParameterName.CURRENCY to AF_VALUE_CURRENCY,
//    AFInAppEventParameterName.QUANTITY to addToCartRequest.quantity,
//    AFInAppEventParameterName.PRICE to addToCartRequest.price.replace("[^0-9]".toRegex(), ""),
//    AF_PARAM_CATEGORY to addToCartRequest.category,
//    AFInAppEventParameterName.CONTENT to jsonArrayAfContent.toString())
//            )

    private double convertToDouble(String value, String type) {
        double result = 0;
        try {
            result = Double.valueOf(value);
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
             exceptionStringToDouble("" + ex.getMessage(),type +"="+ value);
        }
        return result;
    }
}
