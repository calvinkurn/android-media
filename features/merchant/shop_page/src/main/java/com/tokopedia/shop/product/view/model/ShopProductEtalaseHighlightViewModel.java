package com.tokopedia.shop.product.view.model;

import android.text.TextUtils;

import com.tokopedia.shop.etalase.view.model.ShopEtalaseViewModel;
import com.tokopedia.shop.product.view.adapter.ShopProductAdapterTypeFactory;

import java.util.ArrayList;
import java.util.List;

import static com.tokopedia.shop.common.constant.ShopPageConstant.DEFAULT_ETALASE_HIGHLIGHT_POSITION;

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

    public boolean updateWishListStatus(String productId, String selectedEtalaseId, boolean wishList) {
        if (etalaseHighlightCarouselViewModelList != null) {
            for (int i = 0, sizei = etalaseHighlightCarouselViewModelList.size(); i < sizei; i++) {
                EtalaseHighlightCarouselViewModel etalaseHighlightCarouselViewModel = etalaseHighlightCarouselViewModelList.get(i);
                if (TextUtils.isEmpty(selectedEtalaseId) ||
                        etalaseHighlightCarouselViewModel.getShopEtalaseViewModel().getEtalaseId().equalsIgnoreCase(selectedEtalaseId)) {
                    List<ShopProductViewModel> shopProductViewModelList = etalaseHighlightCarouselViewModel.getShopProductViewModelList();
                    for (int j = 0, sizej = shopProductViewModelList.size(); j < sizej; j++) {
                        ShopProductViewModel shopProductViewModel = shopProductViewModelList.get(j);
                        if (shopProductViewModel.getId().equalsIgnoreCase(productId)) {
                            shopProductViewModel.setWishList(wishList);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
