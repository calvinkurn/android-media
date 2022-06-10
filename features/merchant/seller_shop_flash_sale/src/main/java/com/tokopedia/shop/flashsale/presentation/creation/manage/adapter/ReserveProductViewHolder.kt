package com.tokopedia.shop.flashsale.presentation.creation.manage.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsItemDraftBinding
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsItemReserveProductBinding
import com.tokopedia.utils.view.binding.viewBinding

class ReserveProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    companion object {
        fun createRootView(parent: ViewGroup): View = LayoutInflater.from(parent.context)
            .inflate(R.layout.ssfs_item_reserve_product, parent, false)
    }

    private val binding: SsfsItemReserveProductBinding? by viewBinding()

    fun bind(item: String) {
        binding?.apply {
            tvProductName.text = item
        }
    }
}
