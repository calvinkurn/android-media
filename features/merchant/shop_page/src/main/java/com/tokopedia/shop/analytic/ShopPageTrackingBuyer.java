package com.tokopedia.shop.analytic;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.android.gms.tagmanager.DataLayer;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.shop.analytic.model.CustomDimensionShopPage;
import com.tokopedia.shop.analytic.model.CustomDimensionShopPageAttribution;
import com.tokopedia.shop.analytic.model.CustomDimensionShopPageProduct;
import com.tokopedia.shop.analytic.model.ListTitleTypeDef;
import com.tokopedia.shop.analytic.model.TrackShopTypeDef;
import com.tokopedia.shop.product.view.model.ShopProductViewModel;
import com.tokopedia.trackingoptimizer.TrackingQueue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.ADD;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_FOLLOW_FROM_ZERO_FOLLOWER;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_MESSAGE_SELLER;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_PRODUCT_PICTURE;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_SHOP_PAGE;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_WISHLIST;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.FOLLOW;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.IMPRESSION;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.IMPRESSION_FOLLOW_FROM_ZERO_FOLLOWER;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.IMPRESSION_OF_PRODUCT_LIST;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.PRODUCT_CLICK;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.PRODUCT_VIEW;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.REMOVE;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.SHOP_PAGE_BUYER;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.SHOP_PAGE_SELLER;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.TOP_SECTION;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.UNFOLLOW;
import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.VIEW_SHOP_PAGE;

public class ShopPageTrackingBuyer extends ShopPageTrackingUser {

    public ShopPageTrackingBuyer(AbstractionRouter shopTrackingRouter,
                                 TrackingQueue trackingQueue) {
        super(shopTrackingRouter, trackingQueue);
    }

    private List<Object> createProductListMap(List<ShopProductViewModel> shopProductViewModelList,
                                              @ListTitleTypeDef String listTitle, String etalaseName,
                                              String attribution, int productPositionStart,
                                              @TrackShopTypeDef String shopTypeDef,
                                              String shopId, String shopName) {
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
                            ShopPageTrackingConstant.ATTRIBUTION, attribution
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
                                                               String shopId, String shopName) {
        HashMap<String, Object> eventMap = createMap(event, category, action, label, customDimensionShopPage);
        eventMap.put(ShopPageTrackingConstant.ECOMMERCE, DataLayer.mapOf(
                ShopPageTrackingConstant.CURRENCY_CODE, ShopPageTrackingConstant.IDR,
                ShopPageTrackingConstant.IMPRESSIONS,
                createProductListMap(shopProductViewModelList, listTitle, listName,
                        customDimensionShopPage.attribution,
                        productPositionStart,
                        customDimensionShopPage.shopType, shopId, shopName)));
        return eventMap;
    }

    private HashMap<String, Object> createProductClickMap(String event, String category, String action, String label,
                                                          CustomDimensionShopPageAttribution customDimensionShopPage,
                                                          ShopProductViewModel shopProductViewModel,
                                                          @ListTitleTypeDef String listTitle, String etalaseName,
                                                          int productPositionStart,
                                                          String shopId, String shopName) {
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
                                shopId, shopName))
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
                                    String shopId, String shopName) {
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
                            productPosStart, shopId, shopName));
        }
    }

    public void impressionProductList(boolean isOwner,
                                      @ListTitleTypeDef String listType,
                                      String sectionName,
                                      CustomDimensionShopPageAttribution customDimensionShopPage,
                                      List<ShopProductViewModel> shopProductViewModelList,
                                      int productPosStart,
                                      String shopId, String shopName) {
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
                            productPosStart, shopId, shopName));
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

}
