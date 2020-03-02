package com.tokopedia.shop.newproduct.view.viewholder

import android.view.View
import android.widget.ImageView

import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder

import com.tokopedia.shop.R
import com.tokopedia.shop.newproduct.view.datamodel.ShopProductAddEtalaseChipViewModel

/**
 * @author by alvarisi on 12/12/17.
 */

class ShopProductAddEtalaseChipViewHolder(
        itemView: View,
        private val shopProductEtalaseListViewHolderListener: ShopProductEtalaseListViewHolder.ShopProductEtalaseChipListViewHolderListener?
) : AbstractViewHolder<ShopProductAddEtalaseChipViewModel>(itemView) {

    init {
        initLayout(itemView)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_product_add_etalase_chip
    }

    private lateinit var imageViewAddChipEtalase: ImageView

    override fun bind(element: ShopProductAddEtalaseChipViewModel?) {
        imageViewAddChipEtalase.setOnClickListener {
            shopProductEtalaseListViewHolderListener?.onAddEtalaseChipClicked()
        }
    }

    private fun initLayout(view: View) {
        imageViewAddChipEtalase = view.findViewById<ImageView>(R.id.iv_add_chip_etalase)
    }
}