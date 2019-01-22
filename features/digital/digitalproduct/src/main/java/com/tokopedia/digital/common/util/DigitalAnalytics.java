package com.tokopedia.digital.common.util;

import android.content.Context;

import com.google.android.gms.tagmanager.DataLayer;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData;
import com.tokopedia.common_digital.cart.view.model.cart.CartDigitalInfoData;
import com.tokopedia.digital.cart.presentation.activity.CartDigitalActivity;
import com.tokopedia.digital.common.constant.DigitalEventTracking;
import com.tokopedia.digital.newcart.domain.model.DealProductViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.tokopedia.digital.common.constant.DigitalEventTracking.Action.CLICK_PANDUAN_SECTION;
import static com.tokopedia.digital.common.constant.DigitalEventTracking.Category.DIGITAL_NATIVE;
import static com.tokopedia.digital.common.constant.DigitalEventTracking.Event.DIGITAL_GENERAL_EVENT;

/**
 * @author by furqan on 13/08/18.
 */

public class DigitalAnalytics {

    private AnalyticTracker analyticTracker;
    private Context context;

    public DigitalAnalytics(AnalyticTracker analyticTracker, Context context) {
        this.analyticTracker = analyticTracker;
        this.context = context;
    }

    public void eventClickPanduanPage(String categoryName) {
        analyticTracker.sendEventTracking(
                DIGITAL_GENERAL_EVENT,
                DIGITAL_NATIVE,
                CLICK_PANDUAN_SECTION,
                categoryName.toLowerCase()
        );
    }

    public void eventAddToCart(CartDigitalInfoData cartDigitalInfoData, int extraComeFrom) {
        String productName = cartDigitalInfoData.getAttributes().getOperatorName().toLowerCase() + " " +
                cartDigitalInfoData.getAttributes().getPrice().toLowerCase();
        List<Object> products = new ArrayList<>();
        products.add(constructProductEnhanceEcommerce(cartDigitalInfoData, productName));

        analyticTracker.sendEnhancedEcommerce(
                DataLayer.mapOf("event", DigitalEventTracking.Event.ADD_TO_CART,
                        "eventCategory", extraComeFrom == DigitalCheckoutPassData.PARAM_WIDGET ? DigitalEventTracking.Category.HOMEPAGE_DIGITAL_WIDGET :
                                DigitalEventTracking.Category.DIGITAL_NATIVE,
                        "eventAction", DigitalEventTracking.Action.CLICK_BELI,
                        "eventLabel", cartDigitalInfoData.getAttributes().getCategoryName().toLowerCase() +
                                " - " + (cartDigitalInfoData.isInstantCheckout() ? "instant" : "non instant"),
                        "ecommerce", DataLayer.mapOf(
                                "currencyCode", "IDR",
                                "add", DataLayer.mapOf(
                                        "products", DataLayer.listOf(
                                                products.toArray(new Object[products.size()]))
                                )
                        ),
                        "currentSite", DigitalEventTracking.Label.SITE
                )
        );

        analyticTracker.sendEnhancedEcommerce(
                DataLayer.mapOf(
                        "ecommerce", null,
                        "currentSite", null
                )
        );

    }

    private Map<String, Object> constructProductEnhanceEcommerce(CartDigitalInfoData cartDigitalInfoData,
                                                                 String productName) {
        return DataLayer.mapOf(
                "name", productName,
                "id", cartDigitalInfoData.getRelationships().getRelationProduct().getData().getId(),
                "price", String.valueOf(cartDigitalInfoData.getAttributes().getPricePlain()),
                "brand", cartDigitalInfoData.getAttributes().getOperatorName().toLowerCase(),
                "category", cartDigitalInfoData.getAttributes().getCategoryName().toLowerCase(),
                "variant", "none",
                "quantity", "1",
                "category_id", cartDigitalInfoData.getRelationships().getRelationCategory().getData().getId(),
                "cart_id", cartDigitalInfoData.getId()
        );
    }

    public void eventCheckout(CartDigitalInfoData cartDigitalInfoData) {
        String productName = cartDigitalInfoData.getAttributes().getOperatorName().toLowerCase() + " " +
                cartDigitalInfoData.getAttributes().getPrice().toLowerCase();
        List<Object> products = new ArrayList<>();
        products.add(constructProductEnhanceEcommerce(cartDigitalInfoData, productName));

        analyticTracker.sendEnhancedEcommerce(
                DataLayer.mapOf(
                        "event", DigitalEventTracking.Event.CHECKOUT,
                        "eventCategory", DigitalEventTracking.Category.DIGITAL_CHECKOUT,
                        "eventAction", DigitalEventTracking.Action.VIEW_CHECKOUT,
                        "eventLabel", cartDigitalInfoData.getAttributes().getCategoryName().toLowerCase(),
                        "ecommerce", DataLayer.mapOf(
                                "checkout", DataLayer.mapOf(
                                        "products", DataLayer.listOf(
                                                products.toArray(new Object[products.size()]))
                                )
                        ),
                        "currentSite", DigitalEventTracking.Label.SITE
                )
        );

        analyticTracker.sendEnhancedEcommerce(
                DataLayer.mapOf(
                        "ecommerce", null,
                        "currentSite", null
                )
        );
    }


    public void eventProceedToPayment(CartDigitalInfoData cartDataInfo, String voucherCode) {
        analyticTracker.sendEventTracking(
                DigitalEventTracking.Event.HOMEPAGE_INTERACTION,
                DigitalEventTracking.Category.DIGITAL_CHECKOUT,
                DigitalEventTracking.Action.CLICK_PROCEED_PAYMENT,
                cartDataInfo.getAttributes().getCategoryName().toLowerCase() + " - " +
                        (voucherCode != null && voucherCode.length() > 0 ? "promo" : "no promo")
        );
    }

    public void eventClickVoucher(String categoryName, String voucherAutoCode, String operatorName) {

    }

    public void eventSelectDeal(CharSequence dealCategory) {
        analyticTracker.sendEventTracking(
                DigitalEventTracking.Event.CLICK_CHECKOUT,
                DigitalEventTracking.Category.DIGITAL_MULTIPLE_CHECKOUT,
                DigitalEventTracking.Action.SELECT_DEAL_CATEGORY,
                String.valueOf(dealCategory).toLowerCase()
        );
    }

    public void eventAddDeal(DealProductViewModel productViewModel) {
        String label = productViewModel.getCategoryName() + " - " + productViewModel.getBrandName() + " - " +
                productViewModel.getTitle() + " - " + productViewModel.getSalesPriceNumeric();
        analyticTracker.sendEventTracking(
                DigitalEventTracking.Event.CLICK_CHECKOUT,
                DigitalEventTracking.Category.DIGITAL_MULTIPLE_CHECKOUT,
                DigitalEventTracking.Action.ADD_DEAL_OFFER,
                String.valueOf(label).toLowerCase()
        );
    }

    public void eventRemoveDeal(DealProductViewModel productViewModel) {
        String label = productViewModel.getCategoryName() + " - " + productViewModel.getBrandName() + " - " +
                productViewModel.getTitle() + " - " + productViewModel.getSalesPriceNumeric();
        analyticTracker.sendEventTracking(
                DigitalEventTracking.Event.CLICK_CHECKOUT,
                DigitalEventTracking.Category.DIGITAL_MULTIPLE_CHECKOUT,
                DigitalEventTracking.Action.REMOVE_DEAL_OFFER,
                String.valueOf(label).toLowerCase()
        );
    }

    public void eventSkipDeal() {
        analyticTracker.sendEventTracking(
                DigitalEventTracking.Event.CLICK_CHECKOUT,
                DigitalEventTracking.Category.DIGITAL_MULTIPLE_CHECKOUT,
                DigitalEventTracking.Action.CLICK_SKIP,
                DigitalEventTracking.Label.DEFAULT_EMPTY_VALUE
        );
    }

    public void eventDealMaximalError() {
        analyticTracker.sendEventTracking(
                DigitalEventTracking.Event.CLICK_CHECKOUT,
                DigitalEventTracking.Category.DIGITAL_MULTIPLE_CHECKOUT,
                DigitalEventTracking.Action.ERROR_TO_ADD_DEAL,
                DigitalEventTracking.Label.DEFAULT_EMPTY_VALUE
        );

    }

    public void eventAddDeal(String categoryName, String voucherCode, int dealsSize) {
        String newVoucherLabel = voucherCode.length() > 0 ? voucherCode :
                DigitalEventTracking.Label.NO_PROMO;
        String label = categoryName + " - " + newVoucherLabel + " - " + dealsSize;
        analyticTracker.sendEventTracking(
                DigitalEventTracking.Event.CLICK_CHECKOUT,
                DigitalEventTracking.Category.DIGITAL_MULTIPLE_CHECKOUT,
                DigitalEventTracking.Action.CLICK_PROCEED_PAYMENT,
                label.toLowerCase()
        );
    }

    public void eventMulticheckoutDeal(CartDigitalInfoData cartDigitalInfoData) {
        String productName = cartDigitalInfoData.getAttributes().getOperatorName().toLowerCase() + " " +
                cartDigitalInfoData.getAttributes().getPrice().toLowerCase();
        List<Object> products = new ArrayList<>();
        products.add(constructProductEnhanceEcommerce(cartDigitalInfoData, productName));

        analyticTracker.sendEnhancedEcommerce(
                DataLayer.mapOf(
                        "event", DigitalEventTracking.Event.CHECKOUT,
                        "eventCategory", DigitalEventTracking.Category.DIGITAL_MULTIPLE_CHECKOUT,
                        "eventAction", DigitalEventTracking.Action.VIEW_CHECKOUT,
                        "eventLabel", cartDigitalInfoData.getAttributes().getCategoryName().toLowerCase(),
                        "ecommerce", DataLayer.mapOf(
                                "checkout", DataLayer.mapOf(
                                        "products", DataLayer.listOf(
                                                products.toArray(new Object[products.size()]))
                                )
                        )
                )
        );
    }
}
