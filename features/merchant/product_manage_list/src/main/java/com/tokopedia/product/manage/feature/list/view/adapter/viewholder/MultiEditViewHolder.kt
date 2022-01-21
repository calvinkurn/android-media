package com.tokopedia.product.manage.feature.list.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.databinding.ItemProductManageMultiEditBinding
import com.tokopedia.utils.view.binding.viewBinding

class MultiEditViewHolder(itemView: View, private val listener: MenuClickListener): RecyclerView.ViewHolder(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_product_manage_multi_edit
    }

    private val binding by viewBinding<ItemProductManageMultiEditBinding>()

    fun bind(titleId: Int) {
        binding?.textMenu?.text = itemView.context.getString(titleId)
        itemView.setOnClickListener { listener.onClickMenuItem(titleId) }
    }

    interface MenuClickListener {
        fun onClickMenuItem(menuId: Int)
    }
}