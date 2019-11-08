package com.tokopedia.core.analytics.container;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.tagmanager.ContainerHolder;
import com.google.android.gms.tagmanager.DataLayer;
import com.google.android.gms.tagmanager.TagManager;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.tokopedia.analytics.debugger.GtmLogger;
import com.tokopedia.analytics.debugger.TetraDebugger;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.nishikino.model.Authenticated;
import com.tokopedia.core.analytics.nishikino.model.GTMCart;
import com.tokopedia.core.deprecated.SessionHandler;
import com.tokopedia.core.gcm.utils.RouterUtils;
import com.tokopedia.core.util.PriceUtil;
import com.tokopedia.iris.Iris;
import com.tokopedia.iris.IrisAnalytics;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.track.interfaces.ContextAnalytics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import static com.tokopedia.core.analytics.TrackingUtils.getAfUniqueId;

public class GTMAnalytics extends ContextAnalytics {
    private static final String TAG = GTMAnalytics.class.getSimpleName();
    private static final long EXPIRE_CONTAINER_TIME_DEFAULT = 150; // 150 minutes (2.5 hours)
    private static final String KEY_GTM_EXPIRED_TIME = "android_gtm_expired_time";

    private static final String KEY_EVENT = "event";
    private static final String KEY_CATEGORY = "eventCategory";
    private static final String KEY_ACTION = "eventAction";
    private static final String KEY_LABEL = "eventLabel";
    private static final String USER_ID = "userId";
    private static final String SHOP_ID = "shopId";
    private static final String SHOP_TYPE = "shopType";
    private final Iris iris;
    private TetraDebugger tetraDebugger;
    private final RemoteConfig remoteConfig;
    private String clientIdString = "";

    // have status that describe pending.

    public GTMAnalytics(Context context) {
        super(context);
        if (GlobalConfig.isAllowDebuggingTools()) {
            tetraDebugger = TetraDebugger.Companion.instance(context);
        }
        iris = IrisAnalytics.Companion.getInstance(context);
        remoteConfig = new FirebaseRemoteConfigImpl(context);
    }

    @Override
    public void sendGeneralEvent(Map<String, Object> value) {
        pushGeneralGtmV5Internal(value);
    }

    @Override
    public void sendGeneralEvent(String event, String category, String action, String label) {
        Map<String, Object> map = new HashMap<>();
        map.put(KEY_EVENT, event);
        map.put(KEY_CATEGORY, category);
        map.put(KEY_ACTION, action);
        map.put(KEY_LABEL, label);

        pushGeneralGtmV5Internal(map);
    }



    @Override
    public void sendEnhanceEcommerceEvent(Map<String, Object> value) {
        // V4
        clearEnhanceEcommerce();
        pushGeneral(value);

        StringBuilder stacktrace = new StringBuilder();

        // V5
        try {
            for (StackTraceElement ste : Thread.currentThread().getStackTrace()) {
               stacktrace.append(String.format("%s\n", ste.toString()));
            }

            String keyEvent = keyEvent(clone(value));

            // prevent sending null keyevent
            if (keyEvent == null)
                return;
            pushEECommerceInternal(keyEvent, factoryBundle(bruteForceCastToString(value.get("event")), clone(value)));
        }catch (Exception e){
            if(e != null && !TextUtils.isEmpty(e.getMessage())) {
                Timber.e("P2[GTMAnalytic Error]%s %s", e.getMessage(), stacktrace.toString());
            }
        }
    }

    @SuppressWarnings("unchecked")
    private String keyEvent(Map<String, Object> value) {
        String event = bruteForceCastToString(value.get("event"));
        if (event != null) {
            switch (event.toLowerCase()) {
                case CHECKOUT:
                    Map<String, Object> ecommerce = (Map<String, Object>) value.get("ecommerce");
                    Map<String, Object> checkout = (Map<String, Object>) ecommerce.get("checkout");
                    Map<String, Object> actionField = (Map<String, Object>) checkout.get("actionField");
                    String step = bruteForceCastToString(actionField.get("step"));
                    if (step != null) {
                        switch (step) {
                            case "1":
                                return FirebaseAnalytics.Event.BEGIN_CHECKOUT;
                            default:
                                return FirebaseAnalytics.Event.CHECKOUT_PROGRESS;
                        }
                    }
                case REMOVEFROMCART:
                    return FirebaseAnalytics.Event.REMOVE_FROM_CART;
                case ADDTOCART:
                    return FirebaseAnalytics.Event.ADD_TO_CART;
                default:
                    return event;
            }
        } else {
            return null;
        }
    }

    public static class PurchaseKey {
        private static final String KEY_NAME = "name";
        private static final String KEY_ID = "id";
        private static final String KEY_AFFILIATION = "affiliation";
        private static final String KEY_REVENUE = "revenue";
        private static final String KEY_TAX = "tax";
        public static final String KEY_SHIPPING = "shipping";
        private static final String KEY_VARIANT = "variant";
        private static final String KEY_QTY = "quantity";
        public static final String KEY_COUPON = "coupon";
    }

    private Bundle factoryBundle(String keyEvent, Map<String, Object> value) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_CATEGORY, value.remove(KEY_CATEGORY) + "");
        bundle.putString(KEY_ACTION, value.remove(KEY_ACTION) + "");
        bundle.putString(KEY_LABEL, value.remove(KEY_LABEL) + "");

        Map<String, Object> ecommerce = (Map<String, Object>) value.remove("ecommerce");
        if (keyEvent != null) {
            switch (keyEvent.toLowerCase()) {
                case PRODUCTVIEW:
                    productImpressionBundle(keyEvent, bundle, ecommerce);
                    break;
                case PRODUCTCLICK:
                case VIEWPRODUCT:
                    productBundle(keyEvent, bundle, ecommerce);
                    break;
                case REMOVEFROMCART:
                case ADDTOCART:
                    cartBundle(bundle, ecommerce);
                    break;
                case TRANSACTION:
                    transactionBundle(bundle, ecommerce);
                    break;
                case CHECKOUT:
                    checkoutBundle(bundle, ecommerce);
                    break;
                case PROMOVIEW:
                    promoView(bundle, ecommerce);
                    break;
                case PROMOCLICK:
                    promoClickBundle(bundle, ecommerce);
                    break;
            }
        }

        for (Map.Entry<String, Object> entry : value.entrySet()) {
            bundle.putString(entry.getKey(), bruteForceCastToString(entry.getValue()));
        }
        return bundle;
    }

    private void productImpressionBundle(String keyEvent, Bundle bundle, Map<String, Object> ecommerce) {
        Object impressions = ecommerce.remove("impressions");
        if (impressions instanceof List) {
            List viewProduct = (List) impressions;
            ArrayList<Bundle> promotionBundles = new ArrayList<>();
            for (int j = 0; j < viewProduct.size(); j++) {
                Object promotionObj = viewProduct.get(j);
                if (promotionObj != null) {
                    if (promotionObj instanceof Object[]) {
                        Object[] promotions = (Object[]) promotionObj;

                        for (int i = 0; i < promotions.length; i++) {
                            Map<String, Object> promotion = (Map<String, Object>) promotions[i];
                            promotionBundles.add(viewProductMap(promotion, i + 1));
                        }
                    } else if (promotionObj instanceof ArrayList) {
                        List promotions = (List) promotionObj;

                        for (int i = 0; i < promotions.size(); i++) {
                            Map<String, Object> promotion = (Map<String, Object>) promotions.get(i);
                            promotionBundles.add(viewProductMap(promotion, i + 1));
                        }
                    } else if (promotionObj instanceof Map) {
                        Map<String, Object> promotions = (Map<String, Object>) promotionObj;
                        promotionBundles.add(viewProductMap(promotions, j + 1));
                    }
                }
            }
            bundle.putParcelableArrayList("items", promotionBundles);
        }
    }

    private void productBundle(String keyEvent, Bundle bundle, Map<String, Object> ecommerce) {
        Map<String, Object> viewProduct = null;
        switch (keyEvent.toLowerCase()) {
            case PRODUCTCLICK:
                viewProduct = (Map<String, Object>) ecommerce.remove("click");
                break;
            case VIEWPRODUCT:
                viewProduct = (Map<String, Object>) ecommerce.remove("detail");
                break;
        }


        if (viewProduct.get("actionField") != null) {
            Map<String, Object> actionField = (Map<String, Object>) viewProduct.remove("actionField");
            if (actionField.get("list") != null)
                bundle.putString(FirebaseAnalytics.Param.ITEM_LIST, bruteForceCastToString(actionField.remove("list")));
        }

        Object promotionObj = viewProduct.remove("products");
        if (promotionObj != null) {
            ArrayList<Bundle> promotionBundles = new ArrayList<>();
            if (promotionObj instanceof Object[]) {
                Object[] promotions = (Object[]) promotionObj;

                for (int i = 0; i < promotions.length; i++) {
                    Map<String, Object> promotion = (Map<String, Object>) promotions[i];
                    promotionBundles.add(viewProductMap(promotion, i + 1));
                }
            } else if (promotionObj instanceof ArrayList) {
                List promotions = (List) promotionObj;

                for (int i = 0; i < promotions.size(); i++) {
                    Map<String, Object> promotion = (Map<String, Object>) promotions.get(i);
                    promotionBundles.add(viewProductMap(promotion, i + 1));
                }
            }
            bundle.putParcelableArrayList("items", promotionBundles);
        }
    }

    private void cartBundle(Bundle bundle, Map<String, Object> ecommerce) {
        Object promotionObj;
        Map<String, Object> container = null;
        if (ecommerce.get("remove") != null) {
            container = (Map<String, Object>) ecommerce.remove("remove");
        } else if (ecommerce.get("add") != null) {
            container = (Map<String, Object>) ecommerce.remove("add");
        }

        // no action field

        promotionObj = container.remove("products");
        if (promotionObj != null) {
            ArrayList<Bundle> promotionBundles = new ArrayList<>();
            if (promotionObj instanceof Object[]) {
                Object[] promotions = (Object[]) promotionObj;

                for (int i = 0; i < promotions.length; i++) {
                    Map<String, Object> promotion = (Map<String, Object>) promotions[i];
                    promotionBundles.add(atcMap(promotion));
                }
            } else if (promotionObj instanceof ArrayList) {
                List promotions = (List) promotionObj;

                for (int i = 0; i < promotions.size(); i++) {
                    Map<String, Object> promotion = (Map<String, Object>) promotions.get(i);
                    promotionBundles.add(atcMap(promotion));
                }
            }
            bundle.putParcelableArrayList("items", promotionBundles);
        }
    }

    private double emptyDouble(String doubleRaw){
        return TextUtils.isEmpty(doubleRaw) ? 0.0 :
                Double.valueOf(PriceUtil.from(doubleRaw));
    }

    private double emptyInt(String intRaw){
        return TextUtils.isEmpty(intRaw) ? 0 :
                Double.valueOf(PriceUtil.from(intRaw));
    }

    private void transactionBundle(Bundle bundle, Map<String, Object> ecommerce) {
        Object promotionObj;
        Map<String, Object> purchase = (Map<String, Object>) ecommerce.remove("purchase");

        bundle.putString(FirebaseAnalytics.Param.CURRENCY, "IDR");

        if (purchase.get("actionField") != null) {
            Map<String, Object> actionField = (Map<String, Object>) purchase.remove("actionField");

            bundle.putString(FirebaseAnalytics.Param.TRANSACTION_ID, bruteForceCastToString(actionField.remove(PurchaseKey.KEY_ID)));
            bundle.putString(FirebaseAnalytics.Param.AFFILIATION, bruteForceCastToString(actionField.remove(PurchaseKey.KEY_AFFILIATION)));
            bundle.putDouble(FirebaseAnalytics.Param.VALUE, emptyDouble(bruteForceCastToString(actionField.remove(PurchaseKey.KEY_REVENUE)))); // Revenue
            bundle.putDouble(FirebaseAnalytics.Param.TAX, emptyDouble(bruteForceCastToString(actionField.remove(PurchaseKey.KEY_TAX))));
            bundle.putDouble(FirebaseAnalytics.Param.SHIPPING, emptyDouble(bruteForceCastToString(actionField.remove(PurchaseKey.KEY_SHIPPING))));
            bundle.putString(FirebaseAnalytics.Param.COUPON, bruteForceCastToString(actionField.remove(PurchaseKey.KEY_COUPON)));
        }

        // get products
        promotionObj = purchase.get("products");
        if (promotionObj != null) {
            ArrayList<Bundle> promotionBundles = new ArrayList<>();
            if (promotionObj instanceof Object[]) {
                Object[] promotions = (Object[]) purchase.get("products");
                for (int i = 0; i < promotions.length; i++) {
                    Map<String, Object> promotion = (Map<String, Object>) promotions[i];
                    promotionBundles.add(checkoutProductMap(promotion));
                }
            } else if (promotionObj instanceof List) {
                List promotions = (List) purchase.get("products");

                for (int i = 0; i < promotions.size(); i++) {
                    Map<String, Object> promotion = (Map<String, Object>) promotions.get(i);
                    promotionBundles.add(checkoutProductMap(promotion));
                }
            }
            bundle.putParcelableArrayList("items", promotionBundles);
        }
    }

    private void promoClickBundle(Bundle bundle, Map<String, Object> ecommerce) {
        Object promotionObj;
        Map<String, Object> promoClick = (Map<String, Object>) ecommerce.remove("promoClick");

        promotionObj = promoClick.remove("promotions");
        if (promotionObj != null) {
            ArrayList<Bundle> promotionBundles = new ArrayList<>();
            if (promotionObj instanceof Object[]) {
                Object[] promotions = (Object[]) promotionObj;

                for (int i = 0; i < promotions.length; i++) {
                    Map<String, Object> promotion = (Map<String, Object>) promotions[i];
                    promotionBundles.add(promotionMap(promotion));
                }
            } else if (promotionObj instanceof ArrayList) {
                List promotions = (List) promotionObj;

                for (int i = 0; i < promotions.size(); i++) {
                    Map<String, Object> promotion = (Map<String, Object>) promotions.get(i);
                    promotionBundles.add(promotionMap(promotion));
                }
            }
            bundle.putParcelableArrayList("promotions", promotionBundles);
        }
    }

    private void promoView(Bundle bundle, Map<String, Object> ecommerce) {
        Map<String, Object> promoView = (Map<String, Object>) ecommerce.remove("promoView");
        Object promotionObj = promoView.remove("promotions");
        if (promotionObj != null) {
            ArrayList<Bundle> promotionBundles = new ArrayList<>();
            if (promotionObj instanceof Object[]) {
                Object[] promotions = (Object[]) promotionObj;

                for (int i = 0; i < promotions.length; i++) {
                    Map<String, Object> promotion = (Map<String, Object>) promotions[i];
                    promotionBundles.add(promotionMap(promotion));
                }
            } else if (promotionObj instanceof ArrayList) {
                List promotions = (List) promotionObj;

                for (int i = 0; i < promotions.size(); i++) {
                    Map<String, Object> promotion = (Map<String, Object>) promotions.get(i);
                    promotionBundles.add(promotionMap(promotion));
                }
            }
            bundle.putParcelableArrayList("promotions", promotionBundles);
        }
    }

    private void checkoutBundle(Bundle bundle, Map<String, Object> ecommerce) {
        Object checkoutProducts;
        Map<String, Object> checkout = (Map<String, Object>) ecommerce.remove("checkout");

        // get step and option
        if (checkout.get("actionField") != null) {
            Map<String, Object> actionField = (Map<String, Object>) checkout.remove("actionField");

            String step = bruteForceCastToString(actionField.get("step"));
            String option = bruteForceCastToString(actionField.get("option"));

            bundle.putString(FirebaseAnalytics.Param.CHECKOUT_STEP, step);
            bundle.putString(FirebaseAnalytics.Param.CHECKOUT_OPTION, option);
        }

        // get products
        checkoutProducts = checkout.remove("products");
        if (checkoutProducts != null) {
            ArrayList<Bundle> promotionBundles = new ArrayList<>();
            if (checkoutProducts instanceof Object[]) {
                Object[] promotions = (Object[]) checkoutProducts;
                for (int i = 0; i < promotions.length; i++) {
                    Map<String, Object> promotion = (Map<String, Object>) promotions[i];
                    promotionBundles.add(checkoutProductMap(promotion));
                }
            } else if (checkoutProducts instanceof List) {
                List promotions = (List) checkoutProducts;

                for (int i = 0; i < promotions.size(); i++) {
                    Map<String, Object> promotion = (Map<String, Object>) promotions.get(i);
                    promotionBundles.add(checkoutProductMap(promotion));
                }
            }
            bundle.putParcelableArrayList("items", promotionBundles);
        }
    }

    public static class CheckoutKey {

        private static final String KEY_NAME = "name";
        private static final String KEY_ID = "id";
        private static final String KEY_PRICE = "price";
        private static final String KEY_BRAND = "brand";
        private static final String KEY_CAT = "category";
        private static final String KEY_VARIANT = "variant";
        private static final String KEY_QTY = "quantity";
    }

    private Bundle checkoutProductMap(Map<String, Object> value) {
        String id = bruteForceCastToString(value.remove(CheckoutKey.KEY_ID));
        String name = (String) value.remove(CheckoutKey.KEY_NAME);
        String brand = (String) value.remove(CheckoutKey.KEY_BRAND);
        String category = bruteForceCastToString(value.remove(CheckoutKey.KEY_CAT));
        String variant = (String) value.remove(CheckoutKey.KEY_VARIANT);
        String priceString = bruteForceCastToString(value.remove(CheckoutKey.KEY_PRICE));
        double price = TextUtils.isEmpty(priceString) ? 0.0 :
                Double.valueOf(PriceUtil.from(priceString));
        String qtyString = bruteForceCastToString(value.remove(CheckoutKey.KEY_QTY));
        int quantity = TextUtils.isEmpty(qtyString) ? 0 : Integer.valueOf(qtyString);

        Bundle checkoutBundle = new Bundle();
        checkoutBundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
        checkoutBundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
        checkoutBundle.putString(FirebaseAnalytics.Param.ITEM_BRAND, !TextUtils.isEmpty(brand)? brand : "");
        checkoutBundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, category);
        checkoutBundle.putString(FirebaseAnalytics.Param.ITEM_VARIANT, !TextUtils.isEmpty(variant)? variant : "");
        checkoutBundle.putDouble(FirebaseAnalytics.Param.PRICE, price);
        checkoutBundle.putLong(FirebaseAnalytics.Param.QUANTITY, quantity);

        // custom dimension
        for (Map.Entry<String, Object> entry : value.entrySet()) {
            checkoutBundle.putString(entry.getKey(), bruteForceCastToString(entry.getValue()));
        }

        return checkoutBundle;
    }

    private static class ATCKey {
        private static final String KEY_NAME = "name";
        private static final String KEY_ID = "id";
        private static final String KEY_PRICE = "price";
        private static final String KEY_BRAND = "brand";
        private static final String KEY_CAT = "category";
        private static final String KEY_VARIANT = "variant";
        private static final String KEY_QTY = "quantity";
    }

    private static class ProductKey {
        private static final String KEY_NAME = "name";
        private static final String KEY_ID = "id";
        private static final String KEY_PRICE = "price";
        private static final String KEY_BRAND = "brand";
        private static final String KEY_CAT = "category";
        private static final String KEY_VARIANT = "variant";
        private static final String KEY_POSITION = "quantity";
    }

    private Bundle viewProductMap(Map<String, Object> value, int index) {
        String id = bruteForceCastToString(value.remove(ProductKey.KEY_ID));
        String name = (String) value.remove(ProductKey.KEY_NAME);
        String price = bruteForceCastToString(value.remove(ProductKey.KEY_PRICE));
        String brand = (String) value.remove(ProductKey.KEY_BRAND);
        String category = bruteForceCastToString(value.remove(ProductKey.KEY_CAT));
        String variant = (String) value.remove(ProductKey.KEY_VARIANT);
        String position = bruteForceCastToString(value.remove(ProductKey.KEY_POSITION));

        Bundle product1 = new Bundle();
        product1.putString(FirebaseAnalytics.Param.ITEM_ID, id);                    // dimension69 (Product_ID), mandatory
        product1.putString(FirebaseAnalytics.Param.ITEM_NAME, name);   // Product Name, mandatory
        product1.putString(FirebaseAnalytics.Param.ITEM_BRAND, brand);        // if not applicable pass “none / other”, in the future, need brand name and also ID, optional
        product1.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, category);      // Product category {{level1_name}} / {{level2_name}} / {{level3_name}} / {{childCatID}}
        product1.putString(FirebaseAnalytics.Param.ITEM_VARIANT, variant);              // If not applicable pass “none / other”, optional

        product1.putDouble(FirebaseAnalytics.Param.PRICE, Double.valueOf(PriceUtil.from(price)));
        if (position != null && !TextUtils.isEmpty(position)) {
            try {
                index = Integer.valueOf(position);
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace();
            }
        }
        product1.putLong(FirebaseAnalytics.Param.INDEX, index);

        for (Map.Entry<String, Object> entry : value.entrySet()) {
            product1.putString(entry.getKey(), bruteForceCastToString(entry.getValue()));
        }
        return product1;

    }

    private String emptyString(Object string){
        if(string instanceof String){
            return emptyString(string);
        }else{
            return bruteForceCastToString(string);
        }
    }

    private String emptyString(String string){
        return !TextUtils.isEmpty(string) ? string : "";
    }

    private Bundle atcMap(Map<String, Object> value) {
        String id = bruteForceCastToString(value.remove(ATCKey.KEY_ID));
        String name = (String) value.remove(ATCKey.KEY_NAME);
        String price = PriceUtil.from(bruteForceCastToString(value.remove(ATCKey.KEY_PRICE)));
        String brand = (String) value.remove(ATCKey.KEY_BRAND);
        String category = (String) value.remove(ATCKey.KEY_CAT);
        String variant = (String) value.remove(ATCKey.KEY_VARIANT);
        String quantity = bruteForceCastToString(value.remove(ATCKey.KEY_QTY));

        // bundle.putLong (Long.class) <-- Long <-- String<-- Long, Int, String
        // safecast (ke string) kalau null safecast ke integer kalau null safecast ke long
        // target class

        Bundle product1 = new Bundle();
        product1.putString(FirebaseAnalytics.Param.ITEM_ID, id);                    // dimension69 (Product_ID), mandatory
        product1.putString(FirebaseAnalytics.Param.ITEM_NAME, name);   // Product Name, mandatory
        product1.putString(FirebaseAnalytics.Param.ITEM_BRAND, brand);        // if not applicable pass “none / other”, in the future, need brand name and also ID, optional
        product1.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, category);      // Product category {{level1_name}} / {{level2_name}} / {{level3_name}} / {{childCatID}}
        product1.putString(FirebaseAnalytics.Param.ITEM_VARIANT, variant);              // If not applicable pass “none / other”, optional
        product1.putDouble(FirebaseAnalytics.Param.PRICE, TextUtils.isEmpty(quantity) ? 0: Double.valueOf(price)) ;                      // In double format, mandatory
        product1.putLong(FirebaseAnalytics.Param.QUANTITY, TextUtils.isEmpty(quantity) ? 0: Long.valueOf(quantity));

        for (Map.Entry<String, Object> entry : value.entrySet()) {
            product1.putString(entry.getKey(), bruteForceCastToString(entry.getValue()));
        }
        return product1;

    }

    public static String bruteForceCastToString(Object object) {
        Integer integer = safeCast(object, Integer.class);

        if (integer != null)
            return integer.toString();

        Long aLong = safeCast(object, Long.class);

        if (aLong != null)
            return aLong.toString();

        Double aDouble = safeCast(object, Double.class);

        if (aDouble != null)
            return aDouble.toString();

        String aString = safeCast(object, String.class);

        return TextUtils.isEmpty(aString) ? "" : aString;
    }

    public static <T> T safeCast(Object o, Class<T> clazz) {
        return clazz != null && clazz.isInstance(o) ? clazz.cast(o) : null;
    }

    private static class PromotionKey {
        private static final String KEY_NAME = "name";
        private static final String KEY_ID = "id";
        private static final String KEY_CREATIVE = "creative";
        private static final String KEY_POSITION = "position";
    }

    private Bundle promotionMap(Map<String, Object> value) {
        String id = bruteForceCastToString(value.remove(PromotionKey.KEY_ID));
        String name = (String) value.remove(PromotionKey.KEY_NAME);
        String creative = (String) value.remove(PromotionKey.KEY_CREATIVE);
        String position = bruteForceCastToString(value.remove(PromotionKey.KEY_POSITION));

        Bundle promotionsBundle = new Bundle();
        promotionsBundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);  // ‘id’ or ‘name’ is required
        promotionsBundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);    // promotion slot name: ex: /p/fashion-wanita - p1 - slider banner
        promotionsBundle.putString(FirebaseAnalytics.Param.CREATIVE_NAME, creative);
        promotionsBundle.putString(FirebaseAnalytics.Param.CREATIVE_SLOT, position);

        // custom dimension
        for (Map.Entry<String, Object> entry : value.entrySet()) {
            promotionsBundle.putString(entry.getKey(), bruteForceCastToString(entry.getValue()));
        }
        return promotionsBundle;

    }

    public static Map<String, Object> clone(Map<String, Object> original){
        Map<String, Object> map = new HashMap<>();
        for(Iterator<String> iterator = original.keySet().iterator(); iterator.hasNext(); ){
            String key = iterator.next();
            Object value = original.get(key);
            if(value != null){
                if(value instanceof Map){
                    map.put(key, clone((Map<String, Object>) value));
                }else if(value instanceof Object[]){
                    map.put(key, clone((Object[]) value));
                }else if(value instanceof List){
                    map.put(key,clone((List) value));
                }else{
                    map.put(key,value);
                }
            }
        }
        return map;
    }

    public static List clone(List original) {
        List result = new ArrayList();
        if(original!=null){
            for (int i = 0; i < original.size(); i++) {
                Object value = original.get(i);
                if(value != null){
                    if(value instanceof Map){
                        result.add(clone((Map<String, Object>) value));
                    }else if(value instanceof Object[]){
                        result.add(clone((Object[]) value));
                    }else if(value instanceof List){
                        result.add(clone((List) value));
                    }else{
                        result.add(value);
                    }
                }
            }
        }
        return result;
    }

    public static Object[] clone(Object original[]) {
        Object result[] = new Object[original.length];
        if(original!=null){
            for (int i = 0; i < original.length; i++) {
                Object value = original[i];
                if(value != null){
                    if(value instanceof Map){
                        result[i] = clone((Map<String, Object>) value);
                    }else if(value instanceof Object[]){
                        result[i] = clone((Object[]) value);
                    }else if(value instanceof List){
                        result[i] = clone((List) value);
                    }else{
                        result[i] = value;
                    }
                }
            }
        }
        return result;
    }


    public TagManager getTagManager() {
        return TagManager.getInstance(getContext());
    }


    public String getClientIDString() {
        try {
            if(TextUtils.isEmpty(clientIdString)) {
                Bundle bundle = getContext().getPackageManager().getApplicationInfo(getContext().getPackageName(), PackageManager.GET_META_DATA).metaData;
                clientIdString = GoogleAnalytics.getInstance(getContext()).newTracker(bundle.getString(AppEventTracking.GTM.GA_ID)).get("&cid");
            }
            return clientIdString;
        } catch (Exception e) {
            e.printStackTrace();
            return "NO_GA_ID";
        }
    }

    @Override
    public void initialize() {
        super.initialize();
        try {
            Bundle bundle = getContext().getPackageManager().getApplicationInfo(getContext().getPackageName(), PackageManager.GET_META_DATA).metaData;
            TagManager tagManager = getTagManager();
            PendingResult<ContainerHolder> pResult = tagManager.loadContainerPreferFresh(bundle.getString(AppEventTracking.GTM.GTM_ID),
                    bundle.getInt(AppEventTracking.GTM.GTM_RESOURCE));

            pResult.setResultCallback(cHolder -> {
                cHolder.setContainerAvailableListener((containerHolder, s) -> {
                    if (!containerHolder.getStatus().isSuccess()) {
                        Log.d("GTM TKPD", "Container Available Failed");
                        return;
                    }

                    Log.d("GTM TKPD", "Container Available");

                    if (remoteConfig.getBoolean(RemoteConfigKey.ENABLE_GTM_REFRESH, true)) {
                        if (isAllowRefreshDefault(containerHolder)) {
                            Log.d("GTM TKPD", "Refreshed Container ");
                            refreshContainerInBackground(containerHolder);
                        }
                    }
                });
            }, 2, TimeUnit.SECONDS);
        } catch (Exception e) {
            eventError(getContext().getClass().toString(), e.toString());
        }
    }


    private void refreshContainerInBackground(ContainerHolder containerHolder) {
        if (remoteConfig.getBoolean(RemoteConfigKey.GTM_REFRESH_IN_BACKGROUND, false)) {
            //Refresh the container on background thread
            Observable.just(containerHolder)
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .map(it -> {
                        containerHolder.refresh();
                        Log.d("GTM TKPD", "Refreshed Container in Background");
                        return true;
                    })
                    .subscribe(getDefaultSubscriber());
        } else {
            containerHolder.refresh();
            Log.d("GTM TKPD", "Refreshed Container in Main Thread");
        }
    }


    private Boolean isAllowRefreshDefault(ContainerHolder containerHolder) {
        long lastRefresh = 0;
        if (containerHolder.getContainer() != null) {
            lastRefresh = containerHolder.getContainer().getLastRefreshTime();
        }
        if (lastRefresh <= 0) {
            return true;
        }
        long gtmExpiredTime = remoteConfig.getLong(KEY_GTM_EXPIRED_TIME, EXPIRE_CONTAINER_TIME_DEFAULT);
        long gtmExpiredTimeInMillis = TimeUnit.MINUTES.toMillis(gtmExpiredTime);
        return System.currentTimeMillis() - lastRefresh > gtmExpiredTimeInMillis;
    }

    public void eventError(String screenName, String errorDesc) {

    }

    public void sendScreen(String screenName, Map<String, String> customDimension) {
        // v4 sendScreen
        Map<String, Object> map = DataLayer.mapOf("screenName", screenName);
        if (customDimension != null && customDimension.size() > 0) {
            map.putAll(customDimension);
        }
        pushEvent("openScreen", map);


        final SessionHandler sessionHandler = RouterUtils.getRouterFromContext(getContext()).legacySessionHandler();
        final String afUniqueId = !TextUtils.isEmpty(getAfUniqueId(context)) ? getAfUniqueId(context) : "none";


        // V5 sendScreen
        Bundle bundle = new Bundle();
        bundle.putString("screenName", screenName);
        bundle.putString("appsflyerId", afUniqueId);
        bundle.putString("userId", sessionHandler.getLoginID());
        bundle.putString("clientId", getClientIDString());

        if (customDimension != null) {
            for (String key : customDimension.keySet()) {
                if (customDimension.get(key) != null) {
                    bundle.putString(key, customDimension.get(key));
                }
            }
        }

        logEvent("openScreen", bundle, context);
    }

    public void pushEvent(String eventName, Map<String, Object> values) {
        Map<String, Object> data = new HashMap<>(values);
        Observable.just(data)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(it -> {
                    log(getContext(), eventName, it);
                    getTagManager().getDataLayer().pushEvent(eventName, it);
                    pushIris(eventName, it);
                    return true;
                })
                .subscribe(getDefaultSubscriber());
    }

    @Override
    public void sendGTMGeneralEvent(String event, String category, String action, String label,
                                    String shopId, String shopType, String userId,
                                    @Nullable Map<String, Object> customDimension) {
        Map<String, Object> map = new HashMap<>();
        map.put(KEY_EVENT, event);
        map.put(KEY_CATEGORY, category);
        map.put(KEY_ACTION, action);
        map.put(KEY_LABEL, label);
        map.put(USER_ID, userId);
        map.put(SHOP_TYPE, shopType);
        map.put(SHOP_ID, shopId);
        if (customDimension != null) {
            map.putAll(customDimension);
        }
        pushGeneral(map);
    }

    private void log(Context context, String eventName, Bundle bundle) {
        log(context, eventName, bundleToMap(bundle));
    }

    private void log(Context context, String eventName, Map<String, Object> values) {
        String name = eventName == null ? (String) values.get("event") : eventName;
        GtmLogger.getInstance(context).save(name, values);
        if (tetraDebugger != null) {
            tetraDebugger.send(values);
        }
    }

    private static Map<String, Object> bundleToMap(Bundle extras) {
        Map<String, Object> map = new HashMap<>();

        Set<String> ks = extras.keySet();
        for (String key : ks) {
            Object object = extras.get(key);
            if (object != null) {
                if (object instanceof ArrayList) {
                    object = convertAllBundleToMap((ArrayList) object);
                } else if (object instanceof Bundle) {
                    object = bundleToMap((Bundle) object);
                }
                map.put(key, object);
            }
        }
        return map;
    }

    private static List<Object> convertAllBundleToMap(ArrayList list) {
        List<Object> newList = new ArrayList<>();
        for (Object object : list) {
            if (object instanceof Bundle) {
                newList.add(bundleToMap((Bundle) object));
            } else {
                newList.add(object);
            }
        }
        return newList;
    }

    public void sendScreenAuthenticated(String screenName) {
        if (TextUtils.isEmpty(screenName)) return;
        eventAuthenticate(null);
        sendScreen(screenName, null);
    }

    public void sendScreenAuthenticated(String screenName, Map<String, String> customDimension) {
        if (TextUtils.isEmpty(screenName)) return;
        eventAuthenticate(customDimension);
        sendScreen(screenName, customDimension);
    }

    public void sendScreenAuthenticated2(String screenName, String shopID, String shopType, String pageType, String productId) {
        if (TextUtils.isEmpty(screenName)) return;
        Map<String, String> customDimension = new HashMap<>();
        customDimension.put(Authenticated.KEY_SHOP_ID_SELLER, shopID);
        customDimension.put(Authenticated.KEY_PAGE_TYPE, pageType);
        customDimension.put(Authenticated.KEY_SHOP_TYPE, shopType);
        customDimension.put(Authenticated.KEY_PRODUCT_ID, productId);
        eventAuthenticate(customDimension);
        sendScreen(screenName, customDimension);
    }

    public void sendScreenAuthenticated(String screenName, String shopID, String shopType, String pageType, String productId) {
        if (TextUtils.isEmpty(screenName)) return;
        Map<String, String> customDimension = new HashMap<>();
        customDimension.put(Authenticated.KEY_SHOP_ID_SELLER, shopID);
        customDimension.put(Authenticated.KEY_PAGE_TYPE, pageType);
        customDimension.put(Authenticated.KEY_SHOP_TYPE, shopType);
        customDimension.put(Authenticated.KEY_PRODUCT_ID, productId);
        eventAuthenticate(customDimension);
        sendScreen(screenName, customDimension);
    }

    @Override
    public void sendEvent(String eventName, Map<String, Object> eventValue) {
        //no op, only for appsfyler and moengage
    }

    public void eventAuthenticate() {
        eventAuthenticate(null);
    }

    public void eventAuthenticate(Map<String, String> customDimension) {
        String afUniqueId = getAfUniqueId(context);
        final SessionHandler sessionHandler = RouterUtils.getRouterFromContext(getContext()).legacySessionHandler();
        Map<String, Object> map = DataLayer.mapOf(
                Authenticated.KEY_CONTACT_INFO, DataLayer.mapOf(
                        Authenticated.KEY_USER_SELLER, (sessionHandler.isUserHasShop() ? 1 : 0),
                        Authenticated.KEY_USER_FULLNAME, sessionHandler.getLoginName(),
                        Authenticated.KEY_USER_ID, sessionHandler.getGTMLoginID(),
                        Authenticated.KEY_SHOP_ID, sessionHandler.getShopID(),
                        Authenticated.KEY_AF_UNIQUE_ID, (afUniqueId != null ? afUniqueId : "none"),
                        Authenticated.KEY_USER_EMAIL, sessionHandler.getEmail()
                ),
                Authenticated.ANDROID_ID, sessionHandler.getAndroidId(),
                Authenticated.ADS_ID, sessionHandler.getAdsId(),
                Authenticated.GA_CLIENT_ID, getClientIDString()
        );
        if (customDimension != null && customDimension.size() > 0) {
            map.putAll(customDimension);
        }
        pushEvent(Authenticated.KEY_CD_NAME, map);
    }

    public GTMAnalytics eventAddtoCart(GTMCart cart) {
        pushEvent("addToCart", DataLayer.mapOf("ecommerce", cart.getCartMap()));
        return this;
    }

    public GTMAnalytics clearAddtoCartDataLayer(String act) {
        pushGeneral(DataLayer.mapOf("products", null,
                "currencyCode", null, "addToCart", null, "ecommerce", null, act, null));
        return this;
    }

    private static final String TRANSACTION = "transaction";
    private static final String PRODUCTVIEW = "productview";
    private static final String PRODUCTCLICK = "productclick";
    private static final String VIEWPRODUCT = "viewproduct";
    private static final String REMOVEFROMCART = "removefromcart";
    private static final String ADDTOCART = "addtocart";
    private static final String CHECKOUT = "checkout";
    private static final String BEGINCHECKOUT = "begin_checkout";
    private static final String CHECKOUT_PROGRESS = "checkout_progress";
    private static final String PROMOCLICK = "promoclick";
    public static final String PROMOVIEW = "promoview";

    private void pushEECommerceInternal(String keyEvent, Bundle bundle) {
        // replace list
        if (TextUtils.isEmpty(bundle.getString(FirebaseAnalytics.Param.ITEM_LIST))
                && !TextUtils.isEmpty(bundle.getString("list"))) {
            bundle.putString(FirebaseAnalytics.Param.ITEM_LIST, bundle.getString("list"));
            bundle.remove("list");
        }

        switch (keyEvent.toLowerCase()) {
            case PRODUCTVIEW:
                keyEvent = FirebaseAnalytics.Event.VIEW_ITEM_LIST;
                break;
            case PROMOCLICK:
            case PRODUCTCLICK:
                keyEvent = FirebaseAnalytics.Event.SELECT_CONTENT;
                break;
            case PROMOVIEW:
            case VIEWPRODUCT:
                keyEvent = FirebaseAnalytics.Event.VIEW_ITEM;
                break;
            case ADDTOCART:
                keyEvent = FirebaseAnalytics.Event.ADD_TO_CART;
                break;
            case BEGINCHECKOUT:
                keyEvent = FirebaseAnalytics.Event.BEGIN_CHECKOUT;
                break;
            case CHECKOUT_PROGRESS:
                keyEvent = FirebaseAnalytics.Event.CHECKOUT_PROGRESS;
                break;
            case TRANSACTION:
                keyEvent = FirebaseAnalytics.Event.ECOMMERCE_PURCHASE;
                break;
        }
        logEvent(keyEvent, bundle, context);
    }

    public void sendCampaign(Map<String, Object> param) {
        Bundle bundle = new Bundle();

        final SessionHandler sessionHandler = RouterUtils.getRouterFromContext(getContext()).legacySessionHandler();
        String afUniqueId = getAfUniqueId(context);

        bundle.putString("appsflyerId", afUniqueId);
        bundle.putString("userId", sessionHandler.getLoginID());
        bundle.putString("clientId", getClientIDString());

        bundle.putString("screenName", (String) param.get("screenName"));

        bundle.putString("gclid", (String) param.get(AppEventTracking.GTM.UTM_GCLID));
        bundle.putString("utmSource", (String) param.get(AppEventTracking.GTM.UTM_SOURCE));
        bundle.putString("utmMedium", (String) param.get(AppEventTracking.GTM.UTM_MEDIUM));
        bundle.putString("utmCampaign", (String) param.get(AppEventTracking.GTM.UTM_CAMPAIGN));
        bundle.putString("utmContent", (String) param.get(AppEventTracking.GTM.UTM_CAMPAIGN));
        bundle.putString("utmTerm", (String) param.get(AppEventTracking.GTM.UTM_TERM));

        logEvent("campaignTrack", bundle, context);
    }

    public static String GENERAL_EVENT_KEYS[] = new String[]{
            KEY_ACTION, KEY_CATEGORY, KEY_LABEL, KEY_EVENT
    };

    public void pushGeneralGtmV5Internal(Map<String, Object> params) {
        pushGeneral(params);

        if(TextUtils.isEmpty((String)params.get(KEY_EVENT)))
            return;

        Bundle bundle = new Bundle();
        bundle.putString(KEY_CATEGORY, params.get(KEY_CATEGORY) + "");
        bundle.putString(KEY_ACTION, params.get(KEY_ACTION) + "");
        bundle.putString(KEY_LABEL, params.get(KEY_LABEL) + "");

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            if (!Arrays.asList(GENERAL_EVENT_KEYS).contains(entry.getKey()))
                bundle.putString(entry.getKey(), bruteForceCastToString(entry.getValue()));
        }

        logEvent(params.get(KEY_EVENT) + "", bundle, context);
    }

    public void pushGeneralGtmV5Internal(String event, String category, String action, String label) {
        sendGeneralEvent(event, category, action, label);

        Bundle bundle = new Bundle();
        bundle.putString(KEY_CATEGORY, category);
        bundle.putString(KEY_ACTION, action);
        bundle.putString(KEY_LABEL, label);

        logEvent(event, bundle, context);
    }

    public void logEvent(String eventName, Bundle bundle, Context context) {
        try {
            FirebaseAnalytics.getInstance(context).logEvent(eventName, bundle);
            log(context, eventName, bundle);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void pushGeneral(Map<String, Object> values) {
        Map<String, Object> data = clone(values);
        Observable.just(data)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(it -> {
                    log(getContext(), null, it);
                    TagManager.getInstance(getContext()).getDataLayer().push(it);
                    pushIris("", it);
                    return true;
                })
                .subscribe(getDefaultSubscriber());
    }

    public void pushUserId(String userId) {
        Observable.just(userId)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(uid -> {
                    Map<String, Object> maps = new HashMap<>();
                    maps.put("user_id", uid);
                    getTagManager().getDataLayer().push(maps);
                    if (tetraDebugger != null) {
                        tetraDebugger.setUserId(userId);
                    }
                    return true;
                })
                .subscribe(getDefaultSubscriber());
    }

    public void eventOnline(String uid) {
        pushEvent(
                "onapps", DataLayer.mapOf("LoginId", uid));
    }

    public void event(String name, Map<String, Object> data) {
        pushEvent(name, data);
    }

    /**
     * ada skema di gtm yang nge-cache nah ini gimana?
     */
    public void clearEnhanceEcommerce() {
        pushGeneral(
                DataLayer.mapOf("event", null,
                        "eventCategory", null,
                        "eventAction", null,
                        "eventLabel", null,
                        "products", null,
                        "promotions", null,
                        "ecommerce", null,
                        "currentSite", null,
                        "channelId", null
                )
        );
    }

    private void pushIris(String eventName, Map<String, Object> values) {
        if (iris != null) {
            if (!eventName.isEmpty()) {
                values.put("event", eventName);
            }
            if (values.get("event") != null && !String.valueOf(values.get("event")).equals("")) {
                iris.saveEvent(values);
            }
        }
    }

    private static class GTMBody {
        Map<String, Object> values;
        String eventName;
    }

    private Subscriber<Boolean> getDefaultSubscriber() {
        return new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Boolean ignored) {

            }
        };
    }
}
