package com.tokopedia.shop.product.view.datamodel

import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.shop.product.view.adapter.ShopProductAdapterTypeFactory
import java.util.*

/**
 * Created by zulfikarrahman on 1/16/18.
 */
class ShopProductEtalaseHighlightUiModel : BaseShopProductViewModel {
    private var etalaseHighlightCarouselUiModelList: List<EtalaseHighlightCarouselUiModel>? = null

    constructor() {
        etalaseHighlightCarouselUiModelList = ArrayList()
    }

    constructor(etalaseHighlightCarouselUiModelList: List<EtalaseHighlightCarouselUiModel>?) {
        setEtalaseHighlightCarouselUiModelList(etalaseHighlightCarouselUiModelList)
    }

    override fun type(typeFactory: ShopProductAdapterTypeFactory?): Int {
        return typeFactory?.type(this).orZero()
    }

    private fun setEtalaseHighlightCarouselUiModelList(etalaseHighlightCarouselUiModelList: List<EtalaseHighlightCarouselUiModel>?) {
        if (etalaseHighlightCarouselUiModelList == null) {
            this.etalaseHighlightCarouselUiModelList = ArrayList()
        } else {
            this.etalaseHighlightCarouselUiModelList = etalaseHighlightCarouselUiModelList
        }
    }

    fun getEtalaseHighlightCarouselUiModelList(): List<EtalaseHighlightCarouselUiModel>? {
        return etalaseHighlightCarouselUiModelList
    }

    fun updateWishListStatus(productId: String?, wishList: Boolean): Boolean {
        if (etalaseHighlightCarouselUiModelList != null) {
            var i = 0
            val sizei = etalaseHighlightCarouselUiModelList!!.size
            while (i < sizei) {
                val etalaseHighlightCarouselUiModel = etalaseHighlightCarouselUiModelList!![i]
                val shopProductUiModelList = etalaseHighlightCarouselUiModel.shopProductUiModelList
                var j = 0
                val sizej = shopProductUiModelList!!.size
                while (j < sizej) {
                    val shopProductUiModel = shopProductUiModelList[j]
                    if (shopProductUiModel!!.id.equals(productId, ignoreCase = true)) {
                        shopProductUiModel.isWishList = wishList
                        return true
                    }
                    j++
                }
                i++
            }
        }
        return false
    }
}