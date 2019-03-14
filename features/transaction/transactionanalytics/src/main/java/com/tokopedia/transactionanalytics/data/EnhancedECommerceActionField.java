package com.tokopedia.transactionanalytics.data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author anggaprasetiyo on 31/07/18.
 */
public class EnhancedECommerceActionField {

    public static final String STEP_1 = "1";
    public static final String STEP_2 = "2";
    public static final String OPTION_CART_PAGE_LOADED = "cart page loaded";
    public static final String OPTION_CLICK_PAYMENT_OPTION_BUTTON = "click payment option button";
    public static final String OPTION_CLICK_CHECKOUT = "click checkout";
    public static final String OPTION_CLICK_BAYAR = "click bayar";

    private static final String KEY_STEP = "step";
    private static final String KEY_OPTION = "option";

    private Map<String, String> actionFieldMap = new HashMap<>();

    public void setStep(String step) {
        actionFieldMap.put(KEY_STEP, step);
    }

    public void setOption(String option) {
        actionFieldMap.put(KEY_OPTION, option);
    }

    public Map<String, String> getActionFieldMap() {
        return actionFieldMap;
    }
}
