package com.tokopedia.shop.analytic;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.android.gms.tagmanager.DataLayer;
import com.tokopedia.shop.analytic.model.CustomDimensionShopPage;
import com.tokopedia.shop.analytic.model.CustomDimensionShopPageAttribution;
import com.tokopedia.shop.analytic.model.CustomDimensionShopPageProduct;
import com.tokopedia.shop.analytic.model.ListTitleTypeDef;
import com.tokopedia.shop.analytic.model.TrackShopTypeDef;
import com.tokopedia.shop.newproduct.view.datamodel.ShopProductViewModel;
import com.tokopedia.track.TrackApp;
import com.tokopedia.trackingoptimizer.TrackingQueue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static com.tokopedia.shop.analytic.NewShopPageTrackingConstant.*;

public class NewShopPageTrackingBuyer extends NewShopPageTracking {

    public NewShopPageTrackingBuyer(
            TrackingQueue trackingQueue) {
        super(trackingQueue);
    }

    private List<Object> createProductListMap(List<ShopProductViewModel> shopProductViewModelList,
                                              @ListTitleTypeDef String listTitle, String etalaseName,
                                              String attribution, int productPositionStart,
                                              @TrackShopTypeDef String shopTypeDef,
                                              String shopId, String shopName, boolean isActiveFreeOngkir) {
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < shopProductViewModelList.size(); i++) {
            ShopProductViewModel viewModel = shopProductViewModelList.get(i);
            list.add(
                    DataLayer.mapOf(
                            ShopPageTrackingConstant.NAME, viewModel.getName(),
                            ShopPageTrackingConstant.ID, viewModel.getId(),
                            ShopPageTrackingConstant.PRICE, formatPrice(viewModel.getDisplayedPrice()),
                            ShopPageTrackingConstant.BRAND, ShopPageTrackingConstant.NONE,
                            ShopPageTrackingConstant.CATEGORY, ShopPageTrackingConstant.NONE,
                            ShopPageTrackingConstant.VARIANT, ShopPageTrackingConstant.NONE,
                            ShopPageTrackingConstant.LIST, joinDash(SHOPPAGE, listTitle, etalaseName),
                            ShopPageTrackingConstant.POSITION, productPositionStart + i + 1,
                            ShopPageTrackingConstant.SHOP_TYPE, shopTypeDef,
                            ShopPageTrackingConstant.SHOP_ID, shopId,
                            ShopPageTrackingConstant.SHOP_NAME, shopName,
                            ShopPageTrackingConstant.PAGE_TYPE, SHOPPAGE,
                            ShopPageTrackingConstant.ATTRIBUTION, attribution,
                            ShopPageTrackingConstant.DIMENSION83, isActiveFreeOngkir ? FREE_ONGKIR : NONE_OR_OTHER
                    )
            );
        }

        return list;
    }

    private HashMap<String, Object> createProductImpressionMap(String event, String category, String action, String label,
                                                               CustomDimensionShopPageAttribution customDimensionShopPage,
                                                               List<ShopProductViewModel> shopProductViewModelList,
                                                               @ListTitleTypeDef String listTitle, String listName,
                                                               int productPositionStart,
                                                               String shopId, String shopName, boolean isActiveFreeOngkir) {
        HashMap<String, Object> eventMap = createMap(event, category, action, label, customDimensionShopPage);
        eventMap.put(ShopPageTrackingConstant.ECOMMERCE, DataLayer.mapOf(
                ShopPageTrackingConstant.CURRENCY_CODE, ShopPageTrackingConstant.IDR,
                ShopPageTrackingConstant.IMPRESSIONS,
                createProductListMap(shopProductViewModelList, listTitle, listName,
                        customDimensionShopPage.attribution,
                        productPositionStart,
                        customDimensionShopPage.shopType, shopId, shopName, isActiveFreeOngkir)));
        return eventMap;
    }

    private HashMap<String, Object> createProductClickMap(String event, String category, String action, String label,
                                                          CustomDimensionShopPageAttribution customDimensionShopPage,
                                                          ShopProductViewModel shopProductViewModel,
                                                          @ListTitleTypeDef String listTitle, String etalaseName,
                                                          int productPositionStart,
                                                          String shopId, String shopName, boolean isActiveFreeOngkir) {
        ArrayList<ShopProductViewModel> shopProductViewModelArrayList = new ArrayList<>();
        shopProductViewModelArrayList.add(shopProductViewModel);
        HashMap<String, Object> eventMap = createMap(event, category, action, label, customDimensionShopPage);
        eventMap.put(ShopPageTrackingConstant.ECOMMERCE, DataLayer.mapOf(
                ShopPageTrackingConstant.CLICK,
                DataLayer.mapOf(
                        ShopPageTrackingConstant.ACTION_FIELD, DataLayer.mapOf(ShopPageTrackingConstant.LIST, joinDash(SHOPPAGE, listTitle, etalaseName)),
                        ShopPageTrackingConstant.PRODUCTS, createProductListMap(shopProductViewModelArrayList, listTitle, etalaseName,
                                customDimensionShopPage.attribution,
                                productPositionStart,
                                customDimensionShopPage.shopType,
                                shopId, shopName, isActiveFreeOngkir))
        ));
        return eventMap;
    }

    private String formatPrice(String displayedPrice) {
        if (!TextUtils.isEmpty(displayedPrice)) {
            displayedPrice = displayedPrice.replaceAll("[^\\d]", "");
            return displayedPrice;
        } else {
            return "";
        }
    }

    @NonNull
    public String getListNameOfProduct(String tabName, String etalaseName) {
        etalaseName = TextUtils.isEmpty(etalaseName) ? ShopPageTrackingConstant.ALL_ETALASE : etalaseName;
        return ShopPageTrackingConstant.SHOPPAGE + " - " +
                tabName + " - " +
                etalaseName;
    }

    public void clickFollowUnfollowShop(boolean isFollow,
                                        CustomDimensionShopPage customDimensionShopPage) {
        sendEvent(CLICK_SHOP_PAGE,
                SHOP_PAGE_BUYER,
                joinDash(TOP_SECTION, CLICK),
                joinSpace(CLICK, isFollow ? FOLLOW : UNFOLLOW),
                customDimensionShopPage);
    }

    public void clickMessageSeller(CustomDimensionShopPage customDimensionShopPage) {
        sendGeneralEvent(CLICK_SHOP_PAGE,
                SHOP_PAGE_BUYER,
                CLICK_CHAT_SELLER,
                "",
                customDimensionShopPage);
    }

    public void followFromZeroFollower(CustomDimensionShopPage customDimensionShopPage) {
        sendEvent(CLICK_SHOP_PAGE,
                SHOP_PAGE_BUYER,
                joinDash(TOP_SECTION, CLICK),
                CLICK_FOLLOW_FROM_ZERO_FOLLOWER,
                customDimensionShopPage);
    }

    public void impressionFollowFromZeroFollower(CustomDimensionShopPage customDimensionShopPage) {
        sendEvent(VIEW_SHOP_PAGE,
                SHOP_PAGE_BUYER,
                joinDash(TOP_SECTION, IMPRESSION),
                IMPRESSION_FOLLOW_FROM_ZERO_FOLLOWER,
                customDimensionShopPage);
    }

    public void clickProduct(boolean isOwner,
                             @ListTitleTypeDef String listType,
                             String sectionName,
                             CustomDimensionShopPageAttribution customDimensionShopPage,
                             ShopProductViewModel shopProductViewModel,
                             int productPosStart,
                             String shopId, String shopName, boolean isActiveFreeOngkir) {
        sendDataLayerEvent(
                createProductClickMap(PRODUCT_CLICK,
                        getShopPageCategory(isOwner),
                        joinDash(joinSpace(listType, sectionName), CLICK),
                        CLICK_PRODUCT_PICTURE,
                        customDimensionShopPage,
                        shopProductViewModel,
                        listType, sectionName,
                        productPosStart, shopId, shopName, isActiveFreeOngkir));
    }

    public void impressionProductList(boolean isOwner,
                                      @ListTitleTypeDef String listType,
                                      String sectionName,
                                      CustomDimensionShopPageAttribution customDimensionShopPage,
                                      List<ShopProductViewModel> shopProductViewModelList,
                                      int productPosStart,
                                      String shopId, String shopName, boolean isActiveFreeOngkir) {
        if (isOwner) {
            sendEvent(VIEW_SHOP_PAGE,
                    SHOP_PAGE_SELLER,
                    joinDash(joinSpace(listType, sectionName), IMPRESSION),
                    IMPRESSION_OF_PRODUCT_LIST,
                    customDimensionShopPage);
        } else {
            sendDataLayerEvent(
                    createProductImpressionMap(PRODUCT_VIEW,
                            SHOP_PAGE_BUYER,
                            joinDash(joinSpace(listType, sectionName), IMPRESSION),
                            IMPRESSION_OF_PRODUCT_LIST,
                            customDimensionShopPage,
                            shopProductViewModelList,
                            listType, sectionName,
                            productPosStart, shopId, shopName, isActiveFreeOngkir));
        }
    }

    public void clickWishlist(boolean isAdd,
                              @ListTitleTypeDef String listType,
                              String sectionName,
                              CustomDimensionShopPageProduct customDimensionShopPage) {
        sendEvent(CLICK_SHOP_PAGE,
                SHOP_PAGE_BUYER,
                joinDash(joinSpace(listType, sectionName), CLICK),
                joinDash(CLICK_WISHLIST, isAdd ? ADD : REMOVE),
                customDimensionShopPage);
    }

    public void eventShopSendChat() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(CLICK_SHOP_MESSAGE,
                SHOP_PAGE, CLICK_SEND_CHAT, "");
    }

    public void clickSearchBox(String pageName) {
        sendEvent(
                CLICK_TOP_NAV,
                String.format(TOP_NAV, pageName),
                SHOP_SEARCH_PRODUCT_CLICK_SEARCH_BOX,
                "",
                null
        );
    }

    public void sendEventMembership(String eventAction) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                ShopPageTrackingConstant.CLICK_MEMBERSHIP_EVENT,
                ShopPageTrackingConstant.MEMBERSHIP_SHOP_PAGE,
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
        String  action;
        if(!isShopFavorited){
            action= CLICK_FOLLOW;
        }else{
            action= CLICK_UNFOLLOW;
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
        sendEvent(CLICK_SHOP_PAGE,
                getShopPageCategory(isOwner),
                CLICK_SHOWCASE_LIST,
                String.format(ETALASE_X, selectedEtalaseName),
                customDimensionShopPage);
    }

    public void sendMoEngageFavoriteEvent(String shopName, String shopID, String shopDomain ,String shopLocation,
                                          Boolean isShopOfficaial, Boolean isFollowed) {
        Map<String, Object> mapData = DataLayer.mapOf(
                ShopPageTrackingConstant.SHOP_NAME ,shopName,
                ShopPageTrackingConstant.SHOP_ID ,shopID,
                ShopPageTrackingConstant.SHOP_LOCATION ,shopLocation,
                ShopPageTrackingConstant.URL_SLUG ,shopDomain,
                ShopPageTrackingConstant.IS_OFFICIAL_STORE ,isShopOfficaial
        );
        String eventName;
        if (isFollowed)
            eventName =  ShopPageTrackingConstant.SELLER_ADDED_TO_FAVORITE;
        else
            eventName = ShopPageTrackingConstant.SELLER_REMOVED_FROM_FAVORITE;
        TrackApp.getInstance().getMoEngage().sendTrackEvent(mapData,eventName);
    }
}
