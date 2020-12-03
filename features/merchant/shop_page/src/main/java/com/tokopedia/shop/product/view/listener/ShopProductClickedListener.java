package com.tokopedia.shop.product.view.listener;


import com.tokopedia.shop.product.view.datamodel.ShopProductUiModel;

import org.jetbrains.annotations.NotNull;

/**
 * Created by normansyahputa on 2/24/18.
 */

public interface ShopProductClickedListener {

    void onThreeDotsClicked(@NotNull ShopProductUiModel shopProductUiModel, int shopTrackType);

    void onProductClicked(ShopProductUiModel shopProductUiModel, int shopTrackType, int productPosition);
}
