package com.tokopedia.shop.newproduct.view.adapter

import android.view.View

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.newproduct.view.datamodel.ShopProductEtalaseChipItemViewModel
import com.tokopedia.shop.newproduct.view.datamodel.ShopProductAddEtalaseChipViewModel
import com.tokopedia.shop.newproduct.view.viewholder.ShopProductAddEtalaseChipViewHolder
import com.tokopedia.shop.newproduct.view.viewholder.ShopProductEtalaseChipItemViewHolder
import com.tokopedia.shop.newproduct.view.viewholder.ShopProductEtalaseListViewHolder

/**
 * Created by normansyahputa on 2/28/18.
 */

class ShopProductEtalaseAdapterTypeFactory(
        private val shopProductEtalaseListViewHolderListener: ShopProductEtalaseListViewHolder.ShopProductEtalaseChipListViewHolderListener?
) : BaseAdapterTypeFactory() {
    lateinit var shopEtalaseAdapter: ShopProductEtalaseAdapter

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            ShopProductEtalaseChipItemViewHolder.LAYOUT -> ShopProductEtalaseChipItemViewHolder(
                    parent,
                    shopProductEtalaseListViewHolderListener,
                    shopEtalaseAdapter
            )
            ShopProductAddEtalaseChipViewHolder.LAYOUT -> ShopProductAddEtalaseChipViewHolder(
                    parent,
                    shopProductEtalaseListViewHolderListener
            )
            else -> super.createViewHolder(parent, type)
        }
    }

    fun type(shopProductPlusAddEtalaseViewModel: ShopProductAddEtalaseChipViewModel): Int {
        return ShopProductAddEtalaseChipViewHolder.LAYOUT
    }

    fun type(shopProductEtalaseChipItemViewModel: ShopProductEtalaseChipItemViewModel): Int {
        return ShopProductEtalaseChipItemViewHolder.LAYOUT
    }

    fun attachAdapter(shopEtalaseAdapter: ShopProductEtalaseAdapter) {
        this.shopEtalaseAdapter = shopEtalaseAdapter
    }
}
