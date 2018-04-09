package com.tokopedia.shop.product.view.model;

import com.tokopedia.shop.product.view.adapter.ShopProductLimitedAdapterTypeFactory;

import java.util.List;

/**
 * Created by zulfikarrahman on 1/16/18.
 */

public class ShopProductLimitedFeaturedViewModel implements ShopProductBaseViewModel {
    private List<ShopProductViewModel> shopProductViewModelList;

    public List<ShopProductViewModel> getShopProductViewModelList() {
        return shopProductViewModelList;
    }

    public void setShopProductViewModelList(List<ShopProductViewModel> shopProductViewModelList) {
        this.shopProductViewModelList = shopProductViewModelList;
    }

    @Override
    public int type(ShopProductLimitedAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }


}
