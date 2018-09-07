package com.tokopedia.feedplus.view.viewmodel.promo;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.feedplus.view.adapter.typefactory.feed.FeedPlusTypeFactory;
import com.tokopedia.feedplus.view.viewmodel.product.ProductFeedViewModel;

import java.util.ArrayList;

/**
 * Created by stevenfredian on 5/16/17.
 */

public class PromotedShopViewModel implements Visitable<FeedPlusTypeFactory>{

    private String shopName;
    private String shopId;
    private String shopDomain;
    private String src;
    private String adKey;

    private boolean isGoldMerchant;
    private String description;
    private boolean isFavorited;
    private ArrayList<ProductFeedViewModel> listProduct;

    public PromotedShopViewModel(String shopName, boolean isGoldMerchant, String description, ArrayList<ProductFeedViewModel> listProduct) {
        this(shopName, isGoldMerchant, description, listProduct, false);
    }

    public PromotedShopViewModel(String shopName, boolean isGoldMerchant, String description, ArrayList<ProductFeedViewModel> listProduct, boolean isFavorited) {
        this.shopName = shopName;
        this.isGoldMerchant = isGoldMerchant;
        this.description = description;
        this.listProduct = listProduct;
        this.isFavorited = isFavorited;
    }

    @Override
    public int type(FeedPlusTypeFactory favoriteTypeFactory) {
        return favoriteTypeFactory.type(this);
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public boolean isGoldMerchant() {
        return isGoldMerchant;
    }

    public void setGoldMerchant(boolean goldMerchant) {
        isGoldMerchant = goldMerchant;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<ProductFeedViewModel> getListProduct() {
        return listProduct;
    }

    public void setListProduct(ArrayList<ProductFeedViewModel> listProduct) {
        this.listProduct = listProduct;
    }

    public boolean isFavorited() {
        return isFavorited;
    }

    public void setFavorited(boolean favorited) {
        isFavorited = favorited;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopDomain() {
        return shopDomain;
    }

    public void setShopDomain(String shopDomain) {
        this.shopDomain = shopDomain;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getAdKey() {
        return adKey;
    }

    public void setAdKey(String adKey) {
        this.adKey = adKey;
    }
}
