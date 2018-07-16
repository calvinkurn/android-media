package com.tokopedia.shop.product.view.model.newmodel;

import com.tokopedia.shop.product.view.adapter.newadapter.ShopProductAdapterTypeFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 1/16/18.
 */

public class ShopProductFeaturedViewModel implements BaseShopProductViewModel{

    //TODO feature model

    private List<ShopProductViewModel> shopProductViewModelList;

    public ShopProductFeaturedViewModel(){
        shopProductViewModelList = new ArrayList<>();
    }

    @Override
    public int type(ShopProductAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public void setShopProductFeaturedViewModelList(List<ShopProductViewModel> shopProductViewModelList) {
        if (shopProductViewModelList == null) {
            this.shopProductViewModelList = new ArrayList<>();
        } else {
            this.shopProductViewModelList = shopProductViewModelList;
        }
    }

    public List<ShopProductViewModel> getShopProductFeaturedViewModelList() {
        return shopProductViewModelList;
    }
}
