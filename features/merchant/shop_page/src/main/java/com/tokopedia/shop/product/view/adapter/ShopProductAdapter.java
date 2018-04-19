package com.tokopedia.shop.product.view.adapter;

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.shop.product.view.model.ShopProductViewModel;

import java.util.Iterator;

/**
 * Created by zulfikarrahman on 1/22/18.
 */

public class ShopProductAdapter extends BaseListAdapter<ShopProductViewModel, ShopProductAdapterTypeFactory> {

    public ShopProductAdapter(ShopProductAdapterTypeFactory baseListAdapterTypeFactory) {
        super(baseListAdapterTypeFactory);
    }

    public void updateWishListStatus(String productId, boolean wishList) {
        int i = 0;
        for (ShopProductViewModel shopProductViewModel: getData()) {
            if (shopProductViewModel.getId().equalsIgnoreCase(productId)) {
                shopProductViewModel.setWishList(wishList);
                notifyItemChanged(i);
                return;
            }
            i++;
        }
    }
}