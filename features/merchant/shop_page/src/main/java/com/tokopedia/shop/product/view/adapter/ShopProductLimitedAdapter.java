package com.tokopedia.shop.product.view.adapter;

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.shop.product.view.model.ShopProductBaseViewModel;
import com.tokopedia.shop.product.view.model.ShopProductLimitedPromoViewModel;
import com.tokopedia.shop.product.view.model.ShopProductViewModelOld;

import java.util.List;

/**
 * Created by zulfikarrahman on 3/1/18.
 */
@Deprecated
public class ShopProductLimitedAdapter extends BaseListAdapter<ShopProductBaseViewModel, ShopProductLimitedAdapterTypeFactory> {

    public ShopProductLimitedAdapter(ShopProductLimitedAdapterTypeFactory adapterTypeFactory) {
        super(adapterTypeFactory);
    }

    public void updateWishListStatus(String productId, boolean wishList) {
        List<ShopProductBaseViewModel> shopProductBaseViewModelList = getData();
        for (int i = 0; i < shopProductBaseViewModelList.size(); i++) {
            ShopProductBaseViewModel shopProductViewModel = shopProductBaseViewModelList.get(i);
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
        List<ShopProductBaseViewModel> shopProductBaseViewModelList = getData();
        for (int i = 0; i < shopProductBaseViewModelList.size(); i++) {
            ShopProductBaseViewModel shopProductViewModel = shopProductBaseViewModelList.get(i);
            if (shopProductViewModel instanceof ShopProductLimitedPromoViewModel) {
                ShopProductLimitedPromoViewModel shopProductLimitedPromoViewModel = ((ShopProductLimitedPromoViewModel) shopProductViewModel);
                if (shopProductLimitedPromoViewModel.getShopProductUserVisibleHintListener() != null) {
                    shopProductLimitedPromoViewModel.getShopProductUserVisibleHintListener().setUserVisibleHint(visible);
                }
                return;
            }
        }
    }
}
