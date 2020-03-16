package com.tokopedia.shop.analytic;

import androidx.annotation.NonNull;
import android.text.TextUtils;

import com.tokopedia.analyticconstant.DataLayer;
import com.tokopedia.shop.analytic.model.CustomDimensionShopPage;
import com.tokopedia.shop.analytic.model.CustomDimensionShopPageAttribution;
import com.tokopedia.shop.analytic.model.CustomDimensionShopPageProduct;
import com.tokopedia.shop.analytic.model.ListTitleTypeDef;
import com.tokopedia.shop.analytic.model.TrackShopTypeDef;
import com.tokopedia.shop.product.view.model.ShopProductViewModel;
import com.tokopedia.track.TrackApp;
import com.tokopedia.trackingoptimizer.TrackingQueue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tokopedia.shop.analytic.OldShopPageTrackingConstant.ADD;
import static com.tokopedia.shop.analytic.OldShopPageTrackingConstant.CLICK;
import static com.tokopedia.shop.analytic.OldShopPageTrackingConstant.CLICK_FOLLOW_FROM_ZERO_FOLLOWER;
import static com.tokopedia.shop.analytic.OldShopPageTrackingConstant.CLICK_MESSAGE_SELLER;
import static com.tokopedia.shop.analytic.OldShopPageTrackingConstant.CLICK_PRODUCT_PICTURE;
import static com.tokopedia.shop.analytic.OldShopPageTrackingConstant.CLICK_SEND_CHAT;
import static com.tokopedia.shop.analytic.OldShopPageTrackingConstant.CLICK_SHOP_MESSAGE;
import static com.tokopedia.shop.analytic.OldShopPageTrackingConstant.CLICK_SHOP_PAGE;
import static com.tokopedia.shop.analytic.OldShopPageTrackingConstant.CLICK_TOP_NAV;
import static com.tokopedia.shop.analytic.OldShopPageTrackingConstant.CLICK_WISHLIST;
import static com.tokopedia.shop.analytic.OldShopPageTrackingConstant.FOLLOW;
import static com.tokopedia.shop.analytic.OldShopPageTrackingConstant.FREE_ONGKIR;
import static com.tokopedia.shop.analytic.OldShopPageTrackingConstant.IMPRESSION;
import static com.tokopedia.shop.analytic.OldShopPageTrackingConstant.IMPRESSION_FOLLOW_FROM_ZERO_FOLLOWER;
import static com.tokopedia.shop.analytic.OldShopPageTrackingConstant.IMPRESSION_OF_PRODUCT_LIST;
import static com.tokopedia.shop.analytic.OldShopPageTrackingConstant.NONE_OR_OTHER;
import static com.tokopedia.shop.analytic.OldShopPageTrackingConstant.PRODUCT_CLICK;
import static com.tokopedia.shop.analytic.OldShopPageTrackingConstant.PRODUCT_VIEW;
import static com.tokopedia.shop.analytic.OldShopPageTrackingConstant.REMOVE;
import static com.tokopedia.shop.analytic.OldShopPageTrackingConstant.SHOP_PAGE_BUYER;
import static com.tokopedia.shop.analytic.OldShopPageTrackingConstant.SHOP_PAGE_SELLER;
import static com.tokopedia.shop.analytic.OldShopPageTrackingConstant.SHOP_SEARCH_PRODUCT_CLICK_SEARCH_BOX;
import static com.tokopedia.shop.analytic.OldShopPageTrackingConstant.TOP_NAV;
import static com.tokopedia.shop.analytic.OldShopPageTrackingConstant.TOP_SECTION;
import static com.tokopedia.shop.analytic.OldShopPageTrackingConstant.UNFOLLOW;
import static com.tokopedia.shop.analytic.OldShopPageTrackingConstant.VIEW_SHOP_PAGE;

public class OldShopPageTrackingBuyer extends OldShopPageTrackingUser {

    public OldShopPageTrackingBuyer(
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
                            OldShopPageTrackingConstant.NAME, viewModel.getName(),
                            OldShopPageTrackingConstant.ID, viewModel.getId(),
                            OldShopPageTrackingConstant.PRICE, formatPrice(viewModel.getDisplayedPrice()),
                            OldShopPageTrackingConstant.BRAND, OldShopPageTrackingConstant.NONE,
                            OldShopPageTrackingConstant.CATEGORY, OldShopPageTrackingConstant.NONE,
                            OldShopPageTrackingConstant.VARIANT, OldShopPageTrackingConstant.NONE,
                            OldShopPageTrackingConstant.LIST, joinDash(SHOPPAGE, listTitle, etalaseName),
                            OldShopPageTrackingConstant.POSITION, productPositionStart + i + 1,
                            OldShopPageTrackingConstant.SHOP_TYPE, shopTypeDef,
                            OldShopPageTrackingConstant.SHOP_ID, shopId,
                            OldShopPageTrackingConstant.SHOP_NAME, shopName,
                            OldShopPageTrackingConstant.PAGE_TYPE, SHOPPAGE,
                            OldShopPageTrackingConstant.ATTRIBUTION, attribution,
                            OldShopPageTrackingConstant.DIMENSION83, isActiveFreeOngkir? FREE_ONGKIR: NONE_OR_OTHER
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
        eventMap.put(OldShopPageTrackingConstant.ECOMMERCE, DataLayer.mapOf(
                OldShopPageTrackingConstant.CURRENCY_CODE, OldShopPageTrackingConstant.IDR,
                OldShopPageTrackingConstant.IMPRESSIONS,
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
        eventMap.put(OldShopPageTrackingConstant.ECOMMERCE, DataLayer.mapOf(
                OldShopPageTrackingConstant.CLICK,
                DataLayer.mapOf(
                        OldShopPageTrackingConstant.ACTION_FIELD, DataLayer.mapOf(OldShopPageTrackingConstant.LIST, joinDash(SHOPPAGE, listTitle, etalaseName)),
                        OldShopPageTrackingConstant.PRODUCTS, createProductListMap(shopProductViewModelArrayList, listTitle, etalaseName,
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
        etalaseName = TextUtils.isEmpty(etalaseName) ? OldShopPageTrackingConstant.ALL_ETALASE : etalaseName;
        return OldShopPageTrackingConstant.SHOPPAGE + " - " +
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
        sendEvent(CLICK_SHOP_PAGE,
                SHOP_PAGE_BUYER,
                joinDash(TOP_SECTION, CLICK),
                CLICK_MESSAGE_SELLER,
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

    public void clickProductPicture(boolean isOwner,
                                    @ListTitleTypeDef String listType,
                                    String sectionName,
                                    CustomDimensionShopPageAttribution customDimensionShopPage,
                                    ShopProductViewModel shopProductViewModel,
                                    int productPosStart,
                                    String shopId, String shopName, boolean isActiveFreeOngkir) {
        if (isOwner) {
            sendEvent(CLICK_SHOP_PAGE,
                    SHOP_PAGE_SELLER,
                    joinDash(joinSpace(listType, sectionName), CLICK),
                    CLICK_PRODUCT_PICTURE,
                    customDimensionShopPage);
        } else {
            sendDataLayerEvent(
                    createProductClickMap(PRODUCT_CLICK,
                            SHOP_PAGE_BUYER,
                            joinDash(joinSpace(listType, sectionName), CLICK),
                            CLICK_PRODUCT_PICTURE,
                            customDimensionShopPage,
                            shopProductViewModel,
                            listType, sectionName,
                            productPosStart, shopId, shopName, isActiveFreeOngkir));
        }
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
                OldShopPageTrackingConstant.CLICK_MEMBERSHIP_EVENT,
                OldShopPageTrackingConstant.MEMBERSHIP_SHOP_PAGE,
                eventAction,
                ""
        );
    }

    public void sendMoEngageFavoriteEvent(String shopName, String shopID, String shopDomain ,String shopLocation,
                                          Boolean isShopOfficaial, Boolean isFollowed) {
        Map<String, Object> mapData = DataLayer.mapOf(
                OldShopPageTrackingConstant.SHOP_NAME ,shopName,
                OldShopPageTrackingConstant.SHOP_ID ,shopID,
                OldShopPageTrackingConstant.SHOP_LOCATION ,shopLocation,
                OldShopPageTrackingConstant.URL_SLUG ,shopDomain,
                OldShopPageTrackingConstant.IS_OFFICIAL_STORE ,isShopOfficaial
        );
        String eventName;
        if (isFollowed)
            eventName =  OldShopPageTrackingConstant.SELLER_ADDED_TO_FAVORITE;
        else
            eventName = OldShopPageTrackingConstant.SELLER_REMOVED_FROM_FAVORITE;
        TrackApp.getInstance().getMoEngage().sendTrackEvent(mapData,eventName);
    }
}
