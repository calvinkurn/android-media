package com.tokopedia.shop.product.view.model;

import com.tokopedia.shop.etalase.view.model.ShopEtalaseViewModel;
import com.tokopedia.shop.product.view.adapter.ShopProductAdapterTypeFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 1/16/18.
 */

public class ShopProductEtalaseHighlightViewModel implements BaseShopProductViewModel{

    private List<List<ShopProductViewModel>> shopProductViewModelListList;
    private List<ShopEtalaseViewModel> shopEtalaseViewModelList;

    public ShopProductEtalaseHighlightViewModel(){
        shopProductViewModelListList = new ArrayList<>();
    }

    public ShopProductEtalaseHighlightViewModel(List<List<ShopProductViewModel>> shopProductViewModelListList,
                                                List<ShopEtalaseViewModel> shopEtalaseViewModelList) {
        setShopProductEtalaseHighLightViewModelList(shopProductViewModelListList);
        setEtalaseViewModelList(shopEtalaseViewModelList);
    }

    @Override
    public int type(ShopProductAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public void setShopProductEtalaseHighLightViewModelList(List<List<ShopProductViewModel>> shopProductViewModelListList) {
        if (shopProductViewModelListList == null) {
            this.shopProductViewModelListList = new ArrayList<>();
        } else {
            this.shopProductViewModelListList = shopProductViewModelListList;
        }
    }

    public void setEtalaseViewModelList(List<ShopEtalaseViewModel> shopEtalaseViewModelList) {
        if (shopEtalaseViewModelList == null) {
            this.shopEtalaseViewModelList = new ArrayList<>();
        } else {
            this.shopEtalaseViewModelList = shopEtalaseViewModelList;
        }
    }

    public List<List<ShopProductViewModel>> getShopProductViewModelListList() {
        return shopProductViewModelListList;
    }

    public List<ShopEtalaseViewModel> getShopEtalaseViewModelList() {
        return shopEtalaseViewModelList;
    }

    public boolean updateWishListStatus(String productId, boolean wishList) {
        for (int i = 0, sizei = shopProductViewModelListList.size(); i < sizei; i++) {
            List<ShopProductViewModel> shopProductViewModelList = shopProductViewModelListList.get(i);
            for (int j = 0, sizej = shopProductViewModelList.size(); j < sizej; j++) {
                ShopProductViewModel shopProductViewModel = shopProductViewModelList.get(j);
                if (shopProductViewModel.getId().equalsIgnoreCase(productId)) {
                    shopProductViewModel.setWishList(wishList);
                    return true;
                }
            }

        }
        return false;
    }
}
