package com.tokopedia.shop.sort.view.adapter.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.R
import com.tokopedia.shop.databinding.ItemShopProductSortBinding
import com.tokopedia.shop.sort.view.model.ShopProductSortModel
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by normansyahputa on 2/24/18.
 */
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
