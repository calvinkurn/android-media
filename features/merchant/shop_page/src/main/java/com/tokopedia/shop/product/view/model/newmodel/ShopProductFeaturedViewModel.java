package com.tokopedia.shop.product.view.model.newmodel;

import com.tokopedia.shop.product.view.adapter.newadapter.ShopProductAdapterTypeFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 1/16/18.
 */

public class ShopProductFeaturedViewModel implements BaseShopProductViewModel{

    //TODO feature model

    private List<ShopProductFeaturedViewModel> shopProductFeaturedViewModelList;

    public ShopProductFeaturedViewModel(){
        shopProductFeaturedViewModelList = new ArrayList<>();
    }

    @Override
    public int type(ShopProductAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public void setShopProductFeaturedViewModelList(List<ShopProductFeaturedViewModel> shopProductFeaturedViewModelList) {
        if (shopProductFeaturedViewModelList == null) {
            this.shopProductFeaturedViewModelList = new ArrayList<>();
        } else {
            this.shopProductFeaturedViewModelList = shopProductFeaturedViewModelList;
        }
    }

    public List<ShopProductFeaturedViewModel> getShopProductFeaturedViewModelList() {
        return shopProductFeaturedViewModelList;
    }
}
