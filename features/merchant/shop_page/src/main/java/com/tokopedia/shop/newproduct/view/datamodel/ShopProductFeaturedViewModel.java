package com.tokopedia.shop.newproduct.view.datamodel;

import com.tokopedia.shop.newproduct.view.adapter.ShopProductAdapterTypeFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 1/16/18.
 */

public class ShopProductFeaturedViewModel implements BaseShopProductViewModel {

    private List<ShopProductViewModel> shopProductViewModelList;

    public ShopProductFeaturedViewModel(){
        shopProductViewModelList = new ArrayList<>();
    }

    public ShopProductFeaturedViewModel(List<ShopProductViewModel> shopProductViewModelList) {
        setShopProductFeaturedViewModelList(shopProductViewModelList);
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

    public boolean updateWishListStatus(String productId, boolean wishList) {
        for (int i = 0, sizei = shopProductViewModelList.size(); i < sizei; i++) {
            ShopProductViewModel shopProductViewModel = shopProductViewModelList.get(i);
            if (shopProductViewModel.getId().equalsIgnoreCase(productId)) {
                shopProductViewModel.setWishList(wishList);
                return true;
            }
        }
        return false;
    }
}
