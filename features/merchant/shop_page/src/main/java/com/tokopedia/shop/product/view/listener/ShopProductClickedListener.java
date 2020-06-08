package com.tokopedia.shop.product.view.listener;


import com.tokopedia.shop.product.view.datamodel.ShopProductViewModel;

import org.jetbrains.annotations.NotNull;

/**
 * Created by normansyahputa on 2/24/18.
 */

public interface ShopProductClickedListener {

    void onThreeDotsClicked(@NotNull ShopProductViewModel shopProductViewModel, int shopTrackType);

    void onProductClicked(ShopProductViewModel shopProductViewModel, int shopTrackType, int productPosition);
}
