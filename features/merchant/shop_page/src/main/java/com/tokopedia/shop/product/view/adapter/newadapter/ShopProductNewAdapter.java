package com.tokopedia.shop.product.view.adapter.newadapter;

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.shop.product.view.model.ShopProductViewModelOld;
import com.tokopedia.shop.product.view.model.newmodel.BaseShopProductViewModel;
import com.tokopedia.shop.product.view.model.newmodel.ShopProductPromoViewModel;

import java.util.List;

/**
 * Created by hendry on 12/07/18.
 */

public class ShopProductNewAdapter extends BaseListAdapter<BaseShopProductViewModel, ShopProductAdapterTypeFactory> {

    private ShopProductPromoViewModel shopProductPromoViewModel;

    public ShopProductNewAdapter(ShopProductAdapterTypeFactory baseListAdapterTypeFactory) {
        super(baseListAdapterTypeFactory, null);
    }

    public void setShopProductPromoViewModel(ShopProductPromoViewModel shopProductPromoViewModel){
        this.shopProductPromoViewModel = shopProductPromoViewModel;
    }

    public void updateWishListStatus(String productId, boolean wishList) {
        List<BaseShopProductViewModel> shopProductBaseViewModelList = getData();
        for (int i = 0; i < shopProductBaseViewModelList.size(); i++) {
            BaseShopProductViewModel shopProductViewModel = shopProductBaseViewModelList.get(i);
            if (shopProductViewModel instanceof ShopProductViewModelOld) {
                if (((ShopProductViewModelOld)shopProductViewModel).getId().equalsIgnoreCase(productId)) {
                    ((ShopProductViewModelOld)shopProductViewModel).setWishList(wishList);
                    notifyItemChanged(i);
                    return;
                }
            }
        }
    }

    public void updateVisibleStatus(boolean visible) {
        List<BaseShopProductViewModel> shopProductBaseViewModelList = getData();
        for (int i = 0; i < shopProductBaseViewModelList.size(); i++) {
            BaseShopProductViewModel shopProductViewModel = shopProductBaseViewModelList.get(i);
            if (shopProductViewModel instanceof ShopProductPromoViewModel) {
                ShopProductPromoViewModel shopProductPromoViewModel = ((ShopProductPromoViewModel) shopProductViewModel);
                if (shopProductPromoViewModel.getShopProductUserVisibleHintListener() != null) {
                    shopProductPromoViewModel.getShopProductUserVisibleHintListener().setUserVisibleHint(visible);
                }
                return;
            }
        }
    }

    @Override
    protected boolean isItemClickableByDefault() {
        return false;
    }
}
