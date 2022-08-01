package com.tokopedia.shop.flashsale.presentation.creation.manage.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.kotlin.extensions.view.getNumberFormatted
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsItemReserveProductBinding
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsItemWarehouseBinding
import com.tokopedia.shop.flashsale.presentation.creation.manage.model.ReserveProductModel
import com.tokopedia.shop.flashsale.presentation.creation.manage.model.WarehouseUiModel
import com.tokopedia.utils.view.binding.viewBinding

class WarehouseViewHolder(
    itemView: View,
    private val itemOnClick: (position: Int) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    companion object {
        fun createRootView(parent: ViewGroup): View = LayoutInflater.from(parent.context)
            .inflate(R.layout.ssfs_item_warehouse, parent, false)
    }

    private val binding: SsfsItemWarehouseBinding? by viewBinding()

    init {
        binding?.apply {
            root.setOnClickListener {
                itemOnClick(adapterPosition)
            }
            checkboxItem.setOnClickListener {
                itemOnClick(adapterPosition)
            }
        }
    }

    fun bind(item: WarehouseUiModel) {
        binding?.apply {
            val stockText = itemView.context.getString(R.string.editproduct_stock_text, item.stock)
            val itemEnabled = item.stock.isMoreThanZero()
            typographyWarehouseName.text = item.name
            typographyStock.text = stockText
            checkboxItem.isChecked = item.isSelected
            checkboxItem.isEnabled = itemEnabled
            typographyWarehouseName.isEnabled = itemEnabled
            typographyStock.isEnabled = itemEnabled
            root.isEnabled = itemEnabled
        }
    }
}
