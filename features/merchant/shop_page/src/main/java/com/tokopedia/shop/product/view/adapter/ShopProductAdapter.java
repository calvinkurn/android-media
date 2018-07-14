package com.tokopedia.shop.product.view.adapter;

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.shop.product.view.model.ShopProductViewModelOld;

import java.util.List;

/**
 * Created by zulfikarrahman on 1/22/18.
 */
@Deprecated
public class ShopProductAdapter extends BaseListAdapter<ShopProductViewModelOld, ShopProductAdapterTypeFactoryOld> {

    public ShopProductAdapter(ShopProductAdapterTypeFactoryOld baseListAdapterTypeFactory) {
        super(baseListAdapterTypeFactory);
    }

    public void updateWishListStatus(String productId, boolean wishList) {
        int i = 0;
        List<ShopProductViewModelOld> shopProductViewModelOldList = getData();
        for (ShopProductViewModelOld shopProductViewModelOld : shopProductViewModelOldList) {
            if (shopProductViewModelOld.getId().equalsIgnoreCase(productId)) {
                shopProductViewModelOld.setWishList(wishList);
                notifyItemChanged(i);
                return;
            }
            i++;
        }
    }
}