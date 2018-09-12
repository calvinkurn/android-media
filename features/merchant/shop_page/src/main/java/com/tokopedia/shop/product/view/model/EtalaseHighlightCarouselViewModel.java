package com.tokopedia.shop.product.view.model;

import com.tokopedia.shop.etalase.view.model.ShopEtalaseViewModel;
import com.tokopedia.shop.product.view.adapter.ShopProductAdapterTypeFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 1/16/18.
 */

public class EtalaseHighlightCarouselViewModel {

    private List<ShopProductViewModel> shopProductViewModelList;
    private ShopEtalaseViewModel shopEtalaseViewModel;

    public EtalaseHighlightCarouselViewModel(){
        shopProductViewModelList = new ArrayList<>();
    }

    public EtalaseHighlightCarouselViewModel(List<ShopProductViewModel> shopProductViewModelList,
                                             ShopEtalaseViewModel shopEtalaseViewModel) {
        setShopProductEtalaseHighLightViewModel(shopProductViewModelList);
        setEtalaseViewModel(shopEtalaseViewModel);
    }

    private void setShopProductEtalaseHighLightViewModel(List<ShopProductViewModel> shopProductViewModelList) {
        if (shopProductViewModelList == null) {
            this.shopProductViewModelList = new ArrayList<>();
        } else {
            this.shopProductViewModelList = shopProductViewModelList;
        }
    }

    public List<ShopProductViewModel> getShopProductViewModelList() {
        return shopProductViewModelList;
    }

    public ShopEtalaseViewModel getShopEtalaseViewModel() {
        return shopEtalaseViewModel;
    }

    private void setEtalaseViewModel(ShopEtalaseViewModel shopEtalaseViewModel) {
        this.shopEtalaseViewModel = shopEtalaseViewModel;
    }

//    public boolean updateWishListStatus(String productId, boolean wishList) {
//        for (int i = 0, sizei = shopProductViewModelListList.size(); i < sizei; i++) {
//            List<ShopProductViewModel> shopProductViewModelList = shopProductViewModelListList.get(i);
//            for (int j = 0, sizej = shopProductViewModelList.size(); j < sizej; j++) {
//                ShopProductViewModel shopProductViewModel = shopProductViewModelList.get(j);
//                if (shopProductViewModel.getId().equalsIgnoreCase(productId)) {
//                    shopProductViewModel.setWishList(wishList);
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
}
