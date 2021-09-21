package com.tokopedia.shop.product.view.datamodel

import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.shop.product.view.adapter.ShopProductAdapterTypeFactory
import java.util.*

/**
 * Created by zulfikarrahman on 1/16/18.
 */
class ShopProductFeaturedUiModel : BaseShopProductViewModel {
    private var shopProductUiModelList: List<ShopProductUiModel>? = null

    constructor() {
        shopProductUiModelList = ArrayList()
    }

    constructor(shopProductUiModelList: List<ShopProductUiModel>?) {
        shopProductFeaturedViewModelList = shopProductUiModelList
    }

    override fun type(typeFactory: ShopProductAdapterTypeFactory?): Int {
        return typeFactory?.type(this).orZero()
    }

    var shopProductFeaturedViewModelList: List<ShopProductUiModel>?
        get() = shopProductUiModelList
        set(shopProductUiModelList) {
            if (shopProductUiModelList == null) {
                this.shopProductUiModelList = ArrayList()
            } else {
                this.shopProductUiModelList = shopProductUiModelList
            }
        }

    fun updateWishListStatus(productId: String?, wishList: Boolean): Boolean {
        var i = 0
        val sizei = shopProductUiModelList!!.size
        while (i < sizei) {
            val shopProductUiModel = shopProductUiModelList!![i]
            if (shopProductUiModel.id.equals(productId, ignoreCase = true)) {
                shopProductUiModel.isWishList = wishList
                return true
            }
            i++
        }
        return false
    }
}