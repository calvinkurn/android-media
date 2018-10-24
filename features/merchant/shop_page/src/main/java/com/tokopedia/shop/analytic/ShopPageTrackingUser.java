package com.tokopedia.shop.analytic;

import android.app.Activity;
import android.text.TextUtils;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.shop.analytic.model.CustomDimensionShopPage;
import com.tokopedia.shop.analytic.model.CustomDimensionShopPageAttribution;
import com.tokopedia.shop.analytic.model.CustomDimensionShopPageProduct;

import java.util.HashMap;

import javax.annotation.Nullable;

import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_ADD_NOTE;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_ADD_PRODUCT;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_ADD_PRODUCT_FROM_ZERO_PRODUCT;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_DISCUSSION;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_FOLLOWER_LIST;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_HOW_TO_ACTIVATE_SHOP;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_MANAGE_SHOP;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_MENU;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_MENU_FROM_MORE_MENU;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_MORE_MENU;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_OPEN_OPERATIONAL_SHOP;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_READ_NOTES;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_REQUEST_OPEN_SHOP;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_REVIEW;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_SHARE_BUTTON;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_SHOP_PAGE;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_SORT;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_SORT_BY;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_TAB;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_VIEW_ALL;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.IMPRESSION;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.IMPRESSION_ADD_PRODUCT_FROM_ZERO_PRODUCT;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.IMPRESSION_HOW_TO_ACTIVATE_SHOP;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.IMPRESSION_OF_REQUEST_OPEN_SHOP;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.IMPRESSION_OPEN_OPERATIONAL_SHOP;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.INFO;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.MANAGE_PRODUCT;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.MANAGE_SHOP;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.MERCHANT_VOUCHER_CODE;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.MVC_DETAIL;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.NO_SEARCH_RESULT;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.PRODUCT_NAVIGATION;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.SEARCH;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.SEARCH_BAR;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.SEARCH_RESULT;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.SEE_ALL;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.SHOP_PAGE_BUYER;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.SHOP_PAGE_SELLER;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.TOP_SECTION;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.USE_VOUCHER;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.VIEW_SHOP_PAGE;
import static com.tokopedia.shop.analytic.model.ListTitleTypeDef.HIGHLIGHTED;

public class ShopPageTrackingUser {
    public static final String SHOPPAGE = "/shoppage";
    protected final AbstractionRouter shopTrackingRouter;

    public ShopPageTrackingUser(AbstractionRouter router) {
        this.shopTrackingRouter = router;
    }

    private void sendScreenName(Activity activity, String screenName) {
        shopTrackingRouter.getAnalyticTracker().sendScreen(activity, screenName);
    }

    protected void sendEvent(String event, String category, String action, String label,
                             CustomDimensionShopPage customDimensionShopPage) {
        HashMap<String, Object> eventMap = createMap(event, category, action, label, customDimensionShopPage);
        shopTrackingRouter.getAnalyticTracker().sendEventTracking(eventMap);
    }

    protected HashMap<String, Object> createMap(String event, String category, String action, String label,
                                                @Nullable CustomDimensionShopPage customDimensionShopPage) {
        HashMap<String, Object> eventMap = new HashMap<>();
        eventMap.put(ShopPageTrackingConstant.EVENT, event);
        eventMap.put(ShopPageTrackingConstant.EVENT_CATEGORY, category);
        eventMap.put(ShopPageTrackingConstant.EVENT_ACTION, action);
        eventMap.put(ShopPageTrackingConstant.EVENT_LABEL, label);
        if (customDimensionShopPage!= null) {
            addCustomDimension(eventMap, customDimensionShopPage);
            if (customDimensionShopPage instanceof CustomDimensionShopPageProduct) {
                eventMap.put(ShopPageTrackingConstant.PRODUCT_ID,
                        ((CustomDimensionShopPageProduct) customDimensionShopPage).productId);
            }
            if (customDimensionShopPage instanceof CustomDimensionShopPageAttribution) {
                eventMap.put(ShopPageTrackingConstant.ATTRIBUTION,
                        ((CustomDimensionShopPageAttribution) customDimensionShopPage).attribution);
            }
        }
        return eventMap;
    }

    private String shopPageBuyerOrSeller(boolean isOwner) {
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
        return join(" - ", s);
    }

    protected String joinSpace(String... s) {
        return join(" ", s);
    }

    private String join(String separator, String... s) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(s[0]);
        int index = 1;
        int length = s.length;
        while (length > index) {
            if (!TextUtils.isEmpty(s[index])) {
                stringBuilder.append(separator);
                stringBuilder.append(s[index]);
            }
            index++;
        }
        return stringBuilder.toString();
    }

    public void sendScreenShopPage(Activity activity, String shopId) {
        sendScreenName(activity, joinDash(SHOPPAGE, shopId));
    }

    public void clickManageShop(CustomDimensionShopPage customDimensionShopPage) {
        sendEvent(CLICK_SHOP_PAGE,
                SHOP_PAGE_SELLER,
                joinDash(MANAGE_SHOP, CLICK),
                CLICK_MANAGE_SHOP,
                customDimensionShopPage);
    }

    public void clickAddProduct(CustomDimensionShopPage customDimensionShopPage) {
        sendEvent(CLICK_SHOP_PAGE,
                SHOP_PAGE_SELLER,
                joinDash(MANAGE_SHOP, CLICK),
                CLICK_ADD_PRODUCT,
                customDimensionShopPage);
    }

    public void clickFollowerList(boolean isOwner,
                                  CustomDimensionShopPage customDimensionShopPage) {
        sendEvent(CLICK_SHOP_PAGE,
                shopPageBuyerOrSeller(isOwner),
                joinDash(TOP_SECTION, CLICK),
                CLICK_FOLLOWER_LIST,
                customDimensionShopPage);
    }

    public void clickShareButton(boolean isOwner,
                                 CustomDimensionShopPage customDimensionShopPage) {
        sendEvent(CLICK_SHOP_PAGE,
                shopPageBuyerOrSeller(isOwner),
                joinDash(TOP_SECTION, CLICK),
                CLICK_SHARE_BUTTON,
                customDimensionShopPage);
    }

    public void clickTab(boolean isOwner,
                         String tabName,
                         CustomDimensionShopPage customDimensionShopPage) {
        sendEvent(CLICK_SHOP_PAGE,
                shopPageBuyerOrSeller(isOwner),
                joinDash(TOP_SECTION, CLICK),
                joinSpace(CLICK_TAB, tabName),
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

    public void searchKeyword(boolean isOwner,
                              String keyword,
                              boolean hasResult,
                              CustomDimensionShopPage customDimensionShopPage) {
        sendEvent(CLICK_SHOP_PAGE,
                shopPageBuyerOrSeller(isOwner),
                joinDash(SEARCH_BAR, CLICK),
                joinDash(joinSpace(SEARCH, keyword), hasResult ? SEARCH_RESULT : NO_SEARCH_RESULT),
                customDimensionShopPage);
    }

    public void clickEtalaseChip(boolean isOwner,
                                 String etalaseName,
                                 CustomDimensionShopPage customDimensionShopPage) {
        sendEvent(CLICK_SHOP_PAGE,
                shopPageBuyerOrSeller(isOwner),
                joinDash(PRODUCT_NAVIGATION, CLICK),
                joinDash(CLICK_MENU, etalaseName),
                customDimensionShopPage);
    }

    public void clickMoreMenuChip(boolean isOwner,
                                  CustomDimensionShopPage customDimensionShopPage) {
        sendEvent(CLICK_SHOP_PAGE,
                shopPageBuyerOrSeller(isOwner),
                joinDash(PRODUCT_NAVIGATION, CLICK),
                CLICK_MORE_MENU,
                customDimensionShopPage);
    }

    public void clickMenuFromMoreMenu(boolean isOwner,
                                      String etalaseName,
                                      CustomDimensionShopPage customDimensionShopPage) {
        sendEvent(CLICK_SHOP_PAGE,
                shopPageBuyerOrSeller(isOwner),
                joinDash(PRODUCT_NAVIGATION, CLICK),
                joinDash(CLICK_MENU_FROM_MORE_MENU, etalaseName),
                customDimensionShopPage);
    }

    public void clickSort(boolean isOwner,
                          CustomDimensionShopPage customDimensionShopPage) {
        sendEvent(CLICK_SHOP_PAGE,
                shopPageBuyerOrSeller(isOwner),
                joinDash(PRODUCT_NAVIGATION, CLICK),
                CLICK_SORT,
                customDimensionShopPage);
    }

    public void clickSortBy(boolean isOwner,
                            String sortName,
                            CustomDimensionShopPage customDimensionShopPage) {
        sendEvent(CLICK_SHOP_PAGE,
                shopPageBuyerOrSeller(isOwner),
                joinDash(PRODUCT_NAVIGATION, CLICK),
                joinDash(CLICK_SORT_BY, sortName),
                customDimensionShopPage);
    }

    public void clickHighLightSeeAll(boolean isOwner,
                                     String sectionName,
                                     CustomDimensionShopPage customDimensionShopPage) {
        sendEvent(CLICK_SHOP_PAGE,
                shopPageBuyerOrSeller(isOwner),
                joinDash(joinSpace(HIGHLIGHTED, sectionName), CLICK),
                CLICK_VIEW_ALL,
                customDimensionShopPage);
    }

    public void clickZeroProduct(CustomDimensionShopPage customDimensionShopPage) {
        sendEvent(CLICK_SHOP_PAGE,
                SHOP_PAGE_SELLER,
                joinDash(MANAGE_PRODUCT, CLICK),
                CLICK_ADD_PRODUCT_FROM_ZERO_PRODUCT,
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
                shopPageBuyerOrSeller(isOwner),
                joinDash(INFO, CLICK),
                joinDash(CLICK_READ_NOTES, String.valueOf(noteRowIndex + 1)),
                customDimensionShopPage);
    }

    public void clickReview(boolean isOwner,
                               CustomDimensionShopPage customDimensionShopPage) {
        sendEvent(CLICK_SHOP_PAGE,
                shopPageBuyerOrSeller(isOwner),
                joinDash(INFO, CLICK),
                CLICK_REVIEW,
                customDimensionShopPage);
    }

    public void clickDiscussion(boolean isOwner,
                                CustomDimensionShopPage customDimensionShopPage) {
        sendEvent(CLICK_SHOP_PAGE,
                shopPageBuyerOrSeller(isOwner),
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
        sendEvent(CLICK_SHOP_PAGE,
                shopPageBuyerOrSeller(isOwner),
                joinDash(MERCHANT_VOUCHER_CODE, CLICK),
                SEE_ALL,
                null);
    }

    public void clickDetailMerchantVoucher(boolean isOwner) {
        sendEvent(CLICK_SHOP_PAGE,
                shopPageBuyerOrSeller(isOwner),
                joinDash(MERCHANT_VOUCHER_CODE, CLICK),
                MVC_DETAIL,
                null);
    }

    public void clickUseMerchantVoucher(boolean isOwner) {
        sendEvent(CLICK_SHOP_PAGE,
                shopPageBuyerOrSeller(isOwner),
                joinDash(MERCHANT_VOUCHER_CODE, CLICK),
                USE_VOUCHER,
                null);
    }

    public void impressionUseMerchantVoucher(boolean isOwner) {
        sendEvent(VIEW_SHOP_PAGE,
                shopPageBuyerOrSeller(isOwner),
                joinDash(MERCHANT_VOUCHER_CODE, IMPRESSION),
                USE_VOUCHER,
                null);
    }

}
