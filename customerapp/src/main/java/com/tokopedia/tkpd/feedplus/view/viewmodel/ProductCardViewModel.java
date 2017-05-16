package com.tokopedia.tkpd.feedplus.view.viewmodel;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.tkpd.feedplus.view.adapter.FeedPlusTypeFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by nisie on 5/15/17.
 */

public class ProductCardViewModel implements Visitable<FeedPlusTypeFactory> {

    private String shopName;
    private String shopAvatar;
    private ArrayList<ProductFeedViewModel> listProduct;
    private String actionText;
    private boolean isGoldMerchant;
    private String postTime;

    public ProductCardViewModel(String s, ArrayList<ProductFeedViewModel> listProduct) {
        this.shopName = s;
        this.listProduct = listProduct;
        this.actionText = "ubah 1 produk";
        this.shopAvatar = "https://imagerouter.tokopedia.com/img/100-square/shops-1/2016/8/5/1205649/1205649_620e3ec4-9a94-4210-bac4-f31ab1d1b9f5.jpg";
        this.isGoldMerchant = true;
        this.postTime = "4 hours ago";
    }

    @Override
    public int type(FeedPlusTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public ArrayList<ProductFeedViewModel> getListProduct() {
        return listProduct;
    }

    public void setListProduct(ArrayList<ProductFeedViewModel> listProduct) {
        this.listProduct = listProduct;
    }

    public String getActionText() {
        return actionText;
    }

    public void setActionText(String actionText) {
        this.actionText = actionText;
    }

    public String getShopAvatar() {
        return shopAvatar;
    }

    public void setShopAvatar(String shopAvatar) {
        this.shopAvatar = shopAvatar;
    }


    public boolean isGoldMerchant() {
        return isGoldMerchant;
    }

    public void setGoldMerchant(boolean goldMerchant) {
        isGoldMerchant = goldMerchant;
    }

    public String getPostTime() {
        return postTime;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }
}
