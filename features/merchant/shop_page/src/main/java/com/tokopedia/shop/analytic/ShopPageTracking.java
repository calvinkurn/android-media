package com.tokopedia.shop.analytic;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.tokopedia.analyticconstant.DataLayer;
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel;
import com.tokopedia.shop.analytic.model.CustomDimensionShopPage;
import com.tokopedia.shop.analytic.model.CustomDimensionShopPageProduct;
import com.tokopedia.track.TrackApp;
import com.tokopedia.trackingoptimizer.TrackingQueue;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_ADD_NOTE;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_ADD_PRODUCT;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_BACK;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_CART_BUTTON;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_DISCUSSION;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_HOW_TO_ACTIVATE_SHOP;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_MANAGE_SHOP;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_MENU_FROM_MORE_MENU;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_OPEN_OPERATIONAL_SHOP;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_READ_NOTES;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_REQUEST_OPEN_SHOP;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_REVIEW;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_SETTING;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_SHOP_PAGE;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_SHOWCASE_X;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_SORT;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_SORT_BY;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_VIEW_ALL;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_X_TAB;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.ECOMMERCE;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.IMPRESSION;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.IMPRESSION_ADD_PRODUCT_FROM_ZERO_PRODUCT;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.IMPRESSION_HOW_TO_ACTIVATE_SHOP;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.IMPRESSION_OF_REQUEST_OPEN_SHOP;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.IMPRESSION_OPEN_OPERATIONAL_SHOP;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.INFO;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.MANAGE_PRODUCT;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.MANAGE_SHOP;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.MERCHANT_VOUCHER;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.MVC_DETAIL;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.PAGE_SOURCE;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.PAGE_TYPE;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.PRODUCT_NAVIGATION;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.PROMO_CLICK;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.PROMO_VIEW;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.SEE_ALL;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.SHOP_PAGE_BUYER;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.SHOP_PAGE_SELLER;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.SHOP_TYPE;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.SORT_PRODUCT;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.USE_VOUCHER;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.VIEW_SHOP_PAGE;

public class ShopPageTracking {
    public static final String SHOPPAGE = "/shoppage";
    public static final String SHOP_PAGE = "Shop page";

    protected final TrackingQueue trackingQueue;

    public ShopPageTracking(
            TrackingQueue trackingQueue) {
        this.trackingQueue = trackingQueue;
    }

    protected void sendDataLayerEvent(Map<String, Object> eventTracking) {
        if (eventTracking.containsKey(ECOMMERCE)) {
            trackingQueue.putEETracking((HashMap<String, Object>) eventTracking);
        } else {
            TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(eventTracking);
        }
    }

    protected void sendEvent(String event, String category, String action, String label,
                             CustomDimensionShopPage customDimensionShopPage) {
        HashMap<String, Object> eventMap = createMap(event, category, action, label, customDimensionShopPage);
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(eventMap);
    }

    protected void sendGeneralEvent(String event, String category, String action, String label,
                                    CustomDimensionShopPage customDimensionShopPage) {
        HashMap<String, Object> eventMap = createMap(event, category, action, label, customDimensionShopPage);
        TrackApp.getInstance().getGTM().sendGeneralEvent(eventMap);
    }

    public void sendAllTrackingQueue() {
        trackingQueue.sendAll();
    }

    private List<Object> createMvcListMap(List<MerchantVoucherViewModel> viewModelList, String shopId, int startIndex) {
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < viewModelList.size(); i++) {

            int position = startIndex + i + 1;
            MerchantVoucherViewModel viewModel = viewModelList.get(i);

            if (viewModel.isAvailable()) {
                list.add(
                        DataLayer.mapOf(
                                ShopPageTrackingConstant.ID, shopId,
                                ShopPageTrackingConstant.NAME, joinDash(SHOP_PAGE, String.valueOf(position), viewModel.getVoucherName()),
                                ShopPageTrackingConstant.POSITION, position,
                                ShopPageTrackingConstant.CREATIVE, "",
                                ShopPageTrackingConstant.PROMO_ID, viewModel.getVoucherId(), //optional
                                ShopPageTrackingConstant.PROMO_CODE, viewModel.getVoucherCode() //optional
                        )
                );
            }
        }

        if (list.size() == 0) {
            return new ArrayList<>();
        }

        return DataLayer.listOf(list.toArray(new Object[list.size()]));
    }

    protected HashMap<String, Object> createMap(String event, String category, String action, String label,
                                                @Nullable CustomDimensionShopPage customDimensionShopPage) {
        HashMap<String, Object> eventMap = new HashMap<>();
        eventMap.put(ShopPageTrackingConstant.EVENT, event);
        eventMap.put(ShopPageTrackingConstant.EVENT_CATEGORY, category);
        eventMap.put(ShopPageTrackingConstant.EVENT_ACTION, action);
        eventMap.put(ShopPageTrackingConstant.EVENT_LABEL, label);
        if (customDimensionShopPage != null) {
            addCustomDimension(eventMap, customDimensionShopPage);
            if (customDimensionShopPage instanceof CustomDimensionShopPageProduct) {
                eventMap.put(ShopPageTrackingConstant.PRODUCT_ID,
                        ((CustomDimensionShopPageProduct) customDimensionShopPage).productId);
                eventMap.put(ShopPageTrackingConstant.SHOP_REF,
                        ((CustomDimensionShopPageProduct) customDimensionShopPage).shopRef);
            }
        }
        return eventMap;
    }

    protected String getShopPageCategory(boolean isOwner) {
        if (isOwner) {
            return SHOP_PAGE_SELLER;
        } else {
            return SHOP_PAGE_BUYER;
        }
    }

    private void addCustomDimension(HashMap<String, Object> eventMap,
                                    CustomDimensionShopPage customDimensionShopPage) {
        eventMap.put(ShopPageTrackingConstant.SHOP_ID, customDimensionShopPage.shopId);
        eventMap.put(ShopPageTrackingConstant.SHOP_TYPE, customDimensionShopPage.shopType);
        eventMap.put(ShopPageTrackingConstant.PAGE_TYPE, SHOPPAGE);
    }

    protected String joinDash(String... s) {
        return TextUtils.join(" - ", s);
    }

    protected String joinSpace(String... s) {
        return TextUtils.join(" ", s);
    }

    public void sendScreenShopPage(String shopId, String shopType) {
        String screenName = joinDash(SHOPPAGE, shopId);
        Map<String,String> customDimension = new HashMap<>();
        customDimension.put(SHOP_TYPE, shopType);
        customDimension.put(PAGE_TYPE, SHOPPAGE);
        TrackApp.getInstance().getGTM().sendScreenAuthenticated(screenName,customDimension);
    }

    public void sendScreenShopPage(String shopId, String shopType, String pageSource) {
        String screenName = joinDash(SHOPPAGE, shopId);
        Map<String,String> customDimension = new HashMap<>();
        customDimension.put(SHOP_TYPE, shopType);
        customDimension.put(PAGE_TYPE, SHOPPAGE);
        customDimension.put(PAGE_SOURCE, pageSource);
        TrackApp.getInstance().getGTM().sendScreenAuthenticated(screenName,customDimension);
    }

    public void clickManageShop(CustomDimensionShopPage customDimensionShopPage) {
        sendEvent(CLICK_SHOP_PAGE,
                SHOP_PAGE_SELLER,
                joinDash(MANAGE_SHOP, CLICK),
                CLICK_MANAGE_SHOP,
                customDimensionShopPage);
    }

    public void clickAddProduct(CustomDimensionShopPage customDimensionShopPage) {
        sendGeneralEvent(CLICK_SHOP_PAGE,
                SHOP_PAGE_SELLER,
                CLICK_ADD_PRODUCT,
                "",
                customDimensionShopPage);
    }

    public void clickBackArrow(boolean isMyShop, CustomDimensionShopPage customDimensionShopPage) {
        sendGeneralEvent(CLICK_SHOP_PAGE,
                getShopPageCategory(isMyShop),
                CLICK_BACK,
                "",
                customDimensionShopPage);
    }

    public void sendOpenShop() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                "clickManageShop",
                "Manage Shop",
                "Click",
                "Shop Info"
        );
    }

    public void clickCartButton(boolean isOwner,
                                CustomDimensionShopPage customDimensionShopPage) {
        sendGeneralEvent(CLICK_SHOP_PAGE,
                getShopPageCategory(isOwner),
                CLICK_CART_BUTTON,
                "",
                customDimensionShopPage);
    }

    public void clickTab(boolean isOwner,
                         String tabName,
                         CustomDimensionShopPage customDimensionShopPage) {
        sendGeneralEvent(CLICK_SHOP_PAGE,
                getShopPageCategory(isOwner),
                String.format(CLICK_X_TAB, tabName.toLowerCase()),
                "",
                customDimensionShopPage);
    }

    public void clickRequestOpenShop(CustomDimensionShopPage customDimensionShopPage) {
        sendEvent(CLICK_SHOP_PAGE,
                SHOP_PAGE_SELLER,
                joinDash(MANAGE_SHOP, CLICK),
                CLICK_REQUEST_OPEN_SHOP,
                customDimensionShopPage);
    }

    public void impressionRequestOpenShop(CustomDimensionShopPage customDimensionShopPage) {
        sendEvent(VIEW_SHOP_PAGE,
                SHOP_PAGE_SELLER,
                joinDash(MANAGE_SHOP, IMPRESSION),
                IMPRESSION_OF_REQUEST_OPEN_SHOP,
                customDimensionShopPage);
    }

    public void impressionOpenOperationalShop(CustomDimensionShopPage customDimensionShopPage) {
        sendEvent(VIEW_SHOP_PAGE,
                SHOP_PAGE_SELLER,
                joinDash(MANAGE_SHOP, IMPRESSION),
                IMPRESSION_OPEN_OPERATIONAL_SHOP,
                customDimensionShopPage);
    }

    public void clickOpenOperationalShop(CustomDimensionShopPage customDimensionShopPage) {
        sendEvent(CLICK_SHOP_PAGE,
                SHOP_PAGE_SELLER,
                joinDash(MANAGE_SHOP, CLICK),
                CLICK_OPEN_OPERATIONAL_SHOP,
                customDimensionShopPage);
    }

    public void impressionHowToActivateShop(CustomDimensionShopPage customDimensionShopPage) {
        sendEvent(VIEW_SHOP_PAGE,
                SHOP_PAGE_SELLER,
                joinDash(MANAGE_SHOP, IMPRESSION),
                IMPRESSION_HOW_TO_ACTIVATE_SHOP,
                customDimensionShopPage);
    }

    public void clickHowToActivateShop(CustomDimensionShopPage customDimensionShopPage) {
        sendEvent(CLICK_SHOP_PAGE,
                SHOP_PAGE_SELLER,
                joinDash(MANAGE_SHOP, CLICK),
                CLICK_HOW_TO_ACTIVATE_SHOP,
                customDimensionShopPage);
    }

    public void clickEtalaseChip(boolean isOwner,
                                 String etalaseName,
                                 CustomDimensionShopPage customDimensionShopPage) {
        sendGeneralEvent(CLICK_SHOP_PAGE,
                getShopPageCategory(isOwner),
                String.format(CLICK_SHOWCASE_X, etalaseName),
                "",
                customDimensionShopPage);
    }

    public void clickMenuFromMoreMenu(boolean isOwner,
                                      String etalaseName,
                                      CustomDimensionShopPage customDimensionShopPage) {
        sendEvent(CLICK_SHOP_PAGE,
                getShopPageCategory(isOwner),
                joinDash(PRODUCT_NAVIGATION, CLICK),
                joinDash(CLICK_MENU_FROM_MORE_MENU, etalaseName),
                customDimensionShopPage);
    }

    public void clickSort(boolean isOwner,
                          CustomDimensionShopPage customDimensionShopPage) {
        sendGeneralEvent(CLICK_SHOP_PAGE,
                getShopPageCategory(isOwner),
                CLICK_SORT,
                "",
                customDimensionShopPage);
    }

    public void clickSortBy(boolean isOwner,
                            String sortName,
                            CustomDimensionShopPage customDimensionShopPage) {
        sendGeneralEvent(CLICK_SHOP_PAGE,
                getShopPageCategory(isOwner),
                SORT_PRODUCT,
                String.format(CLICK_SORT_BY, sortName),
                customDimensionShopPage);
    }

    public void clickHighLightSeeAll(CustomDimensionShopPage customDimensionShopPage) {
        sendGeneralEvent(CLICK_SHOP_PAGE,
                SHOP_PAGE_BUYER,
                CLICK_VIEW_ALL,
                "",
                customDimensionShopPage);
    }

    public void impressionZeroProduct(CustomDimensionShopPage customDimensionShopPage) {
        sendEvent(VIEW_SHOP_PAGE,
                SHOP_PAGE_SELLER,
                joinDash(MANAGE_PRODUCT, IMPRESSION),
                IMPRESSION_ADD_PRODUCT_FROM_ZERO_PRODUCT,
                customDimensionShopPage);
    }

    public void clickReadNotes(boolean isOwner, int noteRowIndex,
                               CustomDimensionShopPage customDimensionShopPage) {
        sendEvent(CLICK_SHOP_PAGE,
                getShopPageCategory(isOwner),
                joinDash(INFO, CLICK),
                joinDash(CLICK_READ_NOTES, String.valueOf(noteRowIndex + 1)),
                customDimensionShopPage);
    }

    public void clickReview(boolean isOwner,
                            CustomDimensionShopPage customDimensionShopPage) {
        sendEvent(CLICK_SHOP_PAGE,
                getShopPageCategory(isOwner),
                joinDash(INFO, CLICK),
                CLICK_REVIEW,
                customDimensionShopPage);
    }

    public void clickDiscussion(boolean isOwner,
                                CustomDimensionShopPage customDimensionShopPage) {
        sendEvent(CLICK_SHOP_PAGE,
                getShopPageCategory(isOwner),
                joinDash(INFO, CLICK),
                CLICK_DISCUSSION,
                customDimensionShopPage);
    }

    public void clickAddNote(CustomDimensionShopPage customDimensionShopPage) {
        sendEvent(CLICK_SHOP_PAGE,
                SHOP_PAGE_SELLER,
                joinDash(INFO, CLICK),
                CLICK_ADD_NOTE,
                customDimensionShopPage);
    }

    public void clickSeeAllMerchantVoucher(boolean isOwner) {
        Map<String, Object> eventMap = new HashMap<>();
        eventMap.put(ShopPageTrackingConstant.EVENT, CLICK_SHOP_PAGE);
        eventMap.put(ShopPageTrackingConstant.EVENT_CATEGORY, getShopPageCategory(isOwner));
        eventMap.put(ShopPageTrackingConstant.EVENT_ACTION, joinDash(CLICK, MERCHANT_VOUCHER, SEE_ALL));
        eventMap.put(ShopPageTrackingConstant.EVENT_LABEL, "");

        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(eventMap);
    }

    public void clickDetailMerchantVoucher(boolean isOwner, String voucherId) {
        Map<String, Object> eventMap = new HashMap<>();
        eventMap.put(ShopPageTrackingConstant.EVENT, CLICK_SHOP_PAGE);
        eventMap.put(ShopPageTrackingConstant.EVENT_CATEGORY, getShopPageCategory(isOwner));
        eventMap.put(ShopPageTrackingConstant.EVENT_ACTION, joinDash(CLICK, MERCHANT_VOUCHER, MVC_DETAIL));
        eventMap.put(ShopPageTrackingConstant.EVENT_LABEL, "");
        eventMap.put(ShopPageTrackingConstant.EVENT_PROMO_ID, voucherId);

        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(eventMap);
    }

    public void clickUseMerchantVoucher(boolean isOwner, MerchantVoucherViewModel viewModel, String shopId, int positionIndex) {
        if (isOwner) return;

        ArrayList<MerchantVoucherViewModel> viewModelList = new ArrayList<>();
        viewModelList.add(viewModel);

        if (viewModelList.isEmpty()) return;

        HashMap<String, Object> eventMap = new HashMap<>();
        eventMap.put(ShopPageTrackingConstant.EVENT, PROMO_CLICK);
        eventMap.put(ShopPageTrackingConstant.EVENT_CATEGORY, SHOP_PAGE_BUYER);
        eventMap.put(ShopPageTrackingConstant.EVENT_ACTION, joinDash(CLICK, MERCHANT_VOUCHER, USE_VOUCHER));
        eventMap.put(ShopPageTrackingConstant.EVENT_LABEL, "");
        eventMap.put(ShopPageTrackingConstant.EVENT_PROMO_ID, String.valueOf(viewModel.getVoucherId()));
        eventMap.put(
                ShopPageTrackingConstant.ECOMMERCE, DataLayer.mapOf(
                        ShopPageTrackingConstant.PROMO_CLICK, DataLayer.mapOf(
                                ShopPageTrackingConstant.PROMOTIONS, createMvcListMap(viewModelList, shopId, positionIndex)
                        )
                )
        );

        sendDataLayerEvent(eventMap);
    }

    public void impressionUseMerchantVoucher(boolean isOwner, List<MerchantVoucherViewModel> merchantVoucherViewModelList, String shopId) {
        if (isOwner || merchantVoucherViewModelList.isEmpty()) return;

        int index = 0;
        String voucherId = String.valueOf(merchantVoucherViewModelList.get(index).getVoucherId());
        HashMap<String, Object> eventMap = new HashMap<>();
        eventMap.put(ShopPageTrackingConstant.EVENT, PROMO_VIEW);
        eventMap.put(ShopPageTrackingConstant.EVENT_CATEGORY, SHOP_PAGE_BUYER);
        eventMap.put(ShopPageTrackingConstant.EVENT_ACTION, joinDash(IMPRESSION, MERCHANT_VOUCHER, USE_VOUCHER));
        eventMap.put(ShopPageTrackingConstant.EVENT_LABEL, "");
        eventMap.put(ShopPageTrackingConstant.EVENT_PROMO_ID, voucherId);
        eventMap.put(
                ShopPageTrackingConstant.ECOMMERCE, DataLayer.mapOf(
                        ShopPageTrackingConstant.PROMO_VIEW, DataLayer.mapOf(
                                ShopPageTrackingConstant.PROMOTIONS, createMvcListMap(merchantVoucherViewModelList, shopId, index)
                        )
                )
        );

        sendDataLayerEvent(eventMap);
    }

    public void clickReviewMore(@NotNull String shopId, boolean myShop) {
        HashMap<String, Object> eventMap = new HashMap<>();
        eventMap.put("event", "clickOfficialStore");
        eventMap.put("eventCategory", getEventReputationCategory(myShop));
        eventMap.put("eventAction", "Ulasan - bottom navigation - click");
        eventMap.put("eventLabel", "click see more");
        eventMap.put("shop_id", shopId);
        TrackApp.getInstance().getGTM().sendGeneralEvent(eventMap);
    }

    private String getEventReputationCategory(boolean myShop) {
        if (myShop) {
            return "official store shop page - buyer";
        } else {
            return "official store shop page - brand";
        }
    }

    public void clickSettingButton(CustomDimensionShopPage customDimension) {
        sendGeneralEvent(CLICK_SHOP_PAGE, SHOP_PAGE_SELLER, CLICK_SETTING, "", customDimension);
    }

    public void sortProduct(String sortName, boolean isOwner, CustomDimensionShopPage customDimensionShopPage) {
        sendGeneralEvent(
                CLICK_SHOP_PAGE,
                getShopPageCategory(isOwner),
                SORT_PRODUCT,
                String.format(CLICK_SORT_BY, sortName),
                customDimensionShopPage
        );
    }

    protected String formatPrice(String displayedPrice) {
        if (!TextUtils.isEmpty(displayedPrice)) {
            displayedPrice = displayedPrice.replaceAll("[^\\d]", "");
            return displayedPrice;
        } else {
            return "";
        }
    }
}
