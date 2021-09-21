package com.tokopedia.shop.product.view.datamodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.shop.product.view.adapter.EtalaseHighlightAdapterTypeFactory
import com.tokopedia.shop.product.view.adapter.ShopProductAdapterTypeFactory
import java.util.*

/**
 * Created by zulfikarrahman on 1/16/18.
 */
class EtalaseHighlightCarouselUiModel(shopProductUiModelList: List<ShopProductUiModel>?,
                                      shopEtalaseViewModel: ShopEtalaseItemDataModel) : Visitable<BaseAdapterTypeFactory?> {
    var shopProductUiModelList: List<ShopProductUiModel>? = null
        private set
    var shopEtalaseViewModel: ShopEtalaseItemDataModel? = null
        private set

    private fun setShopProductEtalaseHighLightViewModel(shopProductUiModelList: List<ShopProductUiModel>?) {
        if (shopProductUiModelList == null) {
            this.shopProductUiModelList = ArrayList()
        } else {
            this.shopProductUiModelList = shopProductUiModelList
        }
    }

    private fun setEtalaseViewModel(shopEtalaseViewModel: ShopEtalaseItemDataModel) {
        this.shopEtalaseViewModel = shopEtalaseViewModel
    }

    override fun type(typeFactory: BaseAdapterTypeFactory?): Int {
        return if (typeFactory is EtalaseHighlightAdapterTypeFactory) {
            typeFactory.type(this)
        } else if (typeFactory is ShopProductAdapterTypeFactory) {
            typeFactory.type(this)
        } else {
            0
        }
    }

    init {
        setShopProductEtalaseHighLightViewModel(shopProductUiModelList)
        setEtalaseViewModel(shopEtalaseViewModel)
    }
}