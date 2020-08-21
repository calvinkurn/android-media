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

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.*;

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
            String shopId
    ) {
        String loginNonLoginString = isLogin ? LOGIN : NON_LOGIN;
        Map<String, Object> event = createProductClickMap(
                PRODUCT_CLICK,
                isOwner,
                getShopPageCategory(isOwner),
                loginNonLoginString,
                joinDash(CLICK_PRODUCT, getProductEtalaseEvent(selectedEtalaseChipName, etalaseSection), loginNonLoginString),
                shopProductViewModel.getId(),
                customDimensionShopPage,
                shopProductViewModel,
                selectedEtalaseChipName, etalaseSection,
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
            String shopId
    ) {
        String loginNonLoginString = isLogin ? LOGIN : NON_LOGIN;
        Map<String, Object> event = createProductClickSearchResultMap(
                PRODUCT_CLICK,
                isOwner,
                getShopPageCategory(isOwner),
                loginNonLoginString,
                joinDash(CLICK_PRODUCT, getProductEtalaseEvent(selectedEtalaseChipName, etalaseSection), loginNonLoginString, SEARCH_RESULT),
                shopProductViewModel.getId(),
                customDimensionShopPage,
                shopProductViewModel,
                selectedEtalaseChipName, etalaseSection,
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
            String shopId
    ) {
        String loginNonLoginString = isLogin ? LOGIN : NON_LOGIN;
        Map<String, Object> event = createProductImpressionMap(
                PRODUCT_VIEW,
                isOwner,
                getShopPageCategory(isOwner),
                loginNonLoginString,
                joinDash(PRODUCT_LIST_IMPRESSION, getProductEtalaseEvent(selectedEtalaseChipName, etalaseSection), loginNonLoginString),
                "",
                customDimensionShopPage,
                shopProductViewModel,
                selectedEtalaseChipName, etalaseSection,
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
            String shopId
    ) {
        String loginNonLoginString = isLogin ? LOGIN : NON_LOGIN;
        Map<String, Object> event = createProductImpressionSearchResultMap(
                PRODUCT_VIEW,
                isOwner,
                getShopPageCategory(isOwner),
                loginNonLoginString,
                joinDash(PRODUCT_LIST_IMPRESSION, getProductEtalaseEvent(selectedEtalaseChipName, etalaseSection), loginNonLoginString, SEARCH_RESULT),
                "",
                customDimensionShopPage,
                shopProductViewModel,
                selectedEtalaseChipName, etalaseSection,
                productPosStart,
                shopId
        );
        sendDataLayerEvent(event);
    }

    public void clickWishlist(boolean isAdd,
                              boolean isLogin,
                              String selectedEtalaseName,
                              String sectionName,
                              CustomDimensionShopPageProduct customDimensionShopPage) {
        String loginNonLoginString = isLogin ? LOGIN : NON_LOGIN;
        String etalaseEvent = joinDash(getProductEtalaseEvent(selectedEtalaseName, sectionName), loginNonLoginString);
        sendGeneralEvent(CLICK_SHOP_PAGE,
                SHOP_PAGE_BUYER,
                joinDash(joinSpace(isAdd ? ADD : REMOVE, WISHLIST), etalaseEvent),
                customDimensionShopPage.productId,
                customDimensionShopPage);
    }

    public void clickWishlistProductResultPage(boolean isAdd,
                                               boolean isLogin,
                                               String selectedEtalaseName,
                                               CustomDimensionShopPageProduct customDimensionShopPage) {
        String loginNonLoginString = isLogin ? LOGIN : NON_LOGIN;
        String etalaseEvent = joinDash(getProductEtalaseEvent(selectedEtalaseName, ""), loginNonLoginString, SEARCH_RESULT);
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

    public void clickShareButton(CustomDimensionShopPage customDimensionShopPage) {
        sendGeneralEvent(CLICK_SHOP_PAGE,
                SHOP_PROFILE_PAGE_BUYER,
                CLICK_SHARE,
                "",
                customDimensionShopPage);
    }

    public void clickShareButtonSellerView(CustomDimensionShopPage customDimensionShopPage) {
        sendGeneralEvent(CLICK_SHOP_PAGE,
                SETTING_PAGE_SELLER,
                CLICK_SHARE,
                "",
                customDimensionShopPage);
    }

    public void clickCancelShareBottomsheet(CustomDimensionShopPage customDimensionShopPage, boolean isMyShop) {
        sendGeneralEvent(
                isMyShop ? CLICK_SHOP_SETTING : CLICK_PROFILE,
                isMyShop ? SETTING_PAGE_SELLER : SHOP_PROFILE_PAGE_BUYER,
                CLICK_SHARE_DETAIL,
                CLICK_BOTTOMSHEET_DISMISS_BUTTON,
                customDimensionShopPage
        );
    }

    public void clickShareSocialMedia(CustomDimensionShopPage customDimensionShopPage, boolean isMyShop, String socialMediaName) {
        sendGeneralEvent(
                isMyShop ? CLICK_SHOP_SETTING : CLICK_PROFILE,
                isMyShop ? SETTING_PAGE_SELLER : SHOP_PROFILE_PAGE_BUYER,
                CLICK_SHARE_DETAIL,
                socialMediaName,
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
}
