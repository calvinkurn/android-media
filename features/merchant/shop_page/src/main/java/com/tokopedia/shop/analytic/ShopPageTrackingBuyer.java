package com.tokopedia.shop.analytic;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.tokopedia.analyticconstant.DataLayer;
import com.tokopedia.shop.analytic.model.CustomDimensionShopPage;
import com.tokopedia.shop.analytic.model.CustomDimensionShopPageAttribution;
import com.tokopedia.shop.analytic.model.CustomDimensionShopPageProduct;
import com.tokopedia.shop.product.view.datamodel.ShopProductViewModel;
import com.tokopedia.track.TrackApp;
import com.tokopedia.trackingoptimizer.TrackingQueue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.ACTION_FIELD;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.ADD;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.ALL_ETALASE;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.BRAND;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.CATEGORY;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_ADD_ETALASE_BUTTON;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_CHAT_SELLER;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_CLOSE_FILTER;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_FOLLOW;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_MEMBERSHIP_EVENT;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_PRODUCT;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_PRODUCT_SEARCH_SUGGESTION;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_SEARCH;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_SEARCH_PAGE_NO_RESULT;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_SEND_CHAT;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_SHOP_MESSAGE;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_SHOP_PAGE;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_SHOP_PROFILE;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_SHOWCASE_LIST;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_UNFOLLOW;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.CURRENCY_CODE;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.DIMENSION_79;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.DIMENSION_81;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.ECOMMERCE;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.ETALASE_SECTION;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.ETALASE_X;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.EVENT;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.EVENT_ACTION;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.EVENT_CATEGORY;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.EVENT_LABEL;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.FOLLOW;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.ID;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.IDR;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.IMPRESSIONS;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.IMPRESSION_PRODUCT_SEARCH_SUGGESTION;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.IS_OFFICIAL_STORE;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.LABEL_ETALASE_CAMPAIGN;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.LABEL_ETALASE_UPCOMING_ONGOING_CAMPAIGN;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.LIST;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.LOGIN;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.MEMBERSHIP_SHOP_PAGE;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.NAME;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.NONE;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.NON_LOGIN;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.PAGE_TYPE;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.POSITION;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.PRICE;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.PRODUCTS;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.PRODUCT_CLICK;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.PRODUCT_LIST_IMPRESSION;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.PRODUCT_VIEW;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.REMOVE;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.SEARCH;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.SEARCH_NO_RESULT_SUGGESTION;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.SEARCH_ON_TOKOPEDIA;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.SEARCH_PRODUCT;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.SEARCH_PRODUCT_NO_RESULT;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.SEARCH_PRODUCT_RESULT;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.SEARCH_RESULT;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.SELECTED_ETALASE_CHIP;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.SELLER_ADDED_TO_FAVORITE;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.SELLER_REMOVED_FROM_FAVORITE;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.SHOP_ID;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.SHOP_LOCATION;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.SHOP_NAME;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.SHOP_PAGE_BUYER;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.SHOP_PAGE_SELLER;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.SHOP_REF;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.SHOP_SEARCH_PRODUCT_CLICK_ETALASE_AUTOCOMPLETE;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.SHOP_SEARCH_PRODUCT_CLICK_ETALASE_AUTOCOMPLETE_EMPTY;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.SHOP_TYPE;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.TRY_ANOTHER_WORD;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.UNFOLLOW;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.URL_SLUG;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.VALUE_ONGOING;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.VALUE_UPCOMING;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.VARIANT;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.WISHLIST;

public class ShopPageTrackingBuyer extends ShopPageTracking {

    public ShopPageTrackingBuyer(
            TrackingQueue trackingQueue) {
        super(trackingQueue);
    }

    private List<Object> createProductListMap(
            List<ShopProductViewModel> shopProductViewModelList,
            String selectedEtalaseName,
            String etalaseName,
            int productPosition,
            String shopTypeDef,
            String loginNonLoginString,
            String shopId,
            String shopRef
    ) {
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < shopProductViewModelList.size(); i++) {
            ShopProductViewModel viewModel = shopProductViewModelList.get(i);
            HashMap<String, Object> event = new HashMap<>(DataLayer.mapOf(
                    NAME, viewModel.getName(),
                    ID, viewModel.getId(),
                    PRICE, formatPrice(viewModel.getDisplayedPrice()),
                    BRAND, NONE,
                    CATEGORY, NONE,
                    VARIANT, NONE,
                    LIST, joinDash(SHOPPAGE, shopId, getProductEtalaseEvent(selectedEtalaseName, etalaseName), loginNonLoginString),
                    POSITION, productPosition,
                    DIMENSION_81, shopTypeDef,
                    DIMENSION_79, shopId,
                    SHOP_REF, shopRef
            ));
            list.add(event);
        }
        return list;
    }

    private List<Object> createProductListSearchResultMap(
            List<ShopProductViewModel> shopProductViewModelList,
            String selectedEtalaseName,
            String etalaseName,
            int productPosition,
            String shopTypeDef,
            String loginNonLoginString,
            String shopId,
            String shopRef
    ) {
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < shopProductViewModelList.size(); i++) {
            ShopProductViewModel viewModel = shopProductViewModelList.get(i);
            HashMap<String, Object> event = new HashMap<>(DataLayer.mapOf(
                    NAME, viewModel.getName(),
                    ID, viewModel.getId(),
                    PRICE, formatPrice(viewModel.getDisplayedPrice()),
                    BRAND, NONE,
                    CATEGORY, NONE,
                    VARIANT, NONE,
                    LIST, joinDash(SHOPPAGE, shopId, getProductEtalaseEvent(selectedEtalaseName, etalaseName), loginNonLoginString, SEARCH_RESULT),
                    POSITION, productPosition,
                    DIMENSION_81, shopTypeDef,
                    DIMENSION_79, shopId,
                    SHOP_REF, shopRef
            ));
            list.add(event);
        }
        return list;
    }

    private HashMap<String, Object> createProductImpressionMap(
            String event, boolean isOwner, String category, String loginNonLoginString, String action, String label,
            CustomDimensionShopPageAttribution customDimensionShopPage,
            ShopProductViewModel shopProductViewModel,
            String selectedEtalaseChipName, String etalaseName,
            int productPositionStart,
            String shopId
    ) {
        ArrayList<ShopProductViewModel> shopProductViewModelArrayList = new ArrayList<>();
        shopProductViewModelArrayList.add(shopProductViewModel);
        HashMap<String, Object> eventMap = createMap(event, category, action, label, customDimensionShopPage);
        eventMap.put(ECOMMERCE, DataLayer.mapOf(
                CURRENCY_CODE, IDR,
                IMPRESSIONS,
                createProductListMap(
                        shopProductViewModelArrayList,
                        selectedEtalaseChipName,
                        etalaseName,
                        productPositionStart,
                        customDimensionShopPage.shopType,
                        loginNonLoginString,
                        shopId,
                        customDimensionShopPage.shopRef
                )));
        return eventMap;
    }

    private HashMap<String, Object> createProductImpressionSearchResultMap(
            String event, boolean isOwner, String category, String loginNonLoginString, String action, String label,
            CustomDimensionShopPageAttribution customDimensionShopPage,
            ShopProductViewModel shopProductViewModel,
            String selectedEtalaseChipName, String etalaseName,
            int productPositionStart,
            String shopId
    ) {
        ArrayList<ShopProductViewModel> shopProductViewModelArrayList = new ArrayList<>();
        shopProductViewModelArrayList.add(shopProductViewModel);
        HashMap<String, Object> eventMap = createMap(event, category, action, label, customDimensionShopPage);
        eventMap.put(ECOMMERCE, DataLayer.mapOf(
                CURRENCY_CODE, IDR,
                IMPRESSIONS,
                createProductListSearchResultMap(
                        shopProductViewModelArrayList,
                        selectedEtalaseChipName,
                        etalaseName,
                        productPositionStart,
                        customDimensionShopPage.shopType,
                        loginNonLoginString,
                        shopId,
                        customDimensionShopPage.shopRef
                )));
        return eventMap;
    }

    private HashMap<String, Object> createProductClickMap(String event, boolean isOwner, String category, String loginNonLoginString, String action, String label,
                                                          CustomDimensionShopPageAttribution customDimensionShopPage,
                                                          ShopProductViewModel shopProductViewModel,
                                                          String selectedEtalaseChipName, String etalaseName,
                                                          int productPositionStart,
                                                          String shopId) {
        ArrayList<ShopProductViewModel> shopProductViewModelArrayList = new ArrayList<>();
        shopProductViewModelArrayList.add(shopProductViewModel);
        HashMap<String, Object> eventMap = createMap(event, category, action, label, customDimensionShopPage);
        eventMap.put(ECOMMERCE, DataLayer.mapOf(
                CLICK,
                DataLayer.mapOf(
                        ACTION_FIELD, DataLayer.mapOf(LIST, joinDash(SHOPPAGE, shopId, getProductEtalaseEvent(selectedEtalaseChipName, etalaseName), loginNonLoginString)),
                        PRODUCTS, createProductListMap(
                                shopProductViewModelArrayList,
                                selectedEtalaseChipName,
                                etalaseName,
                                productPositionStart,
                                customDimensionShopPage.shopType,
                                loginNonLoginString,
                                shopId,
                                customDimensionShopPage.shopRef
                        ))
        ));
        return eventMap;
    }

    private HashMap<String, Object> createProductClickSearchResultMap(String event, boolean isOwner, String category, String loginNonLoginString, String action, String label,
                                                                      CustomDimensionShopPageAttribution customDimensionShopPage,
                                                                      ShopProductViewModel shopProductViewModel,
                                                                      String selectedEtalaseChipName, String etalaseName,
                                                                      int productPositionStart,
                                                                      String shopId) {
        ArrayList<ShopProductViewModel> shopProductViewModelArrayList = new ArrayList<>();
        shopProductViewModelArrayList.add(shopProductViewModel);
        HashMap<String, Object> eventMap = createMap(event, category, action, label, customDimensionShopPage);
        eventMap.put(ECOMMERCE, DataLayer.mapOf(
                CLICK,
                DataLayer.mapOf(
                        ACTION_FIELD, DataLayer.mapOf(LIST, joinDash(SHOPPAGE, shopId, getProductEtalaseEvent(selectedEtalaseChipName, etalaseName), loginNonLoginString, SEARCH_RESULT)),
                        PRODUCTS, createProductListSearchResultMap(
                                shopProductViewModelArrayList,
                                selectedEtalaseChipName,
                                etalaseName,
                                productPositionStart,
                                customDimensionShopPage.shopType,
                                loginNonLoginString,
                                shopId,
                                customDimensionShopPage.shopRef
                        ))
        ));
        return eventMap;
    }

    @NonNull
    public String getListNameOfProduct(String tabName, String etalaseName) {
        etalaseName = TextUtils.isEmpty(etalaseName) ? ALL_ETALASE : etalaseName;
        return SHOPPAGE + " - " +
                tabName + " - " +
                etalaseName;
    }

    public void clickFollowUnfollowShop(boolean isFollow,
                                        CustomDimensionShopPage customDimensionShopPage) {
        String followUnfollow = isFollow ? FOLLOW : UNFOLLOW;
        sendGeneralEvent(CLICK_SHOP_PAGE,
                SHOP_PAGE_BUYER,
                joinSpace(CLICK, followUnfollow),
                "",
                customDimensionShopPage);
    }

    public void clickMessageSeller(CustomDimensionShopPage customDimensionShopPage) {
        sendGeneralEvent(CLICK_SHOP_PAGE,
                SHOP_PAGE_BUYER,
                CLICK_CHAT_SELLER,
                "",
                customDimensionShopPage);
    }

    public void clickProduct(
            boolean isOwner,
            boolean isLogin,
            String selectedEtalaseChipName,
            String etalaseSection,
            CustomDimensionShopPageAttribution customDimensionShopPage,
            ShopProductViewModel shopProductViewModel,
            int productPosStart,
            String shopId,
            boolean isSelectedEtalaseCampaign,
            boolean isEtalaseSectionCampaign,
            boolean isUpcoming
    ) {
        String loginNonLoginString = isLogin ? LOGIN : NON_LOGIN;
        String etalaseNameTrackerString = isSelectedEtalaseCampaign? String.format(LABEL_ETALASE_CAMPAIGN, selectedEtalaseChipName) : selectedEtalaseChipName;
        String etalaseSectionTrackerString = getEtalaseNameTrackerString(isEtalaseSectionCampaign,  isUpcoming, etalaseSection);
        Map<String, Object> event = createProductClickMap(
                PRODUCT_CLICK,
                isOwner,
                getShopPageCategory(isOwner),
                loginNonLoginString,
                joinDash(CLICK_PRODUCT, getProductEtalaseEvent(etalaseNameTrackerString, etalaseSectionTrackerString), loginNonLoginString),
                shopProductViewModel.getId(),
                customDimensionShopPage,
                shopProductViewModel,
                etalaseNameTrackerString, etalaseSectionTrackerString,
                productPosStart,
                shopId
        );
        sendDataLayerEvent(event);
    }

    public void clickProductSearchResult(
            boolean isOwner,
            boolean isLogin,
            String selectedEtalaseChipName,
            String etalaseSection,
            CustomDimensionShopPageAttribution customDimensionShopPage,
            ShopProductViewModel shopProductViewModel,
            int productPosStart,
            String shopId,
            boolean isEtalaseCampaign,
            boolean isUpcoming,
            String keyword
    ) {
        String loginNonLoginString = isLogin ? LOGIN : NON_LOGIN;
        String etalaseNameTrackerString = getEtalaseNameTrackerString(isEtalaseCampaign,  isUpcoming, selectedEtalaseChipName);
        Map<String, Object> event = createProductClickSearchResultMap(
                PRODUCT_CLICK,
                isOwner,
                getShopPageCategory(isOwner),
                loginNonLoginString,
                joinDash(CLICK_PRODUCT, getProductEtalaseEvent(etalaseNameTrackerString, etalaseSection), loginNonLoginString, SEARCH_RESULT),
                keyword,
                customDimensionShopPage,
                shopProductViewModel,
                etalaseNameTrackerString, etalaseSection,
                productPosStart,
                shopId
        );
        sendDataLayerEvent(event);
    }

    public void impressionProductList(
            boolean isOwner,
            boolean isLogin,
            String selectedEtalaseChipName,
            String etalaseSection,
            CustomDimensionShopPageAttribution customDimensionShopPage,
            ShopProductViewModel shopProductViewModel,
            int productPosStart,
            String shopId,
            boolean isSelectedEtalaseCampaign,
            boolean isEtalaseSectionCampaign,
            boolean isUpcoming
    ) {
        String loginNonLoginString = isLogin ? LOGIN : NON_LOGIN;
        String etalaseNameTrackerString = isSelectedEtalaseCampaign? String.format(LABEL_ETALASE_CAMPAIGN, selectedEtalaseChipName) : selectedEtalaseChipName;
        String etalaseSectionTrackerString = getEtalaseNameTrackerString(isEtalaseSectionCampaign,  isUpcoming, etalaseSection);
        Map<String, Object> event = createProductImpressionMap(
                PRODUCT_VIEW,
                isOwner,
                getShopPageCategory(isOwner),
                loginNonLoginString,
                joinDash(PRODUCT_LIST_IMPRESSION, getProductEtalaseEvent(etalaseNameTrackerString, etalaseSectionTrackerString), loginNonLoginString),
                "",
                customDimensionShopPage,
                shopProductViewModel,
                etalaseNameTrackerString, etalaseSectionTrackerString,
                productPosStart,
                shopId
        );
        sendDataLayerEvent(event);
    }

    public void impressionProductListSearchResult(
            boolean isOwner,
            boolean isLogin,
            String selectedEtalaseChipName,
            String etalaseSection,
            CustomDimensionShopPageAttribution customDimensionShopPage,
            ShopProductViewModel shopProductViewModel,
            int productPosStart,
            String shopId,
            boolean isEtalaseCampaign,
            boolean isUpcoming,
            String keyword
    ) {
        String loginNonLoginString = isLogin ? LOGIN : NON_LOGIN;
        String etalaseNameTrackerString = getEtalaseNameTrackerString(isEtalaseCampaign,  isUpcoming, selectedEtalaseChipName);
        Map<String, Object> event = createProductImpressionSearchResultMap(
                PRODUCT_VIEW,
                isOwner,
                getShopPageCategory(isOwner),
                loginNonLoginString,
                joinDash(PRODUCT_LIST_IMPRESSION, getProductEtalaseEvent(etalaseNameTrackerString, etalaseSection), loginNonLoginString, SEARCH_RESULT),
                keyword,
                customDimensionShopPage,
                shopProductViewModel,
                etalaseNameTrackerString, etalaseSection,
                productPosStart,
                shopId
        );
        sendDataLayerEvent(event);
    }

    public void clickWishlist(boolean isAdd,
                              boolean isLogin,
                              String selectedEtalaseName,
                              String sectionName,
                              CustomDimensionShopPageProduct customDimensionShopPage,
                              boolean isSelectedEtalaseCampaign,
                              boolean isEtalaseSectionCampaign,
                              boolean isUpcoming) {
        String loginNonLoginString = isLogin ? LOGIN : NON_LOGIN;
        String etalaseNameTrackerString = isSelectedEtalaseCampaign? String.format(LABEL_ETALASE_CAMPAIGN, selectedEtalaseName) : selectedEtalaseName;
        String etalaseSectionTrackerString = getEtalaseNameTrackerString(isEtalaseSectionCampaign,  isUpcoming, sectionName);
        String etalaseEvent = joinDash(getProductEtalaseEvent(etalaseNameTrackerString, etalaseSectionTrackerString), loginNonLoginString);
        sendGeneralEvent(CLICK_SHOP_PAGE,
                SHOP_PAGE_BUYER,
                joinDash(joinSpace(isAdd ? ADD : REMOVE, WISHLIST), etalaseEvent),
                customDimensionShopPage.productId,
                customDimensionShopPage);
    }

    public void clickWishlistProductResultPage(
            boolean isAdd,
            boolean isLogin,
            String selectedEtalaseName,
            CustomDimensionShopPageProduct customDimensionShopPage,
            boolean isEtalaseCampaign,
            boolean isUpcoming
    ) {
        String loginNonLoginString = isLogin ? LOGIN : NON_LOGIN;
        String etalaseNameTrackerString = getEtalaseNameTrackerString(isEtalaseCampaign,  isUpcoming, selectedEtalaseName);
        String etalaseEvent = joinDash(getProductEtalaseEvent(etalaseNameTrackerString, ""), loginNonLoginString, SEARCH_RESULT);
        sendGeneralEvent(CLICK_SHOP_PAGE,
                SHOP_PAGE_BUYER,
                joinDash(joinSpace(isAdd ? ADD : REMOVE, WISHLIST), etalaseEvent),
                customDimensionShopPage.productId,
                customDimensionShopPage);
    }

    public void eventShopSendChat() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(CLICK_SHOP_MESSAGE,
                SHOP_PAGE, CLICK_SEND_CHAT, "");
    }

    public void clickSearch(boolean isOwner, CustomDimensionShopPage customDimensionShopPage) {
        sendGeneralEvent(
                CLICK_SHOP_PAGE,
                getShopPageCategory(isOwner),
                CLICK_SEARCH,
                "",
                customDimensionShopPage
        );
    }

    public void sendEventMembership(String eventAction) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                CLICK_MEMBERSHIP_EVENT,
                MEMBERSHIP_SHOP_PAGE,
                eventAction,
                ""
        );
    }

    public void clickShopProfile(CustomDimensionShopPage customDimensionShopPage) {
        sendGeneralEvent(CLICK_SHOP_PAGE,
                SHOP_PAGE_BUYER,
                CLICK_SHOP_PROFILE,
                "",
                customDimensionShopPage);
    }

    public void clickFollowUnfollow(boolean isShopFavorited, CustomDimensionShopPage customDimensionShopPage) {
        String action;
        if (!isShopFavorited) {
            action = CLICK_FOLLOW;
        } else {
            action = CLICK_UNFOLLOW;
        }
        sendGeneralEvent(CLICK_SHOP_PAGE,
                SHOP_PAGE_BUYER,
                action,
                "",
                customDimensionShopPage);
    }

    public void clickMoreMenuChip(boolean isOwner,
                                  String selectedEtalaseName,
                                  CustomDimensionShopPage customDimensionShopPage) {
        sendGeneralEvent(CLICK_SHOP_PAGE,
                getShopPageCategory(isOwner),
                CLICK_SHOWCASE_LIST,
                String.format(ETALASE_X, selectedEtalaseName),
                customDimensionShopPage);
    }

    public void sendMoEngageFavoriteEvent(String shopName, String shopID, String shopDomain, String shopLocation,
                                          Boolean isShopOfficaial, Boolean isFollowed) {
        Map<String, Object> mapData = DataLayer.mapOf(
                SHOP_NAME, shopName,
                SHOP_ID, shopID,
                SHOP_LOCATION, shopLocation,
                URL_SLUG, shopDomain,
                IS_OFFICIAL_STORE, isShopOfficaial
        );
        String eventName;
        if (isFollowed)
            eventName = SELLER_ADDED_TO_FAVORITE;
        else
            eventName = SELLER_REMOVED_FROM_FAVORITE;
        TrackApp.getInstance().getMoEngage().sendTrackEvent(mapData, eventName);
    }

    public void searchProduct(
            String keyword,
            boolean isProductSearchResultEmpty,
            boolean isOwner,
            CustomDimensionShopPage customDimensionShopPage
    ) {
        String productResultLabel = isProductSearchResultEmpty ? SEARCH_PRODUCT_NO_RESULT : SEARCH_PRODUCT_RESULT;
        sendGeneralEvent(
                CLICK_SHOP_PAGE,
                getShopPageCategory(isOwner),
                SEARCH_PRODUCT,
                joinSpace(SEARCH, joinDash(keyword, productResultLabel)),
                customDimensionShopPage
        );
    }

    public void clickAddEtalase(CustomDimensionShopPage customDimensionShopPage) {
        sendGeneralEvent(
                CLICK_SHOP_PAGE,
                SHOP_PAGE_SELLER,
                CLICK_ADD_ETALASE_BUTTON,
                "",
                customDimensionShopPage
        );
    }

    private String getProductEtalaseEvent(
            String selectedEtalaseChipName,
            String etalaseSection
    ) {
        String etalaseEventEtalaseSectionEmpty = String.format(
                SELECTED_ETALASE_CHIP,
                selectedEtalaseChipName
        );
        String etalaseEventEtalaseSectionNotEmpty = joinDash(
                String.format(SELECTED_ETALASE_CHIP, selectedEtalaseChipName),
                String.format(ETALASE_SECTION, etalaseSection)
        );
        return etalaseSection.isEmpty() ? etalaseEventEtalaseSectionEmpty : etalaseEventEtalaseSectionNotEmpty;
    }

    public void clickClearFilter(boolean isOwner, CustomDimensionShopPage customDimensionShopPage) {
        sendGeneralEvent(CLICK_SHOP_PAGE,
                getShopPageCategory(isOwner),
                CLICK_CLOSE_FILTER,
                "",
                customDimensionShopPage
        );
    }

    private String getEtalaseNameTrackerString(
            boolean isEtalaseSectionCampaign,
            boolean isUpcoming,
            String etalaseSection
    ){
        String etalaseSectionTrackerString;
        if (isEtalaseSectionCampaign) {
            etalaseSectionTrackerString = String.format(LABEL_ETALASE_UPCOMING_ONGOING_CAMPAIGN, isUpcoming ? VALUE_UPCOMING : VALUE_ONGOING, etalaseSection);
        } else {
            etalaseSectionTrackerString = etalaseSection;
        }
        return etalaseSectionTrackerString;
    }

    private List<Object> createProductListMapEmptyState(
            List<ShopProductViewModel> shopProductViewModelList,
            int productPosition,
            String shopTypeDef,
            String loginNonLoginString,
            String shopId,
            String shopRef
    ) {
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < shopProductViewModelList.size(); i++) {
            ShopProductViewModel viewModel = shopProductViewModelList.get(i);
            HashMap<String, Object> event = new HashMap<>(DataLayer.mapOf(
                    NAME, viewModel.getName(),
                    ID, viewModel.getId(),
                    PRICE, formatPrice(viewModel.getDisplayedPrice()),
                    BRAND, NONE,
                    CATEGORY, NONE,
                    VARIANT, NONE,
                    LIST, joinDash(SHOPPAGE, shopId, SEARCH_NO_RESULT_SUGGESTION, loginNonLoginString),
                    POSITION, productPosition,
                    DIMENSION_81, shopTypeDef,
                    DIMENSION_79, shopId,
                    SHOP_REF, shopRef
            ));
            list.add(event);
        }
        return list;
    }

    private HashMap<String, Object> createProductImpressionMapEmptyState(
            String event, String category, String action, String label,
            String loginNonLoginString,
            CustomDimensionShopPageAttribution customDimensionShopPage,
            ShopProductViewModel shopProductViewModel,
            int productPositionStart,
            String shopId
    ) {
        ArrayList<ShopProductViewModel> shopProductViewModelArrayList = new ArrayList<>();
        shopProductViewModelArrayList.add(shopProductViewModel);
        HashMap<String, Object> eventMap = createMap(event, category, action, label, customDimensionShopPage);
        eventMap.put(ECOMMERCE, DataLayer.mapOf(
                CURRENCY_CODE, IDR,
                IMPRESSIONS,
                createProductListMapEmptyState(
                        shopProductViewModelArrayList,
                        productPositionStart,
                        customDimensionShopPage.shopType,
                        loginNonLoginString,
                        shopId,
                        customDimensionShopPage.shopRef
                )));
        return eventMap;
    }

    public void impressionProductListEmptyState(boolean isLogin,
                                                CustomDimensionShopPageAttribution customDimensionShopPage,
                                                ShopProductViewModel shopProductViewModel,
                                                int productPosStart,
                                                String shopId) {
        String loginNonLoginString = isLogin ? LOGIN : NON_LOGIN;
        Map<String, Object> event = createProductImpressionMapEmptyState(
                PRODUCT_VIEW,
                SHOP_PAGE_BUYER,
                IMPRESSION_PRODUCT_SEARCH_SUGGESTION,
                "",
                loginNonLoginString,
                customDimensionShopPage,
                shopProductViewModel,
                productPosStart,
                shopId
        );
        sendDataLayerEvent(event);
    }

    private HashMap<String, Object> createProductClickEmptyStateMap(String event, String category, String action, String label,
                                                                    String loginNonLoginString,
                                                                    CustomDimensionShopPageAttribution customDimensionShopPage,
                                                                    ShopProductViewModel shopProductViewModel,
                                                                    int productPositionStart,
                                                                    String shopId) {
        ArrayList<ShopProductViewModel> shopProductViewModelArrayList = new ArrayList<>();
        shopProductViewModelArrayList.add(shopProductViewModel);
        HashMap<String, Object> eventMap = createMap(event, category, action, label, customDimensionShopPage);
        eventMap.put(ECOMMERCE, DataLayer.mapOf(
                CLICK,
                DataLayer.mapOf(
                        ACTION_FIELD, DataLayer.mapOf(LIST, joinDash(SHOPPAGE, shopId, SEARCH_NO_RESULT_SUGGESTION, loginNonLoginString, SEARCH_RESULT)),
                        PRODUCTS, createProductListMapEmptyState(
                                shopProductViewModelArrayList,
                                productPositionStart,
                                customDimensionShopPage.shopType,
                                loginNonLoginString,
                                shopId,
                                customDimensionShopPage.shopRef
                        ))
        ));
        return eventMap;
    }



    public void clickProductListEmptyState(boolean isLogin,
                                           CustomDimensionShopPageAttribution customDimensionShopPage,
                                           ShopProductViewModel shopProductViewModel,
                                           int productPosStart,
                                           String shopId) {
        String loginNonLoginString = isLogin ? LOGIN : NON_LOGIN;
        Map<String, Object> event = createProductClickEmptyStateMap(
                PRODUCT_VIEW,
                SHOP_PAGE_BUYER,
                CLICK_PRODUCT_SEARCH_SUGGESTION,
                "",
                loginNonLoginString,
                customDimensionShopPage,
                shopProductViewModel,
                productPosStart,
                shopId
        );
        sendDataLayerEvent(event);
    }

    public void clickPrimaryBtnEmptyStateSearch(String shopId, String shopType) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(DataLayer.mapOf(
                EVENT, CLICK_SHOP_PAGE,
                EVENT_CATEGORY, SHOP_PAGE_BUYER,
                EVENT_ACTION, CLICK_SEARCH_PAGE_NO_RESULT,
                EVENT_LABEL, TRY_ANOTHER_WORD,
                SHOP_ID, shopId,
                SHOP_TYPE, shopType,
                PAGE_TYPE, SHOPPAGE
        ));
    }

    public void clickSecondaryBtnEmptyStateSearch(String shopId, String shopType) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(DataLayer.mapOf(
                EVENT, CLICK_SHOP_PAGE,
                EVENT_CATEGORY, SHOP_PAGE_BUYER,
                EVENT_ACTION, CLICK_SEARCH_PAGE_NO_RESULT,
                EVENT_LABEL, SEARCH_ON_TOKOPEDIA,
                SHOP_ID, shopId,
                SHOP_TYPE, shopType,
                PAGE_TYPE, SHOPPAGE
        ));
    }

    public void shopPageProductSearchResult(Boolean isOwner, String keyword, CustomDimensionShopPage customDimensionShopPage) {
        sendGeneralEvent(
                CLICK_SHOP_PAGE,
                getShopPageCategory(isOwner),
                String.format(SHOP_SEARCH_PRODUCT_CLICK_ETALASE_AUTOCOMPLETE, keyword),
                keyword,
                customDimensionShopPage
        );
    }

    public void shopPageProductSearchNoResult(Boolean isOwner, String keyword, CustomDimensionShopPage customDimensionShopPage) {
        sendGeneralEvent(
                CLICK_SHOP_PAGE,
                getShopPageCategory(isOwner),
                String.format(SHOP_SEARCH_PRODUCT_CLICK_ETALASE_AUTOCOMPLETE_EMPTY, keyword),
                keyword,
                customDimensionShopPage
        );
    }
}
