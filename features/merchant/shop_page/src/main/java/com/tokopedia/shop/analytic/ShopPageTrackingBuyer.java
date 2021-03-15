package com.tokopedia.shop.analytic;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.tokopedia.analyticconstant.DataLayer;
import com.tokopedia.shop.analytic.model.CustomDimensionShopPage;
import com.tokopedia.shop.analytic.model.CustomDimensionShopPageAttribution;
import com.tokopedia.shop.analytic.model.CustomDimensionShopPageProduct;
import com.tokopedia.shop.common.constant.ShopEtalaseTypeDef;
import com.tokopedia.shop.product.view.datamodel.ShopProductUiModel;
import com.tokopedia.track.TrackApp;
import com.tokopedia.trackingoptimizer.TrackingQueue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.*;
import static com.tokopedia.shop.common.constant.ShopPageConstant.DEFAULT_VALUE_ETALASE_TYPE;

public class ShopPageTrackingBuyer extends ShopPageTracking {

    public ShopPageTrackingBuyer(
            TrackingQueue trackingQueue) {
        super(trackingQueue);
    }

    private List<Object> createProductListMap(
            List<ShopProductUiModel> shopProductUiModelList,
            String selectedEtalaseName,
            String etalaseName,
            int productPosition,
            String shopTypeDef,
            String loginNonLoginString,
            String shopId,
            String shopRef
    ) {
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < shopProductUiModelList.size(); i++) {
            ShopProductUiModel viewModel = shopProductUiModelList.get(i);
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
            List<ShopProductUiModel> shopProductUiModelList,
            String selectedEtalaseName,
            String etalaseName,
            int productPosition,
            String shopTypeDef,
            String loginNonLoginString,
            String shopId,
            String shopRef
    ) {
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < shopProductUiModelList.size(); i++) {
            ShopProductUiModel viewModel = shopProductUiModelList.get(i);
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
            ShopProductUiModel shopProductUiModel,
            String selectedEtalaseChipName, String etalaseName,
            int productPositionStart,
            String shopId
    ) {
        ArrayList<ShopProductUiModel> shopProductUiModelArrayList = new ArrayList<>();
        shopProductUiModelArrayList.add(shopProductUiModel);
        HashMap<String, Object> eventMap = createMap(event, category, action, label, customDimensionShopPage);
        eventMap.put(ECOMMERCE, DataLayer.mapOf(
                CURRENCY_CODE, IDR,
                IMPRESSIONS,
                createProductListMap(
                        shopProductUiModelArrayList,
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
            ShopProductUiModel shopProductUiModel,
            String selectedEtalaseChipName, String etalaseName,
            int productPositionStart,
            String shopId
    ) {
        ArrayList<ShopProductUiModel> shopProductUiModelArrayList = new ArrayList<>();
        shopProductUiModelArrayList.add(shopProductUiModel);
        HashMap<String, Object> eventMap = createMap(event, category, action, label, customDimensionShopPage);
        eventMap.put(ECOMMERCE, DataLayer.mapOf(
                CURRENCY_CODE, IDR,
                IMPRESSIONS,
                createProductListSearchResultMap(
                        shopProductUiModelArrayList,
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
                                                          ShopProductUiModel shopProductUiModel,
                                                          String selectedEtalaseChipName, String etalaseName,
                                                          int productPositionStart,
                                                          String shopId) {
        ArrayList<ShopProductUiModel> shopProductUiModelArrayList = new ArrayList<>();
        shopProductUiModelArrayList.add(shopProductUiModel);
        HashMap<String, Object> eventMap = createMap(event, category, action, label, customDimensionShopPage);
        eventMap.put(ECOMMERCE, DataLayer.mapOf(
                CLICK,
                DataLayer.mapOf(
                        ACTION_FIELD, DataLayer.mapOf(LIST, joinDash(SHOPPAGE, shopId, getProductEtalaseEvent(selectedEtalaseChipName, etalaseName), loginNonLoginString)),
                        PRODUCTS, createProductListMap(
                                shopProductUiModelArrayList,
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
                                                                      ShopProductUiModel shopProductUiModel,
                                                                      String selectedEtalaseChipName, String etalaseName,
                                                                      int productPositionStart,
                                                                      String shopId) {
        ArrayList<ShopProductUiModel> shopProductUiModelArrayList = new ArrayList<>();
        shopProductUiModelArrayList.add(shopProductUiModel);
        HashMap<String, Object> eventMap = createMap(event, category, action, label, customDimensionShopPage);
        eventMap.put(ECOMMERCE, DataLayer.mapOf(
                CLICK,
                DataLayer.mapOf(
                        ACTION_FIELD, DataLayer.mapOf(
                                LIST,
                                joinDash(
                                        SHOPPAGE,
                                        shopId,
                                        getProductEtalaseEvent(
                                                selectedEtalaseChipName,
                                                etalaseName
                                        ),
                                        loginNonLoginString,
                                        SEARCH_RESULT
                                )
                        ),
                        PRODUCTS, createProductListSearchResultMap(
                                shopProductUiModelArrayList,
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

    public void impressionVoucherFollowUnfollowShop(
            String shopId,
            String userId
    ) {
        followUnfollowShop(
                VIEW_SHOP_PAGE_IRIS,
                VIEW_FOLLOW_VOUCHER_ICON,
                SHOP_PAGE_LABEL + shopId,
                userId
        );
    }

    public void impressionCoachMarkFollowUnfollowShop(
            String shopId,
            String userId
    ) {
        followUnfollowShop(
                VIEW_SHOP_PAGE_IRIS,
                VIEW_COACHMARK_FOLLOW,
                SHOP_PAGE_LABEL + shopId,
                userId
        );
    }

    public void impressionCoachMarkDissapearFollowUnfollowShop(
            String shopId,
            String userId
    ) {
        followUnfollowShop(
                VIEW_SHOP_PAGE_IRIS,
                VIEW_COACHMARK_FOLLOW,
                SHOP_PAGE_LABEL + shopId + COACHMARK_DISAPPEAR,
                userId
        );
    }

    public void clickFollowUnfollowShop(
            Boolean isFollowing,
            String shopId,
            String userId
    ) {
        String action;
        if (isFollowing) {
            action = CLICK_FOLLOW;
        } else {
            action = CLICK_UNFOLLOW;
        }
        followUnfollowShop(
                CLICK_SHOP_PAGE,
                action,
                SHOP_PAGE_LABEL + shopId,
                userId
        );
    }

    public void clickFollowUnfollowShopWithoutShopFollower(
            boolean isFollow,
            CustomDimensionShopPage customDimensionShopPage) {
        String followUnfollow = isFollow ? FOLLOW : UNFOLLOW;
        sendGeneralEvent(CLICK_SHOP_PAGE,
                SHOP_PAGE_BUYER,
                joinSpace(CLICK, followUnfollow),
                "",
                customDimensionShopPage);
    }

    public void impressionToasterFollow(
            Boolean isSuccess,
            String shopId,
            String userId
    ) {
        String action;
        if (isSuccess) {
            action = VIEW_TOASTER_FOLLOW_SUCCESS;
        } else {
            action = VIEW_TOASTER_FOLLOW_ERROR;
        }
        followUnfollowShop(
                VIEW_SHOP_PAGE_IRIS,
                action,
                SHOP_PAGE_LABEL + shopId,
                userId
        );
    }

    public void impressionToasterUnfollow(
            Boolean isSuccess,
            String shopId,
            String userId
    ) {
        String action;
        if (isSuccess) {
            action = VIEW_TOASTER_UNFOLLOW_SUCCESS;
        } else {
            action = VIEW_TOASTER_UNFOLLOW_ERROR;
        }
        followUnfollowShop(
                VIEW_SHOP_PAGE_IRIS,
                action,
                SHOP_PAGE_LABEL + shopId,
                userId
        );
    }

    public void clickCekToasterSuccess(
            String shopId,
            String userId
    ) {
        followUnfollowShop(
                CLICK_SHOP_PAGE,
                CLICK_CEK_TOASTER_SUCCESS,
                SHOP_PAGE_LABEL + shopId,
                userId
        );
    }

    public void impressionSeeEntryPointMerchantVoucherCoupon(
            String shopId,
            String userId
    ) {
        followUnfollowShop(
                VIEW_SHOP_PAGE_IRIS,
                SEE_ENTRY_POINT,
                SHOP_PAGE_LABEL + shopId,
                userId
        );
    }

    public void clickFollowShowcaseNplButton(
            String shopId,
            String userId,
            CustomDimensionShopPage customDimensionShopPage
    ) {
        sendGeneralEventNplFollower(
                CLICK_SHOP_PAGE,
                SHOP_PAGE_BUYER,
                String.format(CLICK_SHOWCASE_FOLLOW_NPL, FOLLOW),
                shopId,
                PHYSICAL_GOODS,
                TOKOPEDIA_MARKETPLACE,
                userId,
                customDimensionShopPage
        );
    }

    public void clickCTASuccessFollowNplToaster(
            String shopId,
            String userId,
            CustomDimensionShopPage customDimensionShopPage
    ) {
        sendGeneralEventNplFollower(
                CLICK_SHOP_PAGE,
                SHOP_PAGE_BUYER,
                CLICK_OK_SUCCESS_FOLLOW_TOASTER_NPL,
                shopId,
                PHYSICAL_GOODS,
                TOKOPEDIA_MARKETPLACE,
                userId,
                customDimensionShopPage
        );
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
            ShopProductUiModel shopProductUiModel,
            int productPosStart,
            String shopId,
            boolean isSelectedEtalaseCampaign,
            boolean isEtalaseSectionCampaign,
            boolean isUpcoming
    ) {
        String loginNonLoginString = isLogin ? LOGIN : NON_LOGIN;
        String etalaseNameTrackerString = isSelectedEtalaseCampaign? String.format(LABEL_ETALASE_CAMPAIGN, selectedEtalaseChipName) : selectedEtalaseChipName;
        String etalaseSectionTrackerString = getEtalaseNameTrackerString(
                isEtalaseSectionCampaign,  isUpcoming, etalaseSection, DEFAULT_VALUE_ETALASE_TYPE);
        Map<String, Object> event = createProductClickMap(
                PRODUCT_CLICK,
                isOwner,
                getShopPageCategory(isOwner),
                loginNonLoginString,
                joinDash(CLICK_PRODUCT, getProductEtalaseEvent(etalaseNameTrackerString, etalaseSectionTrackerString), loginNonLoginString),
                shopProductUiModel.getId(),
                customDimensionShopPage,
                shopProductUiModel,
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
            ShopProductUiModel shopProductUiModel,
            int productPosStart,
            String shopId,
            boolean isEtalaseCampaign,
            boolean isUpcoming,
            String keyword,
            int etalaseType
    ) {
        String loginNonLoginString = isLogin ? LOGIN : NON_LOGIN;
        String etalaseNameTrackerString = getEtalaseNameTrackerString(
                isEtalaseCampaign,  isUpcoming, selectedEtalaseChipName, etalaseType);
        Map<String, Object> event = createProductClickSearchResultMap(
                PRODUCT_CLICK,
                isOwner,
                getShopPageCategory(isOwner),
                loginNonLoginString,
                joinDash(
                        CLICK_PRODUCT,
                        getProductEtalaseEvent(etalaseNameTrackerString, etalaseSection),
                        loginNonLoginString,
                        SEARCH_RESULT),
                keyword,
                customDimensionShopPage,
                shopProductUiModel,
                etalaseNameTrackerString,
                etalaseSection,
                productPosStart,
                shopId
        );
        sendDataLayerEvent(event);
    }

    public void clickProductListEmptyState(boolean isLogin,
                                           CustomDimensionShopPageAttribution customDimensionShopPage,
                                           ShopProductUiModel shopProductUiModel,
                                           int productPosStart,
                                           String shopId) {
        String loginNonLoginString = isLogin ? LOGIN : NON_LOGIN;
        Map<String, Object> event = createProductClickEmptyStateMap(
                PRODUCT_CLICK,
                SHOP_PAGE_BUYER,
                CLICK_PRODUCT_SEARCH_SUGGESTION,
                "",
                loginNonLoginString,
                customDimensionShopPage,
                shopProductUiModel,
                productPosStart,
                shopId
        );
        sendDataLayerEvent(event);
    }


    private HashMap<String, Object> createProductClickEmptyStateMap(String event, String category, String action, String label,
                                                                    String loginNonLoginString,
                                                                    CustomDimensionShopPageAttribution customDimensionShopPage,
                                                                    ShopProductUiModel shopProductUiModel,
                                                                    int productPositionStart,
                                                                    String shopId) {
        ArrayList<ShopProductUiModel> shopProductUiModelArrayList = new ArrayList<>();
        shopProductUiModelArrayList.add(shopProductUiModel);
        HashMap<String, Object> eventMap = createMap(event, category, action, label, customDimensionShopPage);
        eventMap.put(ECOMMERCE, DataLayer.mapOf(
                CLICK,
                DataLayer.mapOf(
                        ACTION_FIELD, DataLayer.mapOf(LIST, joinDash(SHOPPAGE, shopId, SEARCH_NO_RESULT_SUGGESTION, loginNonLoginString)),
                        PRODUCTS, createProductListMapEmptyState(
                                shopProductUiModelArrayList,
                                productPositionStart,
                                customDimensionShopPage.shopType,
                                loginNonLoginString,
                                shopId,
                                customDimensionShopPage.shopRef
                        ))
        ));
        return eventMap;
    }

    public void impressionProductList(
            boolean isOwner,
            boolean isLogin,
            String selectedEtalaseChipName,
            String etalaseSection,
            CustomDimensionShopPageAttribution customDimensionShopPage,
            ShopProductUiModel shopProductUiModel,
            int productPosStart,
            String shopId,
            boolean isSelectedEtalaseCampaign,
            boolean isEtalaseSectionCampaign,
            boolean isUpcoming
    ) {
        String loginNonLoginString = isLogin ? LOGIN : NON_LOGIN;
        String etalaseNameTrackerString = isSelectedEtalaseCampaign? String.format(LABEL_ETALASE_CAMPAIGN, selectedEtalaseChipName) : selectedEtalaseChipName;
        String etalaseSectionTrackerString = getEtalaseNameTrackerString(
                isEtalaseSectionCampaign,  isUpcoming, etalaseSection, DEFAULT_VALUE_ETALASE_TYPE);
        Map<String, Object> event = createProductImpressionMap(
                PRODUCT_VIEW,
                isOwner,
                getShopPageCategory(isOwner),
                loginNonLoginString,
                joinDash(PRODUCT_LIST_IMPRESSION, getProductEtalaseEvent(etalaseNameTrackerString, etalaseSectionTrackerString), loginNonLoginString),
                "",
                customDimensionShopPage,
                shopProductUiModel,
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
            ShopProductUiModel shopProductUiModel,
            int productPosStart,
            String shopId,
            boolean isEtalaseCampaign,
            boolean isUpcoming,
            String keyword,
            int etalaseType
    ) {
        String loginNonLoginString = isLogin ? LOGIN : NON_LOGIN;
        String etalaseNameTrackerString = getEtalaseNameTrackerString(
                isEtalaseCampaign,  isUpcoming, selectedEtalaseChipName, etalaseType);
        Map<String, Object> event = createProductImpressionSearchResultMap(
                PRODUCT_VIEW,
                isOwner,
                getShopPageCategory(isOwner),
                loginNonLoginString,
                joinDash(PRODUCT_LIST_IMPRESSION, getProductEtalaseEvent(etalaseNameTrackerString, etalaseSection), loginNonLoginString, SEARCH_RESULT),
                keyword,
                customDimensionShopPage,
                shopProductUiModel,
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
        String etalaseSectionTrackerString = getEtalaseNameTrackerString(
                isEtalaseSectionCampaign,  isUpcoming, sectionName, DEFAULT_VALUE_ETALASE_TYPE);
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
            boolean isUpcoming,
            int etalaseType
    ) {
        String loginNonLoginString = isLogin ? LOGIN : NON_LOGIN;
        String etalaseNameTrackerString = getEtalaseNameTrackerString(
                isEtalaseCampaign,  isUpcoming, selectedEtalaseName, etalaseType);
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

    private String getEtalaseNameTrackerString(
            boolean isEtalaseSectionCampaign,
            boolean isUpcoming,
            String etalaseSection,
            int etalaseType     // Used for validating type of campaign only
    ){
        String etalaseSectionTrackerString;
        if (isEtalaseSectionCampaign) {
            String valueTypeCampaign = "";
            if (etalaseType == ShopEtalaseTypeDef.ETALASE_THEMATIC_CAMPAIGN) {
                valueTypeCampaign = VALUE_THEMATIC;
            } else {
                valueTypeCampaign = isUpcoming ? VALUE_UPCOMING : VALUE_ONGOING;
            }

            etalaseSectionTrackerString = String.format(
                    LABEL_ETALASE_UPCOMING_ONGOING_CAMPAIGN,
                    valueTypeCampaign,
                    etalaseSection
            );
        } else {
            etalaseSectionTrackerString = etalaseSection;
        }
        return etalaseSectionTrackerString;
    }

    private List<Object> createProductListMapEmptyState(
            List<ShopProductUiModel> shopProductUiModelList,
            int productPosition,
            String shopTypeDef,
            String loginNonLoginString,
            String shopId,
            String shopRef
    ) {
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < shopProductUiModelList.size(); i++) {
            ShopProductUiModel viewModel = shopProductUiModelList.get(i);
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
            ShopProductUiModel shopProductUiModel,
            int productPositionStart,
            String shopId
    ) {
        ArrayList<ShopProductUiModel> shopProductUiModelArrayList = new ArrayList<>();
        shopProductUiModelArrayList.add(shopProductUiModel);
        HashMap<String, Object> eventMap = createMap(event, category, action, label, customDimensionShopPage);
        eventMap.put(ECOMMERCE, DataLayer.mapOf(
                CURRENCY_CODE, IDR,
                IMPRESSIONS,
                createProductListMapEmptyState(
                        shopProductUiModelArrayList,
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
                                                ShopProductUiModel shopProductUiModel,
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
                shopProductUiModel,
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

    public void clickProductListToggle(
            String productListName,
            boolean isMyShop,
            CustomDimensionShopPage customDimensionShopPage
    ) {
        sendGeneralEvent(
                CLICK_SHOP_PAGE,
                getShopPageCategory(isMyShop),
                CLICK_PRODUCT_LIST_TOGGLE,
                productListName,
                customDimensionShopPage
        );
    }

    public void clickFilterChips(String productListName, CustomDimensionShopPage customDimensionShopPage) {
        sendGeneralEvent(
                CLICK_SHOP_PAGE,
                SHOP_PAGE_BUYER,
                CLICK_FILTER_CHIP,
                productListName,
                customDimensionShopPage
        );
    }

    public void clickFilterSortBy(String productListName, String sortBy, CustomDimensionShopPage customDimensionShopPage) {
        sendGeneralEvent(
                CLICK_SHOP_PAGE,
                SHOP_PAGE_BUYER,
                CLICK_FILTER_SHORT_BY + sortBy,
                productListName,
                customDimensionShopPage
        );
    }

    public void clickFilterPrice(String productListName, String min, String max, CustomDimensionShopPage customDimensionShopPage) {
        sendGeneralEvent(
                CLICK_SHOP_PAGE,
                SHOP_PAGE_BUYER,
                String.format(CLICK_FILTER_PRICE,min, max),
                productListName,
                customDimensionShopPage
        );
    }

    public void clickFilterRating(String productListName, String rating, CustomDimensionShopPage customDimensionShopPage) {
        sendGeneralEvent(
                CLICK_SHOP_PAGE,
                SHOP_PAGE_BUYER,
                CLICK_FILTER_RATING + rating,
                productListName,
                customDimensionShopPage
        );
    }

    public void sendShopPageProductSearchResultTracker(
            Boolean isOwner,
            String keyword,
            Boolean isProductResultListEmpty,
            CustomDimensionShopPage customDimensionShopPage
    ) {
        String actionEvent = isProductResultListEmpty? SEARCH_NO_RESULT: SEARCH;
        sendGeneralEvent(
                CLICK_SHOP_PAGE,
                getShopPageCategory(isOwner),
                actionEvent,
                keyword,
                customDimensionShopPage
        );
    }

    public void sendShopPageProductSearchClickEtalaseProductResultTracker(
            boolean isMyShop,
            String keyword,
            boolean isProductResultListEmpty,
            CustomDimensionShopPage customDimensionShopPage
    ) {
        String eventActionFormat = isProductResultListEmpty ? SHOP_SEARCH_PRODUCT_CLICK_ETALASE_AUTOCOMPLETE_EMPTY : SHOP_SEARCH_PRODUCT_CLICK_ETALASE_AUTOCOMPLETE;
        sendGeneralEvent(
                CLICK_SHOP_PAGE,
                getShopPageCategory(isMyShop),
                String.format(eventActionFormat, keyword),
                keyword,
                customDimensionShopPage
        );
    }
}
