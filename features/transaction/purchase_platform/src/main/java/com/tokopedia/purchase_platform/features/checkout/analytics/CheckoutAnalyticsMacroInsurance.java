package com.tokopedia.purchase_platform.features.checkout.analytics;

import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics;
import com.tokopedia.purchase_platform.common.analytics.TransactionAnalytics;

import java.util.Map;

import javax.inject.Inject;

public class CheckoutAnalyticsMacroInsurance extends TransactionAnalytics {

    @Inject
    public CheckoutAnalyticsMacroInsurance(){
    }

    public void eventImpressionOfInsuranceProductOnCheckout(String title) {
        sendEventCategoryActionLabel("",
                ConstantTransactionAnalytics.EventCategory.FIN_INSURANCE_CHECKOUT,
                ConstantTransactionAnalytics.EventAction.FIN_INSURANCE_CHECKOUT_IMPRESSION,
                String.format("checkout - %s", title));
    }

    public void eventClickPaymentMethodWithInsurance(String productId, String title) {
        Map<String, Object> mapEvent = getGTMDataFromTrackAppUtils(title);
        mapEvent.put("productId", productId);
        sendGeneralEvent(mapEvent);
    }
}
