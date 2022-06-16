package com.tokopedia.shop.product.view.listener

import com.tokopedia.shop.product.view.datamodel.ShopProductUiModel

/**
 * Created by normansyahputa on 2/24/18.
 */
interface ShopProductClickedListener {
    fun onThreeDotsClicked(shopProductUiModel: ShopProductUiModel, shopTrackType: Int)
    fun onProductClicked(shopProductUiModel: ShopProductUiModel, shopTrackType: Int, productPosition: Int)

    fun onProductAtcNonVariantQuantityEditorChanged(
        shopHomeProductViewModel: ShopProductUiModel,
        quantity: Int
    )

    fun onProductAtcVariantClick(shopHomeProductViewModel: ShopProductUiModel)

    fun onProductAtcDefaultClick(shopHomeProductViewModel: ShopProductUiModel)
}