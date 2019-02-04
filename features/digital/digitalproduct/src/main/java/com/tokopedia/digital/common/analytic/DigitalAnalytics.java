package com.tokopedia.digital.common.analytic;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.google.android.gms.tagmanager.DataLayer;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData;
import com.tokopedia.common_digital.cart.view.model.cart.CartDigitalInfoData;
import com.tokopedia.digital.newcart.domain.model.DealProductViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;



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
                DigitalEventTracking.Event.DIGITAL_GENERAL_EVENT,
                DigitalEventTracking.Category.DIGITAL_NATIVE,
                DigitalEventTracking.Action.CLICK_PANDUAN_SECTION,
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
                        "eventCategory", extraComeFrom == DigitalCheckoutPassData.Companion.getPARAM_WIDGET() ? DigitalEventTracking.Category.HOMEPAGE_DIGITAL_WIDGET :
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
        String label = String.format("%s - %s",
                cartDigitalInfoData.getAttributes().getCategoryName().toLowerCase(),
                cartDigitalInfoData.getAttributes().getOperatorName().toLowerCase()
        );

        analyticTracker.sendEnhancedEcommerce(
                DataLayer.mapOf(
                        "event", DigitalEventTracking.Event.CHECKOUT,
                        "eventCategory", DigitalEventTracking.Category.DIGITAL_CHECKOUT,
                        "eventAction", DigitalEventTracking.Action.VIEW_CHECKOUT,
                        "eventLabel", label,
                        "ecommerce", DataLayer.mapOf(
                                "checkout", DataLayer.mapOf(
                                        "actionField", DataLayer.mapOf(
                                                "step", "1",
                                                "option", DigitalEventTracking.Misc.ACTION_FIELD_STEP1
                                        ),
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
        String productName = cartDataInfo.getAttributes().getOperatorName().toLowerCase() + " " +
                cartDataInfo.getAttributes().getPrice().toLowerCase();
        List<Object> products = new ArrayList<>();
        products.add(constructProductEnhanceEcommerce(cartDataInfo, productName));
        String label = String.format("%s - %s - ",
                cartDataInfo.getAttributes().getCategoryName().toLowerCase(),
                cartDataInfo.getAttributes().getOperatorName().toLowerCase()
        );
        if (TextUtils.isEmpty(voucherCode)) {
            label += DigitalEventTracking.Label.NO_PROMO;
        } else {
            label += DigitalEventTracking.Label.PROMO;
        }


        analyticTracker.sendEnhancedEcommerce(
                DataLayer.mapOf(
                        "event", DigitalEventTracking.Event.CHECKOUT,
                        "eventCategory", DigitalEventTracking.Category.DIGITAL_CHECKOUT,
                        "eventAction", DigitalEventTracking.Action.CLICK_PROCEED_PAYMENT,
                        "eventLabel", label,
                        "ecommerce", DataLayer.mapOf(
                                "checkout", DataLayer.mapOf(
                                        "actionField", DataLayer.mapOf(
                                                "step", "2",
                                                "option", DigitalEventTracking.Misc.ACTION_FIELD_STEP2
                                        ),
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

    public void sendCategoryScreen(Activity activity, String name) {
        analyticTracker.sendScreen(activity, DigitalEventTracking.Screen.DIGITAL_CATEGORY + name.toLowerCase());
    }

    public void sendCartScreen(Activity activity) {
        analyticTracker.sendScreen(activity, DigitalEventTracking.Screen.DIGITAL_CHECKOUT);
    }

    public void eventClickBuyOnNative(String categoryItem, String isInstant) {
        analyticTracker.sendEventTracking(
                DigitalEventTracking.Event.HOMEPAGE_INTERACTION,
                DigitalEventTracking.Category.DIGITAL + categoryItem,
                DigitalEventTracking.Action.CLICK_BELI + " - " + categoryItem,
                isInstant.toLowerCase()
        );
    }

    public void eventSelectOperatorOnNativePage(String categoryName, String operatorName) {
        analyticTracker.sendEventTracking(DigitalEventTracking.Event.HOMEPAGE_INTERACTION,
                DigitalEventTracking.Category.DIGITAL + categoryName,
                DigitalEventTracking.Action.SELECT_OPERATOR,
                DigitalEventTracking.Label.PRODUCT + operatorName
        );
    }

    public void eventSelectProductOnNativePage(String categoryName, String productDesc) {
        analyticTracker.sendEventTracking(DigitalEventTracking.Event.HOMEPAGE_INTERACTION,
                DigitalEventTracking.Category.DIGITAL + categoryName,
                DigitalEventTracking.Action.SELECT_PRODUCT,
                DigitalEventTracking.Label.PRODUCT + productDesc
        );
    }

    public void eventClickDaftarTransaksiEvent(String categoryName) {
        analyticTracker.sendEventTracking(DigitalEventTracking.Event.HOMEPAGE_INTERACTION,
                DigitalEventTracking.Category.RECHARGE + categoryName,
                DigitalEventTracking.Action.CLICK_DAFTAR_TX,
                DigitalEventTracking.Label.PRODUCT + categoryName
        );
    }


    public void eventUssd(String categoryName, String label) {
        analyticTracker.sendEventTracking(DigitalEventTracking.Event.HOMEPAGE_INTERACTION,
                DigitalEventTracking.Category.RECHARGE + categoryName,
                DigitalEventTracking.Action.CLICK_USSD_CEK_SALDO,
                label
        );
    }

    public void eventUssd2(String action, String label) {
        analyticTracker.sendEventTracking(DigitalEventTracking.Event.HOMEPAGE_INTERACTION,
                DigitalEventTracking.Category.RECHARGE + DigitalEventTracking.Category.PULSA,
                action,
                label
        );
    }



    public void eventUssdAttempt(String label) {
        analyticTracker.sendEventTracking(DigitalEventTracking.Event.EVENT_IMPRESSION_HOME_PAGE,
                DigitalEventTracking.Category.DIGITAL + DigitalEventTracking.Category.PULSA ,
                DigitalEventTracking.Action.USSD_ATTEMPT,
                label
        );
    }



    public void eventUssdAttempt(String categoryName, String label) {
        analyticTracker.sendEventTracking(DigitalEventTracking.Event.EVENT_IMPRESSION_HOME_PAGE,
                DigitalEventTracking.Category.DIGITAL + categoryName,
                DigitalEventTracking.Action.USSD_ATTEMPT,
                label
        );
    }

    public void eventSelectNumberOnUserProfileNative(String categoryName) {
        analyticTracker.sendEventTracking(DigitalEventTracking.Event.EVENT_IMPRESSION_HOME_PAGE,
                DigitalEventTracking.Category.DIGITAL + categoryName,
                DigitalEventTracking.Action.CLICK_BILL,
                DigitalEventTracking.Label.DIGITAL
        );
    }

    public void eventBillShortcut() {
        analyticTracker.sendEventTracking(DigitalEventTracking.Event.LONG_CLICK,
                DigitalEventTracking.Category.LONG_PRESS,
                DigitalEventTracking.Action.USSD_ATTEMPT,
                DigitalEventTracking.Label.DIGITAL
        );
    }

    public void eventClickSearchBar(String categoryName) {
        analyticTracker.sendEventTracking(DigitalEventTracking.Event.EVENT_IMPRESSION_HOME_PAGE,
                DigitalEventTracking.Category.RECHARGE + categoryName,
                DigitalEventTracking.Action.CLICK_SEARCH_BAR,
                DigitalEventTracking.Label.PRODUCT + categoryName
        );
    }

    public void eventClickProductOnDigitalHomepage(String categoryName) {
        analyticTracker.sendEventTracking(DigitalEventTracking.Event.HOMEPAGE_INTERACTION,
                DigitalEventTracking.Category.DIGITAL_HOMEPAGE,
                DigitalEventTracking.Action.SELECT_CATEGORY,
                categoryName);
    }

    public void eventClickBuyOnWidget(String categoryName, String label) {
        analyticTracker.sendEventTracking(DigitalEventTracking.Event.HOMEPAGE_INTERACTION,
                DigitalEventTracking.Category.HOMEPAGE_DIGITAL_WIDGET,
                DigitalEventTracking.Action.CLICK_BELI + " - " + categoryName,
                label);
    }

    public void eventSelectNumberOnUserProfileWidget(String name) {
        analyticTracker.sendEventTracking(DigitalEventTracking.Event.EVENT_CLICK_USER_PROFILE,
                DigitalEventTracking.Category.HOMEPAGE_DIGITAL_WIDGET,
                DigitalEventTracking.Action.SELECT_NUMBER_ON_USER_PROFILE,
                name);
    }

    public void eventSelectOperatorOnWidget(String categoryName, String operatorName) {
        analyticTracker.sendEventTracking(DigitalEventTracking.Event.HOMEPAGE_INTERACTION,
                DigitalEventTracking.Category.HOMEPAGE_DIGITAL_WIDGET,
                DigitalEventTracking.Action.SELECT_OPERATOR,
                categoryName + " - " + operatorName);
    }

    public void eventSelectProductOnWidget(String categoryName, String productDesc) {
        analyticTracker.sendEventTracking(DigitalEventTracking.Event.HOMEPAGE_INTERACTION,
                DigitalEventTracking.Category.HOMEPAGE_DIGITAL_WIDGET,
                DigitalEventTracking.Action.SELECT_PRODUCT,
                categoryName + " - " + productDesc);
    }

    public void eventCheckInstantSaldo(String categoryName, boolean isChecked) {
        analyticTracker.sendEventTracking(DigitalEventTracking.Event.HOMEPAGE_INTERACTION,
                DigitalEventTracking.Category.RECHARGE + categoryName,
                isChecked? DigitalEventTracking.Action.CHECK_INSTANT_SALDO :  DigitalEventTracking.Action.UNCHECK_INSTANT_SALDO,
                DigitalEventTracking.Label.PRODUCT + categoryName);
    }
}
