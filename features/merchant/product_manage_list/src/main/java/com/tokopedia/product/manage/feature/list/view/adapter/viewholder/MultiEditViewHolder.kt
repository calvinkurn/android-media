package com.tokopedia.product.manage.feature.list.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
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

    fun bind(titleId: Int, isShopModerated: Boolean) {
        binding?.textMenu?.text = itemView.context.getString(titleId)

        if (isShopModerated && titleId != R.string.product_bs_delete_title) {
            itemView.setOnClickListener(null)
            binding?.textMenu?.setTextColor(
                ContextCompat.getColor(
                    itemView.context,
                    com.tokopedia.unifyprinciples.R.color.Unify_NN400
                )
            )
        } else {
            itemView.setOnClickListener { listener.onClickMenuItem(titleId) }
            binding?.textMenu?.setTextColor(
                ContextCompat.getColor(
                    itemView.context,
                    com.tokopedia.unifyprinciples.R.color.Unify_NN950
                )
            )
        }
    }

    interface MenuClickListener {
        fun onClickMenuItem(menuId: Int)
    }
}
