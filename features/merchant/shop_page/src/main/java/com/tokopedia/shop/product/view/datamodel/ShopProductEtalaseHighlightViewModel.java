package com.tokopedia.shop.product.view.datamodel;

import com.tokopedia.shop.product.view.adapter.ShopProductAdapterTypeFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 1/16/18.
 */

public class ShopProductEtalaseHighlightViewModel implements BaseShopProductViewModel {

    private List<EtalaseHighlightCarouselViewModel> etalaseHighlightCarouselViewModelList;

    public ShopProductEtalaseHighlightViewModel() {
        etalaseHighlightCarouselViewModelList = new ArrayList<>();
    }

    public ShopProductEtalaseHighlightViewModel(List<EtalaseHighlightCarouselViewModel> etalaseHighlightCarouselViewModelList) {
        setEtalaseHighlightCarouselViewModelList(etalaseHighlightCarouselViewModelList);
    }

    @Override
    public int type(ShopProductAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    private void setEtalaseHighlightCarouselViewModelList(List<EtalaseHighlightCarouselViewModel> etalaseHighlightCarouselViewModelList) {
        if (etalaseHighlightCarouselViewModelList == null) {
            this.etalaseHighlightCarouselViewModelList = new ArrayList<>();
        } else {
            this.etalaseHighlightCarouselViewModelList = etalaseHighlightCarouselViewModelList;
        }
    }

    public List<EtalaseHighlightCarouselViewModel> getEtalaseHighlightCarouselViewModelList() {
        return etalaseHighlightCarouselViewModelList;
    }

    public boolean updateWishListStatus(String productId, boolean wishList) {
        if (etalaseHighlightCarouselViewModelList != null) {
            for (int i = 0, sizei = etalaseHighlightCarouselViewModelList.size(); i < sizei; i++) {
                EtalaseHighlightCarouselViewModel etalaseHighlightCarouselViewModel = etalaseHighlightCarouselViewModelList.get(i);
                List<ShopProductUiModel> shopProductUiModelList = etalaseHighlightCarouselViewModel.getShopProductUiModelList();
                for (int j = 0, sizej = shopProductUiModelList.size(); j < sizej; j++) {
                    ShopProductUiModel shopProductUiModel = shopProductUiModelList.get(j);
                    if (shopProductUiModel.getId().equalsIgnoreCase(productId)) {
                        shopProductUiModel.setWishList(wishList);
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
