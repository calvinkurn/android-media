package com.tokopedia.product.manage.feature.list.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.databinding.ItemProductManageMoreMenuBinding
import com.tokopedia.product.manage.feature.list.view.model.ProductMoreMenuModel
import com.tokopedia.utils.view.binding.viewBinding

class ProductManageMoreMenuViewHolder(itemView: View, private val listener: ProductManageMoreMenuListener) : RecyclerView.ViewHolder(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_product_manage_more_menu
    }

    private val binding by viewBinding<ItemProductManageMoreMenuBinding>()

    fun bind(menu: ProductMoreMenuModel) {
        binding?.tvMoreMenuTitle?.text = menu.title
        itemView.setOnClickListener {
            listener.onMoreMenuClicked(menu)
        }
    }

    interface ProductManageMoreMenuListener {
        fun onMoreMenuClicked(menu: ProductMoreMenuModel)
    }
}