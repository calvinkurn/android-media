package com.tokopedia.tkpd.tkpdcustomer.feedplus.view.viewmodel;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.tkpd.tkpdcustomer.feedplus.view.adapter.typefactory.FeedPlusTypeFactory;

import java.util.ArrayList;

/**
 * Created by stevenfredian on 5/16/17.
 */

public class PromotedShopViewModel implements Visitable<FeedPlusTypeFactory>{

    private String shopName;
    private boolean isGoldMerchant;
    private String description;
    private ArrayList<ProductFeedViewModel> listProduct;

    public PromotedShopViewModel(String shopName, boolean isGoldMerchant, String description, ArrayList<ProductFeedViewModel> listProduct) {
        this.shopName = shopName;
        this.isGoldMerchant = isGoldMerchant;
        this.description = description;
        this.listProduct = listProduct;
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
}
