package com.tokopedia.shop.product.view.datamodel;

import com.tokopedia.shop.product.view.adapter.ShopProductAdapterTypeFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 1/16/18.
 */

public class ShopProductEtalaseHighlightUiModel implements BaseShopProductViewModel {

    private List<EtalaseHighlightCarouselUiModel> etalaseHighlightCarouselUiModelList;

    public ShopProductEtalaseHighlightUiModel() {
        etalaseHighlightCarouselUiModelList = new ArrayList<>();
    }

    public ShopProductEtalaseHighlightUiModel(List<EtalaseHighlightCarouselUiModel> etalaseHighlightCarouselUiModelList) {
        setEtalaseHighlightCarouselUiModelList(etalaseHighlightCarouselUiModelList);
    }

    @Override
    public int type(ShopProductAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    private void setEtalaseHighlightCarouselUiModelList(List<EtalaseHighlightCarouselUiModel> etalaseHighlightCarouselUiModelList) {
        if (etalaseHighlightCarouselUiModelList == null) {
            this.etalaseHighlightCarouselUiModelList = new ArrayList<>();
        } else {
            this.etalaseHighlightCarouselUiModelList = etalaseHighlightCarouselUiModelList;
        }
    }

    public List<EtalaseHighlightCarouselUiModel> getEtalaseHighlightCarouselUiModelList() {
        return etalaseHighlightCarouselUiModelList;
    }

    public boolean updateWishListStatus(String productId, boolean wishList) {
        if (etalaseHighlightCarouselUiModelList != null) {
            for (int i = 0, sizei = etalaseHighlightCarouselUiModelList.size(); i < sizei; i++) {
                EtalaseHighlightCarouselUiModel etalaseHighlightCarouselUiModel = etalaseHighlightCarouselUiModelList.get(i);
                List<ShopProductUiModel> shopProductUiModelList = etalaseHighlightCarouselUiModel.getShopProductUiModelList();
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
