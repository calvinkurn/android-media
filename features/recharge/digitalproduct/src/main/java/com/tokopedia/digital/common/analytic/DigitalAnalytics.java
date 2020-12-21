package com.tokopedia.digital.common.analytic;

import android.app.Activity;
import android.text.TextUtils;

import com.tokopedia.analyticconstant.DataLayer;
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData;
import com.tokopedia.digital.newcart.domain.model.DealProductViewModel;
import com.tokopedia.digital.newcart.presentation.model.cart.CartDigitalInfoData;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @author by furqan on 13/08/18.
 */

public class DigitalAnalytics {

    public DigitalAnalytics() {
    }

    public void eventClickPanduanPage(String categoryName) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                DigitalEventTracking.Event.DIGITAL_GENERAL_EVENT,
                DigitalEventTracking.Category.DIGITAL_NATIVE,
                DigitalEventTracking.Action.CLICK_PANDUAN_SECTION,
                categoryName.toLowerCase()
        ));
    }

    public void eventAddToCart(CartDigitalInfoData cartDigitalInfoData, int extraComeFrom) {
        String productName = cartDigitalInfoData.getAttributes().getOperatorName().toLowerCase() + " " +
                cartDigitalInfoData.getAttributes().getPrice().toLowerCase();
        List<Object> products = new ArrayList<>();
        products.add(constructProductEnhanceEcommerce(cartDigitalInfoData, productName));

        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
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

        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
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

        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
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

        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
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


        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
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

        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        "ecommerce", null,
                        "currentSite", null
                )
        );
    }

    public void eventSelectDeal(CharSequence dealCategory) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                DigitalEventTracking.Event.CLICK_CHECKOUT,
                DigitalEventTracking.Category.DIGITAL_MULTIPLE_CHECKOUT,
                DigitalEventTracking.Action.SELECT_DEAL_CATEGORY,
                String.valueOf(dealCategory).toLowerCase()
        ));
    }

    public void eventAddDeal(DealProductViewModel productViewModel) {
        String label = productViewModel.getCategoryName() + " - " + productViewModel.getBrandName() + " - " +
                productViewModel.getTitle() + " - " + productViewModel.getSalesPriceNumeric();
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                DigitalEventTracking.Event.CLICK_CHECKOUT,
                DigitalEventTracking.Category.DIGITAL_MULTIPLE_CHECKOUT,
                DigitalEventTracking.Action.ADD_DEAL_OFFER,
                label.toLowerCase()
        ));
    }

    public void eventRemoveDeal(DealProductViewModel productViewModel) {
        String label = productViewModel.getCategoryName() + " - " + productViewModel.getBrandName() + " - " +
                productViewModel.getTitle() + " - " + productViewModel.getSalesPriceNumeric();
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                DigitalEventTracking.Event.CLICK_CHECKOUT,
                DigitalEventTracking.Category.DIGITAL_MULTIPLE_CHECKOUT,
                DigitalEventTracking.Action.REMOVE_DEAL_OFFER,
                label.toLowerCase()
        ));
    }

    public void eventSkipDeal() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                DigitalEventTracking.Event.CLICK_CHECKOUT,
                DigitalEventTracking.Category.DIGITAL_MULTIPLE_CHECKOUT,
                DigitalEventTracking.Action.CLICK_SKIP,
                DigitalEventTracking.Label.DEFAULT_EMPTY_VALUE
        ));
    }

    public void eventDealMaximalError() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                DigitalEventTracking.Event.CLICK_CHECKOUT,
                DigitalEventTracking.Category.DIGITAL_MULTIPLE_CHECKOUT,
                DigitalEventTracking.Action.ERROR_TO_ADD_DEAL,
                DigitalEventTracking.Label.DEFAULT_EMPTY_VALUE
        ));

    }

    public void eventAddDeal(String categoryName, String voucherCode, int dealsSize) {
        String newVoucherLabel = voucherCode.length() > 0 ? voucherCode :
                DigitalEventTracking.Label.NO_PROMO;
        String label = categoryName + " - " + newVoucherLabel + " - " + dealsSize;
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                DigitalEventTracking.Event.CLICK_CHECKOUT,
                DigitalEventTracking.Category.DIGITAL_MULTIPLE_CHECKOUT,
                DigitalEventTracking.Action.CLICK_PROCEED_PAYMENT,
                label.toLowerCase()
        ));
    }

    public void eventMulticheckoutDeal(CartDigitalInfoData cartDigitalInfoData) {
        String productName = cartDigitalInfoData.getAttributes().getOperatorName().toLowerCase() + " " +
                cartDigitalInfoData.getAttributes().getPrice().toLowerCase();
        List<Object> products = new ArrayList<>();
        products.add(constructProductEnhanceEcommerce(cartDigitalInfoData, productName));

        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
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

        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        "ecommerce", null,
                        "currentSite", null
                )
        );
    }

    public void sendCartScreen(Activity activity) {
        TrackApp.getInstance().getGTM().sendScreenAuthenticated(DigitalEventTracking.Screen.DIGITAL_CHECKOUT);
    }

    public void eventClickBuyOnNative(String categoryItem, String isInstant) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                DigitalEventTracking.Event.HOMEPAGE_INTERACTION,
                DigitalEventTracking.Category.DIGITAL + categoryItem,
                DigitalEventTracking.Action.CLICK_BELI + " - " + categoryItem,
                isInstant.toLowerCase()
        ));
    }

    public void eventSelectOperatorOnNativePage(String categoryName, String operatorName) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(DigitalEventTracking.Event.HOMEPAGE_INTERACTION,
                DigitalEventTracking.Category.DIGITAL + categoryName,
                DigitalEventTracking.Action.SELECT_OPERATOR,
                DigitalEventTracking.Label.PRODUCT + operatorName
        ));
    }

    public void eventSelectProductOnNativePage(String categoryName, String productDesc) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(DigitalEventTracking.Event.HOMEPAGE_INTERACTION,
                DigitalEventTracking.Category.DIGITAL + categoryName,
                DigitalEventTracking.Action.SELECT_PRODUCT,
                DigitalEventTracking.Label.PRODUCT + productDesc
        ));
    }

    public void eventClickDaftarTransaksiEvent(String categoryName) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(DigitalEventTracking.Event.HOMEPAGE_INTERACTION,
                DigitalEventTracking.Category.RECHARGE + categoryName,
                DigitalEventTracking.Action.CLICK_DAFTAR_TX,
                DigitalEventTracking.Label.PRODUCT + categoryName
        ));
    }

    public void eventSelectNumberOnUserProfileNative(String categoryName) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(DigitalEventTracking.Event.EVENT_IMPRESSION_HOME_PAGE,
                DigitalEventTracking.Category.DIGITAL + categoryName,
                DigitalEventTracking.Action.CLICK_BILL,
                DigitalEventTracking.Label.DIGITAL
        ));
    }

    public void eventClickSearchBar(String categoryName) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(DigitalEventTracking.Event.EVENT_IMPRESSION_HOME_PAGE,
                DigitalEventTracking.Category.RECHARGE + categoryName,
                DigitalEventTracking.Action.CLICK_SEARCH_BAR,
                DigitalEventTracking.Label.PRODUCT + categoryName
        ));
    }

    public void eventCheckInstantSaldo(String categoryName, boolean isChecked) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(DigitalEventTracking.Event.HOMEPAGE_INTERACTION,
                DigitalEventTracking.Category.RECHARGE + categoryName,
                isChecked? DigitalEventTracking.Action.CHECK_INSTANT_SALDO :  DigitalEventTracking.Action.UNCHECK_INSTANT_SALDO,
                DigitalEventTracking.Label.PRODUCT + categoryName));
    }

    public void eventclickUseVoucher(String categoryName) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                DigitalEventTracking.Event.CLICK_COUPON,
                DigitalEventTracking.Category.DIGITAL_CHECKOUT,
                DigitalEventTracking.Action.CLICK_USE_COUPON, categoryName.toLowerCase()));
    }

    public void eventclickCancelApplyCoupon(String categoryName, String promoCode) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                DigitalEventTracking.Event.CLICK_COUPON,
                DigitalEventTracking.Category.DIGITAL_CHECKOUT,
                DigitalEventTracking.Action.CLICK_CANCEL_APPLY_COUPON,
                categoryName.toLowerCase() + " - " + promoCode.toLowerCase()));
    }

    public void onClickSliceRecharge(String userId,  String rechargeProductFromSlice) {
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(DataLayer.mapOf(
                DigitalEventTracking.Event.EVENT_KEY, "clickGAMain",
                DigitalEventTracking.Event. EVENT_CATEGORY, "ga main app",
                DigitalEventTracking.Event.EVENT_ACTION, "click item transaction",
                DigitalEventTracking.Event.EVENT_LABEL, rechargeProductFromSlice,
                DigitalEventTracking.Event.BUSINESS_UNIT, "recharge",
                DigitalEventTracking.Event.CURRENT_SITE, "tokopediadigital",
                DigitalEventTracking.Event.USER_ID, userId
        ));
    }

    public void  onOpenPageFromSlice() {
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(DataLayer.mapOf(
                DigitalEventTracking.Event.EVENT_KEY, "openScreen",
                DigitalEventTracking.Event.EVENT_SCREEN_NAME, "digital product - from voice search - mainapp"
        ));
    }
}
