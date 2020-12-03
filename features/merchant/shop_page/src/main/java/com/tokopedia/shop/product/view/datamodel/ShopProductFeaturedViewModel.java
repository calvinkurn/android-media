package com.tokopedia.shop.product.view.datamodel;

import com.tokopedia.shop.product.view.adapter.ShopProductAdapterTypeFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 1/16/18.
 */

public class ShopProductFeaturedViewModel implements BaseShopProductViewModel {

    private List<ShopProductUiModel> shopProductUiModelList;

    public ShopProductFeaturedViewModel(){
        shopProductUiModelList = new ArrayList<>();
    }

    public ShopProductFeaturedViewModel(List<ShopProductUiModel> shopProductUiModelList) {
        setShopProductFeaturedViewModelList(shopProductUiModelList);
    }

    @Override
    public int type(ShopProductAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public void setShopProductFeaturedViewModelList(List<ShopProductUiModel> shopProductUiModelList) {
        if (shopProductUiModelList == null) {
            this.shopProductUiModelList = new ArrayList<>();
        } else {
            this.shopProductUiModelList = shopProductUiModelList;
        }
    }

    public List<ShopProductUiModel> getShopProductFeaturedViewModelList() {
        return shopProductUiModelList;
    }

    public boolean updateWishListStatus(String productId, boolean wishList) {
        for (int i = 0, sizei = shopProductUiModelList.size(); i < sizei; i++) {
            ShopProductUiModel shopProductUiModel = shopProductUiModelList.get(i);
            if (shopProductUiModel.getId().equalsIgnoreCase(productId)) {
                shopProductUiModel.setWishList(wishList);
                return true;
            }
        }
        return false;
    }
}
