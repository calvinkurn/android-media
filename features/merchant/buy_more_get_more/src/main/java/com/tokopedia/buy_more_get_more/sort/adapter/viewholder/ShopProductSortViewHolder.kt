package com.tokopedia.buy_more_get_more.sort.adapter.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buy_more_get_more.R
import com.tokopedia.buy_more_get_more.databinding.ItemShopProductSortBinding
import com.tokopedia.buy_more_get_more.sort.model.ShopProductSortModel
import com.tokopedia.utils.view.binding.viewBinding


class ShopProductSortViewHolder(itemView: View) : AbstractViewHolder<ShopProductSortModel?>(itemView) {

    private val viewBinding: ItemShopProductSortBinding? by viewBinding()
    private var etalasePickerItemName: TextView? = null
    private var checkedImageView: ImageView? = null
    override fun bind(shopProductFilterModel: ShopProductSortModel?) {
        etalasePickerItemName?.text = shopProductFilterModel?.name
        if (shopProductFilterModel?.isSelected == true) {
            checkedImageView?.visibility = View.VISIBLE
        } else {
            checkedImageView?.visibility = View.GONE
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_product_sort
    }

    init {
        etalasePickerItemName = viewBinding?.textViewName
        checkedImageView = viewBinding?.imageViewSelection
    }
}
