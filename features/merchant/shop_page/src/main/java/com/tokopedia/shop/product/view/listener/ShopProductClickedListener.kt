package com.tokopedia.shop.product.view.listener

import com.tokopedia.shop.product.view.datamodel.ShopProductUiModel

/**
 * Created by normansyahputa on 2/24/18.
 */
interface ShopProductClickedListener {
    fun onThreeDotsClicked(shopProductUiModel: ShopProductUiModel, shopTrackType: Int)
    fun onProductClicked(shopProductUiModel: ShopProductUiModel, shopTrackType: Int, productPosition: Int)

    fun onProductAtcNonVariantQuantityEditorChanged(
        shopProductUiModel: ShopProductUiModel,
        quantity: Int
    )

    fun onProductAtcVariantClick(shopProductUiModel: ShopProductUiModel)

    fun onProductAtcDefaultClick(shopProductUiModel: ShopProductUiModel, quantity: Int)
}